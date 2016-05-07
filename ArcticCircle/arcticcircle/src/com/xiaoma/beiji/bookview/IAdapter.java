package com.xiaoma.beiji.bookview;

import android.view.View;

public interface IAdapter {
	public int getCount();
	public Integer getItem(int position);
	public long getItemId(int position);
	public View getView(int position);
}
