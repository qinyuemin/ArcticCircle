package com.xiaoma.beiji.controls.view.chatting;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.ViewPager;
import android.text.InputFilter;
import android.text.InputFilter.LengthFilter;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.*;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;
import com.common.android.lib.controls.view.indicator.IconPageIndicator;
import com.xiaoma.beiji.R;
import com.xiaoma.beiji.adapter.LLBaseAdapter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * 在emotionEditor的基础上修改的，原来用emotionEditor的地方，可以替换成这个。 加了语音的功能
 */
public class InputEditorView extends RelativeLayout implements OnClickListener, OnItemClickListener, OnTouchListener {

    private boolean isKeyBoardShow = false;

    private boolean showEmotion = true;
    private boolean showTakePhoto = true;
    private boolean showAlbum = true;
    private boolean showBurnAfterRead = true;
    private boolean showShotVideo = true;
    private boolean showTuYa = true;
    private boolean showLocation = true;
    private boolean showFile = true;
    private boolean showEmpty = false;

    private Activity mActivity;
    private ImageButton switchInputMode;
    private EmotionsEditText mEditText;
    private View mSendBtn;

    private View mEmotionInputArea;
    private View mEmotionViewpagerLayout;
    private View ll_edit;

    private IconPageIndicator mIconPageIndicator;
    private ViewPager mViewPager;
    private EmoteAdapter mViewPagerAdapter;

    private OnSendClickListener onSendClickListener;
    private OnItemTypeClickListener onItemTypeClickListener;

    private GridView gridViewItemType;
    private ItemTypeAdapter typeAdapter;
    private LayoutInflater layoutInflater;
    private View mengban;
    private InputMethodManager imm;
    private int minMoveDistance = 0;

    private SwitchType switchType = SwitchType.EMOTION;
    private int[] drawableIds = new int[]{R.drawable.sl_icon_editor_menu_express_big
            ,
            R.drawable.sl_icon_editor_menu_photo,
            R.drawable.sl_icon_editor_menu_album,
//            R.drawable.icon_editor_menu_burn_after_reading,
//            R.drawable.sl_icon_editor_menu_shot_video,
//            R.drawable.icon_editor_menu_graffiti,
//            R.drawable.sl_icon_editor_menu_location,
//            R.drawable.icon_editor_menu_file,
//            R.drawable.shape_empty
    };

    public static enum SwitchType {
        EMOTION(0), PLUS(1), KEYBOARD(100), VOICE(2);

        static SwitchType mapIntToValue(final int modeInt) {
            for (SwitchType value : SwitchType.values()) {
                if (modeInt == value.getIntValue()) {
                    return value;
                }
            }

            // If not, return default
            return getDefault();
        }

        static SwitchType getDefault() {
            return EMOTION;
        }

        private int mIntValue;

        // The modeInt values need to match those from attrs.xml
        SwitchType(int value) {
            mIntValue = value;
        }

        int getIntValue() {
            return mIntValue;
        }
    }

    public void setMengban(final View mengban, final View moveView) {
        if (mengban != null && moveView != null) {
            this.mengban = mengban;
            this.mengban.setOnTouchListener(new View.OnTouchListener() {
                float dx = 0;
                float dy = 0;
                long dTime = 0;
                boolean isMoved = false;

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            dx = event.getX();
                            dy = event.getY();
                            dTime = System.currentTimeMillis();
                        case MotionEvent.ACTION_MOVE:
                            Log.i("MotionEvent", "ACTION_MOVE:" + isMoved);
                            if (!isMoved) {
                                float distanceX = dx - event.getX();
                                float distanceY = dy - event.getY();
                                if (Math.abs(distanceX) > minMoveDistance || Math.abs(distanceY) > minMoveDistance) {
                                    isMoved = true;
                                }
                            }
                            break;
                        case MotionEvent.ACTION_CANCEL:
                            isMoved = false;
                            break;
                        case MotionEvent.ACTION_UP:
                            if (!isMoved) {
                                if (System.currentTimeMillis() - dTime <= ViewConfiguration.getTapTimeout()) {
                                    mengban.setVisibility(View.GONE);
                                    if (isBottomPanelShow()) {
                                        hideBottonPanel();
                                    }
                                    if (imm.isActive()) {
                                        imm.hideSoftInputFromWindow(mEditText.getWindowToken(), 0); // 强制隐藏键盘
                                    }
                                }
                            }
                            isMoved = false;
                            break;
                        default:
                            break;
                    }
                    moveView.dispatchTouchEvent(event);
                    return true;
                }
            });
        }

        // 监听键盘弹起隐藏事件
        KeyboardViewInterface keyboardViewInterface = this.getKeyboardView(this);
        if (keyboardViewInterface != null) {
            keyboardViewInterface.addOnKeyboardStateChangeListener(new OnKeyboardStateChangeListener() {
                @Override
                public void onKeyBoardStateChange(int state) {
                    switch (state) {
                        case KEYBOARD_STATE_HIDE:
                            mengban.setVisibility(View.GONE);
                            break;
                        case KEYBOARD_STATE_SHOW:
                            mengban.setVisibility(View.VISIBLE);
                            break;
                        default:
                            break;
                    }
                }
            });
        }

    }

    public InputEditorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttr(context, attrs);
        init(context);
    }

    public InputEditorView(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        minMoveDistance = ViewConfiguration.getTouchSlop();
        mActivity = (Activity) context;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LayoutInflater.from(context).inflate(R.layout.view_input_editor, this, true);
        View view = findViewById(R.id.switchInputModeArea);
        ll_edit = findViewById(R.id.ll_edit);
        gridViewItemType = (GridView) findViewById(R.id.grid_item_type);
        if (switchType == SwitchType.KEYBOARD) {
            view.setVisibility(View.GONE);
        }

        switchInputMode = (ImageButton) view.findViewById(R.id.switchInputMode);
        if (switchType == SwitchType.EMOTION) {
            switchInputMode.setImageResource(R.drawable.btn_editor_emotion);
        } else if (switchType == SwitchType.PLUS) {
            switchInputMode.setImageResource(R.drawable.sl_btn_editor_plus);
        }

        mEditText = (EmotionsEditText) findViewById(R.id.editText);
        // btn_recorder.setOnTouchListener(this);
        mSendBtn = findViewById(R.id.send);

        mEmotionInputArea = findViewById(R.id.emotionInputArea);
        mEmotionViewpagerLayout = findViewById(R.id.emotionViewpagerLayout);

        mViewPager = (ViewPager) findViewById(R.id.emotionViewpager);
        mIconPageIndicator = (IconPageIndicator) findViewById(R.id.emotionIndicator);

        mSendBtn.setOnClickListener(this);

        switchInputMode.setOnClickListener(this);

        mEditText.setOnTouchListener(this);

        initEmotionViewPager();

        // 初始化输入类型，表情，拍照，相册等等。
        List<ItemTypeBean> list = new ArrayList<InputEditorView.ItemTypeBean>();
        if (showEmotion) {
            ItemTypeBean typeBean = new ItemTypeBean();
            typeBean.type = ItemType.Emotion;
            typeBean.imgid = drawableIds[0];
            typeBean.typeName = "表情";
            list.add(typeBean);
        }
        if (showTakePhoto) {
            ItemTypeBean typeBean = new ItemTypeBean();
            typeBean.type = ItemType.TakePhoto;
            typeBean.imgid = drawableIds[1];
            typeBean.typeName = "拍照";
            list.add(typeBean);
        }
        if (showAlbum) {
            ItemTypeBean typeBean = new ItemTypeBean();
            typeBean.type = ItemType.Album;
            typeBean.imgid = drawableIds[2];
            typeBean.typeName = "相册";
            list.add(typeBean);
        }
        if (showEmpty) {
            ItemTypeBean typeBean = new ItemTypeBean();
            typeBean.type = ItemType.Empty;
            typeBean.imgid = drawableIds[8];
            typeBean.typeName = "  ";
            list.add(typeBean);
        }
        typeAdapter = new ItemTypeAdapter(mActivity, list);
        gridViewItemType.setAdapter(typeAdapter);
        gridViewItemType.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ItemTypeBean typeBean = typeAdapter.getItem(position);
                if (typeBean.type == ItemType.Emotion) {
                    showEmotionPanel();
                } else {
                    if (onItemTypeClickListener != null) {
                        onItemTypeClickListener.onTypeClicked(typeAdapter.getItem(position).type);
                    }
                }
            }
        });
    }

    private void initAttr(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.Emotion_Editor);
        if (a.hasValue(R.styleable.Emotion_Editor_Switch_Type)) {
            switchType = SwitchType.mapIntToValue(a.getInteger(R.styleable.Emotion_Editor_Switch_Type, 0));
        }
        if (a.hasValue(R.styleable.Emotion_Editor_show_emotion)) {
            showEmotion = a.getBoolean(R.styleable.Emotion_Editor_show_emotion, true);
        }
        if (a.hasValue(R.styleable.Emotion_Editor_show_takePhoto)) {
            showTakePhoto = a.getBoolean(R.styleable.Emotion_Editor_show_takePhoto, true);
        }
        if (a.hasValue(R.styleable.Emotion_Editor_show_album)) {
            showAlbum = a.getBoolean(R.styleable.Emotion_Editor_show_album, true);
        }
        if (a.hasValue(R.styleable.Emotion_Editor_show_burnAfterRead)) {
            showBurnAfterRead = a.getBoolean(R.styleable.Emotion_Editor_show_burnAfterRead, true);
        }
        if (a.hasValue(R.styleable.Emotion_Editor_show_shotVideo)) {
            showShotVideo = a.getBoolean(R.styleable.Emotion_Editor_show_shotVideo, true);
        }
        if (a.hasValue(R.styleable.Emotion_Editor_show_tuYa)) {
            showTuYa = a.getBoolean(R.styleable.Emotion_Editor_show_tuYa, true);
        }
        if (a.hasValue(R.styleable.Emotion_Editor_show_location)) {
            showLocation = a.getBoolean(R.styleable.Emotion_Editor_show_location, true);
        }
        if (a.hasValue(R.styleable.Emotion_Editor_show_file)) {
            showFile = a.getBoolean(R.styleable.Emotion_Editor_show_file, true);
        }
        if (a.hasValue(R.styleable.Emotion_Editor_show_empty)) {
            showEmpty = a.getBoolean(R.styleable.Emotion_Editor_show_file, false);
        }
        a.recycle();
    }

    private void initEmotionViewPager() {
        mViewPagerAdapter = new EmoteAdapter(getContext(), this, ChattingCache.mEmoticons);
        mViewPager.setAdapter(mViewPagerAdapter);
        mIconPageIndicator.setViewPager(mViewPager);
        mViewPager.setCurrentItem(0);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.switchInputMode:
                // 当前键盘没显示
                if (isBottomPanelShow()) {
                    // 显示键盘，
                    showKeyBoard();
                    // 切换图标
                    showSwitchInputMode(true);

                } else {
                    hideKeyBoard();
                    showSwitchInputMode(false);
                    // 此处延迟显示Panel的原因是为了确保键盘关闭动作完成(键盘关闭是一个需要一点点时间完成的动作)，不会导致UI上出现闪动的情况，详询DingWentao
                    getHandler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            showBottomPanel();
                        }
                    }, 50);
                }
                if (mengban != null) {
                    mengban.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.send:
                if (onSendClickListener != null) {
                    String message = mEditText.getText().toString().trim();
                    if (TextUtils.isEmpty(message) == false) {
                        onSendClickListener.onSendMessage(message);
                    }
                    hideBottonPanel();
                    mEditText.setText("");
                    mEditText.setHint("");
                }
                break;
        }
    }

    public void setOnItemTypeClickListener(OnItemTypeClickListener onItemTypeClickListener) {
        this.onItemTypeClickListener = onItemTypeClickListener;
    }

    private void showSwitchInputMode(boolean keyboardShowing) {
        if (keyboardShowing) {
            if (switchType == SwitchType.EMOTION) {
                switchInputMode.setImageResource(R.drawable.btn_editor_emotion);
            } else if (switchType == SwitchType.PLUS || switchType == SwitchType.VOICE) {
                switchInputMode.setImageResource(R.drawable.sl_btn_editor_plus);
            }
        } else {
            switchInputMode.setImageResource(R.drawable.sl_btn_editor_keyboard);
        }
    }

    private void showBottomPanel() {
        if (switchType == SwitchType.EMOTION) {
            showEmotionPanel();
        } else if (switchType == SwitchType.PLUS || switchType == switchType.VOICE) {
            mEmotionViewpagerLayout.setVisibility(View.GONE);
            mEmotionInputArea.setVisibility(View.VISIBLE);
        }
    }

    private void showEmotionPanel() {
        mEmotionViewpagerLayout.setVisibility(View.VISIBLE);
        mEmotionInputArea.setVisibility(View.GONE);
    }

    public void setText(CharSequence text) {
        mEditText.setText(text);
    }

    public void setText(int resid) {
        mEditText.setText(resid);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (position == 20 || (parent.getId() == 4 && position == 10)) {
            if (mEditText != null) {
                int start = mEditText.getSelectionStart();
                String content = mEditText.getText().toString().trim();
                if (TextUtils.isEmpty(content)) {
                    return;
                }
                String startContent = content.substring(0, start);
                String endContent = content.substring(start, content.length());
                String lastContent = content.substring(start - 1, start);
                int last = startContent.lastIndexOf("[/");
                int lastChar = startContent.substring(0, startContent.length() - 1).lastIndexOf("]");

                if ("]".equals(lastContent) && last > lastChar) {
                    if (last != -1) {
                        mEditText.setText(startContent.substring(0, last) + endContent);
                        // 定位光标位置
                        CharSequence info = mEditText.getText();
                        if (info instanceof Spannable) {
                            Spannable spanText = (Spannable) info;
                            Selection.setSelection(spanText, last);
                        }
                        return;
                    }
                }
                mEditText.setText(startContent.substring(0, start - 1) + endContent);
                // 定位光标位置
                CharSequence info = mEditText.getText();
                if (info instanceof Spannable) {
                    Spannable spanText = (Spannable) info;
                    Selection.setSelection(spanText, start - 1);
                }
            }
        } else {
            String text = null;
            text = ChattingCache.mEmoticons.get(position + parent.getId() * EmoteAdapter.IMG_COUNT_ONE_PAGE);
            if (mEditText != null && TextUtils.isEmpty(text) == false) {
                // 采用先将文本格式化为图片，然后插入到editText中，效率会更高
                int editTextLength = mEditText.length();
                int editTextMaxLength = getMaxLength(mEditText);
                int textLength = text.length();
                if (editTextMaxLength != -1 && editTextLength + textLength >= editTextMaxLength) {
                    //超出最大输入字符限制
                    return;
                }

                int start = mEditText.getSelectionStart();
                CharSequence content = mEditText.formatEmotion(text);
                mEditText.getText().insert(start, content);
                int selection = start + content.length();
                int length = mEditText.length();
                if (selection > length) {
                    selection = length;
                }
                mEditText.setSelection(selection);
                // CharSequence content = mEditText.getText().insert(start,
                // text);
                // mEditText.setText(content);
                // CharSequence info = mEditText.getText();
                // 定位光标位置
                // if (info instanceof Spannable) {
                // Spannable spanText = (Spannable) info;
                // Selection.setSelection(spanText, start + text.length());
                // }
            }
        }

    }

    public int getMaxLength(EditText editText) {
        int maxLength = -1;
        try {
            InputFilter[] inputFilters = editText.getFilters();
            for (InputFilter filter : inputFilters) {
                if (filter instanceof LengthFilter) {
                    Class<?> c = filter.getClass();
                    Field[] fields = c.getDeclaredFields();
                    for (Field f : fields) {
                        if (f.getName().equals("mMax")) {
                            f.setAccessible(true);
                            maxLength = (Integer) f.get(filter);
                            break;
                        }
                    }
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return maxLength;
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

    public interface OnItemTypeClickListener {
        /**
         * position从0开始
         *
         * @param
         */
        public void onTypeClicked(ItemType type);
    }

    public EditText getEditText() {
        return mEditText;
    }

    public boolean isBottomPanelShow() {
        return this.mEmotionInputArea.getVisibility() == View.VISIBLE || mEmotionViewpagerLayout.getVisibility() == View.VISIBLE;
    }

    public void hideBottonPanel() {
        mEmotionViewpagerLayout.setVisibility(View.GONE);
        mEmotionInputArea.setVisibility(View.GONE);
        switchInputMode.setImageResource(R.drawable.sl_btn_editor_plus);
    }

    public void showKeyBoard() {
        if (isBottomPanelShow()) {
            mEmotionViewpagerLayout.setVisibility(View.GONE);
            mEmotionInputArea.setVisibility(View.GONE);
        }
        mEditText.requestFocus();
        ((InputMethodManager) mActivity.getSystemService(Activity.INPUT_METHOD_SERVICE)).showSoftInput(mEditText, 0);
        setKeyBoardShow(true);
    }

    public void hideKeyBoard() {
        ((InputMethodManager) mActivity.getSystemService(Activity.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(mActivity.getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
        setKeyBoardShow(false);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (v.getId()) {
            case R.id.editText:
                showKeyBoard();
                showSwitchInputMode(true);
                if (mengban != null) {
                    mengban.setVisibility(View.VISIBLE);
                }
                return false;
            default:
                break;
        }
        return false;
    }

    public boolean isKeyBoardShow() {
        return isKeyBoardShow;
    }

    public void setKeyBoardShow(boolean isKeyBoardShow) {
        this.isKeyBoardShow = isKeyBoardShow;
    }

    public static enum ItemType {
        Emotion, TakePhoto, Album, BurnAfterRead, ShotVideo, TuYa, Location, File, Empty
    }

    class ItemTypeAdapter extends LLBaseAdapter<ItemTypeBean> {

        public ItemTypeAdapter(Activity activity, List<ItemTypeBean> dataList) {
            super(activity, dataList);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder vh = null;
            if (convertView == null) {
                convertView = layoutInflater.inflate(R.layout.gv_item_chat_input_type, null);
                vh = new ViewHolder();
                vh.img_type = (ImageView) convertView.findViewById(R.id.img_type);
                vh.tv_type = (TextView) convertView.findViewById(R.id.tv_type);
                convertView.setTag(vh);
            }
            vh = (ViewHolder) convertView.getTag();
            ItemTypeBean typeBean = getItem(position);
            vh.tv_type.setText(typeBean.typeName);
            vh.img_type.setImageResource(typeBean.imgid);
            return convertView;
        }
    }

    class ItemTypeBean {
        ItemType type;
        int imgid;
        String typeName;
    }

    class ViewHolder {
        public ImageView img_type;
        public TextView tv_type;
    }

    private KeyboardViewInterface getKeyboardView(View view) {
        View parentView = (View) view.getParent();
        if (parentView == null) {
            return null;
        } else if (parentView instanceof KeyboardViewInterface) {
            return (KeyboardViewInterface) parentView;
        } else {
            return getKeyboardView(parentView);
        }
    }

}
