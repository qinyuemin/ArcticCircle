/**
 * 项目名： arcticCircle
 * 包名： com.xiaoma.beiji.manager.chatting
 * 版本信息： 1.0.0
 * Copyright (c) 2015上海路路由信息科技有限公司-版权所有
 */
package com.xiaoma.beiji.manager.chatting;

/**
 *
 * 类名称： XmppException
 * 类描述： 
 * 创建人： gang.shi
 * 修改人： gang.shi
 * 修改时间： 2015年12月17 23:26
 * 修改备注：
 * @version 1.0.0
 *
 */
public class XmppException extends Exception {
    public XmppException() {
        super();
    }

    public XmppException(String detailMessage) {
        super(detailMessage);
    }

    public XmppException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public XmppException(Throwable throwable) {
        super(throwable);
    }
}
