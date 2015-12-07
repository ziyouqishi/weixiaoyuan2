package com.zhimei.liang.utitls;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import cn.bmob.v3.listener.ThumbnailUrlListener;

/**
 * Created by 张佳亮 on 2015/11/6.
 * 关于文件处理的类
 */
public class FileHelper {
    /**
     * 根据URL获取Bitmap
     * */

    public Bitmap getHttpBitmap(String url){
        Bitmap bitmap=null;
        URL myUrl;

        /**
         * 如果在try里面加载图片发生异常，则在catch里面继续进行加载
         */

        try {
            myUrl=new URL(url);
            HttpURLConnection conn=(HttpURLConnection)myUrl.openConnection();
            conn.setConnectTimeout(5000);
            conn.connect();
            InputStream is=conn.getInputStream();
            bitmap= BitmapFactory.decodeStream(is);
            //把bitmap转成圆形
            Thread.sleep(5000);
            is.close();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch(OutOfMemoryError error){
            try {
                myUrl=new URL(url);
                HttpURLConnection conn=(HttpURLConnection)myUrl.openConnection();
                conn.setConnectTimeout(5000);
                conn.connect();
                InputStream is=conn.getInputStream();
                bitmap= BitmapFactory.decodeStream(is);
                //把bitmap转成圆形
                Thread.sleep(5000);
                is.close();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            error.printStackTrace();

        }

        //返回圆形bitmap
        return bitmap;
    }

    /**
     * value表示每次所加的分数大小
     * @param context
     * @param value
     */

    public void storeUpScore(Context context,int value ){
        int oldScore=getScore(context);
        int newScore=oldScore+value;
        SharedPreferences.Editor editor=context.getSharedPreferences("weixiaoyuan", context.MODE_PRIVATE).edit();
        editor.putInt("score", newScore);
        editor.commit();
    }



    /**
     * 读取存储的分数值
     * @return
     */
    public int getScore(Context context){
        SharedPreferences pref=context.getSharedPreferences("weixiaoyuan", context.MODE_PRIVATE);
        int score=pref.getInt("score",0);
        return  score;
    }


    /**
     * 将图片资源转换为bitmap，此方法使用JNI进行转化，而不是java的createBitmap（）
     * 更加节省内存，避免OOM错误
     * @param context
     * @param resId
     * @return
     */
    public Bitmap readBitmap(Context context, int resId) {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inPreferredConfig = Bitmap.Config.RGB_565;
        opts.inPurgeable = true;
        opts.inInputShareable = true;
        InputStream is = context.getResources().openRawResource(resId);
        return BitmapFactory.decodeStream(is, null, opts);
    }

    /**
     * 回收图片资源
     * @param bitmap
     */
    public void recycle(Bitmap bitmap){
        if(bitmap != null && !bitmap.isRecycled()){
            // 回收并且置为null
            bitmap.recycle();
             bitmap = null;
        }
        System.gc();

    }


}
