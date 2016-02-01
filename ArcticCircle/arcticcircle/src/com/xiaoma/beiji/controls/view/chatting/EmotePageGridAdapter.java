package com.xiaoma.beiji.controls.view.chatting;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import com.xiaoma.beiji.R;

import java.util.List;

public class EmotePageGridAdapter extends BaseAdapter {

	private List<String> imageDatas;
	private LayoutInflater layoutInflater;

	public EmotePageGridAdapter(Context context, List<String> datas) {
		layoutInflater = LayoutInflater.from(context);
		this.imageDatas = datas;
	}

	public int getCount() {
		return imageDatas.size();
	}

	public Object getItem(int position) {
		return imageDatas.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ImageView view = null;
		if(convertView == null){
		    convertView = view = (ImageView) layoutInflater.inflate(R.layout.gv_item_emote, null);
		}else{
		    view = (ImageView) convertView;
		}
		String emotion = getItem(position).toString();
		int res = ChattingCache.mEmoticonsId.get(emotion);
		
		if (res > 0) {
			view.setImageResource(res);
			view.setTag(res);
		}

		return convertView;
	}

}
