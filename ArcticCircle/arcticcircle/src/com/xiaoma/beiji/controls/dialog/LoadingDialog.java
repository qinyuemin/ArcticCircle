package com.xiaoma.beiji.controls.dialog;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xiaoma.beiji.R;


/**
 * Created by zhangqibo on 2015/7/3.
 */
public class LoadingDialog extends Dialog{

    public static  TextView mContentTextView;


    public LoadingDialog(Context context) {
        super(context);
    }

    public LoadingDialog(Context context, int theme) {
        super(context, theme);
    }

    public static LoadingDialog create(Context context,String content){
        LoadingDialog dialog = new LoadingDialog(context, R.style.CommonDialog);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.commonui_dialog_loading, null);
        mContentTextView = (TextView) layout.findViewById(R.id.content);
        if(TextUtils.isEmpty(content)){
            mContentTextView.setVisibility(View.GONE);
        }else{
            mContentTextView.setVisibility(View.VISIBLE);
        }
        mContentTextView.setText(content);
        dialog.addContentView(layout, new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(layout);
        return dialog;
    }
    public void setProgressText(String message) {
        if(mContentTextView != null) {
            mContentTextView.setText(message);
        }
    }
    public static LoadingDialog create(Context context){
        return create(context, "");
    }
}
