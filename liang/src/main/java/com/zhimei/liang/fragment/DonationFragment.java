package com.zhimei.liang.fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.zhimei.liang.customview.XCicleImageView;
import com.zhimei.liang.utitls.DonationRecord;
import com.zhimei.liang.utitls.FileHelper;
import com.zhimei.liang.utitls.FormFile;
import com.zhimei.liang.utitls.MyApplication;
import com.zhimei.liang.utitls.RealPath;
import com.zhimei.liang.utitls.SecondHandGoods;
import com.zhimei.liang.weixiaoyuan.DonateSuccessActivity;
import com.zhimei.liang.weixiaoyuan.R;
import com.zhimei.liang.weixiaoyuan.ScoreActivity;

import java.io.File;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;


public class DonationFragment extends Fragment {
    private  View view;
    private ImageButton commit_photo;
    private Button donate;
    private Bitmap picture;
    private Uri imageUri;
    private XCicleImageView imagetest;
    private EditText et_name;
    private EditText et_description;
    private static final int TAKE_PHOTO = 1;
    private static final int CROP_PHOTO = 2;
    private static final int CHOOSE_PHOTO_CROP=3;
    private  File output;
    private String name;
    private  String organizationi;//接收的组织
    private String picturePath;//照片存储路径
    private ProgressDialog progressDialog;
    private BmobFile bmobFile;
    private Uri uri_photo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_donate,container,false);
        initView();
        chooseWays();
        return view;
    }

    void initView(){
        commit_photo=(ImageButton)view.findViewById(R.id.don_ib_photo);
        imagetest=(XCicleImageView)view.findViewById(R.id.imageView);
        et_name=(EditText)view.findViewById(R.id.don_et_name);
        et_description=(EditText)view.findViewById(R.id.don_et_describle);
        donate=(Button)view.findViewById(R.id.ensure_donate);
        donate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = et_name.getText().toString();
                organizationi = et_description.getText().toString();
               /* Intent intent=new Intent(view.getContext(), DonateSuccessActivity.class);
                startActivity(intent);*/

                if (MyApplication.isSignUPSuccess()) {
                    if (name.equals("")) {
                        Toast.makeText(view.getContext(), "亲，请输入名称", Toast.LENGTH_SHORT).show();
                    } else if (organizationi.equals("")) {
                        Toast.makeText(view.getContext(), "亲，请输入接收单位", Toast.LENGTH_SHORT).show();
                    } else if (picture == null) {
                        Toast.makeText(view.getContext(), "亲，请上传照片", Toast.LENGTH_SHORT).show();
                    } else {
                        loadFileandSignUp();
                    }
                } else {
                    Toast.makeText(view.getContext(), "亲，请先登录", Toast.LENGTH_SHORT).show();
                }


            }
        });
    }

    void chooseWays(){
        commit_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String string[] = {"拍照", "从相册中选取"};

                AlertDialog.Builder dialog = new AlertDialog.Builder(view.getContext());
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
                if (res == this.getActivity().RESULT_OK) {

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
                if (res == this.getActivity().RESULT_OK) {
                    /**
                     * 将裁剪后的图片进行处理， Uri uri = data.getData();得到裁剪后图片的uri。
                     */
                    try {
                        Uri uri = data.getData();
                        Bitmap bit = BitmapFactory
                                .decodeStream(this.getActivity().getContentResolver().openInputStream(
                                        uri));
                        picture=bit;
                        imagetest.setImageBitmap(picture);
                        imagetest.setVisibility(View.VISIBLE);
                        picturePath= RealPath.getPath(view.getContext(), uri);
                        // finish();
                    } catch (Exception e) {
                        /**
                         * 因为htc手机的兼容问题，所以在CROP_PHOTO中抛异常，代码无法正常运行。
                         * 因此该uri来自上一个活动中，而并非截图后的uri
                         */
                        try{
                            Bitmap bit = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(uri_photo));
                            picture=bit;
                            imagetest.setImageBitmap(picture);
                            imagetest.setVisibility(View.VISIBLE);
                        }
                        catch (Exception e1){

                        }
                        picturePath=RealPath.getPath(view.getContext(),uri_photo);
                    }
                }
                break;
            case CHOOSE_PHOTO_CROP:
                /**
                 * 对相册中的图片进行裁剪，首先要得到被选中图片的URI
                 */
                if (res == this.getActivity().RESULT_OK) {
                   uri_photo = data.getData();
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
        progressDialog= ProgressDialog.show(view.getContext(), "", "正在提交数据");
        // String path=Environment.getExternalStorageDirectory()+"/xiao.jpeg";
        bmobFile=new BmobFile(new File(picturePath));
        bmobFile.uploadblock(view.getContext(), new UploadFileListener() {
            @Override
            public void onSuccess() {
                // sign();
                MyApplication.setTestUrl(bmobFile.getFileUrl(view.getContext()));
                loadSecondGoods();

            }

            @Override
            public void onFailure(int i, String s) {
                /**
                 * 图片上传失败时，关掉进度条
                 */
                progressDialog.dismiss();
            }
        });
    }

    void loadSecondGoods(){
        DonationRecord donationRecord=new DonationRecord();
        donationRecord.setName(name);
        donationRecord.setReceiveOrganization(organizationi);
        donationRecord.setPublishMan(MyApplication.getCurrentName());
        donationRecord.setPictureFile(bmobFile);
        donationRecord.save(view.getContext(), new SaveListener() {
            @Override
            public void onSuccess() {
                progressDialog.dismiss();
                /**
                 * 每次的积分加15，并且存储到文件中
                 */
                new FileHelper().storeUpScore(view.getContext(), 15);
                Toast.makeText(view.getContext(), "商品发布成功,快去查看你的积分吧", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(view.getContext(), ScoreActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            }

            @Override
            public void onFailure(int i, String s) {
                progressDialog.dismiss();
            }
        });
    }


    /**
     * 捐赠成功后，修改分数
     * 捐赠一次，积分加15
     */
    void storeUpScore(){
        int oldScore=getScore();
        int newScore=oldScore+15;
        SharedPreferences.Editor editor=view.getContext().getSharedPreferences("weixiaoyuan", view.getContext().MODE_PRIVATE).edit();
        editor.putInt("score", newScore);
        editor.commit();
    }


    /**
     * 读取存储的分数值
     * @return
     */

    int  getScore(){
        SharedPreferences pref=view.getContext().getSharedPreferences("weixiaoyuan", view.getContext().MODE_PRIVATE);
        int score=pref.getInt("score",0);
        return  score;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(picture != null && !picture.isRecycled()){
            // 回收并且置为null
            picture.recycle();
            picture = null;
        }

        System.gc();
    }

}
