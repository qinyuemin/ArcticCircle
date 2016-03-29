package com.xiaoma.beiji.entity;

import java.util.List;


/**
 * Created by zhangqibo on 2016/3/7.
 */
public class Thing {
    private String title;
    private String content;
    private String imgUrl;
    private List<String> freinds;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public List<String> getFreinds() {
        return freinds;
    }

    public void setFreinds(List<String> freinds) {
        this.freinds = freinds;
    }
}
