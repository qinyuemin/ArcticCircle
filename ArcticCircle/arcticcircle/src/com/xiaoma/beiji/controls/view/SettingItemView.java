/**
 *
 *项目名：
 *包名：com.lianlian.controls.view
 *文件名：LianLianItemView.java
 *版本信息：1.0.0
 *创建日期：2014年12月31日-下午5:44:15
 *创建人：DingWentao
 *
 * 
 */
package com.xiaoma.beiji.controls.view;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.Spanned;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;
import android.widget.CompoundButton.OnCheckedChangeListener;
import com.xiaoma.beiji.R;
import com.makeapp.javase.lang.StringUtil;

/**
 *
 * 类名称：LianLianItemView
 * 类描述：
 * 创建人： DingWentao
 * 修改人： DingWentao
 * 修改时间： 2014年12月31日 下午5:44:15
 * 修改备注：
 * @version 1.0.0
 *
 */
public class SettingItemView extends RelativeLayout implements OnCheckedChangeListener {

    private Activity mActivity;
    private OnCheckedChangeListener mOnCheckedChangeListener;

    private ImageView settingItemIcon;
    private TextView settingItemText;
    private TextView settingItemxText;
    private TextView settingItemSubText;
    private ImageView settingItemSubImage;
    private View settingItemRedPoint;
    private ImageView settingItemNext;


	private CheckBox settingItemCheckbox;
    private View settingItemTopLine;
    private View settingItemBottomLine;
    private View nextLayout;

    
    private int nextIconRes = -1;
    private int iconRes = -1;
    boolean isCheckable = false;
    private boolean isChecked = false;
    private boolean showTopDivider = false;
    private boolean showBottomDivider = false;
    private boolean showRedPoint = false;
    private boolean hideNextLayout = false;
    private Spanned textLabelSpanned = null;
    
    private String textLabel = null;
    private String xtextLable = null;
    private String subTextLabel = null;
    private float subLabelSize = -1f;
    private int subLabelColor = -1;
    private int subLabelVisibility = View.GONE;

    /**
     * 创建一个新的实例 SettingItemView.
     */
    public SettingItemView(Context context) {
        super(context);
        init(context, null);
    }

    /**
     * 创建一个新的实例 SettingItemView.
     */
    public SettingItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    /**
     * 创建一个新的实例 SettingItemView.
     */
    public SettingItemView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        mActivity = (Activity) context;

        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.simple);
            int n = a.getIndexCount();
            for (int i = 0; i < n; i++) {
                int attr = a.getIndex(i);
                switch (attr) {
                case R.styleable.simple_icon:
                    iconRes = a.getResourceId(attr, -1);        
                    break;
                case R.styleable.simple_isCheckable:
                    isCheckable = a.getBoolean(attr, false); 
                    break;
                case R.styleable.simple_isChecked:
                    isChecked = a.getBoolean(attr, false);
                    break;
                case R.styleable.simple_showTopDivider:
                    showTopDivider = a.getBoolean(attr, false);      
                    break;
                case R.styleable.simple_hideNextView:
                	hideNextLayout = a.getBoolean(attr, false);  
                	break;
                case R.styleable.simple_showBottomDivider:
                    showBottomDivider = a.getBoolean(attr, false);      
                    break;
                case R.styleable.simple_label:
                    textLabel = a.getString(attr);        
                    break;
                case R.styleable.simple_xlabel:
                	xtextLable = a.getString(attr);        
                    break;
                case R.styleable.simple_subLabel:
                    subTextLabel = a.getString(attr);        
                    break;
                case R.styleable.simple_showRedPoint:
                    showRedPoint = a.getBoolean(attr, false);
                    break;
                case R.styleable.simple_subLabelSize:
                    subLabelSize = a.getDimension(attr, -1);
                    break;
                case R.styleable.simple_subLabelColor:
                	
                    subLabelColor = a.getColor(attr, -1);
                    break;
                case R.styleable.simple_nextIcon:
                	nextIconRes = a.getResourceId(attr, -1);    
                	break;
                case R.styleable.simple_subLabelVisibility:
                    subLabelVisibility = a.getInt(attr, View.VISIBLE);
                    break;
                }
            }
            
            a.recycle();
        }

        LayoutInflater.from(context).inflate(R.layout.view_setting_item, this);

        settingItemIcon = (ImageView) findViewById(R.id.setting_item_icon);
        settingItemText = (TextView) findViewById(R.id.setting_item_text);
        settingItemxText = (TextView) findViewById(R.id.setting_item_xtext);
        settingItemNext = (ImageView) findViewById(R.id.setting_item_next);
        settingItemCheckbox = (CheckBox) findViewById(R.id.setting_item_checkbox);
        settingItemTopLine = findViewById(R.id.setting_item_top_line);
        settingItemBottomLine = findViewById(R.id.setting_item_bottom_line);
        settingItemSubText = (TextView) findViewById(R.id.setting_item_sub_text);
        settingItemSubImage = (ImageView) findViewById(R.id.setting_item_sub_image);
        settingItemRedPoint = findViewById(R.id.setting_item_red_point);
        nextLayout= findViewById(R.id.setting_item_next_layout);
        
        
        setNextIcon(nextIconRes);
        setIcon(iconRes);
        setLabel(textLabel);
        setxLabel(xtextLable);
        setSubLabel(subTextLabel);
        setSubLabelSize(subLabelSize);
        setSubLabelColor(subLabelColor);
        setSubLabelVisibility(subLabelVisibility);
        setCheckable(isCheckable);
        setshowTopDivider(showTopDivider);
        setshowBottomDivider(showBottomDivider);
        setShowRedPoint(showRedPoint);
        hideNextLayout(hideNextLayout);

        Object background = getBackground();
        if (background == null) {
            //如果没有设置背景，则设置默认背景
            setBackgroundResource(R.drawable.sl_bg_person_center_item);
        }

        settingItemCheckbox.setOnCheckedChangeListener(this);
    }

    /**
     * 
     *setIcon(设置左侧图标）
     *（如果值为-1，则表示隐藏该图标）
     * @param iconRes
     * @exception
     * @since 1.0.0
     */
    public void setIcon(int iconRes) {
        if (iconRes != -1) {
            settingItemIcon.setVisibility(View.VISIBLE);
            settingItemIcon.setImageResource(iconRes);
        } else {
            settingItemIcon.setVisibility(View.GONE);
        }
        this.iconRes = iconRes;
    }
    
    public void setIcon(Drawable iconDrawable){
    	if (iconRes != -1) {
            settingItemIcon.setVisibility(View.VISIBLE);
            settingItemIcon.setImageDrawable(iconDrawable);
        } else {
            settingItemIcon.setVisibility(View.GONE);
        }
        this.iconRes = -1;
    }
    
    
    public void setNextIcon(int nextIconRes)
    {
    	if(nextIconRes!=-1)
    	{
    		settingItemNext.setImageResource(nextIconRes);
    	}
    }

    /**
     *setLabel(设置描述文本）
     * @param textLabel
     * @exception
     * @since 1.0.0
     */
    public void setLabel(String textLabel) {
        if (StringUtil.isValid(textLabel)) {
            settingItemText.setText(textLabel);
        } else {
            settingItemText.setText(null);
        }
        this.textLabel = textLabel;
    }
    
    
    public void setLabel(Spanned textLabel)
    {
        if (textLabel!=null &&textLabel.length()>0) {
            settingItemText.setText(textLabel);
        } else {
            settingItemText.setText(null);
        }
        this.textLabelSpanned = textLabel;
    }
    
    public void setxLabel(String xtextLabel) {
        if (StringUtil.isValid(xtextLabel)) {
        	settingItemxText.setText(xtextLabel);
        	settingItemxText.setVisibility(View.VISIBLE);
        } else {
        	settingItemxText.setText(null);
        	settingItemxText.setVisibility(View.GONE);
        }
        this.xtextLable = xtextLabel;
    }

    /**
     *setLabel(设置描述文本）
     * @param subTextLabel
     * @exception
     * @since 1.0.0
     */
    public void setSubLabel(String subTextLabel) {
        if (StringUtil.isValid(subTextLabel)) {
            settingItemSubText.setText(subTextLabel);
        } else {
            settingItemSubText.setText(null);
        }
        this.subTextLabel = subTextLabel;
    }
    
    public void setSubImage(Drawable subImageDrawable){
    	this.settingItemSubImage.setImageDrawable(subImageDrawable);
    }
    
    public void setSubImageVisibility(int subLabelVisibility) {
        this.settingItemSubImage.setVisibility(subLabelVisibility);
    }
    
    /**
     * 
     *setSubLabelSize(设置SubLable的文字大小)
     * @param subLabelSize
     * @exception
     * @since 1.0.0
     */
    public void setSubLabelSize(float subLabelSize) {
        if(subLabelSize > 0){
            this.settingItemSubText.setTextSize(TypedValue.COMPLEX_UNIT_PX, subLabelSize);
            this.subLabelSize = subLabelSize;
        }
    }
    
    /**
     * 
     *setSubLabelColor(设置SubLable的文字颜色）
     * @param subLabelColor
     * @exception
     * @since 1.0.0
     */
    public void setSubLabelColor(int subLabelColor) {
        if(subLabelColor != -1){
            this.settingItemSubText.setTextColor(subLabelColor);
            this.subLabelColor = subLabelColor;
        }
    }
    
    /**
     * 
     * setSubLabelVisibility(设置SubLable的显示状态）
     * @param subLabelVisibility
     * @exception
     * @since 1.0.0
     */
    public void setSubLabelVisibility(int subLabelVisibility) {
        this.settingItemSubText.setVisibility(subLabelVisibility);
    }

    /**
     *setCheckable(设置是否启用CheckBox）
     * @param isCheckable
     * @exception
     * @since 1.0.0
     */
    public void setCheckable(boolean isCheckable) {
        if (isCheckable) {
            settingItemCheckbox.setVisibility(View.VISIBLE);
            settingItemCheckbox.setChecked(isChecked);
            settingItemNext.setVisibility(View.GONE);
        } else {
            settingItemCheckbox.setVisibility(View.GONE);
            settingItemNext.setVisibility(View.VISIBLE);
        }
        this.isCheckable = isCheckable;
    }

    /**
     * setChecked(设置CheckBox的值）
     * @param isChecked
     * @exception
     * @since 1.0.0
     */
    public void setChecked(boolean isChecked) {
        settingItemCheckbox.setChecked(isChecked);
        this.isChecked = isChecked;
    }

    /**
     * setShowRedPoint(设置是否显示提示信息的小红点）
     * @param showRedPoint
     * @exception
     * @since 1.0.0
     */
    public void setShowRedPoint(boolean showRedPoint) {
        settingItemRedPoint.setVisibility(showRedPoint ? View.VISIBLE : View.GONE);
        this.showRedPoint = showRedPoint;
    }
    
    public void hideNextLayout(boolean isHide)
    {
    	if(isHide)
    	{
    		nextLayout.setVisibility(View.GONE);
    	}else
    	{
    		nextLayout.setVisibility(View.VISIBLE);
    	}
    }

    public void setshowTopDivider(boolean showTopDivider) {
        settingItemTopLine.setVisibility(showTopDivider ? View.VISIBLE : View.GONE);
        this.showTopDivider = showTopDivider;
    }

    public void setshowBottomDivider(boolean showBottomDivider) {
        settingItemBottomLine.setVisibility(showBottomDivider ? View.VISIBLE : View.GONE);
        this.showBottomDivider = showBottomDivider;
    }

    public int getIconRes() {
        return iconRes;
    }
    
    public String getLabel() {
        return this.textLabel;
    }
    
    public String getSubLabel() {
        return this.subTextLabel;
    }
    
    public boolean isCheckable() {
        return isCheckable;
    }
    
    public boolean isChecked() {
        return isChecked;
    }
    
    public boolean isShowRedPoint() {
        return showRedPoint;
    }
    
    public boolean isshowTopDivider() {
        return showTopDivider;
    }
    
    public boolean isshowBottomDivider() {
        return showBottomDivider;
    }

    public void setOnCheckedChangeListener(OnCheckedChangeListener listener) {
        mOnCheckedChangeListener = listener;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (mOnCheckedChangeListener != null) {
            mOnCheckedChangeListener.onCheckedChanged(this, buttonView, isChecked);
        }
    }
    
    public interface OnCheckedChangeListener{
        void onCheckedChanged(SettingItemView settingItem, CompoundButton buttonView, boolean isChecked);
    }

	public TextView getSettingItemSubText() {
		return settingItemSubText;
	}
	
	
    public ImageView getSettingItemNext() {
		return settingItemNext;
	}
    
    
}
