package com.common.android.lib.util;

import java.io.File;

import android.os.Environment;

public class SDCardUtil {
	
	public static final boolean SDCardExist(){
		return getSDCardPath() != null;
	}
	
	public static final String getSDCardPath() {
		if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
			return Environment.getExternalStorageDirectory().getPath();
		} else {
			// 需要特殊处理的机型
			File f = new File("/storage/sdcard");
			if (f.exists() && f.isDirectory() && f.canRead() && f.canWrite()) {
				return f.getPath();
			}

			f = new File("/storage/sdcard0");
			if (f.exists() && f.isDirectory() && f.canRead() && f.canWrite()) {
				return f.getPath();
			}

			f = new File("/storage/sdcard1");
			if (f.exists() && f.isDirectory() && f.canRead() && f.canWrite()) {
				return f.getPath();
			}

			f = new File("/storage/sdcard2");
			if (f.exists() && f.isDirectory() && f.canRead() && f.canWrite()) {
				return f.getPath();
			}

			f = new File("/mnt/sdcard2");
			if (f.exists() && f.isDirectory() && f.canRead() && f.canWrite()) {
				return f.getPath();
			}
			
			return null;
		}
	}
}
