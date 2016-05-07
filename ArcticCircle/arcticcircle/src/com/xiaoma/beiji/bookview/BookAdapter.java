package com.xiaoma.beiji.bookview;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.xiaoma.beiji.R;
import com.xiaoma.beiji.util.ImageLoaderUtil;


public class BookAdapter implements IAdapter{
	private List<String> strList = new ArrayList<String>();
	DisplayImageOptions mOptions;
	
	private Context mContext;
	public BookAdapter(Context context) {
		super();
		this.mContext = context;
	}
	public void setStrList(List<String> list){
		strList=list;
	}
	public int getCount() {
		return strList.size();
	}

	public Integer getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}

	public ImageView getView(int position) {
		String data = strList.get(position);
		View v = LayoutInflater.from(mContext).inflate(R.layout.view_img, null);
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

}
