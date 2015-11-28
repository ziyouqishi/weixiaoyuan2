package com.zhimei.liang.weixiaoyuan;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zhimei.liang.utitls.MyApplication;
import com.zhimei.liang.utitls.User;
import com.zhimei.liang.weixiaoyuan.R;

import org.w3c.dom.Text;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

public class AlterAddressActivity extends Activity {
    private ImageButton ensure;
    private RelativeLayout layout;
    private ImageButton monify, back,index;
    private EditText it_address;
    private String objectid;//得到对象的ＩＤ
    private String newAddress;
    private BmobQuery<User> query ;
    private ProgressDialog progressDialog;
    private TextView tv_current_address;
    private TextView current_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alter_address);
        initViews();
        tv_current_address.setText("");
        current_name.setText("");
        getCurrentAddress();
    }

    void initViews(){
        back=(ImageButton)findViewById(R.id.alter_back);
        index=(ImageButton)findViewById(R.id.alter_index);

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
                Intent intent=new Intent(AlterAddressActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });


        it_address=(EditText)findViewById(R.id.new_address);
        ensure=(ImageButton)findViewById(R.id.xiugaidizhi);
        layout=(RelativeLayout)findViewById(R.id.alter_three);
        tv_current_address=(TextView)findViewById(R.id.current_address);
        current_name=(TextView)findViewById(R.id.name);
        monify=(ImageButton)findViewById(R.id.monifyaddress_ensure);

        ensure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout.setVisibility(View.VISIBLE);
            }
        });

        monify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newAddress = it_address.getText().toString().trim();
                if (newAddress.equals("")) {
                    Toast.makeText(AlterAddressActivity.this, "请输入新地址", Toast.LENGTH_SHORT).show();
                } else {
                    progressDialog = ProgressDialog.show(AlterAddressActivity.this, "", "正在提交数据");
                    progressDialog.show();
                    querryObjectId();
                }

            }
        });

    }

    /**
     * 修改地址
     * 注意：修改之前，必须要得到对象的ObjectId的值，通过这个值才能进行修改。
     */

    void alter(){
        User user=new User();
        user.setAddress(newAddress);
        user.update(AlterAddressActivity.this, objectid, new UpdateListener() {
            @Override
            public void onSuccess() {
                progressDialog.dismiss();
                Toast.makeText(AlterAddressActivity.this, "地址修改成功", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(int i, String s) {
                progressDialog.dismiss();
                Toast.makeText(AlterAddressActivity.this, "抱歉，地址修改失败，请检查网络连接", Toast.LENGTH_SHORT).show();
            }
        });


    }

    void querryObjectId(){
        query = new BmobQuery<User>();
        query.addWhereEqualTo("username", MyApplication.getCurrentName());
        query.findObjects(AlterAddressActivity.this, new FindListener<User>() {
            @Override
            public void onSuccess(List<User> list) {
                objectid = list.get(0).getObjectId();
                Log.i("liang", objectid);
                alter();

            }

            @Override
            public void onError(int i, String s) {

            }
        });


    }

    void getCurrentAddress(){
        query = new BmobQuery<User>();
        query.addWhereEqualTo("username", MyApplication.getCurrentName());
        query.findObjects(AlterAddressActivity.this, new FindListener<User>() {
            @Override
            public void onSuccess(List<User> list) {
                String currentAddress = list.get(0).getAddress();
                String name=list.get(0).getUsername();
                current_name.setText(name);
                tv_current_address.setText(currentAddress);
            }

            @Override
            public void onError(int i, String s) {

            }
        });



    }


}

