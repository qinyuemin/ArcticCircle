/**
 * 项目名： arcticCircle
 * 包名： com.xiaoma.beiji.entity
 * 版本信息： 1.0.0
 * Copyright (c) 2015上海路路由信息科技有限公司-版权所有
 */
package com.xiaoma.beiji.entity;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;

/**
 *
 * 类名称： CommentListEntity
 * 类描述： 
 * 创建人： gang.shi
 * 修改人： gang.shi
 * 修改时间： 2015年12月15 19:02
 * 修改备注：
 * @version 1.0.0
 *
 */
public class CommentListEntity {
    @JSONField(name = "list")
    private List<CommentEntity> commentEntities;

    @JSONField(name = "total")
    private int total;
    @JSONField(name = "pages")
    private int pages;

    public List<CommentEntity> getCommentEntities() {
        return commentEntities;
    }

    public void setCommentEntities(List<CommentEntity> commentEntities) {
        this.commentEntities = commentEntities;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }
}
