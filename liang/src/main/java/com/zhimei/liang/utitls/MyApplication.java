package com.zhimei.liang.utitls;

import android.app.Application;
import android.content.Context;

import cn.bmob.v3.Bmob;

/**
 * Created by 张佳亮 on 2015/10/7.
 */
public class MyApplication extends Application {

    private static Context context;
    public static String APPID = "a91ea9411de0d54e0eaf0311d2a9fec3";
    private static boolean signUPSuccess=false;//判断用户是否登录成功
    private static String currentName;
    private static String testUrl;

    public static String getTestUrl() {
        return testUrl;
    }

    public static void setTestUrl(String testUrl) {
        MyApplication.testUrl = testUrl;
    }

    public static boolean isSignUPSuccess() {
        return signUPSuccess;
    }

    public static void setSignUPSuccess(boolean signUPSuccess) {
        MyApplication.signUPSuccess = signUPSuccess;
    }

    public static String getCurrentName() {
        return currentName;
    }

    public static void setCurrentName(String currentName) {
        MyApplication.currentName = currentName;
    }

    @Override
    public void onCreate(){
        context=getApplicationContext();
        Bmob.initialize(context, APPID);
    }
    public static Context getContext(){
        return context;
    }
}
