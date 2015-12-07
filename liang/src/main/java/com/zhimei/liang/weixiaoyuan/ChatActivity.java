package com.zhimei.liang.weixiaoyuan;

import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.zhimei.liang.customview.CircleImageDrawable;
import com.zhimei.liang.customview.chatview.EmoticonsEditText;
import com.zhimei.liang.customview.chatview.XListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatActivity extends Activity{
    ArrayList<HashMap<String,Object>> chatList=null;
    String[] from={"image","text"};
    int[] to={R.id.chatlist_text_me,R.id.send_time,
            R.id.chatlist_text_other,R.id.get_time,R.id.chatlist_image_me,R.id.chatlist_image_other};
    int[] layout={R.layout.item_chat_right,R.layout.item_chat_left};
    String userQQ=null;
    /**
     * 发送的消息类型
     */
    public final static int OTHER=1;
    public final static int ME=0;
    private MyChatAdapter chatAdapter=null;
    private  Drawable myHead;
    private Drawable otherHead;






    private Button btn_chat_emo, btn_chat_send, btn_chat_add,btn_chat_keyboard, btn_speak, btn_chat_voice;

    private ListView chatListView;
    private GridView gridViewEmo, gridViewEmo2;

    private EditText edit_user_comment;

    String targetId = "";

    private static int MsgPagerNum;

    private LinearLayout layout_more, layout_emo, layout_add;

    private ViewPager pager_emo;

    private TextView tv_picture, tv_camera, tv_location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        initView();
        chatTest();
        loadEmo();
    }

/*    private void initView() {
        mListView = (XListView) findViewById(R.id.mListView);
        initBottomView();
    }

    private void initBottomView() {
        // 最左边
        btn_chat_add = (Button) findViewById(R.id.btn_chat_add);
        btn_chat_emo = (Button) findViewById(R.id.btn_chat_emo);
        btn_chat_add.setOnClickListener(this);
        btn_chat_emo.setOnClickListener(this);
        // 最右边
        btn_chat_keyboard = (Button) findViewById(R.id.btn_chat_keyboard);
        btn_chat_voice = (Button) findViewById(R.id.btn_chat_voice);
        btn_chat_voice.setOnClickListener(this);
        btn_chat_keyboard.setOnClickListener(this);
        btn_chat_send = (Button) findViewById(R.id.btn_chat_send);
        btn_chat_send.setOnClickListener(this);
        // 最下面
        layout_more = (LinearLayout) findViewById(R.id.layout_more);
        layout_emo = (LinearLayout) findViewById(R.id.layout_emo);
        layout_add = (LinearLayout) findViewById(R.id.layout_add);
        initAddView();

        // 最中间
        // 语音框
        btn_speak = (Button) findViewById(R.id.btn_speak);
        // 输入框
        edit_user_comment = (EmoticonsEditText) findViewById(R.id.edit_user_comment);
        edit_user_comment.setOnClickListener(this);
        edit_user_comment.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                // TODO Auto-generated method stub
                if (!TextUtils.isEmpty(s)) {
                    btn_chat_send.setVisibility(View.VISIBLE);
                    btn_chat_keyboard.setVisibility(View.GONE);
                    btn_chat_voice.setVisibility(View.GONE);
                } else {
                    if (btn_chat_voice.getVisibility() != View.VISIBLE) {
                        btn_chat_voice.setVisibility(View.VISIBLE);
                        btn_chat_send.setVisibility(View.GONE);
                        btn_chat_keyboard.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }
        });

    }
    private void initAddView() {
        tv_picture = (TextView) findViewById(R.id.tv_picture);
        tv_camera = (TextView) findViewById(R.id.tv_camera);
        tv_location = (TextView) findViewById(R.id.tv_location);
        tv_picture.setOnClickListener(this);
        tv_location.setOnClickListener(this);
        tv_camera.setOnClickListener(this);
    }*/

    void initView(){
        chatList=new ArrayList<HashMap<String,Object>>();
        chatListView = (ListView) findViewById(R.id.mListView);
        edit_user_comment = (EditText) findViewById(R.id.edit_user_comment);
        btn_chat_send = (Button) findViewById(R.id.btn_chat_send);
        btn_chat_keyboard = (Button) findViewById(R.id.btn_chat_keyboard);
        btn_chat_voice = (Button) findViewById(R.id.btn_chat_voice);
        pager_emo=(ViewPager)findViewById(R.id.pager_emo);
        btn_chat_add = (Button) findViewById(R.id.btn_chat_add);
        btn_chat_emo = (Button) findViewById(R.id.btn_chat_emo);
        btn_chat_send = (Button) findViewById(R.id.btn_chat_send);
         myHead=new CircleImageDrawable(
                BitmapFactory.decodeResource(getResources(), R.drawable.goods));
        otherHead=new CircleImageDrawable(
                BitmapFactory.decodeResource(getResources(), R.mipmap.test1));

        /**
         * layout_more包含两个小布局，一个是表情的布局，另一是拍照等的布局。
         */
        layout_more=(LinearLayout)findViewById(R.id.layout_more);
        layout_emo = (LinearLayout) findViewById(R.id.layout_emo);
        layout_add = (LinearLayout) findViewById(R.id.layout_add);
        chatAdapter=new MyChatAdapter(this,chatList,layout,from,to);
        chatListView.setAdapter(chatAdapter);
        btn_chat_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (layout_more.getVisibility() == View.GONE) {
                    layout_more.setVisibility(View.VISIBLE);
                    layout_emo.setVisibility(View.GONE);//必须设为GONE，否则表情的布局可能会出现。
                    layout_add.setVisibility(View.VISIBLE);
                    hideSoftInputView();

                } else {
                    if (layout_emo.getVisibility() == View.VISIBLE) {
                        layout_emo.setVisibility(View.GONE);
                        layout_add.setVisibility(View.VISIBLE);
                    } else {
                        layout_more.setVisibility(View.GONE);
                    }

                }

            }
        });
        btn_chat_emo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (layout_more.getVisibility() == View.GONE) {
                    layout_more.setVisibility(View.VISIBLE);
                    layout_emo.setVisibility(View.VISIBLE);
                    hideSoftInputView();
                } else {
                    if (layout_add.getVisibility() == View.VISIBLE) {
                        layout_add.setVisibility(View.GONE);
                        layout_emo.setVisibility(View.VISIBLE);
                    } else {
                        layout_more.setVisibility(View.GONE);
                    }

                }

            }
        });

        /**
         * 检测输入框是否有输入，有输入则改变相应的View。
         */

        edit_user_comment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(s)) {
                    btn_chat_send.setVisibility(View.VISIBLE);
                    btn_chat_keyboard.setVisibility(View.GONE);
                    btn_chat_voice.setVisibility(View.GONE);
                } else {
                    if (btn_chat_voice.getVisibility() != View.VISIBLE) {
                        btn_chat_voice.setVisibility(View.VISIBLE);
                        btn_chat_send.setVisibility(View.GONE);
                        btn_chat_keyboard.setVisibility(View.GONE);
                    }
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        btn_chat_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String myWord=null;

                /**
                 * 这是一个发送消息的监听器，注意如果文本框中没有内容，那么getText()的返回值可能为
                 * null，这时调用toString()会有异常！所以这里必须在后面加上一个""隐式转换成String实例
                 * ，并且不能发送空消息。
                 */

                myWord=(edit_user_comment.getText()+"").toString();
                if(myWord.length()==0)
                    return;
                edit_user_comment.setText("");
                addTextToList(replace(myWord), "12:12", ME);
                Toast.makeText(ChatActivity.this,chatList.size()+"",Toast.LENGTH_SHORT).show();
                /**
                 * 更新数据列表，并且通过setSelection方法使ListView始终滚动在最底端
                 */
                chatAdapter.notifyDataSetChanged();
                chatListView.setSelection(chatList.size()-1);
            }
        });
    }


    void loadEmo(){
        View view=getLayoutInflater().inflate(R.layout.include_emo_gridview,null);
        View view2=getLayoutInflater().inflate(R.layout.include_emo_gridview,null);
        gridViewEmo=(GridView)view.findViewById(R.id.gridview);
        gridViewEmo2=(GridView)view2.findViewById(R.id.gridview);

        ArrayList<HashMap<String,Integer>> map=new ArrayList<>();
        for(Integer i:getRes()){
            HashMap<String,Integer> hashMap=new HashMap<>();
            hashMap.put("res",i);
            map.add(hashMap);
        }

        ArrayList<HashMap<String,Integer>> map2=new ArrayList<>();
        for(Integer i:getRes2()){
            HashMap<String,Integer> hashMap=new HashMap<>();
            hashMap.put("res",i);
            map2.add(hashMap);
        }

        SimpleAdapter adapter=new SimpleAdapter(ChatActivity.this,
                map,R.layout.item_expression,new String[]{"res"},new int[]{R.id.image1});
        SimpleAdapter adapter2=new SimpleAdapter(ChatActivity.this,
                map2,R.layout.item_expression,new String[]{"res"},new int[]{R.id.image1});

        gridViewEmo.setAdapter(adapter);
        gridViewEmo2.setAdapter(adapter2);


        ArrayList<View> viewList=new ArrayList<>();
        viewList.add(view);
        viewList.add(view2);

        MyPagerAdpter pagerAdpter=new MyPagerAdpter(viewList);
        pager_emo.setAdapter(pagerAdpter);


        /**
         * 因为是两个表情的界面，所以就涉及到两个gridview
         * 下面是两个gridview的点击事件
         *
         */
        gridViewEmo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                /**
                 * 将点击到的表情转化为一定格式的字符串
                 * 例如：V1234567890V,中间的数字表示图片资源
                 * replace（）方法将每次显示的文本中所有代表表情的字符串转化为图片资源的数字，
                 * 然后再转化为SpannableString
                 */
                String emsString = "V" + getRes().get(position) + "V";
                String content = edit_user_comment.getText().toString();
                SpannableString spannable = new SpannableString(content + emsString);
                Drawable drawable = getResources().getDrawable(getRes().get(position));
                drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                ImageSpan span = new ImageSpan(drawable, ImageSpan.ALIGN_BASELINE);
                spannable.setSpan(span, content.length(), content.length() + emsString.length(),
                        Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                String m = spannable + "";
                edit_user_comment.setText(replace(m), TextView.BufferType.SPANNABLE);


            }
        });

        gridViewEmo2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String emsString = "V" + getRes2().get(position) + "V";
                String content = edit_user_comment.getText().toString();
                SpannableString spannable = new SpannableString(content + emsString);
                Drawable drawable = getResources().getDrawable(getRes2().get(position));
                drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                ImageSpan span = new ImageSpan(drawable, ImageSpan.ALIGN_BASELINE);
                spannable.setSpan(span, content.length(), content.length() + emsString.length(),
                        Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                String m = spannable + "";
                edit_user_comment.setText(replace(m), TextView.BufferType.SPANNABLE);
            }
        });


    }


    /**
     * 直接返回图片的资源
     * 第二个表情界面的资源
     * @return
     */
    private ArrayList<Integer> getRes2(){
        ArrayList<Integer> arrayList=new ArrayList<>();
        arrayList.add(R.mipmap.ue40e);
        arrayList.add(R.mipmap.ue40f);
        arrayList.add(R.mipmap.ue410);
        arrayList.add(R.mipmap.ue411);
        arrayList.add(R.mipmap.ue412);
        arrayList.add(R.mipmap.ue413);
        arrayList.add(R.mipmap.ue414);
        arrayList.add(R.mipmap.ue415);
        arrayList.add(R.mipmap.ue416);
        arrayList.add(R.mipmap.ue417);
        arrayList.add(R.mipmap.ue418);
        arrayList.add(R.mipmap.ue41f);
        arrayList.add(R.mipmap.ue00e);
        arrayList.add(R.mipmap.ue421);

        return arrayList;
    }


    /**
     * 第一个表情界面的资源
     * @return
     */
    private ArrayList<Integer> getRes(){
        ArrayList<Integer> arrayList=new ArrayList<>();
        arrayList.add(R.mipmap.ue056);
        arrayList.add(R.mipmap.ue057);
        arrayList.add(R.mipmap.ue058);
        arrayList.add(R.mipmap.ue059);
        arrayList.add(R.mipmap.ue105);
        arrayList.add(R.mipmap.ue106);
        arrayList.add(R.mipmap.ue107);
        arrayList.add(R.mipmap.ue108);
        arrayList.add(R.mipmap.ue401);
        arrayList.add(R.mipmap.ue402);
        arrayList.add(R.mipmap.ue403);
        arrayList.add(R.mipmap.ue404);
        arrayList.add(R.mipmap.ue405);
        arrayList.add(R.mipmap.ue406);
        arrayList.add(R.mipmap.ue407);
        arrayList.add(R.mipmap.ue408);
        arrayList.add(R.mipmap.ue409);
        arrayList.add(R.mipmap.ue40a);
        arrayList.add(R.mipmap.ue40b);
        arrayList.add(R.mipmap.ue40c);
        arrayList.add(R.mipmap.ue40d);

        return arrayList;
    }


    /**
     * 将每次的输入内容按照正则表达式进行判断和解析
     * 显示出图片和文字。
     * indexOf(faceText, start);是faceText在文本中第一次出现的索引，start表示开始搜寻该子字符串的开始位置，
     * 因此寻找下一个子字符串时，start的值就要改变。
     *
     * @param text
     * @return
     */
    private SpannableString replace(String text ){
        try {
            SpannableString spannableString = new SpannableString(text);
            int start = 0;
            Pattern pattern = Pattern.compile("V\\d{10}V");
            Matcher matcher = pattern.matcher(text);
            while (matcher.find()) {
                String faceText = matcher.group();
                int startIndex = text.indexOf(faceText, start);
                int endIndex = startIndex + faceText.length();
                Drawable drawable = getResources().getDrawable(Integer.valueOf(faceText.substring(1, 11)));
                drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                ImageSpan span = new ImageSpan(drawable, ImageSpan.ALIGN_BASELINE);
                spannableString.setSpan(span, startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                start = (endIndex - 1);

            }
            return spannableString;
        }
        catch (Exception e){
           return new SpannableString(text);
        }

    }



    /**
     * 隐藏软键盘
     */

    public void hideSoftInputView() {
        InputMethodManager manager = ((InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE));
        if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getCurrentFocus() != null)
                manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * ViewPager的适配器
     * 每一个页面是一个View
     */
    class MyPagerAdpter extends PagerAdapter{
        List<View> viewLists;

        public MyPagerAdpter(List<View> lists)
        {
            viewLists = lists;
        }

        @Override
        public int getCount() {                                                                 //获得size
            // TODO Auto-generated method stub
            return viewLists.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            // TODO Auto-generated method stub
            return arg0 == arg1;
        }

        @Override
        public void destroyItem(View view, int position, Object object)                       //销毁Item
        {
            ((ViewPager) view).removeView(viewLists.get(position));
        }

        @Override
        public Object instantiateItem(View view, int position)                                //实例化Item
        {
            ((ViewPager) view).addView(viewLists.get(position), 0);
            return viewLists.get(position);
        }

    }


    /**
     * 组装消息，相当于一个javabean
     * @param text
     * @param time
     * @param who
     */
    private void addTextToList(SpannableString text,String time,int who){
        HashMap<String,Object> map=new HashMap<String,Object>();
        map.put("person",who );
        map.put("text", text);
        map.put("time", time);
        chatList.add(map);
    }

     void chatTest(){
        addTextToList(replace("不管你是谁"),"14:09",ME);
        addTextToList(replace("群发的我不回\n  ^_^"),"14:10", OTHER);
        addTextToList(replace("哈哈哈哈"), "14:15",ME);
        addTextToList(replace("新年快乐！"), "14:20",OTHER);
    }


    /**
     * 聊天的ListView的适配器
     */
    class MyChatAdapter extends BaseAdapter {

        Context context=null;
        ArrayList<HashMap<String,Object>> chatList=null;
        int[] layout;
        String[] from;
        int[] to;



        public MyChatAdapter(Context context,
                             ArrayList<HashMap<String, Object>> chatList, int[] layout,
                             String[] from, int[] to) {
            super();
            this.context = context;
            this.chatList = chatList;
            this.layout = layout;
            this.from = from;
            this.to = to;
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return chatList.size();
        }

        @Override
        public Object getItem(int arg0) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
     /*       // TODO Auto-generated method stub
            ViewHolder holder=null;
            int who=(Integer)chatList.get(position).get("person");

            convertView= LayoutInflater.from(context).inflate(
                    layout[who==ME?0:1], null);
            holder=new ViewHolder();
            holder.imageView=(ImageView)convertView.findViewById(to[who*2+0]);
            holder.textView=(TextView)convertView.findViewById(to[who*2+1]);

           // holder.imageView.setBackgroundResource((Integer)chatList.get(position).get(from[0]));
            holder.textView.setText(chatList.get(position).get("text").toString());
            return convertView;*/

            View view;
            ViewHolder viewHolder;
           /* if(convertView==null){*/
                int who=(Integer)chatList.get(position).get("person");
                viewHolder=new ViewHolder();
                if(who==ME){
                    view= LayoutInflater.from(context).inflate(layout[0], null);
                    viewHolder.imageView=(ImageView)view.findViewById(to[4]);
                    viewHolder.time=(TextView)view.findViewById(to[1]);
                    viewHolder.textView=(TextView)view.findViewById(to[0]);
                    viewHolder.textView.setText((SpannableString) chatList.get(position).get("text"));
                    viewHolder.time.setText(chatList.get(position).get("time").toString());
                  viewHolder.imageView.setImageDrawable(myHead
                    );
                    view.setTag(viewHolder);
                }else{
                    view= LayoutInflater.from(context).inflate(layout[1], null);
                    viewHolder.imageView=(ImageView)view.findViewById(to[5]);
                    viewHolder.time=(TextView)view.findViewById(to[3]);
                    viewHolder.textView=(TextView)view.findViewById(to[2]);
                    viewHolder.textView.setText((SpannableString) chatList.get(position).get("text"));
                    viewHolder.time.setText(chatList.get(position).get("time").toString());
                    viewHolder.imageView.setImageDrawable(otherHead);
                    view.setTag(viewHolder);
                }

            /*else{
                view=convertView;
                viewHolder=(ViewHolder)view.getTag();
            }*/


            return  view;

        }




         class  ViewHolder{
            public ImageView imageView=null;//表示头像
            public TextView textView=null;
             public TextView time=null;
        }



    }

}

