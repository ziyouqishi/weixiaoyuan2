package com.zhimei.liang.utitls;

/**
 * Created by 张佳亮 on 2015/10/12.
 */
public class ShopGoods {
    private int picture;
    private String name;
    private String price;
    private String sell;
    private String state;
    private int buynum=0;

    public int getBuynum() {
        return buynum;
    }

    public void setBuynum(int buynum) {
        this.buynum = buynum;
    }

    public ShopGoods(int picture, String state, String sell, String price, String name) {
        this.picture = picture;
        this.state = state;
        this.sell = sell;
        this.price = price;
        this.name = name;
    }

    public int getPicture() {
        return picture;
    }

    public void setPicture(int picture) {
        this.picture = picture;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getSell() {
        return sell;
    }

    public void setSell(String sell) {
        this.sell = sell;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
