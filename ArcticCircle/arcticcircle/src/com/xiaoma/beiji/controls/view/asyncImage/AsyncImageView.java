package com.xiaoma.beiji.controls.view.asyncImage;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;

/**
 * 异步普通图片工具
 *
 */
public class AsyncImageView extends AsyncShapedImageView {
	public AsyncImageView(Context context) {
		super(context);
	}

	public AsyncImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public AsyncImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected Bitmap getShapedBitmap(Bitmap lruBitmap) {
		return lruBitmap;
	}

	@Override
	protected void drawBorder(Canvas canvas, Paint paint) {
		canvas.drawRect(0, 0, getWidth(), getHeight(), paint);
	}
}