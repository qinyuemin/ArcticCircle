/**
 *
 * 项目名：
 * 包名： com.lianlian.controls.view
 * 版本信息： 1.0.0
 * Copyright (c) 2015上海路路由信息科技有限公司-版权所有
 *
 */
package com.xiaoma.beiji.controls.view;

import android.content.Context;
import android.widget.GridView;
import android.widget.ListView;

/**
 * 类名称： ExpandListView
 * 类描述： 全展开 ListView
 * 创建人： gang.shi
 * 修改人： gang.shi
 * 修改时间： 2015年01月20 11:22
 * 修改备注：
 *
 * @version 1.0.0
 */
public class ExpandGridView extends GridView {
    public ExpandGridView(Context context) {
        super(context);
    }

    public ExpandGridView(Context context, android.util.AttributeSet attrs) {
        super(context, attrs);
    }

    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
