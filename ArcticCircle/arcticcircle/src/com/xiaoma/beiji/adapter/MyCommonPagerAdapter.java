/**
 * 项目名： arcticCircle
 * 包名： com.xiaoma.beiji.adapter
 * 版本信息： 1.0.0
 * Copyright (c) 2015 版权所有
 */
package com.xiaoma.beiji.adapter;

/**
 *
 * 类名称： MyCommonPagerAdapter
 * 类描述： 
 * 创建人： gang.shi
 * 修改人： gang.shi
 * 修改时间： 2015年12月14 21:36
 * 修改备注：
 * @version 1.0.0
 *
 */

import android.view.LayoutInflater;
import com.common.android.lib.controls.view.indicator.IconPagerAdapter;
import com.xiaoma.beiji.R;

public class MyCommonPagerAdapter<String> extends CommonPagerAdapter<String> implements IconPagerAdapter {
    public MyCommonPagerAdapter(LayoutInflater inf, ViewCreator<String> creator) {
        super(inf, creator);
    }

    @Override
    public int getIconResId(int index) {
        return R.drawable.circle_indicator;
    }


}