/**
 * 项目名： arcticCircle
 * 包名： com.xiaoma.beiji.controls.view
 * 版本信息： 1.0.0
 * Copyright (c) 2015上海路路由信息科技有限公司-版权所有
 */
package com.xiaoma.beiji.controls.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;

import java.util.Timer;

/**
 *
 * 类名称： GetCodeButton
 * 类描述： 
 * 创建人： gang.shi
 * 修改人： gang.shi
 * 修改时间： 2015年12月31 9:58
 * 修改备注：
 * @version 1.0.0
 *
 */
public class GetCodeButton extends Button{
    private OnCodeListener onCodeListener;
    private String tipText = "获取验证码";

    public GetCodeButton(Context context) {
        super(context);
    }

    public GetCodeButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GetCodeButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public GetCodeButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void setOnClickListener(OnClickListener l) {
        super.setOnClickListener(l);
    }

    public void setOnCodeListener(OnCodeListener onCodeListener1) {
        this.onCodeListener = onCodeListener1;
        tipText = String.valueOf(getText());
        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                setEnabled(false);
                post(runnable);
                if(onCodeListener != null){
                    onCodeListener.onClick();
                }
            }
        });
    }

    public void initView(){

    }

    int currentTime = 0;
    int maxTime = 60;
    int delayed = 1 * 1000;
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if(currentTime++ <= maxTime){
                setText("" + (maxTime - currentTime));

                postDelayed(this, delayed);
            }else {
                removeCallbacks(this);
                setText(tipText);
                setEnabled(true);
                currentTime = 0;
            }
        }
    };

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        try {
            removeCallbacks(runnable);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public interface OnCodeListener{
        void onClick();
    }
}
