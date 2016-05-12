package com.xiaoma.beiji.controls.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.xiaoma.beiji.R;


/**
 * Created by zhangqibo on 2015/7/3.
 */
public class InputDialog extends Dialog{

    public InputDialog(Context context) {
        super(context);
    }

    public InputDialog(Context context, int theme) {
        super(context, theme);
    }

    public static InputDialog create(Context context,final InputCallBack callBack){
        final InputDialog dialog = new InputDialog(context, R.style.CommonDialog);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.layout_input_dialog, null);
        final EditText inputEdite = (EditText) layout.findViewById(R.id.edit_comment);
        Button sendBtn = (Button) layout.findViewById(R.id.btn_send);
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callBack.success(inputEdite.getText().toString().trim());
                inputEdite.setText("");
                dialog.dismiss();
            }
        });
        dialog.addContentView(layout, new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(true);
        dialog.setContentView(layout);
        return dialog;
    }

    public interface InputCallBack{
        public void success(String content);
    }
}
