package com.zhimei.liang.utitls;

import android.graphics.Bitmap;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * 购物清单
 * Created by 张佳亮 on 2015/11/17.
 */
public class ShoppingTable extends BmobObject {
    private String goodName;
    private String goodPrice;
    private String buyManName;
    private String goodID;
    private String goodDescription;
    private Bitmap goodPicture;
    private BmobFile goodPictureFile;
    private String tradeWay;

    public String getGoodName() {
        return goodName;
    }

    public void setGoodName(String goodName) {
        this.goodName = goodName;
    }

    public String getGoodPrice() {
        return goodPrice;
    }

    public void setGoodPrice(String goodPrice) {
        this.goodPrice = goodPrice;
    }

    public String getBuyManName() {
        return buyManName;
    }

    public void setBuyManName(String buyManName) {
        this.buyManName = buyManName;
    }

    public String getGoodID() {
        return goodID;
    }

    public void setGoodID(String goodID) {
        this.goodID = goodID;
    }

    public String getGoodDescription() {
        return goodDescription;
    }

    public void setGoodDescription(String goodDescription) {
        this.goodDescription = goodDescription;
    }

    public Bitmap getGoodPicture() {
        return goodPicture;
    }

    public void setGoodPicture(Bitmap goodPicture) {
        this.goodPicture = goodPicture;
    }

    public BmobFile getGoodPictureFile() {
        return goodPictureFile;
    }

    public void setGoodPictureFile(BmobFile goodPictureFile) {
        this.goodPictureFile = goodPictureFile;
    }

    public String getTradeWay() {
        return tradeWay;
    }

    public void setTradeWay(String tradeWay) {
        this.tradeWay = tradeWay;
    }

    public ShoppingTable(String goodName, String goodPrice, String buyManName, String goodID,
                         String goodDescription, Bitmap goodPicture, String tradeWay) {
        this.goodName = goodName;
        this.goodPrice = goodPrice;
        this.buyManName = buyManName;
        this.goodID = goodID;
        this.goodDescription = goodDescription;
        this.goodPicture = goodPicture;
        this.tradeWay = tradeWay;
    }

    public ShoppingTable(){

    }
}
