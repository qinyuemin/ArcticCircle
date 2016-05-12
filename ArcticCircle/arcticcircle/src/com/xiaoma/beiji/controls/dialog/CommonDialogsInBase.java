package com.xiaoma.beiji.controls.dialog;

import android.app.Activity;
import android.content.DialogInterface;

/**
 * Created by zhangqibo on 2016/5/11.
 */
public class CommonDialogsInBase {
    private InputDialog inputDialog;
    private LoadingDialog mProgressDialog;
    public void showProgressDialog(Activity context,boolean cancelable,
                                   DialogInterface.OnCancelListener onCancelListener) {
        if (null == mProgressDialog) {
            mProgressDialog = new LoadingDialog(context).create(context);
            if (onCancelListener != null) {
                mProgressDialog.setOnCancelListener(onCancelListener);
            }
            mProgressDialog.setCancelable(cancelable);
            mProgressDialog.setCanceledOnTouchOutside(false);
        }
        if (!mProgressDialog.isShowing()) {
            mProgressDialog.show();
        }
    }

    public void closeProgressDialog() {
        if (null != mProgressDialog && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }

    public void showInputDialog(Activity activity,InputDialog.InputCallBack callBack){
        if (null == inputDialog) {
            inputDialog = InputDialog.create(activity,callBack);
            inputDialog.setCanceledOnTouchOutside(false);
        }
        if (!inputDialog.isShowing()) {
            inputDialog.show();
        }
    }
}
