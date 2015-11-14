package com.zhimei.liang.weixiaoyuan;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import com.zhimei.liang.customview.RoundImageView;
import com.zhimei.liang.utitls.RealPath;
import com.zhimei.liang.utitls.User;
import java.io.File;
import java.util.HashMap;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.UploadFileListener;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.gui.RegisterPage;


public class RegisterActivity extends Activity {
    private ImageView addPhoto;
    private Bitmap picture;
    private Uri imageUri;
    private EditText et_address;
    private EditText et_password;
    private EditText et_name;
    private Button next;
    private static final int TAKE_PHOTO = 1;
    private static final int CROP_PHOTO = 2;
    private static final int REQUEST_CODE_PICK_IMAGE=3;
    private static final int CHOOSE_PHOTO_CROP=4;
    private  File output;
    private String picturePath;
    private String address;//地址
    private String password;//密码
    private String name;//昵称
    private String phone_number;//电话号码
    private BmobFile bmobFile;
    private ImageButton index,back;
    private ProgressDialog progressDialog;

    public static String APPID = "a91ea9411de0d54e0eaf0311d2a9fec3";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        SMSSDK.initSDK(this, "bb55d5170bea", "89c378517a900a7b3d4fea2199d2c561");
        //test();
        initView();
        chooseWays();
        sendSMS();


    }
    void initView(){
        addPhoto=(RoundImageView)findViewById(R.id.register_ib_photo);
       // imagetest=(ImageView)findViewById(R.id.imageView_test);
        et_address=(EditText)findViewById(R.id.reg_et_address);
        et_password=(EditText)findViewById(R.id.reg_et_password);
        et_name=(EditText)findViewById(R.id.reg_et_name);
        next=(Button)findViewById(R.id.reg_submit);
        password=et_password.getText().toString();
        name=et_name.getText().toString();
        index=(ImageButton)findViewById(R.id.reg_index);
        back=(ImageButton)findViewById(R.id.reg_back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        index.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                address = et_address.getText().toString();
                password = et_password.getText().toString();
                name = et_name.getText().toString();
                if (name.equals("")) {
                    Toast.makeText(RegisterActivity.this, "亲，请填写昵称", Toast.LENGTH_SHORT).show();
                } else if (password.equals("")) {
                    Toast.makeText(RegisterActivity.this, "亲，请填写密码", Toast.LENGTH_SHORT).show();
                } else if (address.equals("")) {
                    Toast.makeText(RegisterActivity.this, "亲，请填写地址", Toast.LENGTH_SHORT).show();
                } else {
                   loadFileandSignUp();
                   // new SignUpTast().execute();
                }
            }
        });





    }

    /**
     * 上传图片后，进行登录
     */
    void loadFileandSignUp(){
        progressDialog=ProgressDialog.show(RegisterActivity.this, "", "正在登录中");
        // String path=Environment.getExternalStorageDirectory()+"/xiao.jpeg";
         bmobFile=new BmobFile(new File(picturePath));
        bmobFile.uploadblock(RegisterActivity.this, new UploadFileListener() {
            @Override
            public void onSuccess() {
                Toast.makeText(RegisterActivity.this, "图片上传成功", Toast.LENGTH_SHORT).show();
                sign();

            }

            @Override
            public void onFailure(int i, String s) {
                Toast.makeText(RegisterActivity.this, s, Toast.LENGTH_SHORT).show();
                Log.i("jialiang", s);
                /**
                 * 图片上传失败时，关掉进度条
                 */
                progressDialog.dismiss();
            }
        });
    }

    void sendSMS(){
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                address = et_address.getText().toString();
                password = et_password.getText().toString();
                name = et_name.getText().toString();
                RegisterPage registerPage = new RegisterPage();
                // Toast.makeText(RegisterActivity.this,address,Toast.LENGTH_SHORT).show();
                if (name.equals("")) {
                    Toast.makeText(RegisterActivity.this, "亲，请填写昵称", Toast.LENGTH_SHORT).show();
                } else if (password.equals("")) {
                    Toast.makeText(RegisterActivity.this, "亲，请填写密码", Toast.LENGTH_SHORT).show();
                } else if (address.equals("")) {
                    Toast.makeText(RegisterActivity.this, "亲，请填写地址", Toast.LENGTH_SHORT).show();
                } else {
                    registerPage.show(RegisterActivity.this);
                }

                registerPage.setRegisterCallback(new EventHandler() {
                    public void afterEvent(int event, int result, Object data) {

                        if (result == SMSSDK.RESULT_COMPLETE) {
                            @SuppressWarnings("unchecked")
                            HashMap<String, Object> phoneMap = (HashMap<String, Object>) data;
                            String country = (String) phoneMap.get("country");
                            String phone = (String) phoneMap.get("phone");
                            Log.i("liang", phone);
                            // Toast.makeText(RegisterActivity.this,RegisterPage.etPhoneNum.getText(),Toast.LENGTH_SHORT).show();
                            Toast.makeText(RegisterActivity.this, phone, Toast.LENGTH_SHORT).show();
                            phone_number = phone;

                            //进行注册
                            sign();

                            Log.i("jialiang", address);
                            Log.i("jialiang", name);
                            //SMSSDK.submitUserInfo("123", "jialiang", null, "", "18842647883");
                        }
                    }

                    @Override
                    public void beforeEvent(int i, Object o) {
                        super.beforeEvent(i, o);
                    }
                });

            }
        });
    }



     void chooseWays(){
        addPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String string[] = {"拍照", "从相册中选取"};

                AlertDialog.Builder dialog = new AlertDialog.Builder(RegisterActivity.this);
                dialog.setTitle("选择照片");
                dialog.setItems(string, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                takephoto();
                                break;
                            case 1:
                                choosephoto();
                            default:
                                break;

                        }
                    }
                });
                dialog.show();

            }

        });

    }

    /**
     * 打开相机拍照
     */

    private void takephoto() {


                output = new File(Environment
                        .getExternalStorageDirectory(), "output_image.jpg");
                Log.i("jialiang", output.getPath());
                try {
                    if (output.exists()) {
                        output.delete();
                    }
                    output.createNewFile();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                imageUri = Uri.fromFile(output);
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(intent, TAKE_PHOTO);

            }


    public void onActivityResult(int req, int res, Intent data) {
        switch (req) {
            case TAKE_PHOTO:
                if (res == RESULT_OK) {
                    Intent intent = new Intent("com.android.camera.action.CROP");
                    intent.setDataAndType(imageUri, "image/*");
                    intent.putExtra("scale", true);
                    intent.putExtra("MediaStore.EXTRA_OUTPUT", imageUri);
                    intent.putExtra("outputX", 300);
                    intent.putExtra("outputY", 400);

                    startActivityForResult(intent, CROP_PHOTO);
                }
                break;
            case CROP_PHOTO:
                if (res == RESULT_OK) {
                    try {
                        Uri uri = data.getData();
                        Bitmap bit = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
                        picturePath=RealPath.getPath(RegisterActivity.this,uri);
                        Log.i("jialiang",picturePath);
                        Toast.makeText(RegisterActivity.this,picturePath,Toast.LENGTH_LONG).show();
                        picture=bit;
                        addPhoto.setImageBitmap(picture);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            case CHOOSE_PHOTO_CROP:
                /**
                 * 对相册中的图片进行裁剪，首先要得到被选中图片的URI
                 */
                if (res == RESULT_OK) {
                    Uri uri_photo = data.getData();
                    Intent intent = new Intent("com.android.camera.action.CROP");
                    intent.setDataAndType(uri_photo, "image/*");
                    intent.putExtra("scale", true);
                    intent.putExtra("MediaStore.EXTRA_OUTPUT", imageUri);
                    intent.putExtra("outputX", 300);
                    intent.putExtra("outputY", 400);
                    startActivityForResult(intent, CROP_PHOTO);
                }
                break;


            default:
                break;
        }
    }

    /**
     * 选择相册图片
     */
    private void choosephoto() {
        output = new File(Environment
                .getExternalStorageDirectory(), "output_image2.jpg");
        Log.i("jialiang",output.getPath());
        try {
            if (output.exists()) {
                output.delete();
            }
            output.createNewFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
        imageUri = Uri.fromFile(output);
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");//相片类型
        intent.putExtra("MediaStore.EXTRA_OUTPUT", imageUri);
       /* intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 400);*/
        startActivityForResult(intent, CHOOSE_PHOTO_CROP);

    }

    /**
     * 登录
     */

    void sign(){
        User user=new User();
        user.setUsername(name);
        user.setPassword(password);
                //默认电话号码是主键
        user.setMobilePhoneNumber("18842647883");
        user.setGrade(0);
        user.setAddress(address);
        user.setPicture(bmobFile);
        user.signUp(RegisterActivity.this, new cn.bmob.v3.listener.SaveListener() {

            @Override
            public void onSuccess() {
                // TODO Auto-generated method stub
                progressDialog.dismiss();
                Toast.makeText(RegisterActivity.this, "成功了", Toast.LENGTH_SHORT).show();
                Log.i("jialiang", "成功了");
            }

            @Override
            public void onFailure(int arg0, String arg1) {                progressDialog.dismiss();
                // TODO Auto-generated method stub
                if(arg1.contains("already taken")&&arg1.contains("username")){
                    Toast.makeText(RegisterActivity.this, "对不起，该用户名已经被注册，请重新注册", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(RegisterActivity.this, arg1, Toast.LENGTH_SHORT).show();
                }
            }
                });
    }


    class SignUpTast extends AsyncTask<Void,Integer,Boolean>{
        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog=ProgressDialog.show(RegisterActivity.this, "", "正在上传数据");
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            progressDialog.dismiss();


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
            loadFileandSignUp();
            try
            {
                Thread.sleep(3000);
            }
            catch (Exception e){

            }

            return true;

        }
    }


}
