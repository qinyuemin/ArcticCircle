package com.xiaoma.beiji.controls.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;

import com.common.android.lib.util.TimeUtil;
import com.makeapp.android.util.TextViewUtil;
import com.makeapp.android.util.ViewUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.xiaoma.beiji.R;
import com.xiaoma.beiji.adapter.RecyclerViewAdapter;
import com.xiaoma.beiji.entity.FriendDynamicEntity;
import com.xiaoma.beiji.entity.PicEntity;
import com.xiaoma.beiji.util.ToastUtil;

import java.util.List;

/**
 * Created by zhangqibo on 2016/5/16.
 */
public class ShareDialog extends Dialog{

    private InputDialog.InputCallBack inputCallBack;
    private FriendDynamicEntity friendDynamicEntity;
    private EditText desEdit;

    public ShareDialog(Context context,FriendDynamicEntity entity,InputDialog.InputCallBack inputCallBack) {
        super(context, R.style.base_dialog);
        this.inputCallBack = inputCallBack;
        friendDynamicEntity = entity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.dimAmount = 0.5f;
        this.onWindowAttributesChanged(lp);
        getWindow().setAttributes(lp);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_share,null);
        desEdit = (EditText) view.findViewById(R.id.edit_des);
        initView(view,friendDynamicEntity);
        ViewUtil.setViewOnClickListener(view, R.id.btn_close, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        ViewUtil.setViewOnClickListener(view, R.id.btn_share_ok, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String description = desEdit.getText().toString().trim();
                if(TextUtils.isEmpty(description)){
                    ToastUtil.showToast(getContext(),"写点评论吧");
                }else{
                    dismiss();
                    inputCallBack.success(description);
                }
            }
        });
        setContentView(view, new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        setCancelable(true);
    }

    private void initView(View view, FriendDynamicEntity entity){
        String type = "";
        switch (entity.getReleaseType()){
            case RecyclerViewAdapter.TYPE_DIANPING:
                type = "点评";
                break;
            case RecyclerViewAdapter.TYPE_CHANGWEN:
                type = "发布长文";
                break;
            case RecyclerViewAdapter.TYPE_WENWEN:
                type = "求助";
                break;
            default:
                type = "点评";
                break;
        }
        TextViewUtil.setText(view,R.id.text_item_flag,type);
        String avatar = entity.getAvatar();
        TextViewUtil.setText(view, R.id.text_item_time, TimeUtil.getDisplayTime(entity.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
        TextViewUtil.setText(view, R.id.text_item_name, entity.getNickName());
        ImageView  headImage = (ImageView) view.findViewById(R.id.img_head);
        if(!TextUtils.isEmpty(avatar)){
            ImageLoader.getInstance().displayImage(entity.getAvatar(), headImage);
        }else{
            headImage.setImageResource(R.drawable.ic_logo);
        }
        ImageView  picImage = (ImageView) view.findViewById(R.id.ipv_item_img);
        List<PicEntity> picLists = entity.getPic();
        if(picLists.size()>0){
            PicEntity entity1 = picLists.get(0);
            String url = entity1.getPicUrl();
            ImageLoader.getInstance().displayImage(url, picImage);
        }
    }
}
