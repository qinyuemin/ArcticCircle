/**
 * 项目名： arcticCircle
 * 包名： com.xiaoma.beiji.manager
 * 版本信息： 1.0.0
 * Copyright (c) 2015上海路路由信息科技有限公司-版权所有
 */
package com.xiaoma.beiji.manager;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.text.TextUtils;
import android.util.Log;
import com.xiaoma.beiji.entity.ContactEntity;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * 类名称： ContactManager
 * 类描述： 
 * 创建人： gang.shi
 * 修改人： gang.shi
 * 修改时间： 2015年12月24 10:29
 * 修改备注：
 * @version 1.0.0
 *
 */
public class ContactManager {

    /**获取库Phon表字段**/
    private static final String[] PHONES_PROJECTION = new String[] {
            Phone.DISPLAY_NAME, Phone.NUMBER };

    /**联系人显示名称**/
    private static final int PHONES_DISPLAY_NAME_INDEX = 0;

    /**电话号码**/
    private static final int PHONES_NUMBER_INDEX = 1;

    /**联系人的ID**/
    private static final int PHONES_CONTACT_ID_INDEX = 3;

    private Context context;

    private List<ContactEntity> contactEntities;


    private static ContactManager instance;

    private ContactManager(Context context) {
        this.context = context;
        contactEntities = new ArrayList<>();

    }

    public static ContactManager getInstance(Context context){
        if(instance == null){
            instance = new ContactManager(context);
        }

        return instance;
    }


    public List<ContactEntity> getContactEntities() {
        Log.i("ContactManager","getContactEntities:"+contactEntities.size());
        try {
            contactEntities.clear();
            getPhoneContacts();
            getSIMContacts();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return contactEntities;
    }

    /**得到手机通讯录联系人信息**/
    private void getPhoneContacts() {
        ContentResolver resolver = context.getContentResolver();

        // 获取手机联系人
        Cursor phoneCursor = resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,PHONES_PROJECTION, null, null, null);

        if (phoneCursor != null) {
            loadPhoneEntity(phoneCursor);

            phoneCursor.close();
        }
    }


    /**得到手机SIM卡联系人人信息**/
    private void getSIMContacts() {
        ContentResolver resolver = context.getContentResolver();
        // 获取Sims卡联系人
        Uri uri = Uri.parse("content://icc/adn");
        Cursor phoneCursor = resolver.query(uri, PHONES_PROJECTION, null, null, null);

        if (phoneCursor != null) {
            loadPhoneEntity(phoneCursor);

            phoneCursor.close();
        }
    }

    private void loadPhoneEntity(Cursor phoneCursor) {
        ContactEntity entity = null;
        while (phoneCursor.moveToNext()) {

            // 得到手机号码
            String phoneNumber = phoneCursor.getString(PHONES_NUMBER_INDEX);
            // 当手机号码为空的或者为空字段 跳过当前循环
            if (TextUtils.isEmpty(phoneNumber))
                continue;
            // 得到联系人名称
            String contactName = phoneCursor .getString(PHONES_DISPLAY_NAME_INDEX);

            entity = new ContactEntity();
            entity.setName(contactName);
            entity.setPhone(phoneNumber);
            contactEntities.add(entity);
        }
    }
}
