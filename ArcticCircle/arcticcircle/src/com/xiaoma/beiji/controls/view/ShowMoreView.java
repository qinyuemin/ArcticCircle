package com.xiaoma.beiji.controls.view;

import com.xiaoma.beiji.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ShowMoreView extends LinearLayout {

	private LayoutInflater inflater;
	private LinearLayout layout;
	private RelativeLayout more_rl;
	private LinearLayout more_ll;
	private LinearLayout less_ll;
	private TextView input_tv;
	private int lineCount=0;
	private final int LINECOUNT = 3;

	public ShowMoreView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}

	public ShowMoreView(Context context) {
		super(context);
		initView(context);
	}

	private void initView(Context context) {
		inflater = (LayoutInflater) this.getContext().getSystemService(
				Context.LAYOUT_INFLATER_SERVICE);
		layout = (LinearLayout) inflater.inflate(R.layout.show_more_view, this);
		input_tv = (TextView) layout.findViewById(R.id.tv_input);
		more_rl = (RelativeLayout) layout.findViewById(R.id.showmore);
		more_ll = (LinearLayout) layout.findViewById(R.id.showmore_more);
		less_ll = (LinearLayout) layout.findViewById(R.id.showmore_less);
		more_ll.setOnClickListener(new showMoreListener());
		less_ll.setOnClickListener(new showLessListener());
		initShowMore();
	}

	private class showMoreListener implements OnClickListener {

		@Override
		public void onClick(View view) {
			more_rl.setVisibility(VISIBLE);
			more_ll.setVisibility(GONE);
			less_ll.setVisibility(VISIBLE);
			input_tv.setMaxLines(lineCount);
		}
	}

	private class showLessListener implements OnClickListener {

		@Override
		public void onClick(View view) {
			more_rl.setVisibility(VISIBLE);
			more_ll.setVisibility(VISIBLE);
			less_ll.setVisibility(GONE);
			input_tv.setMaxLines(LINECOUNT);
		}
	}

	private void initShowMore() {
		if (input_tv != null) {
			input_tv.post(new Runnable() {
				@Override
				public void run() {
					lineCount=input_tv.getLineCount();
					setLayoutVisibility(lineCount);
				}
			});
		}
	}

	private void setLayoutVisibility(int textLineCount) {
		if (textLineCount <= LINECOUNT) {
			more_rl.setVisibility(GONE);
			more_ll.setVisibility(GONE);
			input_tv.setMaxLines(textLineCount);
		} else {
			more_rl.setVisibility(VISIBLE);
			more_ll.setVisibility(VISIBLE);
			input_tv.setMaxLines(LINECOUNT);
		}
		less_ll.setVisibility(GONE);
	}

	public void setText(String textInput) {
		if (input_tv != null) {
			input_tv.setText(textInput);
			initShowMore();
		}
	}

	public int getTextLine() {
		initShowMore() ;
		return lineCount;
	}

}
