package com.zhimei.liang.weixiaoyuan;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bmob.pay.tool.BmobPay;
import com.bmob.pay.tool.PayListener;
import com.zhimei.liang.customview.RoundImageView;
import com.zhimei.liang.utitls.FileHelper;
import com.zhimei.liang.utitls.MyApplication;
import com.zhimei.liang.utitls.SecondHandGoods;
import com.zhimei.liang.utitls.ShoppingTable;
import com.zhimei.liang.utitls.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.DeleteListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.ThumbnailUrlListener;

public class TradeWindowActivity extends Activity {
    private RoundImageView publishManPicture;
    private TextView et_publishManName;
    private TextView et_goodName;
    private TextView et_goodDescription;
    private TextView et_goodPrice;
    private TextView et_time;
    private TextView et_address;
    private ImageButton index,back;
    private RelativeLayout duanxin,phone,chat,zhifubao;
    private HashMap<String,Object> hashMap;//得到上一个活动中的Acyivity
    private ImageView goodPicture;
    private String phoneNumber;
    private BmobQuery<User> query ;
    private ProgressDialog progressDialog;
    private String headPictureUrl;
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trade_window);
        initViews();
    }

    void initViews(){
        hashMap= MyApplication.getHashmap();
        et_address=(TextView)findViewById(R.id.tv_address);
        et_publishManName=(TextView)findViewById(R.id.publishman_name);
        et_goodName=(TextView)findViewById(R.id.goods_name);
        et_goodDescription=(TextView)findViewById(R.id.good_miaoshu);
        et_goodPrice=(TextView)findViewById(R.id.goods_price);
        et_time=(TextView)findViewById(R.id.publish_time);
        index=(ImageButton)findViewById(R.id.trade_window_succ_index);
        back=(ImageButton)findViewById(R.id.trade_window_succ_back);
        duanxin=(RelativeLayout)findViewById(R.id.all);
        phone=(RelativeLayout)findViewById(R.id.per_center);
        chat=(RelativeLayout)findViewById(R.id.trade_windowation);
        zhifubao=(RelativeLayout)findViewById(R.id.sec_market);
        goodPicture=(ImageView)findViewById(R.id.trade_goods_picture);
        publishManPicture=(RoundImageView)findViewById(R.id.publishman_touxiang);

        duanxin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                /**
                 * 短信内容
                 */
                final String message="你好，我对你发布的商品”"+hashMap.get("name").toString()
                        +"“比较满意，咱们能进一步联系吗？";
                AlertDialog.Builder dialog=new AlertDialog.Builder(TradeWindowActivity.this);
                dialog.setTitle("将发送短信至"+phoneNumber);
                dialog.setIcon(R.mipmap.test1);
                dialog.setMessage(message);
                dialog.setCancelable(false);
                dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SmsManager smsManager=SmsManager.getDefault();
                        smsManager.sendTextMessage(phoneNumber, null, message, null, null);
                    }
                });

                dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                dialog.show();



            }
        });

        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog=new AlertDialog.Builder(TradeWindowActivity.this);
                dialog.setTitle("将打电话至：");
                dialog.setMessage(phoneNumber);
                dialog.setIcon(R.mipmap.test1);
                dialog.setCancelable(false);
                dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumber));
                        startActivity(intent);
                    }
                });
                dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                dialog.show();



            }
        });

        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        /**
         * 支付宝支付，支付宝支付成功后，要删除数据
         */

        zhifubao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(TradeWindowActivity.this);
                dialog.setTitle("支付说明");
                dialog.setIcon(R.mipmap.zhifubao);
                dialog.setMessage("亲爱的用户，你将向本品台支付人民币" + hashMap.get("price") + "元，支付后，本平台将立刻派送" +
                        "配货人员，将你所需要的商品，送到你的住址。祝你购物愉快");
                dialog.setCancelable(false);

                dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        pay();
                    }
                });
                dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                dialog.show();
            }
        });

        et_goodDescription.setText(hashMap.get("description").toString());
        if(hashMap.get("publishMan")==null){
            et_publishManName.setText("自由的心");
        }
        else{
            et_publishManName.setText(hashMap.get("publishMan").toString());
        }

        et_goodName.setText(hashMap.get("name").toString());
        et_goodPrice.setText(hashMap.get("price").toString());
        et_time.setText(hashMap.get("time").toString());
        goodPicture.setImageBitmap((Bitmap) hashMap.get("bitmap"));
        /**
         * 查询用户信息
         */
        getPublishManInformation();
    }

    /**
     * 通过查询获取发布人的一些信息
     */
    void getPublishManInformation(){
        String publishMan;
        if(hashMap.get("publishMan").toString().equals("")){
            publishMan="11";
        }
        else{
            publishMan=hashMap.get("publishMan").toString();
        }

        query=new BmobQuery<User>();
        progressDialog=ProgressDialog.show(TradeWindowActivity.this, "", "正在连接服务器");
        query.addWhereEqualTo("username",publishMan);
        query.findObjects(TradeWindowActivity.this, new FindListener<User>() {
            @Override
            public void onSuccess(List<User> list) {
               // Log.i("liang",list.get(0).getMobilePhoneNumber());
                progressDialog.dismiss();
                et_address.setText(list.get(0).getAddress());
                phoneNumber=list.get(0).getMobilePhoneNumber();

               // list.get(0).getPicture().loadImage(TradeWindowActivity.this,goodPicture);
              //  headPictureUrl=list.get(0).getPicture().getFileUrl(TradeWindowActivity.this);
              //  new LoadPicture().execute();
                list.get(0).getPicture().getThumbnailUrl(TradeWindowActivity.this, 200, 275, 15, new ThumbnailUrlListener() {
                    @Override
                    public void onSuccess(String s) {
                        headPictureUrl = s;
                        /**
                         * 加载缩略图
                         */
                        new LoadPicture().execute();
                    }

                    @Override
                    public void onFailure(int i, String s) {
                        progressDialog.dismiss();

                    }
                });
            }

            @Override
            public void onError(int i, String s) {
                progressDialog.dismiss();

            }
        });


    }

    class LoadPicture extends AsyncTask<Void,Integer,Boolean>{
        private Bitmap bitmap;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            publishManPicture.setImageBitmap(bitmap);

        }

        @Override
        protected Boolean doInBackground(Void... params) {
             bitmap=new FileHelper().getHttpBitmap(headPictureUrl);
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }
    }

    /**
     * 支付结束后要从后台删除数据,删除之前，必须要通过查询获取商品id
     */

    void queryDataThenDelete(){
         BmobQuery<SecondHandGoods> query_decond=new BmobQuery<>() ;
        query_decond.addWhereEqualTo("name",hashMap.get("name").toString());
        query_decond.addWhereEqualTo("description",hashMap.get("description").toString());
        query_decond.addWhereEqualTo("publishMan",MyApplication.getCurrentName());
        query_decond.findObjects(TradeWindowActivity.this, new FindListener<SecondHandGoods>() {
            @Override
            public void onSuccess(List<SecondHandGoods> list) {
                /**
                 * 删除数据
                 * 将购买的信息表传至服务器
                 */
               // deleteData(list.get(0).getObjectId());
                loadShoppingTable((ArrayList) list);
            }

            @Override
            public void onError(int i, String s) {
                Log.i("liang", s);

            }
        });
    }

    /**
     * 根据查询获得的id删除数据
     * @param id
     */

    void deleteData(String id){
        SecondHandGoods secondHandGoods=new SecondHandGoods();
        secondHandGoods.setObjectId(id);
        secondHandGoods.delete(TradeWindowActivity.this, new DeleteListener() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onFailure(int i, String s) {

            }
        });
    }

    void pay(){
        new BmobPay(TradeWindowActivity.this).pay(0.02, hashMap.get("name").toString(), new PayListener() {
            @Override
            public void orderId(String s) {
                Log.i("liang", s);

            }

            @Override
            public void succeed() {
                queryDataThenDelete();
                Toast.makeText(TradeWindowActivity.this, "支付成功", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void fail(int i, String s) {
                Toast.makeText(TradeWindowActivity.this, s, Toast.LENGTH_SHORT).show();

            }

            @Override
            public void unknow() {

            }
        });

    }

    /**
     * 将购物信息上传至服务器
     * @param arrayList
     */
    void loadShoppingTable(ArrayList<SecondHandGoods> arrayList){
        ShoppingTable shoppingTable =new ShoppingTable();
        shoppingTable.setGoodName(arrayList.get(0).getName());
        shoppingTable.setBuyManName(MyApplication.getCurrentName());
        shoppingTable.setGoodDescription(arrayList.get(0).getDescription());
        shoppingTable.setTradeWay(arrayList.get(0).getTradeWay());
        shoppingTable.setGoodPictureFile(arrayList.get(0).getPictureFile());
        shoppingTable.setGoodID(arrayList.get(0).getObjectId());
        shoppingTable.setGoodPrice(arrayList.get(0).getPrice());
        shoppingTable.save(TradeWindowActivity.this, new SaveListener() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onFailure(int i, String s) {

            }
        });



    }

}
