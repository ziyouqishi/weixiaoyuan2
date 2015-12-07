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
import com.zhimei.liang.utitls.DonationRecord;
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

public class IndividulDemandFragment extends Fragment {
    private View view;
    private ListView listView;
    private SwipeRefreshLayout swipeLayout;
    private ArrayList<DemandObject> al_demandobject;
    private ArrayList<HashMap<String,Object>> arrayList_map;
    private SimpleAdapter simpleAdapter;
    private BmobQuery<DemandObject> query ;
    private BmobQuery<User>  queryUser;
    private boolean isRefreshFlag=false;
    private String  headPictureUrl;
    private final static int DATACHANGED=1;
    private ProgressDialog progressDialog;
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
        view=inflater.inflate(R.layout.fragment_individul_demand,container,false);
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

        listView=(ListView)view.findViewById(R.id.demand_listview);
        swipeLayout=(SwipeRefreshLayout)view.findViewById(R.id.swipe_container);
        swipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);

        swipeLayout.setColorSchemeResources(android.R.color.holo_blue_light,
                android.R.color.holo_red_light, android.R.color.holo_orange_light, android.R.color.holo_green_light);
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
/*                *//**
                 * j用于判断当前界面是第几次刷新
                 *//*
                Log.i("liang", refreshTime + "");
                swipeLayout.setRefreshing(true);
                if (refreshTime == 0) {
                    arrayList_map.clear();
                    simpleAdapter.notifyDataSetChanged();
                    judgeAndquery();

                } else {
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            swipeLayout.setRefreshing(false);
                            //进行数据更新
                        }
                    }, 2000);
                    Toast.makeText(view.getContext(), "当前已经是最新数据", Toast.LENGTH_SHORT).show();
                }
                refreshTime = refreshTime + 1;*/

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
                new int[] { R.id.goods_name, R.id.demand_touxiang,R.id.goods_description,
                       R.id.release_time });
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
        query.addWhereEqualTo("publishMan", MyApplication.getCurrentName());
        query.count(view.getContext(), DemandObject.class, new CountListener() {
            @Override
            public void onSuccess(int i) {
                if (i > 0) {
                    querydata();
                } else {
                    progressDialog.dismiss();
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
     * 根据用户名查询用户的其他信息
     */

    void queryUserInfomation(){
        queryUser=new BmobQuery<>();
        query.addWhereEqualTo("username", MyApplication.getCurrentName());
        queryUser.findObjects(view.getContext(), new FindListener<User>() {
            @Override
            public void onSuccess(List<User> list) {
                headPictureUrl=list.get(0).getPicture().getFileUrl(view.getContext());

                new LoadUserTask().execute();

            }

            @Override
            public void onError(int i, String s) {

            }
        });

    }

    void querydata(){
        query.findObjects(view.getContext(), new FindListener<DemandObject>() {
            @Override
            public void onSuccess(List<DemandObject> list) {
                for (DemandObject demandObject : list) {
                    HashMap<String, Object> map = new HashMap<String, Object>();
                    map.put("goodname", demandObject.getGoodName());
                    // map.put("picture",demandObject.getHeadPicture());
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
     * 时间格式处理
     * @param time
     * @return
     */
    String dealTime(String time){
        String tm=time.substring(0,10);
        return tm;
    }


    class LoadDemands extends AsyncTask<Void,Integer,Boolean> {
       private ProgressDialog progressDialog;
        private ArrayList<TradeRecord>  arrayList=new ArrayList<>();
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            swipeLayout.setRefreshing(false);
            queryUserInfomation();




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
               // Bitmap bitmap=new FileHelper().getHttpBitmap(map.get("thumbnailUrl").toString());

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

            return true;

        }
    }

    class LoadUserTask extends AsyncTask<Void,Integer,Boolean>{
        private Bitmap bitmap;
        @Override
        protected Boolean doInBackground(Void... params) {
            bitmap=new FileHelper().getHttpBitmap(headPictureUrl);
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    for(int i=0;i<arrayList_map.size();i++){
                        arrayList_map.get(i).put("picture",bitmap);
                        Message message=new Message();
                        message.what=DATACHANGED;
                        handler.sendMessage(message);
                    }
                    isRefreshFlag=true;
                }
            }).start();

        }
    }
}
