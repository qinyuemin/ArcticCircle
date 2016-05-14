package com.xiaoma.beiji.controls.dialog;

import android.app.Activity;
import android.content.DialogInterface;

import java.util.List;

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

    public void showChooseDialog(Activity activity,List<String> items,ActionSheetDialog.OnSheetItemClickListener listener){
        ActionSheetDialog dialog =  new ActionSheetDialog(activity).builder();
        dialog.setCancelable(true).setCanceledOnTouchOutside(true);
        for(String str : items){
            dialog.addSheetItem(str, ActionSheetDialog.SheetItemColor.Gray,listener);
        }
        dialog.show();
    }
}
