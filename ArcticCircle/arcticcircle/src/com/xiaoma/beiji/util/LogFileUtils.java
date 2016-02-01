package com.xiaoma.beiji.util;

import com.common.android.lib.util.BeanUtil;
import com.common.android.lib.util.SDCardUtil;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class LogFileUtils {
	static boolean logControl = true;
	public static String exction;
	private static PrintWriter mLogWriter = null;
	private static FileWriter mFileWriter = null;

	public LogFileUtils() {
		super();
	}

	public static void log(String logFilePath,String message) {
	    logInfo2File(logFilePath,message);
	}
	
	public  static boolean initLogWriter(String logFilePath){
	    boolean bRet = false;
	    if (SDCardUtil.getSDCardPath() != null) {
	        try {
                File f = new File(logFilePath);
                if(!f.exists() || !f.isFile()){
                    f.createNewFile();
                }
                mFileWriter = new FileWriter(f, true);
                mLogWriter = new PrintWriter(mFileWriter);
            } catch (Exception e) {
                e.printStackTrace();
            }
	        
	    }
	    return bRet;
	}
	
	public static void finalLogWriter(){
	    if(mFileWriter != null){
            try {
                mFileWriter.close();
                mFileWriter = null;
            } catch (IOException e) {}
        }
        if(mLogWriter != null){
            mLogWriter.close();
            mLogWriter = null;
        }
	}
	
	public static void logInfo2File(String logFilePath,String logInfo){
	    if(logControl){
	        if(null == mLogWriter || null == mFileWriter){
	            initLogWriter(logFilePath);
	        }
	        if(null != mLogWriter &&mFileWriter != null){
	            String longDetailInfo = BeanUtil.getDateTime() + ":" + logInfo;
	            mLogWriter.print(longDetailInfo+"\r\n");
	            mLogWriter.flush();
	            try {
                    mFileWriter.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
	        }
	    }
	    
	}


	public static void recordLog(String savePathStr, String saveFileNameS, String saveDataStr, boolean saveTypeStr) {
		FileWriter fw = null;
		PrintWriter pw = null;
		try {
			File folder = new File(savePathStr);
			if(folder.exists() == false || folder.isDirectory() == false){
				folder.mkdirs();
			}
			File f = new File(folder, saveFileNameS);
			fw = new FileWriter(f, saveTypeStr);

			pw = new PrintWriter(fw);
			pw.print(saveDataStr+"\r\n");
			pw.flush();
			fw.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(fw != null){
				try {
					fw.close();
				} catch (IOException e) {}
			}
			if(pw != null){
				pw.close();
			}
		}
	}
	
	@Override
	protected void finalize() throws Throwable {
	    super.finalize();
	    finalLogWriter();
	}

}