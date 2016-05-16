package com.xiaoma.beiji.controls.dialog;

import android.app.Activity;
import android.content.DialogInterface;

import com.xiaoma.beiji.entity.FriendDynamicEntity;
import com.xiaoma.beiji.network.AbsHttpResultHandler;
import com.xiaoma.beiji.network.HttpClientUtil;
import com.xiaoma.beiji.util.ToastUtil;

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

    public void shoShareDialog(final Activity activity, final FriendDynamicEntity entity, final AbsHttpResultHandler absHttpResultHandler){
        final ShareDialog shareDialog = new ShareDialog(activity, entity, new InputDialog.InputCallBack() {
            @Override
            public void success(String content) {
                showProgressDialog(activity,true,null);
                HttpClientUtil.Dynamic.dynamicDoShare(entity.getReleaseId(), content, new AbsHttpResultHandler() {
                    @Override
                    public void onSuccess(int resultCode, String desc, Object data) {
                        ToastUtil.showToast(activity,"分享成功");
                        closeProgressDialog();
                        absHttpResultHandler.onSuccess(resultCode,desc,data);
                    }

                    @Override
                    public void onFailure(int resultCode, String desc) {
                        ToastUtil.showToast(activity,"分享失败" + desc);
                        closeProgressDialog();
                        absHttpResultHandler.onFailure(resultCode,desc);
                    }
                });
            }
        });
        shareDialog.show();
    }
}
