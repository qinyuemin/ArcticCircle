package com.xiaoma.beiji.util;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.xiaoma.beiji.R;
import com.xiaoma.beiji.base.BaseApplication;

public class ToastUtil {
	private static Toast mToast;
	private static Handler mToastHander;

    public static final int DEFAULT_DURATION = 3 * 1000;
	
	public static void showToast(Context context, int msgRes) {
		if(context == null){
			context = BaseApplication.getInstance();
		}
		showToast(context, context.getResources().getString(msgRes), Toast.LENGTH_SHORT);
	}
	
	public static void showToast(Context context, int msgRes, int duration) {
		if(context == null){
			context = BaseApplication.getInstance();
		}
		showToast(context, context.getResources().getString(msgRes), duration);
	}
	
	public static void showToast(Context context, String msg) {
		if(context == null){
			context = BaseApplication.getInstance();
		}
		showToast(context, msg, Toast.LENGTH_SHORT);
	}
	
	public static void showToast(final Context context, final String msg, final int duration) {
		if(mToastHander == null){
			mToastHander = new Handler(Looper.getMainLooper());
		}
		mToastHander.post(new Runnable() {
			@Override
			public void run() {
				if (mToast == null) {
					if(context == null){
						mToast = new LLToast(BaseApplication.getInstance());
					}else{
						mToast = new LLToast(context);
					}
					View view = LayoutInflater.from(context).inflate(R.layout.view_toast_layout, null);
					mToast.setView(view);
				}
				mToast.setText(msg);
				mToast.setDuration(duration);
				mToast.show();
			}
		});
	}
	
	public static void hideToast(){
		if(mToastHander == null){
			mToastHander = new Handler(Looper.getMainLooper());
		}
		mToastHander.post(new Runnable() {
			public void run() {
				if(mToast != null){
					mToast.cancel();
					//必须置空，不然快速点击会出现Toast被立即销毁的问题
					mToast = null;
				}
			};
		});
	}
	
	private static class LLToast extends Toast {
		
		private TextView mTextView;

		public LLToast(Context context) {
			super(context);
		}
		
		@Override
		public void setView(View view) {
			super.setView(view);
			if(view instanceof TextView){
				mTextView = (TextView) view;
			}
		}
		
		@Override
		public void setText(CharSequence s) {
			if(mTextView != null){
				mTextView.setText(s);
			}else{
				super.setText(s);
			}
		}
		
	} 
}
