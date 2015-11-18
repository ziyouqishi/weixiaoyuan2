package com.zhimei.liang.weixiaoyuan;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

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

public class QueryGoodsActivity extends Activity {
    private SwipeRefreshLayout swipeLayout;
    private GridView gridview;
    private SimpleAdapter goods_adapter;
    private ArrayList<HashMap<String,Object>> mapArrayList;
    private  ArrayList<HashMap<String,Object>> al;//存储从网络中获取的资源
    private final static int DATACHANGED=1;
    private int refreshTime=0;//刷新次数的记录
    private String queryData;
    private String way;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch(msg.what){
                case DATACHANGED:
                    goods_adapter.notifyDataSetChanged();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query_goods);
        initViews();
    }

    void initViews(){
        swipeLayout=(SwipeRefreshLayout)findViewById(R.id.query_swipe_container);
        gridview=(GridView)findViewById(R.id.query_gridview);
        Intent intent=getIntent();
        queryData=intent.getStringExtra("queryData");
        way=intent.getStringExtra("way");
        mapArrayList=new ArrayList<>();
        al=new ArrayList<>();

        goods_adapter = new SimpleAdapter(QueryGoodsActivity.this,
                mapArrayList,
                R.layout.item,
                new String[] { "bitmap", "price","time","name","description" },
                new int[] { R.id.goods_image, R.id.goods_price,R.id.goods_time,R.id.goods_name,R.id.goods_attention });
       gridview.setAdapter(goods_adapter);

        /**
         * 将Bitmap转化为ImageView在SimpleAdapter中进行显示，否则，无法进行正常显示。
         */
        goods_adapter.setViewBinder(new SimpleAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Object data, String textRepresentation) {

                if (view instanceof ImageView && data instanceof Bitmap) {
                    ImageView iv = (ImageView) view;
                    iv.setImageBitmap((Bitmap) data);
                    return true;
                } else {
                    return false;
                }
            }
        });
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MyApplication.setHashmap(mapArrayList.get(position));
                Intent intent = new Intent(QueryGoodsActivity.this, TradeWindowActivity.class);
                startActivity(intent);
            }
        });

        swipeLayout.setColorSchemeResources(
                android.R.color.holo_blue_light,
                android.R.color.holo_red_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_green_light);
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeLayout.setRefreshing(true);
                if (refreshTime == 0) {
                    queryData(queryData);

                } else {
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            swipeLayout.setRefreshing(false);
                            //进行数据更新
                        }
                    }, 2000);
                    Toast.makeText(QueryGoodsActivity.this, "当前已经是最新数据", Toast.LENGTH_SHORT).show();
                }
                refreshTime = refreshTime + 1;

            }
        });



        if(queryData!=null){
           queryData(queryData);
       }

    }

    /**
     * 根据查询条件，判断记录个数，如果记录个数大于1，则进行查询和显示
     * 查询采用模糊查询
     * @param goodInformation
     */
    void queryData(String goodInformation){
        final BmobQuery<SecondHandGoods> bmobQuery=new BmobQuery<>();
        if(way.equals("name")){
            bmobQuery.addWhereContains("name", goodInformation);
        }else if(way.equals("category")){
            bmobQuery.addWhereContains("category", goodInformation);

        }

        bmobQuery.count(QueryGoodsActivity.this, SecondHandGoods.class, new CountListener() {
            @Override
            public void onSuccess(int i) {
                /**
                 *
                 * 以免重复查询，每次查询之前，将旧数据清空
                 */
                al.clear();
                mapArrayList.clear();
                goods_adapter.notifyDataSetChanged();
                if (i > 0) {
                    bmobQuery.findObjects(QueryGoodsActivity.this, new FindListener<SecondHandGoods>() {
                        @Override
                        public void onSuccess(List<SecondHandGoods> list) {
                            for (SecondHandGoods secondHandGoods : list) {
                                HashMap<String, Object> map = new HashMap<String, Object>();
                                map.put("name", secondHandGoods.getName());
                                map.put("price", secondHandGoods.getPrice());
                                map.put("time", dealTime(secondHandGoods.getCreatedAt()));
                                map.put("tradeway", secondHandGoods.getTradeWay());
                                map.put("description", secondHandGoods.getDescription());
                                map.put("publishMan", secondHandGoods.getPublishMan());
                                map.put("url", secondHandGoods.getPictureFile().getFileUrl(QueryGoodsActivity.this));
                                al.add(map);
                            }
                            swipeLayout.setRefreshing(false);
                            if (swipeLayout.isRefreshing()) {
                                Toast.makeText(QueryGoodsActivity.this, "刷新有点频繁，先休息一会吧", Toast.LENGTH_SHORT).show();
                            } else {
                                new Loadgoods().execute();

                            }


                        }

                        @Override
                        public void onError(int i, String s) {

                        }
                    });

                } else {
                    Toast.makeText(QueryGoodsActivity.this, "抱歉，你查询的数据不存在", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(int i, String s) {

            }
        });

    }

    /**
     * 时间处理函数
     * @param time
     * @return
     */
    String dealTime(String time){
        String tm=time.substring(0,10);
        return tm;
    }

    class Loadgoods extends AsyncTask<Void,Integer,Boolean> {
        private ArrayList<TradeRecord>  arrayList=new ArrayList<>();
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            swipeLayout.setRefreshing(false);
            if(al.size()>0&&mapArrayList.size()>0){
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        for(int i=0;i<al.size();i++){
                            Bitmap bitmap=new FileHelper().getHttpBitmap(al.get(i).get("url").toString());
                            mapArrayList.get(i).put("bitmap",bitmap);
                            Log.i("liang",mapArrayList.size()+" haha");
                            Message message=new Message();
                            message.what=DATACHANGED;
                            handler.sendMessage(message);
                        }
                    }
                }).start();

            }

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
                Bitmap bitmap= BitmapFactory.decodeResource(getResources(), R.mipmap.test1);
                /**4
                 * 每次得到数据，边将数据加载进simpleAdapter的数据院中
                 * 并用handler通知ListView数据更新
                 */

                HashMap<String,Object> maps=new HashMap<>();
                maps.put("bitmap",bitmap);
                maps.put("name", map.get("name").toString());
                maps.put("price","¥"+map.get("price").toString());
                maps.put("time",map.get("time").toString());
                maps.put("description",map.get("description").toString());
                maps.put("publishMan",map.get("publishMan"));
                mapArrayList.add(maps);
                Message message=new Message();
                message.what=DATACHANGED;
                handler.sendMessage(message);


            }
            return true;

        }
    }

}
