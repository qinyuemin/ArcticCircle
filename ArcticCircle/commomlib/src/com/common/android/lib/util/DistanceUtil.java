package com.common.android.lib.util;

/**
 *
 * 类名称： DistanceUtil
 * 类描述： GPS 坐标相关计算工具类
 * 创建人： gang.shi
 * 修改人： gang.shi
 * 修改时间： 2015年06月08 10:42
 * 修改备注：
 * @version 1.0.0
 *
 */
public class DistanceUtil {
	public static final double R = 6371.004;

	/**
	 * 根据给定的两个经纬度计算两地之间的距离，单位m
	 * 
	 * @param lon1
	 *            经度1
	 * @param lat1
	 *            纬度1
	 * @param lon2
	 *            经度2
	 * @param lat2
	 *            纬度2
	 * @return 两地距离
	 */
	public static double getDistance(double lon1, double lat1, double lon2,
			double lat2) {
		double x = changeToRad(lon1);
		double y = changeToRad(lat1);
		double a = changeToRad(lon2);
		double b = changeToRad(lat2);
		double rad = Math.acos(Math.cos(y) * Math.cos(b) * Math.cos(x - a)
				+ Math.sin(y) * Math.sin(b));
		if (rad > Math.PI)
			rad = Math.PI * 2 - rad;
		return R * rad *1000;
	}

	/**
	 * 将角度转化为弧度
	 * 
	 * @param angle
	 *            角度
	 * @return 弧度
	 */
	public static double changeToRad(double angle) {
		return angle / 180 * Math.PI;
	}
}
