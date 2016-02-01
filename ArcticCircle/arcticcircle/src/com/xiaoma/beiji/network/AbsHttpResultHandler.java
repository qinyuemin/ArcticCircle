/**
 * 项目名： eyah
 * 包名： com.sttri.eyah.network
 * 版本信息： 1.0.0
 * Copyright (c) -版权所有
 */
package com.xiaoma.beiji.network;

import java.util.List;

/**
 * 类名称： JsonHttpResonseHandler
 * 类描述：
 * 创建人： gang.shi
 * 修改人： gang.shi
 * 修改时间： 2015年06月10 11:46
 * 修改备注：
 *
 * @version 1.0.0
 */
public abstract class AbsHttpResultHandler<T> {

    public abstract void onSuccess(int resultCode, String desc, T data);

    public  void onSuccess(int resultCode, String desc, List<T> data){}

    public abstract void onFailure(int resultCode, String desc);
}
