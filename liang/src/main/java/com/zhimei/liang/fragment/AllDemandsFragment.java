package com.zhimei.liang.fragment;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.zhimei.liang.customview.CircleImageDrawable;
import com.zhimei.liang.utitls.DemandObject;
import com.zhimei.liang.utitls.FileHelper;
import com.zhimei.liang.utitls.MyApplication;
import com.zhimei.liang.utitls.TradeRecord;
import com.zhimei.liang.utitls.User;
import com.zhimei.liang.weixiaoyuan.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.CountListener;
import cn.bmob.v3.listener.FindListener;

public class AllDemandsFragment extends Fragment {
    private View view;
    private ListView listView;
    private SwipeRefreshLayout swipeLayout;
    private ArrayList<DemandObject> al_demandobject;
    private ArrayList<HashMap<String,Object>> arrayList_map;
    private SimpleAdapter simpleAdapter;
    private BmobQuery<DemandObject> query ;
    private final static int DATACHANGED=1;
    private ProgressDialog progressDialog;
    private boolean isRefreshFlag=false;
    private BmobQuery<User>  queryUser;
    private ArrayList<String>  listHeadPicture;//存储发布者的头像Url
    private  ArrayList<HashMap<String,Object>> al;//存储网络上查询到的资源
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


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_all_demand,container,false);
        initViews();
        initreso();
        return view;
    }

    void initViews(){
        progressDialog=new ProgressDialog(view.getContext());
        progressDialog.setMessage("正在加载数据，请稍后");
        progressDialog.setCancelable(false);
        progressDialog.show();
        judgeAndquery();


        listView=(ListView)view.findViewById(R.id.all_demand_listview);
        swipeLayout=(SwipeRefreshLayout)view.findViewById(R.id.all_demand_swipe_container);
        swipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.all_demand_swipe_container);

        swipeLayout.setColorSchemeResources(android.R.color.holo_blue_light,
                android.R.color.holo_red_light, android.R.color.holo_orange_light, android.R.color.holo_green_light);
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeLayout.setRefreshing(true);
                if(isRefreshFlag){
                    arrayList_map.clear();
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
                    Toast.makeText(view.getContext(), "正在加载数据，请稍后", Toast.LENGTH_SHORT).show();

                }

            }
        });



    }
    /**
     * 初始化资源
     */
    void initreso(){
        al_demandobject=new ArrayList<>();
        arrayList_map=new ArrayList<>();


        simpleAdapter = new SimpleAdapter(view.getContext(),
                arrayList_map,
                R.layout.demanditem,
                new String[] { "goodname", "picture","descreption","time" },
                new int[] { R.id.goods_name, R.id.demand_touxiang,R.id.goods_description
                        ,R.id.release_time });
        listView.setAdapter(simpleAdapter);


        simpleAdapter.setViewBinder(new SimpleAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Object data, String textRepresentation) {

                if (view instanceof ImageView && data instanceof Bitmap) {
                    ImageView iv = (ImageView) view;
                   // iv.setImageBitmap((Bitmap) data);
                    iv.setImageDrawable(new CircleImageDrawable((Bitmap)data));
                    return true;
                } else {
                    return false;
                }
            }
        });



    }


    /**
     * 判断记录是否为空并且查询数据
     */

    void judgeAndquery(){
        query = new BmobQuery<DemandObject>();
        al=new ArrayList<>();
        query.count(view.getContext(), DemandObject.class, new CountListener() {
            @Override
            public void onSuccess(int i) {
                if (i > 0) {
                    querydata();
                } else {
                    Toast.makeText(view.getContext(), "你还没有捐赠物品，赶紧去捐赠物品吧☺", Toast.LENGTH_LONG).show();
                    swipeLayout.setRefreshing(false);
                }
            }

            @Override
            public void onFailure(int i, String s) {

            }
        });
    }

    /**
     * 后期需要从服务器取得头像
     */

    void querydata(){
        query.findObjects(view.getContext(), new FindListener<DemandObject>() {
            @Override
            public void onSuccess(List<DemandObject> list) {
                for (DemandObject demandObject : list) {
                    HashMap<String, Object> map = new HashMap<String, Object>();
                    map.put("goodname", demandObject.getGoodName());
                    map.put("publishMan", demandObject.getPublishMan());
                    map.put("descreption", demandObject.getDescreption());
                    map.put("time", dealTime(demandObject.getCreatedAt()));
                    al.add(map);

                }

                new LoadDemands().execute();


            }

            @Override
            public void onError(int i, String s) {

            }
        });
    }

    /**
     * 得到所有信息发布人的头像url
     * @return
     */

    void getPublishmanPictureUrl(){
        listHeadPicture=new ArrayList<>();
        queryUser=new BmobQuery<>();
        for(HashMap hashMap:al){
            queryUser.addWhereEqualTo("username", hashMap.get("publishMan"));
            queryUser.findObjects(view.getContext(), new FindListener<User>() {
                @Override
                public void onSuccess(List<User> list) {
                    String url = list.get(0).getPicture().getFileUrl(view.getContext());
                    listHeadPicture.add(url);
                    Log.i("liang",url+"张佳亮");

                }

                @Override
                public void onError(int i, String s) {

                }
            });



        }

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


    class LoadDemands extends AsyncTask<Void,Integer,Boolean> {
        ProgressDialog progressDialog;
        private ArrayList<TradeRecord>  arrayList=new ArrayList<>();
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            swipeLayout.setRefreshing(false);
            isRefreshFlag=true;
           /* for(int i=0;i<listHeadPicture.size();i++){
                Log.i("liang",listHeadPicture.get(i));

            }*/

/*            new Thread(new Runnable() {
                *//**
                 * 求出item_list从哪个位置上开始是新加载的数据
                 *//*
                int count=arrayList_map.size()-al.size();

                @Override
                public void run() {
                    for(int i=0;i<al.size();i++,count++){
                        Bitmap bitmap=new FileHelper().getHttpBitmap(al.get(i).get("url").toString());
                        arrayList_map.get(count).put("picture",bitmap);
                        Message message=new Message();
                        message.what=DATACHANGED;
                        handler.sendMessage(message);


                    }

                }
            }).start();*/

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

                /**
                 * 后面要换成真正的头像
                 */
                Bitmap bitmap= BitmapFactory.decodeResource(getResources(), R.mipmap.logo4);

                /**
                 * 每次得到数据，边将数据加载进simpleAdapter的数据院中
                 * 并用handler通知ListView数据更新
                 */

                HashMap<String,Object> maps=new HashMap<>();
                maps.put("picture",bitmap);
                maps.put("goodname", map.get("goodname").toString());
                maps.put("time",map.get("time").toString());
                maps.put("descreption",map.get("descreption").toString());
                arrayList_map.add(maps);
                Message message=new Message();
                message.what=DATACHANGED;
                handler.sendMessage(message);


            }

            getPublishmanPictureUrl();
            return true;

        }
    }



}
