package com.xiaoma.beiji.controls.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.common.android.lib.controls.view.indicator.IconPageIndicator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.xiaoma.beiji.R;
import com.xiaoma.beiji.adapter.CommonPagerAdapter;
import com.xiaoma.beiji.adapter.MyCommonPagerAdapter;
import com.xiaoma.beiji.util.ImageLoaderUtil;

import java.util.List;

public class ImgPagerView extends RelativeLayout {
    private ViewPager mViewPager;
    private IconPageIndicator mIndicator;
    private MyCommonPagerAdapter<String> pagerAdapter;
    private Runnable autoRollRunnable;

    private final int PAGE_SCROLL_SPEED = 3000;

    private Activity activity;

    public ImgPagerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ImgPagerView(Context context) {
        super(context);
        init(context);
    }

    private void init(final Context context) {
        View rootView = LayoutInflater.from(getContext().getApplicationContext()).inflate(R.layout.view_img_pager, this, true);
        mViewPager = (ViewPager) rootView.findViewById(R.id.ad_viewpager);
        mIndicator = (IconPageIndicator) rootView.findViewById(R.id.ad_indicator);
        pagerAdapter = new MyCommonPagerAdapter<>(LayoutInflater.from(context),viewCreator);
        mViewPager.setAdapter(pagerAdapter);
        mIndicator.setViewPager(mViewPager);
    }

    private CommonPagerAdapter.ViewCreator<String> viewCreator = new CommonPagerAdapter.ViewCreator<String>() {
        DisplayImageOptions mOptions;

        @Override
        public View createView(LayoutInflater inflater, final int position, final String data) {
            View v = inflater.inflate(R.layout.view_img, null);
            final ImageView imageView = (ImageView) v.findViewById(R.id.img);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                }
            });
            if (mOptions == null) {
                mOptions = ImageLoaderUtil.getDefaultImageLoaderOptionBuilder().build();
            }
            ImageLoader.getInstance().displayImage(data, imageView, mOptions);
            return imageView;
        }

        @Override
        public void updateView(View view, int position, String data) {

        }

        @Override
        public void releaseView(View view, String data) {
            Bitmap bitmap = null;
            if (view instanceof ImageView) {
                Drawable d = ((ImageView) view).getDrawable();
                if (d instanceof BitmapDrawable) {
                    bitmap = ((BitmapDrawable) d).getBitmap();
                }
            }
            if (bitmap != null && bitmap.isRecycled() == false) {
                bitmap.recycle();
                bitmap = null;
            }
        }

    };


    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
//        startAutoRoll();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopAutoRoll();
    }

    public void notifyData(List<String> dataList) {
        if (dataList != null && !dataList.isEmpty()) {
            setVisibility(View.VISIBLE);
            pagerAdapter.toggleForceUpdate(true);
            pagerAdapter.update(dataList);
            if (dataList.size() > 1) {
                mIndicator.setVisibility(View.VISIBLE);
                mIndicator.notifyDataSetChanged();

                startAutoRoll();
            } else {
                mIndicator.setVisibility(View.GONE);
            }
        } else {
            setVisibility(View.GONE);
        }
    }

    @Override
    public void setVisibility(int visibility) {
        if (visibility == View.VISIBLE) {
            if (getVisibility() != View.VISIBLE) {
                startAutoRoll();
            }
        } else {
            if (getVisibility() == View.VISIBLE) {
                stopAutoRoll();
            }
        }
        super.setVisibility(visibility);
    }

    private void startAutoRoll() {
        if (autoRollRunnable == null) {
            autoRollRunnable = new Runnable() {
                @Override
                public void run() {
                    if (getVisibility() == View.VISIBLE) {
                        int position = mViewPager.getCurrentItem();
                        position += 1;
                        if (position < pagerAdapter.getCount()) {
                            mViewPager.setCurrentItem(position);
                        } else {
                            mViewPager.setCurrentItem(0);
                        }
                        callAdAutoRoll(autoRollRunnable, PAGE_SCROLL_SPEED);
                    }
                }
            };
        }

        callAdAutoRoll(autoRollRunnable, PAGE_SCROLL_SPEED);
    }

    private void callAdAutoRoll(Runnable action, long delayMillis) {
        removeCallbacks(action);
        postDelayed(action, delayMillis);
    }

    private void stopAutoRoll() {
        removeCallbacks(autoRollRunnable);
    }
}
