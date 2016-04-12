package com.xiaoma.beiji.entity;

import java.util.List;

public class HotShop{
        private List<ShopEntity> list;

        public HotShop(List<ShopEntity> list) {
            this.list = list;
        }

    public List<ShopEntity> getList() {
        return list;
    }

    public void setList(List<ShopEntity> list) {
        this.list = list;
    }
}