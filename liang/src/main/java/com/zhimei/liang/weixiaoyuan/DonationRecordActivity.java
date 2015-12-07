package com.zhimei.liang.weixiaoyuan;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.zhimei.liang.customview.CircleImageView;
import com.zhimei.liang.customview.RoundImageView;
import com.zhimei.liang.utitls.DonationRecord;
import com.zhimei.liang.utitls.FileHelper;
import com.zhimei.liang.utitls.MyApplication;
import com.zhimei.liang.utitls.SecondHandGoods;
import com.zhimei.liang.utitls.TradeRecord;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.CountListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.ThumbnailUrlListener;

public class DonationRecordActivity extends Activity {
    private ArrayList<DonationRecord> trList;
    private ArrayList<HashMap<String,Object>> mapArrayList;
    private TextView name,organization,time;
    private CircleImageView picture;
    private ListView lv;
    private int loopNum=0;//循环计数
    private int refreshTime=0;//刷新次数的记录
    private BmobQuery<DonationRecord> query ;
    private boolean isRefreshFlag=false;
    private  ArrayList<HashMap<String,Object>> al;//存储网络上查询到的资源
    private SwipeRefreshLayout swipeLayout;
    private final static int DATACHANGED=1;
    private  SimpleAdapter simpleAdapter;
    private ImageButton back,index;
    private ProgressDialog progressDialog;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch(msg.what){
                case DATACHANGED:
                    progressDialog.dismiss();
                    simpleAdapter.notifyDataSetChanged();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donation_record);
        initViews();
    }

    void initViews(){
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("正在加载数据，请稍后");
        progressDialog.setCancelable(true);
        progressDialog.show();
        judgeAndquery();
        back=(ImageButton)findViewById(R.id.donation_record_back);
        index=(ImageButton)findViewById(R.id.donation_record_index);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        index.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent=new Intent(DonationRecordActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });



        trList=new ArrayList<>();
        mapArrayList=new ArrayList<>();
        name=(TextView)findViewById(R.id.donation_good_name);
        organization=(TextView)findViewById(R.id.receive_organization);
        time=(TextView)findViewById(R.id.donation_good_time);
        picture=(CircleImageView)findViewById(R.id.donation_good_picture);
        lv=(ListView)findViewById(R.id.donation_record_listview);
        swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);

        swipeLayout.setColorSchemeResources(android.R.color.holo_blue_light,android.R.color.holo_green_dark,
                android.R.color.holo_red_light, android.R.color.primary_text_light,android.R.color.holo_orange_light, android.R.color.holo_green_light);
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
  /*              *//**
                 * j用于判断当前界面是第几次刷新
                 *//*

                swipeLayout.setRefreshing(true);
                if(refreshTime==0) {
                    mapArrayList.clear();
                    simpleAdapter.notifyDataSetChanged();
                    judgeAndquery();
                    refreshTime=refreshTime+1;
                }
                else{
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            swipeLayout.setRefreshing(false);
                            //进行数据更新
                        }
                    }, 2000);
                    Toast.makeText(DonationRecordActivity.this, "当前已经是最新数据", Toast.LENGTH_SHORT).show();
                }*/

                swipeLayout.setRefreshing(true);
                if(isRefreshFlag){
                    mapArrayList.clear();
                    simpleAdapter.notifyDataSetChanged();
                    isRefreshFlag=false;
                    judgeAndquery();

                }
                else{
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            swipeLayout.setRefreshing(false);
                            //进行数据更新
                        }
                    }, 1500);
                    Toast.makeText(DonationRecordActivity.this, "正在加载数据，请稍后", Toast.LENGTH_SHORT).show();

                }

            }
        });



         simpleAdapter=new SimpleAdapter(this,mapArrayList,R.layout.donation_record_item,
                new String[] { "picture", "name","organization","time" },
                new int[]{R.id.donation_good_picture,R.id.donation_good_name,R.id.receive_organization,R.id.donation_good_time
                       });


        lv.setAdapter(simpleAdapter);

        /**
         * 将Bitmap转化为ImageView在SimpleAdapter中进行显示，否则，无法进行正常显示。
         */
        simpleAdapter.setViewBinder(new SimpleAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Object data, String textRepresentation) {

                if(view instanceof CircleImageView && data instanceof Bitmap){
                    CircleImageView iv=(CircleImageView)view;
                    // iv.setImageBitmap((Bitmap) attentionList);
                    iv.setImageBitmap((Bitmap)data);
                    return true;
                }else{
                    return false;
                }
            }
        });

    }


    /**
     * 判断记录是否为空并且查询数据
     */

    void judgeAndquery(){
        query = new BmobQuery<DonationRecord>();
        al=new ArrayList<>();
        query.addWhereEqualTo("publishMan", MyApplication.getCurrentName());
        query.count(DonationRecordActivity.this, DonationRecord.class, new CountListener() {
            @Override
            public void onSuccess(int i) {
                if(i>0){
                    querydata();
                }
                else{
                    progressDialog.dismiss();
                    Toast.makeText(DonationRecordActivity.this, "你还没有捐赠物品，赶紧去捐赠物品吧☺", Toast.LENGTH_LONG).show();
                    swipeLayout.setRefreshing(false);
                }
            }

            @Override
            public void onFailure(int i, String s) {

            }
        });


    }

    void querydata(){

        query.findObjects(DonationRecordActivity.this, new FindListener<DonationRecord>() {
            @Override
            public void onSuccess(final List<DonationRecord> list) {
                for (DonationRecord donationRecord : list) {
                    final HashMap<String, Object> map = new HashMap<String, Object>();
                    map.put("name",donationRecord.getName());
                    map.put("organization",donationRecord.getReceiveOrganization());
                    map.put("time", dealTime(donationRecord.getCreatedAt()));


                    /**
                     * 获取缩略图地址
                     */
                    donationRecord.getPictureFile().getThumbnailUrl(DonationRecordActivity.this, 200, 275, 15, new ThumbnailUrlListener() {

                        @Override
                        public void onSuccess(String s) {
                            map.put("thumbnailUrl", s);
                            al.add(map);
                            loopNum++;
                            if (loopNum == list.size()) {
                                new LoadDoantions().execute();
                                loopNum=0;
                                Toast.makeText(DonationRecordActivity.this, "正在查询", Toast.LENGTH_SHORT).show();
                            }

                        }

                        @Override
                        public void onFailure(int i, String s) {
                            swipeLayout.setRefreshing(false);
                            Log.i("liang", s + "   失败");

                        }
                    });


                }


            }

            @Override
            public void onError(int i, String s) {
                Toast.makeText(DonationRecordActivity.this, s, Toast.LENGTH_LONG).show();
                Log.i("liang", s + "   失败");
                swipeLayout.setRefreshing(false);
            }
        });
    }

    /**
     * 时间格式处理
     * @param time
     * @return
     */
    String dealTime(String time){
        String tm=time.substring(0,10);
        return tm;
    }

    class LoadDoantions extends AsyncTask<Void,Integer,Boolean> {
        ProgressDialog progressDialog;
        private ArrayList<TradeRecord>  arrayList=new ArrayList<>();
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //  progressDialog=ProgressDialog.show(TradeRecordActivity.this, "", "正在加载资源中");
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            swipeLayout.setRefreshing(false);
            new Thread(new Runnable() {
                /**
                 * 求出item_list从哪个位置上开始是新加载的数据
                 */
                int count=mapArrayList.size()-al.size();

                @Override
                public void run() {
                    for(int i=0;i<al.size();i++,count++){
                        Bitmap bitmap=new FileHelper().getHttpBitmap(al.get(i).get("thumbnailUrl").toString());
                        mapArrayList.get(count).put("picture",bitmap);
                        Message message=new Message();
                        message.what=DATACHANGED;
                        handler.sendMessage(message);


                    }
                    isRefreshFlag=true;

                }
            }).start();

        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }



        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            for(HashMap map:al){
              /*  Log.i("liang", map.get("thumbnailUrl").toString() + "---" + map.get("name").toString() + "---" + map.get("time").toString()
                        + "---" + map.get("price").toString() + "---" + map.get("tradeway").toString());*/
                Bitmap bitmap= BitmapFactory.decodeResource(getResources(), R.mipmap.logo4);


                /**
                 * 每次得到数据，边将数据加载进simpleAdapter的数据院中
                 * 并用handler通知ListView数据更新
                 */

                HashMap<String,Object> maps=new HashMap<>();
                maps.put("picture",bitmap);
                maps.put("name", map.get("name").toString());
                maps.put("time",map.get("time").toString());
                maps.put("organization",map.get("organization").toString());
                mapArrayList.add(maps);
                Message message=new Message();
                message.what=DATACHANGED;
                handler.sendMessage(message);


            }
            return true;

        }
    }

}
