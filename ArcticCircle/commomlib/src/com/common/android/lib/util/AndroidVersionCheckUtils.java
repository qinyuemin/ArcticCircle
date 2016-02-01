/**
 *
 *项目名：LuluYouLib
 *包名：com.luluyou.android.lib.utils
 *文件名：AndroidVersionCheckUtils.java
 *版本信息：1.0.0
 *创建日期：2013年12月6日-下午2:43:17
 *创建人：jerry.deng
 *Copyright (c) 2013上海路路由信息科技有限公司-版权所有
 * 
 */
package com.common.android.lib.util;

import android.os.Build;

/**
 * @Title AndroidVersionCheckUtils
 * @Package com.ta.core.util.extend.app
 * @Description AndroidVersionCheckUtils 用于多版本兼容检测
 * @author 白猫
 * @date 2013-1-10 上午 9:53
 * @version V1.0
 */
public class AndroidVersionCheckUtils
{
	private AndroidVersionCheckUtils()
	{
	};

	/**
	 * 当前Android系统版本是否在（ Donut） Android 1.6或以上
	 * 
	 * @return
	 */
	public static boolean hasDonut()
	{
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.DONUT;
	}

	/**
	 * 当前Android系统版本是否在（ Eclair） Android 2.0或 以上
	 * 
	 * @return
	 */
	public static boolean hasEclair()
	{
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR;
	}

	/**
	 * 当前Android系统版本是否在（ Froyo） Android 2.2或 Android 2.2以上
	 * 
	 * @return
	 */
	public static boolean hasFroyo()
	{
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO;
	}

	/**
	 * 当前Android系统版本是否在（ Gingerbread） Android 2.3x或 Android 2.3x 以上
	 * 
	 * @return
	 */
	public static boolean hasGingerbread()
	{
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD;
	}

	/**
	 * 当前Android系统版本是否在（ Honeycomb） Android3.1或 Android3.1以上
	 * 
	 * @return
	 */
	public static boolean hasHoneycomb()
	{
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
	}

	/**
	 * 当前Android系统版本是否在（ HoneycombMR1） Android3.1.1或 Android3.1.1以上
	 * 
	 * @return
	 */
	public static boolean hasHoneycombMR1()
	{
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1;
	}

	/**
	 * 当前Android系统版本是否在（ IceCreamSandwich） Android4.0或 Android4.0以上
	 * 
	 * @return
	 */
	public static boolean hasIcecreamsandwich()
	{
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH;
	}

	/*
	 * public static boolean hasJellyBean() { return Build.VERSION.SDK_INT >=
	 * Build.VERSION_CODES.JELLY_BEAN; }
	 */
}
