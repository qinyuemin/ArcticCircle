package com.xiaoma.beiji.controls.view.chatting;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

public class KeyboardRelativeLayout extends RelativeLayout implements KeyboardViewInterface {
	private boolean mHasInit;
	private boolean mHasKeybord;
	private int mHeight;
	private List<OnKeyboardStateChangeListener> mListenerList;
	
	private Activity activity;
	private int screenHeight;

	public KeyboardRelativeLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	public KeyboardRelativeLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public KeyboardRelativeLayout(Context context) {
		super(context);
		init(context);
	}

	private void init(Context context){
		if(context instanceof Activity){
			activity = (Activity) context;
			screenHeight = activity.getWindowManager().getDefaultDisplay().getHeight();
		}
	}
	
	/**
	 * set keyboard state listener
	 */
	public void addOnKeyboardStateChangeListener(OnKeyboardStateChangeListener listener) {
		if(listener == null){
			return;
		}
		
		if(mListenerList == null){
			mListenerList = new ArrayList<OnKeyboardStateChangeListener>();
		}
		if(mListenerList.indexOf(mListenerList) == -1){
			mListenerList.add(listener);
		}
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
		if (!mHasInit) {
			mHasInit = true;
			mHeight = b;
		} else {
			//如果b == screenHeight，则证明，此View根本就没有显示，有可能是初始化的时候调用的onLayout
			if(screenHeight != 0 && b != screenHeight){
				mHeight = mHeight < b ? b : mHeight;
			}
		}
		if (mHasInit && mHasKeybord == false && mHeight > b) {
			// 软键盘弹出
			mHasKeybord = true;
			if (mListenerList != null && mListenerList.isEmpty() == false) {
				for(OnKeyboardStateChangeListener listener : mListenerList){
					if(listener != null){
						listener.onKeyBoardStateChange(OnKeyboardStateChangeListener.KEYBOARD_STATE_SHOW);
					}
				}
			}
		}
		if (mHasInit && mHasKeybord && mHeight == b) {
			// 软键盘隐藏
			mHasKeybord = false;
			if (mListenerList != null && mListenerList.isEmpty() == false) {
				for(OnKeyboardStateChangeListener listener : mListenerList){
					if(listener != null){
						listener.onKeyBoardStateChange(OnKeyboardStateChangeListener.KEYBOARD_STATE_HIDE);
					}
				}
			}
		}
	}
}