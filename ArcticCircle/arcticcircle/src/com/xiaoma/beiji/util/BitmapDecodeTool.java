package com.xiaoma.beiji.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;


public class BitmapDecodeTool {
	/**
	 * 调入BITMAP（支持调入图片N次失败又重试）
	 * 
	 * @param filename
	 * @param width
	 *            在缩放模式下，输出宽度
	 * @param height
	 *            在缩放模式下，输出高度
	 * @param maxMultiple
	 *            内存不足时，缩放步宽 比如 maxMultiple=3，那么按3倍缩小
	 * @param config
	 * @param isScale
	 *            支持缩放吗 ？
	 * @author DingWentao
	 * @return
	 */
	public static synchronized Bitmap decodeBitmap(String filename, int width, int height, Bitmap.Config config, boolean outerTarget) {
		// 只加载基础信息,并不真正解码图片
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		// 这里进行假解码,获取宽高等信息,此时返回的bitmap是null的,因为并不真正进行解码
		BitmapFactory.decodeFile(filename, options);
		if (options.outWidth < 1 || options.outHeight < 1) {
			String fn = filename;
			File ft = new File(fn);
			if (ft.exists()) {
				ft.delete();
				return null;
			}
		}
		options.inPreferredConfig = config;
		
		int rotate = ImgHelper.getImageFileRotate(filename);
		int _inSampleSize = 1;
		if(rotate == 0 || rotate == 180){
			_inSampleSize = calculateOriginal(options.outWidth, options.outHeight, width, height, outerTarget);
		}else{
			_inSampleSize = calculateOriginal(options.outWidth, options.outHeight, height, width, outerTarget);
		}
		_inSampleSize = _inSampleSize < 1 ? 1 : _inSampleSize;
		// 计算缩放率
		options.inSampleSize = _inSampleSize;
		// 设置为false开始真的解码图片
		options.inJustDecodeBounds = false;
		Bitmap bm1 = null;
		try {
			bm1 = BitmapFactory.decodeFile(filename, options);
		} catch (Error e) {
			// 发生错误进行再次压缩
			Runtime.getRuntime().runFinalization();
			try {
				Thread.sleep(600);
				options.inSampleSize = options.inSampleSize << 1;
				options.inJustDecodeBounds = false;
				options.inDither = true;
				options.inPreferredConfig = null;
				try {
					bm1 = BitmapFactory.decodeFile(filename, options);
				} catch (Error e2) {
					// 还错,继续压缩并且在sdcard中建立缓存区
					Runtime.getRuntime().runFinalization();
					try {
						Thread.sleep(600);
						options.inSampleSize = options.inSampleSize << 1;
						// 内存不足的情况下尝试在sdcard开辟空间存储内存
						options.inTempStorage = new byte[12 * 1024];
						options.inJustDecodeBounds = false;
						options.inDither = true;
						options.inPreferredConfig = null;
						try {
							bm1 = BitmapFactory.decodeFile(filename, options);
						} catch (Error e4) {
							// 实在不行了返回null,解码失败
							Runtime.getRuntime().runFinalization();
							bm1 = null;
						}
					} catch (InterruptedException e3) {
					}
				}
			} catch (InterruptedException e1) {
			}
		}
		return bm1;
	}

	public static float calculateMaxScale(int width, int height, int targetWidth, int targetHeight, int rotate, boolean outerTarget){
		float scaleW = 0;
		float scaleH = 0;
		if(rotate == 0 || rotate == 180){
			scaleW = targetWidth / (float)width;
			scaleH = targetHeight / (float)height;
		}else{//如果是90或270度，则宽高要对调
			scaleW = targetWidth / (float)height;
			scaleH = targetHeight / (float)width;
		}
		if(outerTarget){
			return scaleW > scaleH ? scaleW : scaleH;
		}else{
			return scaleW < scaleH ? scaleW : scaleH;
		}
	}

	private static int calculateOriginal(int width, int height, int targetWidth, int targetHeight, boolean outerTarget){
		int inSampleSize = 1;
		
		if(outerTarget){
			if (height > targetHeight && width > targetWidth) {
				while(true){
					if(targetHeight * inSampleSize > height || targetWidth * inSampleSize > width){
						if(inSampleSize > 1){
							inSampleSize = inSampleSize >> 1;
							break;
						}
					}
					inSampleSize = inSampleSize << 1;
				}
			}
		}else{
			if (height > targetHeight || width > targetWidth) {
				while(true){
					if(targetHeight * inSampleSize > height && targetWidth * inSampleSize > width){
						if(inSampleSize > 1){
							inSampleSize = inSampleSize >> 1;
							break;
						}
					}
					inSampleSize = inSampleSize << 1;
				}
			}
		}
		
		return inSampleSize;
	}

//	/**
//	 * 按照宽高计算bitmap所占内存大小
//	 */
//	public static long GetBitmapSize(long bmpwidth, long bmpheight, Bitmap.Config config) {
//		return bmpwidth * bmpheight * getBytesxPixel(config);
//	}
//
//	/**
//	 * 计算bitmap所占空间,单位bytes
//	 */
//	public static int sizeOfBitmap(Bitmap bitmap, Bitmap.Config config) {
//		int size = 1;
//		// 3.1或者以上
//		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
//			size = bitmap.getByteCount() * getBytesxPixel(config) >> 10;
//		}
//		// 3.1以下
//		else {
//			size = bitmap.getRowBytes() * bitmap.getHeight() * getBytesxPixel(config) >> 10;
//		}
//		return size;
//	}
//
//	/**
//	 * 按照不同格式计算所占字节数
//	 */
//	public static int getBytesxPixel(Bitmap.Config config) {
//		int bytesxPixel = 1;
//		// 3.1或者以上
//		switch (config) {
//		case RGB_565:
//		case ARGB_4444:
//			bytesxPixel = 2;
//			break;
//		case ALPHA_8:
//			bytesxPixel = 1;
//			break;
//		case ARGB_8888:
//			bytesxPixel = 4;
//			break;
//		}
//		return bytesxPixel;
//	}
//
//	/**
//	 * 当前空闲的堆内存
//	 */
//	public static long FreeMemory() {
//		return Runtime.getRuntime().maxMemory() - Debug.getNativeHeapAllocatedSize();
//	}
//
//	/**
//	 * 检测当前是否有足够的内存进行读取bitmap
//	 */
//	public static boolean CheckBitmapFitsInMemory(long bmpwidth, long bmpheight, Bitmap.Config config) {
//		return (GetBitmapSize(bmpwidth, bmpheight, config) < FreeMemory());
//	}
}
