package com.zhimei.liang.utitls;

import android.graphics.Bitmap;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by 张佳亮on 2015/11/11.
 */
public class DemandObject extends BmobObject {
    private String goodName;
    private Bitmap headPicture;
    private BmobFile pictureZFile;
    private String descreption;
    private String publishMan;
    private String time;
    private String categary;

    public String getCategary() {
        return categary;
    }

    public void setCategary(String categary) {
        this.categary = categary;
    }

    public String getGoodName() {
        return goodName;
    }

    public void setGoodName(String goodName) {
        this.goodName = goodName;
    }

    public Bitmap getHeadPicture() {
        return headPicture;
    }

    public void setHeadPicture(Bitmap headPicture) {
        this.headPicture = headPicture;
    }

    public BmobFile getPictureZFile() {
        return pictureZFile;
    }

    public void setPictureZFile(BmobFile pictureZFile) {
        this.pictureZFile = pictureZFile;
    }

    public String getDescreption() {
        return descreption;
    }

    public void setDescreption(String descreption) {
        this.descreption = descreption;
    }


    public String getPublishMan() {
        return publishMan;
    }

    public void setPublishMan(String publishMan) {
        this.publishMan = publishMan;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public DemandObject(String goodName, Bitmap headPicture, String descreption, String time) {
        this.goodName = goodName;
        this.headPicture = headPicture;
        this.descreption = descreption;
        this.time = time;
    }

    public DemandObject(){

    }
}
