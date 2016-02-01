/*******************************************************************************
 * Copyright 2011, 2012 Chris Banes.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.xiaoma.beiji.controls.view.photoview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.xiaoma.beiji.R;
import com.xiaoma.beiji.util.ImageLoaderUtil;
import com.xiaoma.beiji.util.ImageUtils;

import java.io.File;
import java.lang.ref.SoftReference;

public class PhotoView extends ImageView implements IPhotoView {

	private final PhotoViewAttacher mAttacher;
	private DisplayImageOptions options;

	private ScaleType mPendingScaleType;
	private SoftReference<Bitmap> mBmp = null;
	private String image_uri = null;
	private int width;
	private int height;

	public PhotoView(Context context) {
		this(context, null);
	}
	
	public void setArea(int width, int height){	
		this.width = width;
		this.height = height;
	}

	public PhotoView(Context context, AttributeSet attr) {
		this(context, attr, 0);
	}
	
	public PhotoView(Context context, AttributeSet attr, int defStyle) {
		super(context, attr, defStyle);
		super.setScaleType(ScaleType.MATRIX);
		mAttacher = new PhotoViewAttacher(this);

		if (null != mPendingScaleType) {
			setScaleType(mPendingScaleType);
			mPendingScaleType = null;
		}
	}
	

	@Override
	public boolean canZoom() {
		return mAttacher.canZoom();
	}

	@Override
	public RectF getDisplayRect() {
		return mAttacher.getDisplayRect();
	}

	@Override
	public float getMinScale() {
		return mAttacher.getMinScale();
	}

	@Override
	public float getMidScale() {
		return mAttacher.getMidScale();
	}

	@Override
	public float getMaxScale() {
		return mAttacher.getMaxScale();
	}

	@Override
	public float getScale() {
		return mAttacher.getScale();
	}

	@Override
	public ScaleType getScaleType() {
		return mAttacher.getScaleType();
	}

    @Override
    public void setAllowParentInterceptOnEdge(boolean allow) {
        mAttacher.setAllowParentInterceptOnEdge(allow);
    }

    @Override
	public void setMinScale(float minScale) {
		mAttacher.setMinScale(minScale);
	}

	@Override
	public void setMidScale(float midScale) {
		mAttacher.setMidScale(midScale);
	}

	@Override
	public void setMaxScale(float maxScale) {
		mAttacher.setMaxScale(maxScale);
	}

	@Override
	// setImageBitmap calls through to this method
	public void setImageDrawable(Drawable drawable) {
		super.setImageDrawable(drawable);
		if (null != mAttacher) {
			mAttacher.update();
		}
	}

	@Override
	public void setImageResource(int resId) {
		super.setImageResource(resId);
		if (null != mAttacher) {
			mAttacher.update();
		}
	}
	
	private Bitmap getBitmapFromSoftReference(String uri){
		Bitmap map = null;
		if(mBmp != null){
			if(uri.equals(image_uri)){
				map = mBmp.get();
			}else{
//				//如果URI变换了，则清除脏数据
				Bitmap bitmaptemp = mBmp.get();
				if(bitmaptemp != null && bitmaptemp.isRecycled() == false){
					bitmaptemp.recycle();
				}
				bitmaptemp = null;
				mBmp.clear();
			}
		}
		return map;
	}

	public void setImageURI(String uri) {
		Bitmap m = null;
		if (options == null) {
			options = ImageLoaderUtil.getDefaultImageLoaderOption();
		}		
		if(uri == null || uri.length() < 1){
			this.setImageResource(R.drawable.ic_empty);
			return;						
		}
		
		m = getBitmapFromSoftReference(uri);
		
		if(m != null){
			setImageBitmap(m);
			return;
		}
		image_uri = uri;
		ImageLoader imageLoader = ImageLoader.getInstance();
		m = imageLoader.getMemoryCache().get(uri);
		boolean hasBmp = false;
		if(m != null){
			setImageBitmap(m);
			mBmp = new SoftReference<Bitmap>(m);
			m = null;
		    hasBmp = true;
		}else{
			File file = imageLoader.getDiscCache().get(uri);
			if(file != null && file.exists()) {
				String path = file.getPath();
				try {
					m = ImageUtils.getBitmapFromFile(path, ImageUtils.getMaxSampleSize(path, width, height));
					setImageBitmap(m);
					mBmp = new SoftReference<Bitmap>(m);
					m = null;
					hasBmp = true;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		if(hasBmp == false){
			setImageResource(R.drawable.ic_empty);
			imageLoader.displayImage(uri, this, options, new ImageLoadingListener() {
				
				@Override
				public void onLoadingStarted(String arg0, View arg1) {}
				
				@Override
				public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {}
				
				@Override
				public void onLoadingComplete(String uri, View arg1, Bitmap bmp) {
					image_uri = uri;
					mBmp = new SoftReference<Bitmap>(bmp);
					bmp = null;
				}
				
				@Override
				public void onLoadingCancelled(String arg0, View arg1) {}
			});
			//imageLoader.displayImage(uri, this, options);
		}
	}

	@Override
	public void setImageURI(Uri uri) {
		super.setImageURI(uri);
		if (null != mAttacher) {
			mAttacher.update();
		}
	}

	@Override
	public void setOnMatrixChangeListener(PhotoViewAttacher.OnMatrixChangedListener listener) {
		mAttacher.setOnMatrixChangeListener(listener);
	}
	
	public void setPictureClickListener(PhotoViewAttacher.PictureClickListener listener) {
		mAttacher.setPictureClickListener(listener);
	}		

	@Override
	public void setOnLongClickListener(OnLongClickListener l) {
		mAttacher.setOnLongClickListener(l);
	}

	@Override
	public void setOnPhotoTapListener(PhotoViewAttacher.OnPhotoTapListener listener) {
		mAttacher.setOnPhotoTapListener(listener);
	}

	@Override
	public void setOnViewTapListener(PhotoViewAttacher.OnViewTapListener listener) {
		mAttacher.setOnViewTapListener(listener);
	}

	@Override
	public void setScaleType(ScaleType scaleType) {
		if (null != mAttacher) {
			mAttacher.setScaleType(scaleType);
		} else {
			mPendingScaleType = scaleType;
		}
	}

	@Override
	public void setZoomable(boolean zoomable) {
		mAttacher.setZoomable(zoomable);
	}

	@Override
	public void zoomTo(float scale, float focalX, float focalY) {
		mAttacher.zoomTo(scale, focalX, focalY);
	}

	@Override
	protected void onDetachedFromWindow() {
		mAttacher.cleanup();
		super.onDetachedFromWindow();
	}

}