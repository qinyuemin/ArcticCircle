/**
 * 项目名： arcticCircle
 * 包名： com.xiaoma.beiji.network
 * 版本信息： 1.0.0
 * Copyright (c) 2015 版权所有
 */
package com.xiaoma.beiji.network;

import com.makeapp.javase.util.DateUtil;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.util.EntityUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 类名称： TestCase
 * 类描述：
 * 创建人： gang.shi
 * 修改人： gang.shi
 * 修改时间： 2015年12月11 11:20
 * 修改备注：
 *
 * @version 1.0.0
 */
public class TestCase {
    public static String SendRequest(String strJson) {
        strJson = "{\"app_key\":\"6c5ea2ca96c165e86cd11f83875f0135\",\"app_session\":\"f81eb851a320a29b7f792d99f85f4abe\",\"charset\":\"UTF-8\",\"format\":\"json\",\"method\":\"Images.upload\",\"params\":\"{\\\"app_stats\\\":{\\\"app_udid\\\":\\\"sfafafaefaefaefasfeasef\\\",\\\"app_version\\\":\\\"1.0\\\"},\\\"user_id\\\":4,\\\"user_session\\\":\\\"fe08bfd65c6aaf65eee14652458bd36d\\\"}\",\"sign\":\"22ec4f8275aa90c14cd7f0edf164f19e\",\"sign_method\":\"md5\",\"timestamp\":\"2015-12-11\",\"version\":\"2015101501\"}";
        String BASE_URL = "http://testbjqapi.xiaomakeji.com.cn/rest";

        String returnLine = "";
        try {
            URL my_url = new URL(UrlConstants.BASE_URL);
            HttpURLConnection connection = (HttpURLConnection) my_url.openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setRequestMethod("POST");
            connection.setUseCaches(false);
            connection.setInstanceFollowRedirects(true);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.connect();
            DataOutputStream out = new DataOutputStream(connection.getOutputStream());
            byte[] content = strJson.getBytes("utf-8");
            out.write(content, 0, content.length);
            out.flush();
            out.close(); // flush and close
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));
            //StringBuilder builder = new StringBuilder();
            String line = "";
            System.out.println("Contents of post request start");
            while ((line = reader.readLine()) != null) {
                // line = new String(line.getBytes(), "utf-8");
                returnLine += line;
                System.out.println(line);

            }
            System.out.println("Contents of post request ends");

            reader.close();
            connection.disconnect();
            System.out.println("========返回的结果的为========");

        } catch (Exception e) {
            e.printStackTrace();
        }

        return returnLine;

    }

    public static void postFile() {
        try {
            HttpClient httpclient = new DefaultHttpClient();
            httpclient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
            HttpPost httppost = new HttpPost("http://testbjqapi.xiaomakeji.com.cn/rest/upload_image");
            MultipartEntity mpEntity = new MultipartEntity();
            ContentBody cbFile = new FileBody(new File("D://111.jpg"));
            //写入值
            mpEntity.addPart("app_key", new StringBody("6c5ea2ca96c165e86cd11f83875f0135"));
            mpEntity.addPart("app_session", new StringBody("f81eb851a320a29b7f792d99f85f4abe"));
            mpEntity.addPart("charset", new StringBody("UTF-8"));
            mpEntity.addPart("format", new StringBody("json"));
            mpEntity.addPart("method", new StringBody("Images.upload"));
            mpEntity.addPart("params", new StringBody("{\"app_stats\":{\"app_udid\":\"sfafafaefaefaefasfeasef\",\"app_version\":\"1.0\"},\"user_id\":4,\"user_session\":\"fe08bfd65c6aaf65eee14652458bd36d\"}"));
            mpEntity.addPart("sign", new StringBody("22ec4f8275aa90c14cd7f0edf164f19e"));
            mpEntity.addPart("sign_method", new StringBody("md5"));
            mpEntity.addPart("timestamp", new StringBody("2015-12-11"));
            mpEntity.addPart("version", new StringBody("2015101501"));
            mpEntity.addPart("userfile", cbFile);

            httppost.setEntity(mpEntity);
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity resEntity = response.getEntity();
            System.out.println(response.getStatusLine());
            String result = "";
            if (resEntity != null) {
                result = EntityUtils.toString(resEntity, "utf-8");
                System.out.println(result);
            }
            httpclient.getConnectionManager().shutdown();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getShowTime(String date) {
        String showTime = date;
        long currentTime = System.currentTimeMillis();
        long msgTime = DateUtil.toDate(date).getTime();
        long l = Math.abs(msgTime - currentTime);
        if (l < 60 * 1000) {// 1分钟
            showTime = "刚刚";
        } else if (l < 60 * 60 * 1000) {
            showTime = (l / 60 / 1000) + "分钟前";
        } else if (l < 24 * 60 * 60 * 1000) {
            showTime = (l / 60 / 60 / 1000) + "小时前";
        } else if (l < 2 * 24 * 60 * 60 * 1000) {
            showTime = "昨天";
        } else if (l < 8 * 24 * 60 * 60 * 1000) {
            showTime = (l / 24 / 60 / 60 / 1000) + "天前";
        }
        return showTime;
    }

    public static void main(String args[]) {
//        postFile();
        System.out.println(getShowTime("2016-01-04 10:15:41"));
    }


//    public View getView(int position, View convertView, ViewGroup parent) {
//        // TODO Auto-generated method stub
//        viewHolder1 holder1 = null;
//        viewHolder2 holder2 = null;
//        viewHolder3 holder3 = null;
//        int type = getItemViewType(position);
//
//
//        //无convertView，需要new出各个控件
//        if (convertView == null) {
//            Log.e("convertView = ", " NULL");
//
//            //按当前所需的样式，确定new的布局
//            switch (type) {
//                case TYPE_1:
//                    convertView = inflater.inflate(R.layout.listitem1, parent, false);
//                    holder1 = new viewHolder1();
//                    holder1.textView = (TextView) convertView.findViewById(R.id.textview1);
//                    holder1.checkBox = (CheckBox) convertView.findViewById(R.id.checkbox);
//                    Log.e("convertView = ", "NULL TYPE_1");
//                    convertView.setTag(holder1);
//                    break;
//                case TYPE_2:
//                    convertView = inflater.inflate(R.layout.listitem2, parent, false);
//                    holder2 = new viewHolder2();
//                    holder2.textView = (TextView) convertView.findViewById(R.id.textview2);
//                    Log.e("convertView = ", "NULL TYPE_2");
//                    convertView.setTag(holder2);
//                    break;
//                case TYPE_3:
//                    convertView = inflater.inflate(R.layout.listitem3, parent, false);
//                    holder3 = new viewHolder3();
//                    holder3.textView = (TextView) convertView.findViewById(R.id.textview3);
//                    holder3.imageView = (ImageView) convertView.findViewById(R.id.imageview);
//                    Log.e("convertView = ", "NULL TYPE_3");
//                    convertView.setTag(holder3);
//                    break;
//
//            }
//
//        } else {
//            //有convertView，按样式，取得不用的布局
//            switch (type) {
//                case TYPE_1:
//                    holder1 = (viewHolder1) convertView.getTag();
//                    Log.e("convertView !!!!!!= ", "NULL TYPE_1");
//                    break;
//                case TYPE_2:
//                    holder2 = (viewHolder2) convertView.getTag();
//                    Log.e("convertView !!!!!!= ", "NULL TYPE_2");
//                    break;
//                case TYPE_3:
//                    holder3 = (viewHolder3) convertView.getTag();
//                    Log.e("convertView !!!!!!= ", "NULL TYPE_3");
//                    break;
//
//            }
//
//        }
//
//        //设置资源
//        switch (type) {
//            case TYPE_1:
//                holder1.textView.setText(Integer.toString(position));
//                holder1.checkBox.setChecked(true);
//                break;
//            case TYPE_2:
//                holder2.textView.setText(Integer.toString(position));
//                break;
//            case TYPE_3:
//                holder3.textView.setText(Integer.toString(position));
//                holder3.imageView.setBackgroundResource(R.drawable.icon);
//                break;
//
//        }
//
//
//        return convertView;
//
//    }


}
