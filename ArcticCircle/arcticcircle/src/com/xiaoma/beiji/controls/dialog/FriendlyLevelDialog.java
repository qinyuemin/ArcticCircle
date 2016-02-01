/**
 * 项目名： arcticCircle
 * 包名： com.xiaoma.beiji.controls.dialog
 * 版本信息： 1.0.0
 * Copyright (c) 2015上海路路由信息科技有限公司-版权所有
 */
package com.xiaoma.beiji.controls.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.*;
import android.widget.AdapterView;
import android.widget.ListView;
import com.makeapp.android.adapter.ArrayListAdapter;
import com.makeapp.android.util.TextViewUtil;
import com.xiaoma.beiji.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 类名称： FriendlyLevelDialog
 * 类描述：
 * 创建人： gang.shi
 * 修改人： gang.shi
 * 修改时间： 2015年12月03 21:04
 * 修改备注：
 *
 * @version 1.0.0
 */
public class FriendlyLevelDialog extends Dialog {
    private ListView lst;
    private List<String> data;
    private Activity context;
    private OnSelectListener onSelectListener;

    public FriendlyLevelDialog(Activity context, OnSelectListener onSelectListener) {
        super(context, R.style.base_dialog);
        this.context = context;
        this.onSelectListener = onSelectListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.dimAmount = 0.5f;
        lp.width = getWindow().getWindowManager().getDefaultDisplay().getWidth();
        lp.x = 0;
        lp.y = getWindow().getWindowManager().getDefaultDisplay().getHeight();
        // 设置显示位置
        this.onWindowAttributesChanged(lp);
        getWindow().setAttributes(lp);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        //这句必须加上，要不然dialog上会有一段空白（标题栏）
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.dialog_friendly_level);
        setCanceledOnTouchOutside(true);

        getWindow().setWindowAnimations(R.style.AlertListDialogAnimationStyle);

        data = new ArrayList<>();
        data.add("好友");
        data.add("好友的好友");
        data.add("三度好友");
        data.add("取消");

        lst = (ListView) findViewById(R.id.lst);
        ArrayListAdapter<String> adapter = new ArrayListAdapter<String>(context, R.layout.lst_item_friendly_level, data) {
            @Override
            public void fillView(ViewGroup viewGroup, View view, String s, int i) {
                TextViewUtil.setText(view, R.id.txt, s);
            }
        };
        lst.setAdapter(adapter);
        lst.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                dismiss();
                if (onSelectListener != null && i != data.size() -1) {
                    onSelectListener.onItemClick(i+1,data.get(i));
                }
            }
        });
    }

    public interface OnSelectListener {
        void onItemClick(int i,String s);
    }

    @Override
    public void show() {
        super.show();

        Display display = context.getWindowManager().getDefaultDisplay();
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.width = (display.getWidth()); // 设置宽度
        getWindow().setAttributes(lp);
    }
}
