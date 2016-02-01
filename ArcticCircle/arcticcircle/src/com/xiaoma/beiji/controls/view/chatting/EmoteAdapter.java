package com.xiaoma.beiji.controls.view.chatting;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import com.common.android.lib.controls.view.indicator.IconPagerAdapter;
import com.xiaoma.beiji.R;

import java.util.List;

public class EmoteAdapter extends PagerAdapter implements IconPagerAdapter {

	private Context context;
	private List<String> lists;
	private OnItemClickListener itemClickListener;
	private int pageCount = 0;
	private LayoutInflater mInflater;
	public  static final int IMG_COUNT_ONE_PAGE = 21;
	
	public EmoteAdapter(Context context, OnItemClickListener itemClickListener, List<String> lists) {
		this.context = context;
		this.lists = lists;
		this.itemClickListener = itemClickListener;

		mInflater = LayoutInflater.from(context);
		pageCount = (lists.size() +(IMG_COUNT_ONE_PAGE-1))/IMG_COUNT_ONE_PAGE;
	}

	@Override
	public int getCount() {
		return pageCount;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		
	}

	@Override
	public Object instantiateItem(ViewGroup container, int pos) {
			
		GridView gridView = (android.widget.GridView) mInflater.inflate(R.layout.layout_emotion_gridview, null);
		
		int position = pos;
		int start = position*IMG_COUNT_ONE_PAGE;
		int end = position*IMG_COUNT_ONE_PAGE + IMG_COUNT_ONE_PAGE;
		if(end>lists.size()){
			end = lists.size();
		}
		List<String> subList = lists.subList(start, end);
		
		gridView.setAdapter(new EmotePageGridAdapter(context,subList));
		gridView.invalidate();
		//gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
		gridView.setOnItemClickListener(itemClickListener);
		gridView.setId(position);
		
		container.addView(gridView);
		return gridView;
	}
	
	@Override
	public int getIconResId(int index) {
		return R.drawable.sl_icon_pageview_indicator;
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}

}
