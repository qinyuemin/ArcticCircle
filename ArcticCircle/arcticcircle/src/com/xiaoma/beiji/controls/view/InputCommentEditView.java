/**
 * 项目名： arcticCircle
 * 包名： com.xiaoma.beiji.controls
 * 版本信息： 1.0.0
 * Copyright (c) 2015上海路路由信息科技有限公司-版权所有
 */
package com.xiaoma.beiji.controls.view;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.makeapp.javase.lang.StringUtil;
import com.xiaoma.beiji.R;
import com.xiaoma.beiji.controls.view.chatting.ChattingCache;
import com.xiaoma.beiji.controls.view.chatting.EmotePageGridAdapter;
import com.xiaoma.beiji.controls.view.chatting.EmotionsEditText;

/**
 * 类名称： InputCommentEditView
 * 类描述：
 * 创建人： gang.shi
 * 修改人： gang.shi
 * 修改时间： 2015年12月16 11:01
 * 修改备注：
 *
 * @version 1.0.0
 */
public class InputCommentEditView extends RelativeLayout implements View.OnClickListener, AdapterView.OnItemClickListener, View.OnTouchListener {

    private Activity activity;
    private GridView gvSmiley;
    private EmotionsEditText mEditText;

    private RelativeLayout rlSmiley, rlSmileyLogo;
    private OnSendClickListener onSendClickListener;


    public InputCommentEditView(Context context) {
        super(context);
        init(context);
    }

    public InputCommentEditView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public InputCommentEditView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        activity = (Activity) context;
        LayoutInflater.from(context).inflate(R.layout.view_input_comment_editor, this, true);

        rlSmileyLogo = (RelativeLayout) findViewById(R.id.rl_smiley_logo);
        rlSmiley = (RelativeLayout) findViewById(R.id.rl_smiley);

        mEditText = (EmotionsEditText) findViewById(R.id.editText);

        gvSmiley = (GridView) findViewById(R.id.gv_smiley);

        rlSmileyLogo.setOnClickListener(this);
        mEditText.setOnTouchListener(this);

        gvSmiley.setAdapter(new EmotePageGridAdapter(context, ChattingCache.mEmoticons));
        gvSmiley.setOnItemClickListener(this);

        mEditText.setImeOptions(EditorInfo.IME_ACTION_GO);
        mEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_GO) {
                    String message = mEditText.getText().toString().trim();
                    if (StringUtil.isValid(message) && onSendClickListener != null) {
                        onSendClickListener.onSendMessage(message);
                    }
                    mEditText.setText("");
                }

                return true;
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_smiley_logo:
                int visibility = rlSmiley.getVisibility();
                if (visibility == View.GONE) {
                    hideKeyBoard();
                    rlSmiley.setVisibility(View.VISIBLE);
                } else {
                    rlSmiley.setVisibility(View.GONE);
                }
                return;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        String text = ChattingCache.mEmoticons.get(i);
        if (mEditText != null && TextUtils.isEmpty(text) == false) {
            int start = mEditText.getSelectionStart();
            CharSequence content = mEditText.formatEmotion(text);
            mEditText.getText().insert(start, content);
            int selection = start + content.length();
            int length = mEditText.length();
            if (selection > length) {
                selection = length;
            }
            mEditText.setSelection(selection);
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (view.getId()) {
            case R.id.editText:
                showKeyBoard();
                return false;
            default:
                break;
        }
        return false;
    }

    public void showKeyBoard() {
        int visibility = rlSmiley.getVisibility();
        if (visibility == View.VISIBLE) {
            rlSmiley.setVisibility(View.GONE);
        }

        mEditText.requestFocus();
        ((InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE)).showSoftInput(mEditText, 0);
    }

    public void hideKeyBoard() {
        ((InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public void setHint(String hint){
        mEditText.setHint(hint);
    }

    public void setOnSendClickListener(OnSendClickListener onSendClickListener) {
        this.onSendClickListener = onSendClickListener;
    }

    public interface OnSendClickListener {
        /**
         * 文本发送
         *
         * @param message
         */
        public void onSendMessage(String message);
    }
}
