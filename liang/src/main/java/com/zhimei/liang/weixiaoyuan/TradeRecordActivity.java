package com.zhimei.liang.weixiaoyuan;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import com.zhimei.liang.customview.CircleImageDrawable;
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
    private ImageView picture;
    private ListView lv;
    private BmobQuery<SecondHandGoods> query ;
    private  SimpleAdapter simpleAdapter;
    private SwipeRefreshLayout swipeLayout;
    private ProgressDialog progressDialog;
    private boolean isRefreshFlag=false;
    private  ArrayList<HashMap<String,Object>> al;//存储网络上查询到的资源
    private final static int DATACHANGED=1;
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
        setContentView(R.layout.activity_trade_record);
        initViews();

    }
    void initViews(){
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("正在加载数据，请稍后");
        progressDialog.setCancelable(true);
        progressDialog.show();
        query();

        trList=new ArrayList<TradeRecord>();
        mapArrayList=new ArrayList<>();
        name=(TextView)findViewById(R.id.trade_good_name);
        price=(TextView)findViewById(R.id.trade_good_price);
        time=(TextView)findViewById(R.id.trade_good_time);
        picture=(ImageView)findViewById(R.id.trade_good_picture);
        lv=(ListView)findViewById(R.id.record_listview);
        back=(ImageButton)findViewById(R.id.record_back);
        index=(ImageButton)findViewById(R.id.record_index);
        swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);

        swipeLayout.setColorSchemeResources(android.R.color.holo_blue_light,android.R.color.holo_green_dark,
                android.R.color.holo_red_light, android.R.color.primary_text_light,android.R.color.holo_orange_light, android.R.color.holo_green_light);
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeLayout.setRefreshing(true);
                if(isRefreshFlag){
                    mapArrayList.clear();
                    simpleAdapter.notifyDataSetChanged();
                    isRefreshFlag=false;
                    query();

                }
                else{
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            swipeLayout.setRefreshing(false);
                            //进行数据更新
                        }
                    }, 1500);
                    Toast.makeText(TradeRecordActivity.this, "正在加载数据，请稍后", Toast.LENGTH_SHORT).show();

                }
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

                if (view instanceof ImageView && data instanceof Bitmap) {
                    ImageView iv = (ImageView) view;

                    iv.setImageDrawable(new CircleImageDrawable((Bitmap)data));
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
                                al.add(map);
                            }
                            new Loadgoods().execute();

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
                    progressDialog.dismiss();
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
            new Thread(new Runnable() {
                /**
                 * 求出item_list从哪个位置上开始是新加载的数据
                 */
                int count=mapArrayList.size()-al.size();

                @Override
                public void run() {
                    for(int i=0;i<al.size();i++,count++){
                        Bitmap bitmap=new FileHelper().getHttpBitmap(al.get(i).get("url").toString());
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
                maps.put("price","¥ "+map.get("price").toString());
                maps.put("time",map.get("time").toString());
                maps.put("ways", map.get("tradeway").toString());
                mapArrayList.add(maps);
                Message message=new Message();
                message.what=DATACHANGED;
                handler.sendMessage(message);

            }
            return true;

        }
    }


}
