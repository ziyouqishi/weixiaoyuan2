package com.zhimei.liang.weixiaoyuan;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.zhimei.liang.utitls.FileHelper;
import com.zhimei.liang.utitls.MyApplication;
import com.zhimei.liang.utitls.RealPath;
import com.zhimei.liang.utitls.SecondHandGoods;

import java.io.File;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;

public class Goods_release_Activity extends Activity {
    private ImageButton getPhoto,back,index,ensure,cancal;
    private static final int TAKE_PHOTO = 1;
    private static final int CROP_PHOTO = 2;
    private static final int CHOOSE_PHOTO_CROP=3;
    private Bitmap picture;
    private Uri imageUri;
    private File output;
    private  String[] planets;
    private String catagary;//商品的种类
    private Spinner type;
    private EditText name;
    private EditText price;
    private EditText descreption;
    private BmobFile bmobFile;
    private String goodName;
    private String goodPrice;
    private String picturePath;
    private ProgressDialog progressDialog;
    private String goodDescreption;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_release_);
        initViews();
    }
    void initViews(){
        getPhoto=(ImageButton)findViewById(R.id.rel_two_getphoto);
        back=(ImageButton)findViewById(R.id.rel_back);
        index=(ImageButton)findViewById(R.id.rel_index);
        type=(Spinner)findViewById(R.id.rel_spinner1);
        name=(EditText)findViewById(R.id.rel_two_getname);
        price=(EditText)findViewById(R.id.rel_two_getprice);
        descreption=(EditText)findViewById(R.id.rel_two_getdescreption);
        ensure=(ImageButton)findViewById(R.id.rel_two_commit_ensure);
        cancal=(ImageButton)findViewById(R.id.rel_two_giveup);
        catagary="其他";

        //获取数组资源
        Resources res = getResources();
         planets = res.getStringArray(R.array.goods_catagory);

        index.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent=new Intent(Goods_release_Activity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        getPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String string[] = {"拍照", "从相册中选取"};

                AlertDialog.Builder dialog = new AlertDialog.Builder(Goods_release_Activity.this);
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

       type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
           @Override
           public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               catagary = planets[position];
               Toast.makeText(Goods_release_Activity.this, planets[position], Toast.LENGTH_SHORT).show();

           }

           @Override
           public void onNothingSelected(AdapterView<?> parent) {

           }
       });


        ensure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MyApplication.isSignUPSuccess()) {

                    goodName = name.getText().toString().trim();
                    goodPrice = price.getText().toString().trim();
                    goodDescreption = descreption.getText().toString().trim();
                    if (goodName.equals("")) {
                        Toast.makeText(Goods_release_Activity.this, "亲，请输入商品名称", Toast.LENGTH_SHORT).show();
                    } else if (goodPrice.equals("")) {
                        Toast.makeText(Goods_release_Activity.this, "亲，请输入商品价格", Toast.LENGTH_SHORT).show();
                    } else if (goodDescreption.equals("")) {
                        Toast.makeText(Goods_release_Activity.this, "亲，请输入商品描述", Toast.LENGTH_SHORT).show();
                    }
               /* else if(goodDescreption.length()<=15){
                    Toast.makeText(Goods_release_Activity.this,"亲，商品描述应该大于15字",Toast.LENGTH_SHORT).show();
                }*/
                    else if (picture == null) {
                        Toast.makeText(Goods_release_Activity.this, "亲，请上传商品图片", Toast.LENGTH_SHORT).show();
                    } else {
                        loadFileandSignUp();
                    }

                } else {
                    Toast.makeText(Goods_release_Activity.this, "亲，请先登录", Toast.LENGTH_SHORT).show();
                }

            }
        });


        cancal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* Bitmap bitmap=new FileHelper().getHttpBitmap(MyApplication.getTestUrl());
                Toast.makeText(Goods_release_Activity.this, MyApplication.getTestUrl(), Toast.LENGTH_SHORT).show();
                ensure.setImageBitmap(bitmap);*/
               // new SignUpTast().execute();

            }
        });
    }

    /**
     * 打开相机拍照
     */

    private void takephoto() {


        output = new File(Environment
                .getExternalStorageDirectory(), "output_image.jpg");
        try {
            if (output.exists()) {
                output.delete();
            }
            output.createNewFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
        imageUri = Uri.fromFile(output);
        /**
         * 打开拍照的界面
         */
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);

        startActivityForResult(intent, TAKE_PHOTO);

    }


    public void onActivityResult(int req, int res, Intent data) {
        switch (req) {
            case TAKE_PHOTO:
                if (res ==RESULT_OK) {

                    /**
                     * 打开裁剪的界面。
                     */
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
                    /**
                     * 将裁剪后的图片进行处理， Uri uri = data.getData();得到裁剪后图片的uri。
                     */
                    try {
                        Uri uri = data.getData();
                        Bitmap bit = BitmapFactory
                                .decodeStream(getContentResolver().openInputStream(
                                        uri));
                        picture=bit;
                        getPhoto.setImageBitmap(bit);
                        picturePath= RealPath.getPath(Goods_release_Activity.this, uri);
                        Log.i("jialiang",picturePath);
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
                    intent.putExtra("MediaStore.EXTRA_OUTPUT", uri_photo);
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
        /**
         * 打开选择图片的界面，选择结束后，进入裁剪界面
         */
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");//相片类型
        startActivityForResult(intent, CHOOSE_PHOTO_CROP);

    }

    void loadFileandSignUp(){
        progressDialog= ProgressDialog.show(Goods_release_Activity.this, "", "正在登录中");
        // String path=Environment.getExternalStorageDirectory()+"/xiao.jpeg";
        bmobFile=new BmobFile(new File(picturePath));
        bmobFile.uploadblock(Goods_release_Activity.this, new UploadFileListener() {
            @Override
            public void onSuccess() {
                Toast.makeText(Goods_release_Activity.this, "图片上传成功"+bmobFile.getFileUrl(Goods_release_Activity.this), Toast.LENGTH_SHORT).show();
                // sign();
                MyApplication.setTestUrl(bmobFile.getFileUrl(Goods_release_Activity.this));
                loadSecondGoods();

            }

            @Override
            public void onFailure(int i, String s) {
                Toast.makeText(Goods_release_Activity.this, s, Toast.LENGTH_SHORT).show();
                Log.i("jialiang", s);
                /**
                 * 图片上传失败时，关掉进度条
                 */
                progressDialog.dismiss();
            }
        });
    }

    void loadSecondGoods(){
        SecondHandGoods secondHandGoods=new SecondHandGoods();
        secondHandGoods.setName(goodName);
        secondHandGoods.setPrice(goodPrice);
        secondHandGoods.setDescription(goodDescreption);
        secondHandGoods.setPictureFile(bmobFile);
        secondHandGoods.setCategory(catagary);
        secondHandGoods.setTradeWay("卖出");
        secondHandGoods.setPublishMan(MyApplication.getCurrentName());//得到当前用户的名称
        secondHandGoods.save(Goods_release_Activity.this, new SaveListener() {
            @Override
            public void onSuccess() {
                progressDialog.dismiss();
                Toast.makeText(Goods_release_Activity.this, "商品发布成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int i, String s) {
                progressDialog.dismiss();
                Toast.makeText(Goods_release_Activity.this, s, Toast.LENGTH_SHORT).show();
            }
        });
    }



    class SignUpTast extends AsyncTask<Void,Integer,Boolean> {
        ProgressDialog progressDialog;
        Bitmap bitmap;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog=ProgressDialog.show(Goods_release_Activity.this, "", "正在下载中");
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
           progressDialog.dismiss();
            ensure.setImageBitmap(bitmap);


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
            try
            {
                 bitmap=new FileHelper().getHttpBitmap(MyApplication.getTestUrl());
               // Thread.sleep(3000);

            }
            catch (Exception e){

            }

            return true;

        }
    }


}
