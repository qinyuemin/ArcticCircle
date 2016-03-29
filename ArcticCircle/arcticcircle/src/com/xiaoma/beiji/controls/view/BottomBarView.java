package com.xiaoma.beiji.controls.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import com.xiaoma.beiji.R;

import java.util.ArrayList;
import java.util.List;

public class BottomBarView extends LinearLayout implements OnClickListener {
    public static final String BOTTOM_BAR_POINT_MINE = "bottom.bar.point.mine";

    private PageItem mCurrentPage;
    private List<PageItem> mAllPage;
    private OnPageItemClickListener mOnPageItemClickListener;

    public BottomBarView(Context context) {
        super(context);
        init(context);
    }

    public BottomBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        initAllPage();
        setOrientation(RadioGroup.HORIZONTAL);
        LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        lp.weight = 1;
        LayoutInflater from = LayoutInflater.from(getContext());
        View view = null;
        for (PageItem pageItem : mAllPage) {
            view = from.inflate(R.layout.view_bootom_bar_item, null);
            ImageView iv = (ImageView) view.findViewById(R.id.img);//new ImageView(context);
            ImageView imgPoint = (ImageView) view.findViewById(R.id.img_point);//new ImageView(context);
            if (pageItem == PageItem.MINE) {
            }
            iv.setScaleType(ScaleType.CENTER_INSIDE);
            iv.setImageResource(pageItem.getResId());
            view.setTag(pageItem);
            view.setOnClickListener(this);
            this.addView(view, lp);
        }

        setWeightSum(mAllPage.size());

        setBackgroundColor(getResources().getColor(R.color.white));
//        setBackgroundResource(R.drawable.toolbar_bg);
    }

    private void initAllPage() {
        mAllPage = new ArrayList<PageItem>();
        mAllPage.add(PageItem.INDEX);
        mAllPage.add(PageItem.FIND);
        mAllPage.add(PageItem.STATISTICS);
        mAllPage.add(PageItem.MINE);
    }

    public void setCurrentPage(PageItem pageItem) {
        if (mCurrentPage == pageItem) {
            return;
        }

        int size = mAllPage.size();
        for (int i = 0; i < size; i++) {
            PageItem page = mAllPage.get(i);
            ImageView iv = (ImageView) getChildAt(i).findViewById(R.id.img);
            //此处是做的一个优化，为了避免非mCurrentPage 和 pageItem 的 Item 也被刷新
            if (page == mCurrentPage) {
                iv.setImageResource(page.getResId());
            } else if (page == pageItem) {
                iv.setImageResource(page.getSelectedResId());
            }
        }
        this.mCurrentPage = pageItem;
    }

    @Override
    public void onClick(View v) {
        PageItem pageItem = (PageItem) v.getTag();
        setCurrentPage(pageItem);
        if (mOnPageItemClickListener != null) {
            mOnPageItemClickListener.onClick(pageItem);
        }
    }

    public void setOnPageItemClickListener(OnPageItemClickListener onPageItemClickListener) {
        this.mOnPageItemClickListener = onPageItemClickListener;
    }

    public PageItem getCurrentPage() {
        return mCurrentPage;
    }

    public static enum PageItem {
        INDEX(0, R.drawable.toolbar_circle, R.drawable.toolbar_circle_press),
        FIND(1, R.drawable.toolbar_chat, R.drawable.toolbar_chat_press),
        STATISTICS(2, R.drawable.toolbar_chat, R.drawable.toolbar_chat_press),
        MINE(3, R.drawable.toolbar_mine, R.drawable.toolbar_mine_press);
        private int resId;
        private int selectedResId;
        private int order;

        private PageItem(int order, int resId, int selectedResId) {
            this.order = order;
            this.resId = resId;
            this.selectedResId = selectedResId;
        }

        public int getResId() {
            return resId;
        }

        public int getSelectedResId() {
            return selectedResId;
        }

        public int getOrder() {
            return order;
        }
    }

    public static interface OnPageItemClickListener {
        public void onClick(PageItem pageItem);
    }

    public void toggleMessageDot(boolean showDot) {
        for (int i = 0; i < getChildCount(); i++) {
            View v = getChildAt(i);
            PageItem pageItem = (PageItem) v.getTag();
            if (pageItem != null && pageItem == PageItem.MINE) {
                v.findViewById(R.id.img_point).setVisibility(showDot ? View.VISIBLE : View.GONE);
                break;
            }
        }
    }

}