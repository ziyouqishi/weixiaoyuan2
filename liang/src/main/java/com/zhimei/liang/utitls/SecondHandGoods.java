package com.zhimei.liang.utitls;


import android.graphics.Bitmap;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by 张佳亮 on 2015/10/5.
 * 关于商品的类，目前每个商品只有5个属性
 */
public class SecondHandGoods extends BmobObject {
    private int picture;//商品图片
    private String price;//商品价格
    private String name;//商品名称
    private String var_data;//商品发布日期
    private String description;//商品描述
    private String publishMan;//商品发布人
    private BmobFile pictureFile;
    private String category;
    private Bitmap photo;//照片
    private String tradeWay;

    public String getTradeWay() {
        return tradeWay;
    }

    public void setTradeWay(String tradeWay) {
        this.tradeWay = tradeWay;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public BmobFile getPictureFile() {
        return pictureFile;
    }

    public void setPictureFile(BmobFile pictureFile) {
        this.pictureFile = pictureFile;
    }

    public String getPublishMan() {
        return publishMan;
    }

    public void setPublishMan(String publishMan) {
        this.publishMan = publishMan;
    }

    public String getDescription() {
        return description;
    }

    public Bitmap getPhoto() {
        return photo;
    }

    public void setPhoto(Bitmap photo) {
        this.photo = photo;
    }

    public SecondHandGoods(Bitmap photo, String price, String name, String var_data, String description) {
        this.photo=photo;
        this.price = price;
        this.name = name;

        this.var_data = var_data;
        this.description = description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


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



    public void setVar_data(String var_data) {
        this.var_data = var_data;
    }
    public SecondHandGoods(){

    }


}
