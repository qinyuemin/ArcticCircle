package com.xiaoma.beiji.base;

import android.content.Context;
import android.util.Log;
import com.common.android.lib.manager.ActivityManager;
import com.xiaoma.beiji.common.AppConstants;
import com.xiaoma.beiji.util.LogFileUtils;

import java.io.*;

public class CrashHandler implements Thread.UncaughtExceptionHandler {


    private static final String TAG = "CrashHandler";
    private static final String OOM = "java.lang.OutOfMemoryError";
    private Thread.UncaughtExceptionHandler mOldUncaughtExceptionHandler;


    public CrashHandler(){
        this.mOldUncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
    }

    public void init(){
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    /**
     * @author carrie.chen
     * @param throwable
     * @return
     */
    public boolean isOOM(Throwable throwable){
        Log.d(TAG, "getName:" + throwable.getClass().getName());
        if(OOM.equals(throwable.getClass().getName())){
            return true;
        }else{
            Throwable cause = throwable.getCause();
            if(cause != null){
                return isOOM(cause);
            }
            return false;
        }
    }


    @Override
    public void uncaughtException(Thread thread, Throwable throwable) {
        Log.e(TAG, "catch", throwable);
        String error = getErrorInfo(throwable);

        saveErrorToLogFile(throwable);
        ActivityManager.getActivityManager().exit();
        BaseApplication.getInstance().finalApplication();
        killProcess(BaseApplication.getInstance());
    }

    public static void killProcess(Context context) {
        android.app.ActivityManager am = (android.app.ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        am.killBackgroundProcesses(context.getPackageName());
        am.restartPackage(context.getPackageName());
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }

    private void saveErrorToLogFile(Throwable ex) {
        Writer writer = new StringWriter();
        PrintWriter pw = new PrintWriter(writer);
        ex.printStackTrace(pw);
        Throwable cause = ex.getCause();
        // ѭ���Ű����е��쳣��Ϣд��writer��
        while (cause != null) {
            cause.printStackTrace(pw);
            cause = cause.getCause();
        }
        pw.close();// �ǵùر�
        String result = writer.toString();
        LogFileUtils.log(getAppLogFilename(), "[---error---]:" + result);
    }

    public String getAppLogFilename() {
        String fileCrashLog = AppConstants.FileConstant.FILE_CRASH_LOG;
        File logFile = new File(fileCrashLog);
        File logPath = logFile.getParentFile();
        if (!logPath.exists()) {
            logPath.mkdirs();
        }
        return fileCrashLog;
    }

    private String getErrorInfo(Throwable ex) {
        Writer writer = null;
        PrintWriter pw = null;
        String result = null;
        try {
            writer = new StringWriter();
            pw = new PrintWriter(writer);
            ex.printStackTrace(pw);
            Throwable cause = ex.getCause();
            // ѭ���Ű����е��쳣��Ϣд��writer��
            while (cause != null) {
                cause.printStackTrace(pw);
                cause = cause.getCause();
            }
            result = writer.toString();

        } catch (Exception e) {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e1) {
                }
                writer = null;
            }

            if (pw != null) {
                pw.close();// �ǵùر�
                pw = null;
            }
        }
        return result;
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        Thread.setDefaultUncaughtExceptionHandler(mOldUncaughtExceptionHandler);
    }

}