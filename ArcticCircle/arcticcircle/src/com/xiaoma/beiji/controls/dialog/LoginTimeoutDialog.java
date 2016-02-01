/**
 * 项目名： arcticCircle
 * 包名： com.xiaoma.beiji.controls.dialog
 * 版本信息： 1.0.0
 * Copyright (c) 2015上海路路由信息科技有限公司-版权所有
 */
package com.xiaoma.beiji.controls.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import com.baidu.location.b.d;
import com.makeapp.android.util.ViewUtil;
import com.xiaoma.beiji.R;
import com.xiaoma.beiji.util.IntentUtil;

/**
 *
 * 类名称： LoginTimeoutDialog
 * 类描述： 
 * 创建人： gang.shi
 * 修改人： gang.shi
 * 修改时间： 2015年12月03 19:52
 * 修改备注：
 * @version 1.0.0
 *
 */
public class LoginTimeoutDialog extends Dialog {

    public LoginTimeoutDialog(Context context) {
        super(context,R.style.base_dialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.dimAmount = 0.5f;

//        lp.width = getWindow().getWindowManager().getDefaultDisplay().getWidth();
//        lp.x = 0;
//        lp.y = getWindow().getWindowManager().getDefaultDisplay().getHeight();
        // 设置显示位置
        this.onWindowAttributesChanged(lp);
        getWindow().setAttributes(lp);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        //这句必须加上，要不然dialog上会有一段空白（标题栏）
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.dialog_login_timeout);
        setCancelable(true);

        ViewUtil.setViewOnClickListener(this, R.id.txt_login, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                IntentUtil.goUserLoginActivity(getContext());
            }
        });
    }
}
