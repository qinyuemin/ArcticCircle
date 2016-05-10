package com.xiaoma.beiji.controls.view.asyncImage;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.xiaoma.beiji.util.BitmapOptUtil;


/**
 * 异步模糊图片
 *
 */
public class AsyncBlurImageView extends AsyncImageView{

    public AsyncBlurImageView(Context context) {
        super(context);
        setScaleType(ImageView.ScaleType.CENTER_CROP);
    }

    public AsyncBlurImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setScaleType(ImageView.ScaleType.CENTER_CROP);
    }

    public AsyncBlurImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setScaleType(ImageView.ScaleType.CENTER_CROP);
    }

    @Override
    protected Bitmap getShapedBitmap(Bitmap lruBitmap) {
        return BitmapOptUtil.doBlur(lruBitmap);
    }



}
