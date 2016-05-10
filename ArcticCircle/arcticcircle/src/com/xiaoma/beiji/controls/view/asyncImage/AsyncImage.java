package com.xiaoma.beiji.controls.view.asyncImage;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;

/**
 * 定义了不同形状的异步图片工具需实现的基本接口
 *
 */
public abstract class AsyncImage extends RelativeLayout{

    /**
     * 设置缩放模式
     * @param scaleType ImageView缩放模式
     */
    public abstract void setScaleType(ImageView.ScaleType scaleType);

    /**
     * 载入本地图片资源
     * @param imageRes 本地图片资源
     */
    public abstract void setLocalImageRes(int imageRes);

    /**
     * 载入网络url资源制作成不同形状设置给imageView
     * @param imageUrl 网络url
     */
    public abstract void loadUrl(String imageUrl);

    /**
     * 载入网络url资源制作成不同形状设置给imageView,并限定宽高
     * @param imageUrl 网络url
     * @param width imageView宽
     * @param height imageView高
     */
    public abstract void loadUrl(String imageUrl, int width, int height);

    /**
     * 给图片添加描边
     * @param widthDp 描边宽度，单位dp
     * @param color 描边颜色
     */
    public abstract void setBorderWidthAndColor(float widthDp, int color);

    /**
     * 设置加载失败时要显示的图片
     * @param loadFailedImageRes 本地图片资源
     */
    public abstract AsyncImage fail(int loadFailedImageRes);

    /**
     * 设置未加载完成时要显示的图片
     * @param imageResBeforeLoaded 本地图片资源
     */
    public abstract AsyncImage before(int imageResBeforeLoaded);

    /**
     *
     * @param isCrop
     *      true时执行根据imageView的宽高进行centerCrop制作位图切图（不会变形，但会使图片不完整）
     *      false不执行
     *      默认为false
     */
    public abstract AsyncImage setCrop(boolean isCrop);

    /**
     * constructor
     */
    public AsyncImage(Context context) {
        super(context);
    }
    /**
     * constructor
     */
    public AsyncImage(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    /**
     * constructor
     */
    public AsyncImage(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
