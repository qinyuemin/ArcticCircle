/**
 * 项目名： trunk
 * 包名： com.lianlian.controls.view
 * 版本信息： 1.0.0
 * Copyright (c) 2015上海路路由信息科技有限公司-版权所有
 */
package com.common.android.lib.controls.view.pulltorefresh.internal;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import com.common.android.lib.R;
import com.common.android.lib.controls.view.pulltorefresh.ILoadingLayout;
import com.common.android.lib.controls.view.pulltorefresh.PullToRefreshBase;

/**
 *
 * 类名称： WifiLoadingLayout
 * 类描述： wifi列表下拉刷新 UI
 * 创建人： gang.shi
 * 修改人： gang.shi
 * 修改时间： 2015年04月15 11:41
 * 修改备注：
 * @version 1.0.0
 *
 */
public class WifiLoadingLayout extends LoadingLayout implements ILoadingLayout {

    private AnimationDrawable loadingAnimation;
    private Animation animation;


    public WifiLoadingLayout(Context context, PullToRefreshBase.Mode mode, PullToRefreshBase.Orientation scrollDirection, TypedArray attrs) {
        super(context, mode, scrollDirection, attrs);
//        loadingAnimation = AnimationUtils.loadAnimation(context,R.anim.anim_wifi_loading);
        animation = AnimationUtils.loadAnimation(context,R.anim.anim_wifi_to_loading);
    }

    @Override
    protected int getLayoutResId(PullToRefreshBase.Orientation orientation) {
        int resId;
        if(orientation == PullToRefreshBase.Orientation.HORIZONTAL){
            resId = R.layout.pull_to_refresh_header_horizontal;
        }else {
            resId = R.layout.pull_to_refresh_header_vertical_wifi;
        }
        return resId;
    }

    @Override
    protected int getDefaultDrawableResId() {
        return -1;
    }

    @Override
    protected void onLoadingDrawableSet(Drawable imageDrawable) {

    }

    @Override
    protected void onPullImpl(float scaleOfLayout) {
        mHeaderImage.setImageDrawable(null);

    }

    @Override
    protected void pullToRefreshImpl() {

    }

    @Override
    protected void refreshingImpl() {
        mHeaderImage.setImageResource(R.drawable.ic_wifi_refresh_1);
        mHeaderImage.requestLayout();
        mHeaderImage.startAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                // 启动动画
                mHeaderImage.setImageResource(R.drawable.anim_wifi_loading);
                loadingAnimation = (AnimationDrawable) mHeaderImage.getDrawable();
                loadingAnimation.start();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });


    }

    @Override
    protected void releaseToRefreshImpl() {

    }

    @Override
    protected void resetImpl() {
        mHeaderImage.setImageDrawable(null);

    }
}
