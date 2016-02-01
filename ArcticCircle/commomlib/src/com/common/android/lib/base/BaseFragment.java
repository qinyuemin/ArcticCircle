/**
 *
 *项目名：LuluYouLib
 *包名：com.luluyou.android.lib.ui
 *文件名：BaseFragment.java
 *版本信息：1.0.0
 *创建日期：2013年12月16日-下午1:24:52
 *创建人：jerry.deng
 *Copyright (c) 2013上海路路由信息科技有限公司-版权所有
 * 
 */
package com.common.android.lib.base;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import android.widget.TextView;
import com.common.android.lib.R;
import com.common.android.lib.controls.dialog.CommonProgressDialog;

/**
 *
 * 类名称： BaseFragment
 * 类描述：
 * 创建人： gang.shi
 * 修改人： gang.shi
 * 修改时间： 2015年06月08 10:42
 * 修改备注：
 * @version 1.0.0
 *
 */
public abstract class BaseFragment extends Fragment {
    private Handler mHandler = null;
    private boolean bReloadData = false;
    private FragmentActivity mActivity = null;
    private CommonProgressDialog loadingProgressDialog;
    
    public BaseFragment(){
        this.mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                BaseFragment.this.handleMessage(msg);
            }
        };
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = getActivity();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View retView = null;
        if(this.getFragmentLayoutId() > 0){
            retView = inflater.inflate(this.getFragmentLayoutId(), container, false);
        }else{
            retView = this.getFragmentView();
        }
        
        this.onPreInitUI(retView);
        this.initComponents(retView);
        this.bReloadData = this.IsReloadData();
        if(!bReloadData){
            this.loadData();
        }
        return retView;
    }

    protected abstract int getFragmentLayoutId();

    protected abstract void initComponents(View v);

    protected  View getFragmentView(){
        return null;
    }
    
    protected abstract void loadData();
    
    protected void onPreInitUI(View v){
        
    }
    public Handler getHandler(){
        return this.mHandler;
    }
    protected boolean IsReloadData(){
        return false;
    }

    /**
     * 为了解决getActivity() = null而写的方法，效果未知
     * @author DingWentao
     * @date 2014-02-25
     */
    public FragmentActivity getFragmentActivity() {
        FragmentActivity fActivity = getActivity();
        if (fActivity == null) {
            return mActivity;
        } else {
            return fActivity;
        }
    }
    
    @Override
    public void onResume() {
        super.onResume();
        if(bReloadData){
            this.loadData();
        }
    }
    
    protected void handleMessage(Message msg){
    }
    
    public CommonProgressDialog showProgressDialog(String title, String message){
        return showProgressDialog(title,message,true);
	}

    public CommonProgressDialog showProgressDialog(String title, String message,boolean isCanceled){
        try{
            if (getFragmentActivity().isFinishing() == false){
                if(loadingProgressDialog == null){
                    loadingProgressDialog = CommonProgressDialog.show(getFragmentActivity(), title, message);
                    loadingProgressDialog.setCancelable(true);
                    loadingProgressDialog.setCanceledOnTouchOutside(isCanceled);
                    loadingProgressDialog.show();
                }else{
                    loadingProgressDialog.setTitle(title);
                    loadingProgressDialog.setMessage(message);
                    if(loadingProgressDialog.isShowing() == false){
                        loadingProgressDialog.show();
                    }
                }
            }
        }catch(Exception e){}
        return loadingProgressDialog;
    }

    public void dismissProgressDialog() {
		try{
			if (getFragmentActivity().isFinishing() == false && loadingProgressDialog != null) {
				loadingProgressDialog.dismiss();
			}
		}catch(Exception e){}
	}
	
	
	protected void keyBoardCancel() {
		View view = getActivity().getWindow().peekDecorView();
		if (view != null) {			
			InputMethodManager inputmanger = (InputMethodManager)getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
			inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
		}
	}
    
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		return false;
	}
	/**
     * 
     * finalizeBeforeDestroy(这里用一句话描述这个方法的作用） （activity退出前执行的操作） void
     * 
     * @exception
     * @since 1.0.0
     */
    protected void finalizeBeforeDestroy() {

    }
}
