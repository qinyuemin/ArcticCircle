package com.xiaoma.beiji.controls.view.imagechooser;

import android.content.Context;
import android.text.TextUtils;

import java.util.ArrayList;

/**
 * 类名称： ImageManager
 * 类描述：
 * 创建人： gang.shi
 * 修改人： gang.shi
 * 修改时间： 2015年12月24 11:03
 * 修改备注：
 *
 * @version 1.0.0
 */
public class ImageManager {
    private static ImageManager ourInstance = new ImageManager();

    private String images = "";

    public static ImageManager getInstance() {
        return ourInstance;
    }

    private ImageManager() {
    }

    public String getImages() {
        return images;
    }

    /**
     * 获取已选中个图片列表
     *
     * @param context
     * @return
     */
    public  ArrayList<String> getSelectedImages(Context context) {
        ArrayList<String> selectedList = new ArrayList<String>();
        if (TextUtils.isEmpty(images)) {
            return selectedList;
        }
        String[] imgArr = images.split(",");
        for (String item : imgArr) {
            if (TextUtils.isEmpty(item)) {
                continue;
            }
            selectedList.add(item);
        }
        return selectedList;
    }

    /**
     * 保存选中的图片
     *
     * @param context
     * @param imgList
     */
    public  void saveSelectedImages(Context context, ArrayList<String> imgList) {
        if (imgList == null) {
            return;
        }
        StringBuffer result = new StringBuffer("");
        for (String item : imgList) {
            result.append(item).append(",");
        }
        images = result.toString();
    }

    public void clear(){
        images = "";
    }
}
