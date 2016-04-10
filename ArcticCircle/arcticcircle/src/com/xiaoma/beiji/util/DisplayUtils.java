package com.xiaoma.beiji.util;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.TypedValue;

public class DisplayUtils {

	public static int getScreenHeight(Context context) {
		DisplayMetrics metric = context.getResources().getDisplayMetrics();
		int screenHeight = metric.heightPixels;
		return screenHeight;
	}

	public static int getScreenWidth(Context context) {
		DisplayMetrics metric = context.getResources().getDisplayMetrics();
		int screenWidth = metric.widthPixels;
		return screenWidth;
	}

	public static int getStatusBarHeight(Context context) {
		int height = 0;
		int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
		if (resourceId > 0) {
			height = context.getResources().getDimensionPixelSize(resourceId);
		}
		return height;
	}

	public static int getActionBarHeight(Context context) {
		// Calculate ActionBar height
		int actionBarHeight = 0;
		TypedValue tv = new TypedValue();
		if (context.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
			actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, context.getResources()
                    .getDisplayMetrics());
		}
		if (actionBarHeight == 0) {
			actionBarHeight = 45;
		}
		return actionBarHeight;
	}

	public static int dp2px(Context context,float dp) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dp * scale + 0.5f);
	}
}
