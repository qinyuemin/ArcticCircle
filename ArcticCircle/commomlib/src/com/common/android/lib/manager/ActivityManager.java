package com.common.android.lib.manager;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import android.app.Activity;

public class ActivityManager {
     
     private static ActivityManager activityManager;
      
     public static ActivityManager getActivityManager(){
             if(activityManager == null){
                     activityManager = new ActivityManager();
             }
             return activityManager;
     }
      
     private ActivityManager(){
     }
      
     /**
      * task map，用于记录activity栈，方便退出程序（这里为了不影响系统回收activity，所以用软引用）
     */
     private final HashMap<String, SoftReference<Activity>> taskMap = new HashMap<String, SoftReference<Activity>>();
      
     /**
      * 往应用task map加入activity
      */
     public final void putActivity(Activity atv) {
             taskMap.put(atv.toString(), new SoftReference<Activity>(atv));
     }
      
     /**
      * 往应用task map加入activity
      */
     public final void removeActivity(Activity atv) {
             taskMap.remove(atv.toString());
     }
      
     /**
      * 清除应用的task栈，如果程序正常运行这会导致应用退回到桌面
     */
     public final void exit() {
		List<Activity> list = new ArrayList<Activity>();
		for (Iterator<Entry<String, SoftReference<Activity>>> iterator = taskMap.entrySet().iterator(); iterator.hasNext();) {
			SoftReference<Activity> activityReference = iterator.next().getValue();
			Activity activity = activityReference.get();
			if (activity != null) {
				list.add(activity);
			}
		}
		for (Activity activity : list) {
			activity.finish();
//			taskMap.remove(activity.toString());//activity.finish()中会回调此方法，所以不需要重复调用
		}
		//taskMap.clear();
     }
     
     
     public final void removeActivity(String activityName){
		Activity targetActivity = null;
		for (Iterator<Entry<String, SoftReference<Activity>>> iterator = taskMap.entrySet().iterator(); iterator.hasNext();) {
			SoftReference<Activity> activityReference = iterator.next().getValue();
			Activity activity = activityReference.get();
			if (activity != null && activity.getClass().getName().equals(activityName)) {
				targetActivity = activity;
				break;
			}

		}
		if (targetActivity != null) {
			targetActivity.finish();
		}
     }
     
     
     public final boolean  isExistActivity(String activityName)
     {
 		for (Iterator<Entry<String, SoftReference<Activity>>> iterator = taskMap.entrySet().iterator(); iterator.hasNext();) {
 			SoftReference<Activity> activityReference = iterator.next().getValue();
 			Activity activity = activityReference.get();
 			if (activity != null && activity.getClass().getName().equals(activityName)) {
 				return true;
 			}

 		}
 		return false;
     }
     
     
     public final void exitPreActivity(Activity currentActivity) {
    	List<Activity> list = new ArrayList<Activity>();
		
    	for (Iterator<Entry<String, SoftReference<Activity>>> iterator = taskMap.entrySet().iterator(); iterator.hasNext();) {
			SoftReference<Activity> activityReference = iterator.next().getValue();
			Activity activity = activityReference.get();
			if (activity != null && !activity.toString().equals(currentActivity.toString())) {
				list.add(activity);
			}
		}
		
		for(Activity a : list){
			a.finish();
		}
		//taskMap.clear();
     }
}
