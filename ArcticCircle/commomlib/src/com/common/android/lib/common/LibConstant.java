/**
 *
 *项目名：LuluyouLibFor3.0
 *包名：com.luluyou.android.lib.common
 *文件名：Constance.java
 *版本信息：1.0.0
 *创建日期：2015年2月11日-下午6:55:44
 *创建人：jerry.deng
 *Copyright (c) 2015上海路路由信息科技有限公司-版权所有
 * 
 */
package com.common.android.lib.common;


/**
 *
 * 类名称：Constance
 * 类描述：
 * 创建人： Gang.Shi
 * 修改人： Gang.Shi
 * 修改时间： 2015年2月11日 下午6:55:44
 * 修改备注：
 * @version 1.0.0
 *
 */
public class LibConstant {
    public static final UrlType BASE_URL_TYPE = UrlType.RELEASE;
    
    
    public static enum UrlType {
        INTERNAL("内部测试版"), PRE_PUBLISH("预发布测试版"), RELEASE("正式版");
        
        private String value;
        private UrlType(String value){
            this.value = value;
        }
        
        public String getValue() {
            return value;
        }
    }
}
