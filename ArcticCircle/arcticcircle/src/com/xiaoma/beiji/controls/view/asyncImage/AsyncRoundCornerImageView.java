package com.xiaoma.beiji.controls.view.asyncImage;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;

import com.xiaoma.beiji.util.BitmapOptUtil;
import com.xiaoma.beiji.util.DisplayUtils;


/**
 * 带圆角的异步图片工具
 *
 */
public class AsyncRoundCornerImageView extends AsyncShapedImageView {

    /**
     * 圆角弧度大小
     */
    private float defaultRadiusRatio;

    public AsyncRoundCornerImageView(Context context) {
        super(context);
        init();
    }

    public AsyncRoundCornerImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AsyncRoundCornerImageView(Context context, AttributeSet attrs,int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init(){
        //设置8dp
        defaultRadiusRatio = DisplayUtils.dp2px(getContext(),8);
    }

    @Override
    protected Bitmap getShapedBitmap(Bitmap lruBitmap){
        return BitmapOptUtil.getRoundCornerBitmap(lruBitmap, defaultRadiusRatio);
    }

    @Override
    protected void drawBorder(Canvas canvas, Paint paint) {
        Rect mRect = new Rect(0, 0, getWidth(), getHeight());
        RectF mRectF = new RectF(mRect);
        canvas.drawRoundRect(mRectF, defaultRadiusRatio,defaultRadiusRatio,paint);
    }
}