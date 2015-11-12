package com.zhimei.liang.utitls;

import android.graphics.Bitmap;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by 张佳亮 on 2015/11/3.
 */
public class DonationRecord  extends BmobObject {
    private String name;//商品名称
    private String receiveOrganization;//接收单位
    private String time;//时间
    private Bitmap picture;//照片
    private BmobFile pictureFile;
    private String publishMan;//发布人

    public String getPublishMan() {
        return publishMan;
    }

    public void setPublishMan(String publishMan) {
        this.publishMan = publishMan;
    }

    public BmobFile getPictureFile() {
        return pictureFile;
    }

    public void setPictureFile(BmobFile pictureFile) {
        this.pictureFile = pictureFile;
    }

    public Bitmap getPicture() {
        return picture;
    }

    public DonationRecord(String name, String receiveOrganization, String time, Bitmap picture) {
        this.name = name;
        this.receiveOrganization = receiveOrganization;
        this.time = time;
        this.picture = picture;
    }

    public void setPicture(Bitmap picture) {
        this.picture = picture;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getReceiveOrganization() {
        return receiveOrganization;
    }

    public void setReceiveOrganization(String receiveOrganization) {
        this.receiveOrganization = receiveOrganization;
    }

    public String getTime() {
        return time;
    }


    public void setTime(String time) {
        this.time = time;

    }
    public DonationRecord(){

    }
}
