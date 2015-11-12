package com.zhimei.liang.weixiaoyuan;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.zhimei.liang.customview.RoundImageView;
import com.zhimei.liang.nineoldandroids.animation.ObjectAnimator;
import com.zhimei.liang.utitls.FileHelper;
import com.zhimei.liang.utitls.MyApplication;
import com.zhimei.liang.utitls.SecondHandGoods;
import com.zhimei.liang.utitls.TradeRecord;
import com.zhimei.liang.weixiaoyuan.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.CountListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.ThumbnailUrlListener;

public class TradeRecordActivity extends Activity {
    private ArrayList<TradeRecord> trList;
    private ImageButton back,index;
    private ArrayList<HashMap<String,Object>> mapArrayList;
    private TextView name,price,time;
    private RoundImageView picture;
    private ListView lv;
    private BmobQuery<SecondHandGoods> query ;
    private  SimpleAdapter simpleAdapter;
    private SwipeRefreshLayout swipeLayout;
    private int loopNum=0;//循环计数
    private int refreshTime=0;//刷新次数的记录
    private  ArrayList<HashMap<String,Object>> al;//存储网络上查询到的资源
    private final static int DATACHANGED=1;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch(msg.what){
                case DATACHANGED:
                    simpleAdapter.notifyDataSetChanged();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trade_record);
        initViews();

    }
    void initViews(){
        trList=new ArrayList<TradeRecord>();
        mapArrayList=new ArrayList<>();
        name=(TextView)findViewById(R.id.trade_good_name);
        price=(TextView)findViewById(R.id.trade_good_price);
        time=(TextView)findViewById(R.id.trade_good_time);
        picture=(RoundImageView)findViewById(R.id.trade_good_picture);
        lv=(ListView)findViewById(R.id.record_listview);
        back=(ImageButton)findViewById(R.id.record_back);
        index=(ImageButton)findViewById(R.id.record_index);
        swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);

        swipeLayout.setColorSchemeResources(android.R.color.holo_blue_light,android.R.color.holo_green_dark,
                android.R.color.holo_red_light, android.R.color.primary_text_light,android.R.color.holo_orange_light, android.R.color.holo_green_light);
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                /**
                 * j用于判断当前界面是第几次刷新
                 */
                Log.i("liang", refreshTime + "");
                swipeLayout.setRefreshing(true);
               if(refreshTime==0) {
                   query();

                }
                else{
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            swipeLayout.setRefreshing(false);
                            //进行数据更新
                        }
                    }, 2000);
                    Toast.makeText(TradeRecordActivity.this, "当前已经是最新数据", Toast.LENGTH_SHORT).show();
                }
                refreshTime=refreshTime+1;

            }
        });


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
                Intent intent=new Intent(TradeRecordActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });


        /**
         * 根据图片资源获取Bitmap对象
         */
        Drawable drawable=this.getResources().getDrawable(R.mipmap.test1);
        BitmapDrawable bitmapDrawable=(BitmapDrawable)drawable;
        Bitmap bitmap= bitmapDrawable.getBitmap();

        for(int i=0;i<12;i++){
            TradeRecord tr=new TradeRecord("海贼王 航海王","2015-02-15",bitmap,"¥ 45.3","买进");
            trList.add(tr);
        }

        for(int j=0;j<trList.size();j++){
            HashMap<String,Object> map=new HashMap<>();
            map.put("picture",trList.get(j).getPicture());
            map.put("name",trList.get(j).getGood_name());
            map.put("price",trList.get(j).getPrice());
            map.put("time",trList.get(j).getTime());
            map.put("ways",trList.get(j).getWays());
           mapArrayList.add(map);
        }

         simpleAdapter=new SimpleAdapter(this,mapArrayList,R.layout.trade_record_item,
                new String[] { "picture", "name","price","time","ways" },
                new int[]{R.id.trade_good_picture,R.id.trade_good_name,R.id.trade_good_price,R.id.trade_good_time,
                R.id.trade_good_way});

        lv.setAdapter(simpleAdapter);


        /**
         * 将Drawable转化为ImageView在SimpleAdapter中进行显示，否则，无法进行正常显示。
         */
        simpleAdapter.setViewBinder(new SimpleAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Object data, String textRepresentation) {

                if (view instanceof RoundImageView && data instanceof Bitmap) {
                    RoundImageView iv = (RoundImageView) view;
                    // iv.setImageBitmap((Bitmap) attentionList);
                    iv.setImageBitmap((Bitmap) data);
                    return true;
                } else {
                    return false;
                }
            }
        });





    }

    /**
     * 查询数据
     */

    void query(){
        query = new BmobQuery<SecondHandGoods>();
        al=new ArrayList<>();
        Log.i("liang","正在查询");
        query.addWhereEqualTo("publishMan",MyApplication.getCurrentName());
        /**
         * 在查询之前，必须要判断符合该条件的记录是否存在，如不存在，则不执行查询。
         */
        query.count(TradeRecordActivity.this,SecondHandGoods.class,new CountListener(){
            @Override
            public void onSuccess(int i) {
                Log.i("liang",i+"");
                if(i>0){

                    /**
                     * 存在的记录数大于0，执行查询
                     */

                    query.findObjects(TradeRecordActivity.this, new FindListener<SecondHandGoods>() {


                        @Override
                        public void onSuccess(final List<SecondHandGoods> list) {

                            for (SecondHandGoods secondHandGoods : list) {
                                final HashMap<String, Object> map = new HashMap<String, Object>();
                                map.put("name", secondHandGoods.getName());
                                map.put("price", secondHandGoods.getPrice());
                                // dealTime(secondHandGoods.getCreatedAt())
                                map.put("time", dealTime(secondHandGoods.getCreatedAt()));
                                map.put("tradeway", secondHandGoods.getTradeWay());
                                map.put("url", secondHandGoods.getPictureFile().getFileUrl(TradeRecordActivity.this));


                                /**
                                 * 获取缩略图地址
                                 */
                                secondHandGoods.getPictureFile().getThumbnailUrl(TradeRecordActivity.this, 200, 275, 15, new ThumbnailUrlListener() {

                                    @Override
                                    public void onSuccess(String s) {
                                        map.put("thumbnailUrl", s);
                                        al.add(map);
                                        loopNum++;
                                        if (loopNum == list.size()) {
                                            new Loadgoods().execute();
                                            loopNum=0;
                                            Toast.makeText(TradeRecordActivity.this, "正在查询", Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(TradeRecordActivity.this, s, Toast.LENGTH_LONG).show();
                            Log.i("liang", s + "   失败");
                            swipeLayout.setRefreshing(false);
                        }
                    });


                }
                else{
                    Toast.makeText(TradeRecordActivity.this, "你还没有发布商品，赶紧去发布商品吧☺", Toast.LENGTH_LONG).show();
                    swipeLayout.setRefreshing(false);
                }

            }

            @Override
            public void onFailure(int i, String s) {

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


    class Loadgoods extends AsyncTask<Void,Integer,Boolean> {
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
                Bitmap bitmap=new FileHelper().getHttpBitmap(map.get("thumbnailUrl").toString());


                /**
                 * 每次得到数据，边将数据加载进simpleAdapter的数据院中
                 * 并用handler通知ListView数据更新
                 */

                HashMap<String,Object> maps=new HashMap<>();
                maps.put("picture",bitmap);
                maps.put("name", map.get("name").toString());
                maps.put("price","¥ "+map.get("price").toString());
                maps.put("time",map.get("time").toString());
                maps.put("ways",map.get("tradeway").toString());
                mapArrayList.add(maps);
                Message message=new Message();
                message.what=DATACHANGED;
                handler.sendMessage(message);


            }
            return true;

        }
    }


}
