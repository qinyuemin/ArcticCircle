package com.xiaoma.beiji.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import com.makeapp.javase.lang.StringUtil;
import com.xiaoma.beiji.R;
import com.xiaoma.beiji.base.SimpleBaseActivity;
import com.xiaoma.beiji.base.SimpleFragment;
import com.xiaoma.beiji.controls.view.imagechooser.ImageChooserGroupActivity;
import com.xiaoma.beiji.util.*;
import com.xiaoma.beiji.util.CompressImageTask.CompressImageTaskParam;
import com.xiaoma.beiji.util.CompressImageTask.OnCompressImageListener;
import android.os.AsyncTask.Status;

import java.io.File;
import java.io.Serializable;

public class GetImageActivity extends SimpleBaseActivity implements OnClickListener {
    
    private static final int REQUEST_GALLERY_CODE = 1;
    private static final int REQUEST_CAMERA_CODE = 2;
    private static final int REQUEST_COMPRESS_CODE = 3;
    
    private ViewGroup rootView;
    private View deleteLine;
    private View deleteView;
    
    private  boolean hasDelete = false;
    
    private static final String HAS_CHOOSE_TYPE = "HAS_CHOOSE_TYPE";
    private boolean hasChooseGetImageType = false;
    
    public static final int RESULT_DELETE = 333;
    public static final int REQUEST_CODE_GET_IMAGE = 9999;
    public static final String KEY_INTENT_IMAGE_PATH = "KEY_INTENT_IMAGE_PATH";
    public static final String KEY_GET_IMAGE_CONFIG = "KEY_GET_IMAGE_CONFIG";
    public static final String KEY_HAS_DELETE= "KEY_HAS_DELETE";
    public static final String KEY_INTENT_IMAGE_WIDTH = "KEY_INTENT_IMAGE_WIDTH";
    public static final String KEY_INTENT_IMAGE_HEIGHT = "KEY_INTENT_IMAGE_HEIGHT";
    public GetImageConfig getImageConfig;
    
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(savedInstanceState != null){
            hasChooseGetImageType = savedInstanceState.getBoolean(HAS_CHOOSE_TYPE);
            getImageConfig = (GetImageConfig) savedInstanceState.getSerializable(KEY_GET_IMAGE_CONFIG);
            hasDelete = savedInstanceState.getBoolean(KEY_HAS_DELETE,false);
        }else{
            getImageConfig = (GetImageConfig) getIntent().getSerializableExtra(KEY_GET_IMAGE_CONFIG);
            hasDelete = getIntent().getBooleanExtra(KEY_HAS_DELETE,false);
        }
        initGetImageConfig();
        
        super.onCreate(savedInstanceState);
    }
    
    private void initGetImageConfig(){
        getImageConfig = getImageConfig == null ? new GetImageConfig() : getImageConfig;
        if(getImageConfig.getImageContainerHeight() <= 0 && getImageConfig.getImageContainerWidth() <= 0){
            //如果没有设置高宽，则以宽度640为准，高度设置1136，这是产品确定的数据
            getImageConfig.setOuterImageContainer(false);
            getImageConfig.setImageContainerHeight(1136);
            getImageConfig.setImageContainerWidth(640);
        }else if(getImageConfig.getImageContainerHeight() <= 0){
            //如果只设置了宽度，则高度跟宽度一致
            getImageConfig.setImageContainerHeight(getImageConfig.getImageContainerWidth());
        }else if(getImageConfig.getImageContainerWidth() <= 0){
            //如果只设置了宽度，则高度跟高度一致
            getImageConfig.setImageContainerWidth(getImageConfig.getImageContainerHeight());
        }
        if(getImageConfig.getGetImageType() == null){
            getImageConfig.setGetImageType(GetImageType.BOTH);
        }
    }
    
    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_get_image;
    }

    @Override
    protected String getActivityTitle() {
        return null;
    }

    @Override
    protected void initComponents() {
        rootView = (ViewGroup) findViewById(R.id.rootView);
        rootView.setOnClickListener(this);
        
        deleteLine = findViewById(R.id.delete_line);
        deleteView = findViewById(R.id.delete);
        if(hasDelete){
            deleteLine.setVisibility(View.VISIBLE);
            deleteView.setVisibility(View.VISIBLE);
            deleteView.setOnClickListener(this);
        }else{
            deleteLine.setVisibility(View.GONE);
            deleteView.setVisibility(View.GONE);
        }
        if(hasChooseGetImageType){
            hideActivityComponents();
        }else{
            String title = getImageConfig.getImageChooseViewTitle();
            if(StringUtil.isValid(title)){
                TextView titleView = (TextView) findViewById(R.id.get_image_title);
                titleView.setText(title);
            }
            
            GetImageType getImageType = getImageConfig.getGetImageType();
            String[] items = getImageConfig.getImageChooseViewItems();
            
            if(getImageType == GetImageType.BOTH || getImageType == GetImageType.BOTH_SIMPLE){
                TextView gallery = (TextView) findViewById(R.id.from_gallery);
                gallery.setOnClickListener(this);
                if(items != null && items.length > 0){
                    String s = items[0];
                    if(StringUtil.isValid(s)){
                        gallery.setText(s);
                    }
                }
                
                TextView camera = (TextView) findViewById(R.id.from_camera);
                camera.setOnClickListener(this);
                if(items != null && items.length > 1){
                    String s = items[1];
                    if(StringUtil.isValid(s)){
                        camera.setText(s);
                    }
                }
            }else if(getImageType == GetImageType.CAMERA){
                openCamera();
            }else if(getImageType == GetImageType.GALLERY){
                openGallery();
            }
        }
    }
    
    @Override
    protected int getTitleBarId() {
        return -1;
    }
    
    @Override
    protected void loadData() {}
    
    @Override
    protected void onDestroy() {
        dismissProgressDialog();
        super.onDestroy();
    }
    
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK){
            if(requestCode == REQUEST_COMPRESS_CODE){
                openGallery();
            }else{
                setResultAndFinish(null, 0, 0);
            }
            return;
        }

        if(requestCode == ImageChooserGroupActivity.REQUEST_CODE_IMAGE_CHOOSER){
            setResult(Activity.RESULT_OK,data);
            finish();
            return;
        }
        
        if (requestCode == REQUEST_GALLERY_CODE || requestCode == REQUEST_CAMERA_CODE) {
            String imageFile = doImageLoad(this, requestCode, resultCode, data);
            if (StringUtil.isInvalid(imageFile)){
                ToastUtil.showToast(getBaseContext(), "图片获取失败~");
                setResultAndFinish(null, 0, 0);
                return;
            }
            //图片获取成功
//          new CompressImageTask(requestCode == REQUEST_GALLERY_CODE ? GetImageType.GALLERY : GetImageType.CAMERA).execute(imageFile);
            
            //如果从相册走，则让用户选择是否压缩
            if(getImageConfig.isChooseOriginalImageStatus() && requestCode == REQUEST_GALLERY_CODE){
//                SendImageActivity.goSendImageActivity(this, imageFile, getImageConfig, REQUEST_COMPRESS_CODE);
            }else{
                CompressImageTaskParam param = new CompressImageTaskParam();
                param.config = (requestCode == REQUEST_GALLERY_CODE? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
                param.imageContainerHeight = getImageConfig.getImageContainerHeight();
                param.imageContainerWidth = getImageConfig.getImageContainerWidth();
                param.outerImageContainer = getImageConfig.isOuterImageContainer();
                param.outputImagePath = getImageConfig.getOutputImagePath();
                new CompressImageTask(param, new OnCompressImageListener() {
                    public void onCompressStart() {
                        showProgressDialog("", getImageConfig.getProgressMessage() != null ? getImageConfig.getProgressMessage() : "正在处理图片");
                    }
                    public void onCompressFinish(String imagePath, int imageWidth, int imageHeight) {
                        dismissProgressDialog();
                        setResultAndFinish(imagePath, imageWidth, imageHeight);
                    }
                }).execute(imageFile);
            }
        }else if(requestCode == REQUEST_COMPRESS_CODE){
//            String imagePath = data.getStringExtra(SendImageActivity.INTENT_KEY_IMAGE_PATH);
//            int imageWidth = data.getIntExtra(SendImageActivity.INTENT_KEY_IMAGE_WIDTH, 0);
//            int imageHeight = data.getIntExtra(SendImageActivity.INTENT_KEY_IMAGE_HEIGHT, 0);
//            setResultAndFinish(imagePath, imageWidth, imageHeight);
        }else{
            setResultAndFinish(null, 0, 0);
        }
    }
    
    private void setResultAndFinish(String imagePath, int imageWidth, int imageHeight){
        Intent i = getIntent();
        int resultCode = Activity.RESULT_OK;
        if(StringUtil.isValid(imagePath)){
            Bundle b = new Bundle();
            b.putString(KEY_INTENT_IMAGE_PATH, imagePath);
            b.putInt(KEY_INTENT_IMAGE_WIDTH, imageWidth);
            b.putInt(KEY_INTENT_IMAGE_HEIGHT, imageHeight);
            i.putExtras(b);
            resultCode = Activity.RESULT_OK;
        }else{
            resultCode = Activity.RESULT_CANCELED;
        }
        setResult(resultCode, i);
        finish();
    }
    
    public static String doImageLoad(Context ctx, int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) return null;

        Bitmap bitmap = null;
        String resultpath = null;
        if (requestCode == REQUEST_CAMERA_CODE) {//从相册获取的图片
            if (FileHelper.isExists(FileHelper.getTmpImgFileCAPTURE())){
                return FileHelper.getTmpImgFileCAPTURE();
            }
            if(data == null){
                return null;
            }
            resultpath = data.getDataString();
            // file:///mnt/sdcard/dface/cache/img/temp/temp_CAPTURE.png
            String strIndex = "file://";
            if (resultpath.lastIndexOf(strIndex) != -1) resultpath = resultpath.substring(
                    resultpath.lastIndexOf(strIndex) + strIndex.length(), resultpath.length());

            if (StringUtil.isValid(resultpath) && FileHelper.isExists(resultpath)) return resultpath;
            resultpath = null;
            // 获取BMP方法1
            if (StringUtil.isInvalid(resultpath)) {
                Bundle bundle = data.getExtras();
                if (bundle != null) {
                    bitmap = (Bitmap) bundle.get("data");
                    resultpath = ImgHelper.saveImage(bitmap, FileHelper.getTmpImgFile(), 100);
                    bitmap.recycle();
                }
            }
        } else if (requestCode == REQUEST_GALLERY_CODE) {//从相册获取的图片
            if (FileHelper.isExists(FileHelper.getTmpImgFileLoad())){
                return FileHelper.getTmpImgFileLoad();
            }
            if(data == null){
                return null;
            }
            resultpath = null;
            // 获取BMP方法1
            if (StringUtil.isInvalid(resultpath)) {
                try {
                    String[] filePathColumn = { MediaStore.Images.Media.DATA };
                    Cursor cursor = ctx.getContentResolver().query(data.getData(), filePathColumn, null, null, null);
                    if (cursor != null) {
                        cursor.moveToFirst();
                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                        resultpath = cursor.getString(columnIndex);
                        if (!FileHelper.isExists(resultpath)) {
                            resultpath = null;
                        }
                        cursor.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    resultpath = null;
                }
            }
            // 获取BMP方法2
            if (StringUtil.isInvalid(resultpath)) {
                try {
                    resultpath = FileHelper.getTmpImgFile();
                    Uri selectedImage = data.getData();
                    if (selectedImage != null) {
                        // 小米手机只有这种方法才可以
                        File f = new File(resultpath);
                        FileHelper.inputstreamToFile(ctx.getContentResolver().openInputStream(selectedImage), f);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    resultpath = null;
                }
            }
            // 获取BMP方法3
            if (StringUtil.isInvalid(resultpath)) {
                Bundle bundle = data.getExtras();
                if (bundle != null) {
                    bitmap = (Bitmap) bundle.get("data");
                    resultpath = ImgHelper.saveImage(bitmap, FileHelper.getTmpImgFile(), 100);
                    bitmap.recycle();
                }
            }

        }
        return resultpath;
    }
    
    @Override
    public void onBackPressed() {
        setResultAndFinish(null, 0, 0);
    }
    
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.rootView:
            //点击页面空白处，关闭页面
            setResultAndFinish(null, 0, 0);
            break;
        case R.id.from_gallery:
            if(getImageConfig.getGetImageType() == GetImageType.BOTH_SIMPLE){
                openSimpleGallery();
            }else{
                openGallery();
            }
            break;
        case R.id.from_camera:
                openCamera();
            break;
        case R.id.delete:
            setResult(RESULT_DELETE);
            finish();
        }
    }
    
    private void openCamera(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File file = FileHelper.clearTempPngFile(FileHelper.getTmpImgFileCAPTURE());
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        startActivityForResult(intent, REQUEST_CAMERA_CODE);
        
        hideActivityComponents();
    }
    
    private void openGallery(){
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        File file = FileHelper.clearTempPngFile(FileHelper.getTmpImgFileLoad());
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        startActivityForResult(intent, REQUEST_GALLERY_CODE);
        
        hideActivityComponents();
    }

    private void openSimpleGallery(){
        Intent intent = getIntent();
        intent.setClass(this, ImageChooserGroupActivity.class);
        startActivityForResult(intent,ImageChooserGroupActivity.REQUEST_CODE_IMAGE_CHOOSER);

        hideActivityComponents();
    }
    
    private void hideActivityComponents(){
        rootView.removeAllViewsInLayout();
        rootView.setBackgroundColor(Color.TRANSPARENT);
        hasChooseGetImageType = true;
    }
    
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if(outState == null){
            outState = new Bundle();
        }
        outState.putBoolean(HAS_CHOOSE_TYPE, hasChooseGetImageType);
        outState.putSerializable(KEY_GET_IMAGE_CONFIG, getImageConfig);
        super.onSaveInstanceState(outState);
    }
    
    
    /*** 打开Activity ***/
    public static final void gotoGetImage(Activity from, int requestCode){
        gotoGetImage(from, null, null, requestCode);
    }
    
    public static final void gotoGetImage(SimpleFragment from, int requestCode){
        gotoGetImage(from.getFragmentActivity(), from, null, requestCode);
    }
    
    public static final void gotoGetImage(Activity from, GetImageConfig getImageConfig, int requestCode){
        gotoGetImage(from, null, getImageConfig, requestCode);
    }
    
    
    public static final void gotoGetImageHasDelete(SimpleFragment from, int requestCode){
        gotoGetImageHasDelete(from.getFragmentActivity(), from, null, requestCode);
    }
    
    public static final void gotoGetImageHasDelete(Activity from, GetImageConfig getImageConfig, int requestCode){
        gotoGetImageHasDelete(from, null, getImageConfig, requestCode);
    }
    

    public static final void gotoGetImage(SimpleFragment from, GetImageConfig getImageConfig, int requestCode){
        gotoGetImage(from.getFragmentActivity(), from, getImageConfig, requestCode);
    }
    
    public static final void gotoGetImage(Activity from, SimpleFragment fromFragment, GetImageConfig getImageConfig, int requestCode){
        Intent intent = new Intent();
        intent.setClass(from, GetImageActivity.class);
        if(getImageConfig != null){
            intent.putExtra(KEY_GET_IMAGE_CONFIG, getImageConfig);
        }
        if(fromFragment != null){
            fromFragment.startActivityForResult(intent, requestCode);
        }else{
            from.startActivityForResult(intent, requestCode);
        }
    }

    public static final void gotoGetImageSimple(Activity from,GetImageConfig getImageConfig,int selectMaxCount){
        Intent intent = new Intent();
        intent.setClass(from, GetImageActivity.class);
        getImageConfig.setGetImageType(GetImageType.BOTH_SIMPLE);
        intent.putExtra(KEY_GET_IMAGE_CONFIG, getImageConfig);
        intent.putExtra(ImageChooserGroupActivity.EXTRA_SELECTED_COUNT, selectMaxCount);
        from.startActivityForResult(intent, ImageChooserGroupActivity.REQUEST_CODE_IMAGE_CHOOSER);
    }
    
    public static final void gotoGetImageHasDelete(Activity from, SimpleFragment fromFragment, GetImageConfig getImageConfig, int requestCode){
        Intent intent = new Intent();
        intent.setClass(from, GetImageActivity.class);
        if(getImageConfig != null){
            intent.putExtra(KEY_GET_IMAGE_CONFIG, getImageConfig);
        }
        intent.putExtra(KEY_HAS_DELETE, true);
        if(fromFragment != null){
            fromFragment.startActivityForResult(intent, requestCode);
        }else{
            from.startActivityForResult(intent, requestCode);
        }
    }

    public enum GetImageType{
        CAMERA,
        GALLERY,
        BOTH_SIMPLE,
        BOTH;
    }
    
    public static class GetImageConfig implements Serializable {
        
        private static final long serialVersionUID = 2347137285188266570L;
        
        private GetImageType getImageType; //获取图片的方式
        private int imageContainerWidth; //图片容器的宽
        private int imageContainerHeight; //图片容器的高
        private boolean outerImageContainer; //图片Size>=容器
        private String outputImagePath;
        private String imageChooseViewTitle;
        private String[] imageChooseViewItems;
        private String progressMessage;
        private boolean chooseOriginalImageStatus;
        
        public GetImageType getGetImageType() {
            return getImageType;
        }
        public void setGetImageType(GetImageType getImageType) {
            this.getImageType = getImageType;
        }
        public int getImageContainerWidth() {
            return imageContainerWidth;
        }
        public void setImageContainerWidth(int imageContainerWidth) {
            this.imageContainerWidth = imageContainerWidth;
        }
        public int getImageContainerHeight() {
            return imageContainerHeight;
        }
        public void setImageContainerHeight(int imageContainerHeight) {
            this.imageContainerHeight = imageContainerHeight;
        }
        public boolean isOuterImageContainer() {
            return outerImageContainer;
        }
        public void setOuterImageContainer(boolean outerImageContainer) {
            this.outerImageContainer = outerImageContainer;
        }
        public String getOutputImagePath() {
            return outputImagePath;
        }
        public void setOutputImagePath(String outputImagePath) {
            this.outputImagePath = outputImagePath;
        }
        public void setImageChooseViewItems(String[] imageChooseViewItems) {
            this.imageChooseViewItems = imageChooseViewItems;
        }
        public String[] getImageChooseViewItems() {
            return imageChooseViewItems;
        }
        public void setImageChooseViewTitle(String imageChooseViewTitle) {
            this.imageChooseViewTitle = imageChooseViewTitle;
        }
        public String getImageChooseViewTitle() {
            return imageChooseViewTitle;
        }
        public void setProgressMessage(String progressMessage) {
            this.progressMessage = progressMessage;
        }
        public String getProgressMessage() {
            return progressMessage;
        }
        public void setChooseOriginalImageStatus(boolean chooseOriginalImageStatus) {
            this.chooseOriginalImageStatus = chooseOriginalImageStatus;
        }
        public boolean isChooseOriginalImageStatus() {
            return chooseOriginalImageStatus;
        }
    }
}
