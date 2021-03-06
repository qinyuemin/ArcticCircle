package com.xiaoma.beiji.controls.view.chatting;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.widget.EditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmotionsEditText extends EditText {

	public EmotionsEditText(Context context) {
		super(context);
	}

	public EmotionsEditText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public EmotionsEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public void setText(CharSequence text, BufferType type) {
		if (!TextUtils.isEmpty(text)) {
			super.setText(formatEmotion(text), type);
		} else {
			super.setText(text, type);
		}
	}
	
	private Pattern buildPattern() {
		StringBuilder patternString = new StringBuilder(
				ChattingCache.mEmoticons.size() * 3);
		patternString.append('(');
		for (int i = 0; i < ChattingCache.mEmoticons.size(); i++) {
			String s = ChattingCache.mEmoticons.get(i);
			patternString.append(Pattern.quote(s));
			patternString.append('|');
		}
		patternString.replace(patternString.length() - 1,
				patternString.length(), ")");
		return Pattern.compile(patternString.toString());
	}

	public CharSequence formatEmotion(CharSequence text) {
		try {
			SpannableStringBuilder builder = new SpannableStringBuilder(text);
			Pattern pattern = buildPattern();
			Matcher matcher = pattern.matcher(text);
			while (matcher.find()) {
				if (ChattingCache.mEmoticonsId.containsKey(matcher.group())) {
					int id = ChattingCache.mEmoticonsId.get(matcher.group());
					Bitmap bitmap = BitmapFactory.decodeResource(
							getResources(), id);
					if (bitmap != null) {
						ImageSpan span = new ImageSpan(getContext(), bitmap);
						builder.setSpan(span, matcher.start(), matcher.end(),
								Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
					}
				}
			}
			return builder;
		} catch (Exception e) {
			return text;
		}
	}
}
