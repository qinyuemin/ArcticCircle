/**
 * 项目名： eyah
 * 包名： com.sttri.eyah.controls.dialog
 * 版本信息： 1.0.0
 * Copyright (c) -版权所有
 */
package com.xiaoma.beiji.controls.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.common.android.lib.base.BaseActivity;
import com.xiaoma.beiji.R;

/**
 * 类名称： EyahDialog
 * 类描述：
 * 创建人： gang.shi
 * 修改人： gang.shi
 * 修改时间： 2015年06月08 15:10
 * 修改备注：
 *
 * @version 1.0.0
 */
public class SimpleDialog extends Dialog {
    private Activity activity;

    private CharSequence title;
    private CharSequence message;
    private View messageView;
    private RelativeLayout.LayoutParams messageViewLayoutParams;
    private CharSequence leftButtonTxt;
    private android.view.View.OnClickListener leftButtonClickListenerForView;
    private CharSequence centerButtonTxt;
    private android.view.View.OnClickListener centerButtonClickListenerForView;
    private CharSequence rightButtonTxt;
    private android.view.View.OnClickListener rightButtonClickListenerForView;
    private int leftButtonColor;
    private int centerButtonColor;
    private int rightButtonColor;

    public SimpleDialog(Activity activity) {
        super(activity, R.style.base_dialog);
        this.activity = activity;
    }

    public SimpleDialog setDialogTitle(CharSequence title) {
        this.title = title;
        this.flagTitleChanged = true;
        refresh();
        return this;
    }

    public SimpleDialog setDialogMessage(CharSequence message) {
        this.message = message;
        this.flagMessageChanged = true;

        //如果设置文本，必须将messageView等相关的数据清空，否则如果messageView不为空，则message设置会无效
        this.messageView = null;
        this.messageViewLayoutParams = null;
        refresh();
        return this;
    }

    public SimpleDialog setDialogMessageView(View messageView, RelativeLayout.LayoutParams messageViewLayoutParams) {
        this.messageView = messageView;
        this.messageViewLayoutParams = messageViewLayoutParams;
        this.flagMessageChanged = true;

        this.message = null;
        refresh();
        return this;
    }

    public SimpleDialog setDialogLeftButton(CharSequence leftButtonTxt) {
        this.leftButtonTxt = leftButtonTxt;
        this.flagBottomBtnChanged = true;
        refresh();
        return this;
    }

    public SimpleDialog setDialogLeftButton(CharSequence leftButtonTxt, android.view.View.OnClickListener clickListener) {
        this.leftButtonTxt = leftButtonTxt;
        this.leftButtonClickListenerForView = clickListener;
        this.flagBottomBtnChanged = true;
        refresh();
        return this;
    }

    public SimpleDialog setDialogCenterButton(CharSequence centerButtonTxt) {
        this.centerButtonTxt = centerButtonTxt;
        this.flagBottomBtnChanged = true;
        refresh();
        return this;
    }

    public SimpleDialog setDialogCenterButton(CharSequence centerButtonTxt, android.view.View.OnClickListener clickListener) {
        this.centerButtonTxt = centerButtonTxt;
        this.leftButtonClickListenerForView = clickListener;
        this.flagBottomBtnChanged = true;
        refresh();
        return this;
    }

    public SimpleDialog setDialogRightButton(CharSequence rightButtonTxt) {
        this.rightButtonTxt = rightButtonTxt;
        this.flagBottomBtnChanged = true;
        refresh();
        return this;
    }

    public SimpleDialog setDialogRightButton(CharSequence rightButtonTxt, android.view.View.OnClickListener clickListener) {
        this.rightButtonTxt = rightButtonTxt;
        this.rightButtonClickListenerForView = clickListener;
        this.flagBottomBtnChanged = true;
        refresh();
        return this;
    }

    public SimpleDialog setDialogLeftColor(int leftButtonColor) {
        this.leftButtonColor = leftButtonColor;
        this.flagBottomBtnChanged = true;
        refresh();
        return this;
    }

    public SimpleDialog setDialogCenterColor(int centerButtonColor) {
        this.centerButtonColor = centerButtonColor;
        this.flagBottomBtnChanged = true;
        refresh();
        return this;
    }

    public SimpleDialog setDialogRightColor(int rightButtonColor) {
        this.rightButtonColor = rightButtonColor;
        this.flagBottomBtnChanged = true;
        refresh();
        return this;
    }

    private TextView DialogTitle;
    private View DialogMessageTextFrameLayout;
    private TextView DialogMessage;
    private RelativeLayout DialogMessageViewRelativeLayout;
    private View DialogBtnDividerLine;
    private View DialogBtnLayout;
    private TextView DialogButtonLeft;
    private View DialogLineLeftAndCenter;
    private TextView DialogButtonCenter;
    private View DialogLineCenterAndRight;
    private TextView DialogButtonRight;

    private boolean flagTitleChanged = true;
    private boolean flagMessageChanged = true;
    private boolean flagBottomBtnChanged = true;

    private boolean hasInitView = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.dimAmount = 0.5f;
        getWindow().setAttributes(lp);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        // 这句必须加上，要不然dialog上会有一段空白（标题栏）
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_base_layout);

        DialogTitle = (TextView) findViewById(R.id.DialogTitle);
        DialogMessageTextFrameLayout = findViewById(R.id.DialogMessageTextFrameLayout);
        DialogMessage = (TextView) DialogMessageTextFrameLayout.findViewById(R.id.DialogMessage);
        DialogMessageViewRelativeLayout = (RelativeLayout) findViewById(R.id.DialogMessageViewRelativeLayout);

        DialogBtnDividerLine = findViewById(R.id.DialogBtnDividerLine);
        DialogBtnLayout = findViewById(R.id.DialogBtnLayout);
        DialogButtonLeft = (TextView) findViewById(R.id.DialogButtonLeft);
        DialogButtonCenter = (TextView) findViewById(R.id.DialogButtonCenter);
        DialogButtonRight = (TextView) findViewById(R.id.DialogButtonRight);
        DialogLineLeftAndCenter = findViewById(R.id.DialogLineLeftAndCenter);
        DialogLineCenterAndRight = findViewById(R.id.DialogLineCenterAndRight);

        hasInitView = true;
    }

    @Override
    protected void onStart() {
        refresh();
    }

    /**
     * 设置Title相关的View
     */
    private void setTitleView() {
        if (isNotEmpty(title)) {
            DialogTitle.setVisibility(View.VISIBLE);
            DialogTitle.setText(title);
        } else {
            DialogTitle.setVisibility(View.GONE);
        }
    }

    /**
     * 设置Message相关的View
     */
    private void setMessageView() {
        if (messageView != null) {
            DialogMessageTextFrameLayout.setVisibility(View.GONE);
            DialogMessageViewRelativeLayout.setVisibility(View.VISIBLE);
            DialogMessageViewRelativeLayout.removeAllViewsInLayout();
            if (messageViewLayoutParams == null) {
                DialogMessageViewRelativeLayout.addView(messageView);
            } else {
                DialogMessageViewRelativeLayout.addView(messageView, messageViewLayoutParams);
            }
        } else {
            DialogMessageTextFrameLayout.setVisibility(View.VISIBLE);
            DialogMessageViewRelativeLayout.setVisibility(View.GONE);
            DialogMessage.setText(message);
        }
    }

    /**
     * 设置底部按钮相关的View
     */
    private void setBottomBtnView() {
        boolean showBtnLayout = false;
        if (isNotEmpty(leftButtonTxt)) {
            showBtnLayout = true;
            if (leftButtonColor != 0) {
                DialogButtonLeft.setTextColor(leftButtonColor);
            }
            DialogButtonLeft.setText(leftButtonTxt);
            DialogButtonLeft.setVisibility(View.VISIBLE);
            DialogButtonLeft.setOnClickListener(new android.view.View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    dismiss();
                    if (leftButtonClickListenerForView != null) {
                        leftButtonClickListenerForView.onClick(v);
                    }
                }
            });
        } else {
            DialogButtonLeft.setVisibility(View.GONE);
        }


        if (isNotEmpty(centerButtonTxt)) {
            showBtnLayout = true;
            if (centerButtonColor != 0) {
                DialogButtonCenter.setTextColor(centerButtonColor);
            }
            DialogButtonCenter.setText(centerButtonTxt);
            DialogButtonCenter.setVisibility(View.VISIBLE);
            DialogButtonCenter.setOnClickListener(new android.view.View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    dismiss();
                    if (centerButtonClickListenerForView != null) {
                        centerButtonClickListenerForView.onClick(v);
                    }
                }
            });
        } else {
            DialogButtonCenter.setVisibility(View.GONE);
        }

        if (isNotEmpty(rightButtonTxt)) {
            showBtnLayout = true;
            if (rightButtonColor != 0) {
                DialogButtonRight.setTextColor(rightButtonColor);
            }
            DialogButtonRight.setText(rightButtonTxt);
            DialogButtonRight.setVisibility(View.VISIBLE);
            DialogButtonRight.setOnClickListener(new android.view.View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    dismiss();
                    if (rightButtonClickListenerForView != null) {
                        rightButtonClickListenerForView.onClick(v);
                    }
                }
            });
        } else {
            DialogButtonRight.setVisibility(View.GONE);
        }

        if (showBtnLayout == false) {
            DialogBtnDividerLine.setVisibility(View.GONE);
            DialogBtnLayout.setVisibility(View.GONE);
        } else {
            DialogBtnDividerLine.setVisibility(View.VISIBLE);
            DialogBtnLayout.setVisibility(View.VISIBLE);

            if (DialogButtonCenter.getVisibility() == View.VISIBLE && DialogButtonCenter.getVisibility() == View.VISIBLE) {
                DialogLineLeftAndCenter.setVisibility(View.VISIBLE);
            } else {
                DialogLineLeftAndCenter.setVisibility(View.GONE);
            }

            if (DialogButtonRight.getVisibility() == View.VISIBLE && (DialogButtonCenter.getVisibility() == View.VISIBLE || DialogButtonLeft.getVisibility() == View.VISIBLE)) {
                DialogLineCenterAndRight.setVisibility(View.VISIBLE);
            } else {
                DialogLineCenterAndRight.setVisibility(View.GONE);
            }
        }
    }

    public void refresh() {
        if (hasInitView == false) {
            return;
        }

        if (flagTitleChanged) {
            flagTitleChanged = false;
            setTitleView();
        }

        if (flagMessageChanged) {
            flagMessageChanged = false;
            setMessageView();
        }

        if (flagBottomBtnChanged) {
            flagBottomBtnChanged = false;
            setBottomBtnView();
        }
    }

    private static boolean isNotEmpty(CharSequence text) {
        return text != null && text.length() > 0;
    }

    @Override
    public void show() {
        try {
            if ((activity instanceof BaseActivity && ((BaseActivity) activity).isActivityShowing()) || activity.isFinishing() == false) {
                super.show();
                updateDialogWidth(activity, this);
            }
        } catch (Exception e) {
        }
    }

    /**
     * 将 dialog 的宽度设置为联连标准通用宽度
     * @param context
     * @param dialog
     */
    public static void updateDialogWidth(Activity context,Dialog dialog) {
        Display display = context.getWindowManager().getDefaultDisplay();
        Window window = dialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = (display.getWidth()) - display.getWidth()/10; //设置宽度
        window.setAttributes(lp);
    }


    /**
     * @deprecated 不推荐使用，请使用 {@link #setDialogTitle(CharSequence)}
     */
    public void setTitle(CharSequence title) {
        this.setDialogTitle(title);
    }

    /**
     * @deprecated 不推荐使用，请使用 {@link #setDialogMessage(CharSequence)}
     */
    public void setMessage(CharSequence message) {
        this.setDialogMessage(message);
    }

    public static final SimpleDialog showDialog(Activity activity, CharSequence title, CharSequence message, CharSequence leftBtnTxt, CharSequence rightBtnTxt, android.view.View.OnClickListener leftBtnClickListener, android.view.View.OnClickListener rightBtnClickListener) {
        SimpleDialog d = new SimpleDialog(activity).setDialogTitle(title).setDialogMessage(message);
        d.setDialogLeftButton(leftBtnTxt, leftBtnClickListener);
        d.setDialogRightButton(rightBtnTxt, rightBtnClickListener);
        d.show();
        return d;
    }

    /**
     * 只有一个按钮的Dialog，一般用作提示，无需处理用户点击的情况（例如：我知道了）
     * showDialog(这里用一句话描述这个方法的作用）
     * （这里描述这个方法是用条件 - 可选）
     *
     * @param activity
     * @param title
     * @param message
     * @param leftBtnTxt
     * @return LianLianDialog
     * @throws
     * @since 1.0.0
     */
    public static final SimpleDialog showDialog(Activity activity, String title, String message, String leftBtnTxt) {
        return showDialog(activity, title, message, leftBtnTxt, null, null, null);
    }

    public static final SimpleDialog showDialog(Activity activity, String title, View messageView, RelativeLayout.LayoutParams layoutParams, String leftBtnTxt) {
        SimpleDialog d = new SimpleDialog(activity).setDialogTitle(title).setDialogMessageView(messageView, layoutParams);
        d.setDialogLeftButton(leftBtnTxt, null);
        d.show();
        return d;
    }

    public static final SimpleDialog showDialog(Activity activity, String title, String message, String leftBtnTxt, android.view.View.OnClickListener leftBtnClickListener) {
        return showDialog(activity, title, message, leftBtnTxt, null, leftBtnClickListener, null);
    }

    public static final SimpleDialog showDialogWithConfirmAndCancel(Activity activity, String title, String message, android.view.View.OnClickListener confirmClickListener) {
        return showDialog(activity, title, message, "取消", "确定", null, confirmClickListener);
    }

    public static final SimpleDialog showDialogWithIKnow(Activity activity, String title, String message) {
        return showDialog(activity, title, message, "我知道了", null, null, null);
    }

    /**
     * 无按钮的Dialog
     * showDialog(这里用一句话描述这个方法的作用）
     * （这里描述这个方法是用条件 - 可选）
     *
     * @param activity
     * @param title
     * @param message
     * @return LianLianDialog
     * @throws
     * @since 1.0.0
     */
    public static final SimpleDialog showDialog(Activity activity, String title, String message) {
        return showDialog(activity, title, message, null, null, null, null);
    }
}
