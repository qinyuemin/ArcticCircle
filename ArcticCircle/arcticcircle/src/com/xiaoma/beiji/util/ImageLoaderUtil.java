package com.xiaoma.beiji.util;

import android.graphics.Bitmap;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.xiaoma.beiji.R;

import java.io.File;

public class ImageLoaderUtil {

    public static DisplayImageOptions getDefaultImageLoaderOption() {
        return getDefaultImageLoaderOption(Bitmap.Config.RGB_565);
    }

    public static DisplayImageOptions getDefaultBigImageLoaderOption() {
        return getDefaultBigImageLoaderOption(Bitmap.Config.RGB_565);
    }

    public static DisplayImageOptions getDefaultSmallImageLoaderOption() {
        return getDefaultSmallImageLoaderOption(Bitmap.Config.RGB_565);
    }

    public static DisplayImageOptions getDefaultBannerImageLoaderOption() {
        return getDefaultBannerImageLoaderOption(Bitmap.Config.RGB_565);

    }

    public static DisplayImageOptions getDefaultAvatarWriteCircleOption() {
        return getOptionDefaultAvatarWriteCircle(Bitmap.Config.RGB_565);
    }

    public static DisplayImageOptions getDefaultAvatarOption() {
        return getOptionDefaultAvatar(Bitmap.Config.RGB_565);
    }

    public static DisplayImageOptions getDefaultImageLoaderOption(Bitmap.Config config) {
        return getDefaultImageLoaderOptionBuilder(config).showImageOnLoading(R.drawable.ic_empty).showImageForEmptyUri(R.drawable.ic_empty).showImageOnFail(R.drawable.ic_empty).build();
    }

    public static DisplayImageOptions getDefaultDownloadImageOption() {
        return getDefaultImageLoaderOptionBuilder(Bitmap.Config.RGB_565).showImageOnLoading(R.drawable.img_download_ing).showImageForEmptyUri(R.drawable.img_download_failed).showImageOnFail(R.drawable.img_download_failed).build();
    }

    public static DisplayImageOptions getDefaultBigImageLoaderOption(Bitmap.Config config) {
        //		return getDefaultImageLoaderOptionBuilder(config).showImageOnLoading(R.drawable.ic_empty_big).showImageForEmptyUri(R.drawable.ic_empty_big).showImageOnFail(R.drawable.ic_empty_big).build();
        return getDefaultImageLoaderOptionBuilder(config).showImageOnLoading(R.drawable.ic_empty).showImageForEmptyUri(R.drawable.ic_empty).showImageOnFail(R.drawable.ic_empty).build();
    }

    public static DisplayImageOptions getDefaultSmallImageLoaderOption(Bitmap.Config config) {
        //		return getDefaultImageLoaderOptionBuilder(config).showImageOnLoading(R.drawable.ic_empty_small).showImageForEmptyUri(R.drawable.ic_empty_small).showImageOnFail(R.drawable.ic_empty_small).build();
        return getDefaultImageLoaderOptionBuilder(config).showImageOnLoading(R.drawable.ic_empty).showImageForEmptyUri(R.drawable.ic_empty).showImageOnFail(R.drawable.ic_empty).build();
    }

    public static DisplayImageOptions getDefaultBannerImageLoaderOption(Bitmap.Config config) {
        //		return getDefaultImageLoaderOptionBuilder(config).showImageOnLoading(R.drawable.ic_empty_bar).showImageForEmptyUri(R.drawable.ic_empty_bar).showImageOnFail(R.drawable.ic_empty_bar).build();
        return getDefaultImageLoaderOptionBuilder(config).showImageOnLoading(R.drawable.ic_empty).showImageForEmptyUri(R.drawable.ic_empty).showImageOnFail(R.drawable.ic_empty).build();
    }

    public static DisplayImageOptions getOptionDefaultAvatarWriteCircle(Bitmap.Config config) {
        return getDefaultImageLoaderOptionBuilder(config).showImageOnLoading(R.drawable.ic_def_write_circle_header).showImageForEmptyUri(R.drawable.ic_def_write_circle_header).showImageOnFail(R.drawable.ic_def_write_circle_header).build();
    }

    public static DisplayImageOptions getOptionDefaultAvatar(Bitmap.Config config) {
        return getDefaultImageLoaderOptionBuilder(config).showImageOnLoading(R.drawable.ic_def_header).showImageForEmptyUri(R.drawable.ic_def_header).showImageOnFail(R.drawable.ic_def_header).build();
    }

    public static DisplayImageOptions.Builder getDefaultImageLoaderOptionBuilder() {
        return getDefaultImageLoaderOptionBuilder(Bitmap.Config.RGB_565);
    }

    public static DisplayImageOptions.Builder getDefaultImageLoaderOptionBuilder(Bitmap.Config config) {
        DisplayImageOptions.Builder bulder = new DisplayImageOptions.Builder().imageScaleType(ImageScaleType.IN_SAMPLE_INT).cacheOnDisc(true).considerExifParams(true);
        if (config != null) {
            bulder.bitmapConfig(config);
        }
        return bulder;
    }

    public static DisplayImageOptions getOptionDefaultUserInfo() {
        return getOptionDefaultUserInfo(Bitmap.Config.RGB_565);
    }

    public static DisplayImageOptions getOptionDefaultUserInfo(Bitmap.Config config) {
        return getDefaultImageLoaderOptionBuilder(config).showImageOnLoading(R.drawable.default_user_profiler_inner).showImageForEmptyUri(R.drawable.default_user_profiler_inner).showImageOnFail(R.drawable.default_user_profiler_inner).build();
    }

    public static boolean hasImageFileInDisc(String imageUrl) {
        File file = ImageLoader.getInstance().getDiscCache().get(imageUrl);
        return file != null && file.exists() && file.isFile();
    }
}
