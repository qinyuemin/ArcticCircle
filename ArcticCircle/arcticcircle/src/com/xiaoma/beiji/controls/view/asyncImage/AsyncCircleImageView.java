package com.xiaoma.beiji.controls.view.asyncImage;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;

/**
 * 异步圆形图片工具，其中的图片剪切模式为CenterCrop
 *
 */
public class AsyncCircleImageView extends AsyncShapedImageView {

	public AsyncCircleImageView(Context context) {
		super(context);
        init();
	}

	public AsyncCircleImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
        init();
	}

	public AsyncCircleImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
        init();
	}

    private void init(){
        setCrop(true);
    }

	@Override
	protected Bitmap getShapedBitmap(Bitmap lruBitmap){
		return com.xiaoma.beiji.util.BitmapOptUtil.getCircularBitmap(lruBitmap, getWidth());
	}

	@Override
	protected void drawBorder(Canvas canvas,Paint paint) {
		canvas.drawCircle(getWidth() / 2,getWidth() / 2,(getWidth() - paint.getStrokeWidth()) / 2,paint);
	}
}