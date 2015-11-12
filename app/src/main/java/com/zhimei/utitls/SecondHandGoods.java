package com.zhimei.utitls;


/**
 * Created by 张佳亮 on 2015/10/5.
 * 关于商品的类，目前每个商品只有5个属性
 */
public class SecondHandGoods {

    private int picture;//商品图片
    private String price;//商品价格
    private String name;//商品名称
    private String var_data;//商品发布日期
    private String attention;//商品关注度

    public int getPicture() {
        return picture;
    }

    public void setPicture(int picture) {
        this.picture = picture;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVar_data() {
        return var_data;
    }

    public String getAttention() {
        return attention;
    }

    public void setAttention(String attention) {
        this.attention = attention;
    }

    public void setVar_data(String var_data) {
        this.var_data = var_data;
    }

    public SecondHandGoods(int picture, String price, String name, String var_data, String attention) {
        this.picture = picture;
        this.price = price;
        this.name = name;
        this.var_data = var_data;
        this.attention = attention;
    }
}
