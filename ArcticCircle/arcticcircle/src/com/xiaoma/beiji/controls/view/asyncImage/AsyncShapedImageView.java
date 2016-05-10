package com.xiaoma.beiji.controls.view.asyncImage;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.xiaoma.beiji.manager.ThreadManager;
import com.xiaoma.beiji.util.BitmapMemoryCacheUtil;
import com.xiaoma.beiji.util.BitmapOptUtil;
import com.xiaoma.beiji.util.DisplayUtils;
import com.xiaoma.beiji.util.ViewSizeHelper;

import java.util.ArrayList;


/**
 * 不同形状的异步图片工具,需要子类重写{@link #getShapedBitmap}实现新的切图形状（生成新形状的位图）
 * 每张做好的不同形状的位图会由{@link BitmapMemoryCacheUtil}生成内存缓存和sd卡缓存
 *
 * 注：当包版本号改变时，不再使用之前版本的缓存图片资源
 *
 */
public abstract class AsyncShapedImageView extends AsyncImage {

    private Context context;
    private ImageViewContainer imageView;
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions options;

    private BitmapMemoryCacheUtil memoryCacheUtil;

    private int beforeImageRes;
    private int loadFailedImageRes;
    private Class mChildClass;

    /**
     * true时执行根据imageView的宽高进行centerCrop制作位图切图（不会变形，但会使图片不完整）
     * false不执行
     */
    private boolean isCrop;

    //在控件宽高还未确定时，加入待执行，等onLayout时有宽高时再执行
    private ArrayList<ToBeExecutedListener> beExecutedListeners = new ArrayList<>(2);

    public AsyncShapedImageView(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public AsyncShapedImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    public AsyncShapedImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        init();
    }

    public void setScaleType(ImageView.ScaleType scaleType){
        imageView.setScaleType(scaleType);
    }

    private void init() {
        imageView = new ImageViewContainer(context);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);
        this.addView(imageView, lp);
//        options = new DisplayImageOptions.Builder().
//                cacheInMemory(true).cacheOnDisk(true).build();
        options = new DisplayImageOptions.Builder().
                cacheInMemory(true).build();
        memoryCacheUtil = BitmapMemoryCacheUtil.getInstance(getContext());
        mChildClass = getClass();
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (beExecutedListeners.size() != 0) {
            for(int i = beExecutedListeners.size() - 1;i >= 0;i--){
                ToBeExecutedListener listener = beExecutedListeners.get(i);
                listener.execute();
                beExecutedListeners.remove(i);
            }
        }
    }

    private void addToBeExecuted(ToBeExecutedListener listener) {
        if (!beExecutedListeners.contains(listener)) {
            beExecutedListeners.add(listener);
        }
    }

    private interface ToBeExecutedListener{
        void execute();
    }

    /**
     * 载入网络url资源制作成不同形状设置给imageView
     * @param imageUrl 图片url
     */
    public void loadUrl(String imageUrl) {
        if (!TextUtils.isEmpty(imageUrl)) {
            setResourceProxy(imageUrl);
        }
    }

    /**
     * 把资源图片制作成不同形状设置给imageView
     * @param imageRes 本地图片资源id
     */
    public void setLocalImageRes(int imageRes) {
        setResourceProxy(String.valueOf(imageRes));
    }

    public void loadUrl(String imageUrl, int width, int height) {
        ViewSizeHelper.getInstance().setWidth(this, width);
        ViewSizeHelper.getInstance().setHeight(this, height);
        ViewSizeHelper.getInstance().setWidth(imageView, width);
        ViewSizeHelper.getInstance().setHeight(imageView, height);
        loadUrl(imageUrl);
    }

    /**
     * 包的版本号、子类名、图片url、控件的宽和高共同决定一个缓存的Key
     *
     * 适配不同的形状子类使用相同资源不同剪裁不同比例的缓存
     *
     * 注：当包版本号改变时，不再使用上个版本的缓存资源
     */
    private String createCacheKey(Class clazz,String url,int width,int height){
        return memoryCacheUtil.getAppVersion(context) + clazz.getSimpleName() + url + width + height;
    }

    private SimpleImageLoadingListener simpleImageLoadingListener = new SimpleImageLoadingListener() {

        @Override
        public void onLoadingStarted(String imageUri, View view) {
            if (beforeImageRes != 0) setLocalImageRes(beforeImageRes);
        }

        @Override
        public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
            if (loadFailedImageRes != 0) setLocalImageRes(loadFailedImageRes);
        }

        @Override
        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
            String cacheKey = createCacheKey(mChildClass, imageUri, getWidth(), getHeight());
            setLruBitmap(cacheKey, loadedImage);
        }
    };

    /**
     * 通用，给imageView设置加过缓存的位图
     */
    private void setLruBitmap(final String cacheKey, final Bitmap lruBitmap) {
        ThreadManager.executeInBackground(new Runnable() {
            @Override
            public void run() {
                Bitmap b = lruBitmap;
                if (isCrop) {
                    b = BitmapOptUtil.resizeBitmapByCenterCrop(lruBitmap, getWidth(), getHeight());
                }
                Bitmap cropCornerBitmap = getShapedBitmap(b);
                post(new SetImageBitmapRunnable(cropCornerBitmap));
                memoryCacheUtil.put(cacheKey, cropCornerBitmap);
            }
        });
    }

    /**
     * 实现新的切图形状，只会在内存缓存和sd卡缓存都不存在时创建
     * @param lruBitmap 原图
     * @return 实现的新形状位图
     */
    protected abstract Bitmap getShapedBitmap(Bitmap lruBitmap);

    private void setResourceProxy(final String resource) {
        if (getWidth() == 0 || getHeight() == 0) {
            addToBeExecuted(new ToBeExecutedListener() {
                @Override
                public void execute() {
                    setResource(resource);
                }
            });
        } else {
            setResource(resource);
        }
    }

    private void setResource(String resource){
        String cacheKey = createCacheKey(mChildClass, resource, getWidth(), getHeight());
        Bitmap cache = memoryCacheUtil.get(cacheKey);
        if (cache != null) {
            imageView.setShapedImageBitmap(cache);
        } else {
            if (TextUtils.isDigitsOnly(resource)) {//静态资源
                try {
                    Bitmap src = BitmapFactory.decodeStream(getResources().openRawResource(Integer.valueOf(resource)));
                    setLruBitmap(cacheKey, src);
                } catch (Resources.NotFoundException e) {
                }
            } else {//网络资源
                imageLoader.displayImage(resource, imageView, options, simpleImageLoadingListener);
            }
        }
    }

    public AsyncShapedImageView fail(int loadFailedImageRes) {
        this.loadFailedImageRes = loadFailedImageRes;
        return this;
    }


    public AsyncShapedImageView before(int imageResBeforeLoaded) {
        beforeImageRes = imageResBeforeLoaded;
        return this;
    }

    public AsyncShapedImageView setCrop(boolean crop) {
        isCrop = crop;
        return this;
    }

    /**
     * 设置图片的不同宽度和颜色的描边
     * @param widthDp
     * @param color
     */
    public void setBorderWidthAndColor(float widthDp,int color){
        imageView.getPaint().setStrokeWidth(DisplayUtils.dp2px(getContext(), widthDp));
        imageView.getPaint().setColor(color);
        invalidate();
    }

    /**
     * 子类实现其自身要画的边缘线形状
     * @param canvas
     * @param paint
     */
    protected abstract void drawBorder(Canvas canvas,Paint paint);

    private class ImageViewContainer extends ImageView {

        private Paint borderPaint;

        Paint getPaint() {
            if (borderPaint == null) {
                borderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
                borderPaint.setStyle(Paint.Style.STROKE);
                borderPaint.setDither(true);
            }
            return borderPaint;
        }

        public ImageViewContainer(Context context) {
            super(context);
        }

        public ImageViewContainer(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        /**
         * 什么都不做，去除imageLoader自动给imageView放置位图的影响
         */
        @Override
        public void setImageBitmap(Bitmap bm) {}

        public void setShapedImageBitmap(Bitmap bm) {
            super.setImageBitmap(bm);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            if (borderPaint != null) {
                drawBorder(canvas, borderPaint);
            }
        }
    }

    private class SetImageBitmapRunnable implements Runnable {

        private Bitmap cropBitmap;

        SetImageBitmapRunnable(Bitmap bitmap){
            cropBitmap = bitmap;
        }

        @Override
        public void run() {
            imageView.setShapedImageBitmap(cropBitmap);
        }
    }
}