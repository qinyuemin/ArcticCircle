package com.common.android.lib.controls.dialog;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.TextView;

import com.common.android.lib.R;

public class CommonProgressDialog extends ProgressDialog {
	private TextView messageTextView;
	private View rotateImageView;
	private CharSequence message;
	private RotateAnimation rotateAnimation;
	public CommonProgressDialog(Context context) {
		super(context, R.style.LuluyouProgressDialogStyle);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.dialog_custome_progress_layout);
		this.messageTextView = (TextView)findViewById(R.id.messageTextView);
		this.rotateImageView = findViewById(R.id.rotateImageView);
		if(message == null || message.length() == 0){
			this.messageTextView.setVisibility(View.GONE);
		}else{
			this.messageTextView.setVisibility(View.VISIBLE);
			this.messageTextView.setText(message);
		}
		this.getWindow().getAttributes().gravity = Gravity.CENTER;
		if(rotateAnimation == null){
			rotateAnimation = new RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
			rotateAnimation.setInterpolator(new LinearInterpolator());
			rotateAnimation.setDuration(2000);
			rotateAnimation.setRepeatCount(RotateAnimation.INFINITE);
		}
	}
	
	@Override
	public void onAttachedToWindow() {
		if(rotateAnimation.hasStarted() == false || rotateAnimation.hasEnded()){
			rotateImageView.startAnimation(rotateAnimation);
		}
		super.onAttachedToWindow();
	}
	
	@Override
	public void onDetachedFromWindow() {
		if(rotateAnimation.hasStarted() && rotateAnimation.hasEnded() == false){
			rotateImageView.clearAnimation();
			rotateAnimation.cancel();
		}
		super.onDetachedFromWindow();
	}
	
	public void setMessage(CharSequence message){
		if(messageTextView != null){
			if(message == null || message.length() == 0){
				this.messageTextView.setVisibility(View.GONE);
			}else{
				this.messageTextView.setVisibility(View.VISIBLE);
				this.messageTextView.setText(message);
			}
		}
		this.message = message;
	}
	
	public static final CommonProgressDialog show(Context context, CharSequence title, CharSequence message){
		CommonProgressDialog d = new CommonProgressDialog(context);
		d.setMessage(message);
		d.show();
		return d;
	}

    public static ProgressDialog show(Context context, CharSequence title, CharSequence message, boolean indeterminate, boolean cancelable, OnCancelListener cancelListener) {
        return show(context, title, message);
    }
}
