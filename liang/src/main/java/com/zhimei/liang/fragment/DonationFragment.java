package com.zhimei.liang.fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.zhimei.liang.utitls.MyApplication;
import com.zhimei.liang.utitls.RealPath;
import com.zhimei.liang.utitls.SecondHandGoods;
import com.zhimei.liang.weixiaoyuan.DonateSuccessActivity;
import com.zhimei.liang.weixiaoyuan.R;

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

                if(MyApplication.isSignUPSuccess()){
                    if (name.equals("")) {
                        Toast.makeText(view.getContext(), "亲，请输入名称", Toast.LENGTH_SHORT).show();
                    } else if (organizationi.equals("")) {
                        Toast.makeText(view.getContext(), "亲，请输入接收单位", Toast.LENGTH_SHORT).show();
                    } else if (picture == null) {
                        Toast.makeText(view.getContext(), "亲，请上传照片", Toast.LENGTH_SHORT).show();
                    } else {
                        loadFileandSignUp();
                    }
                }
                else{
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
                        Log.i("jialiang", picturePath);
                        // finish();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            case CHOOSE_PHOTO_CROP:
                /**
                 * 对相册中的图片进行裁剪，首先要得到被选中图片的URI
                 */
                if (res == this.getActivity().RESULT_OK) {
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
        progressDialog= ProgressDialog.show(view.getContext(), "", "正在登录中");
        // String path=Environment.getExternalStorageDirectory()+"/xiao.jpeg";
        bmobFile=new BmobFile(new File(picturePath));
        bmobFile.uploadblock(view.getContext(), new UploadFileListener() {
            @Override
            public void onSuccess() {
                Toast.makeText(view.getContext(), "图片上传成功" + bmobFile.getFileUrl(view.getContext()), Toast.LENGTH_SHORT).show();
                // sign();
                MyApplication.setTestUrl(bmobFile.getFileUrl(view.getContext()));
                loadSecondGoods();

            }

            @Override
            public void onFailure(int i, String s) {
                Toast.makeText(view.getContext(), s, Toast.LENGTH_SHORT).show();
                Log.i("jialiang", s);
                /**
                 * 图片上传失败时，关掉进度条
                 */
                progressDialog.dismiss();
            }
        });
    }

    void loadSecondGoods(){
       /* SecondHandGoods secondHandGoods=new SecondHandGoods();
        secondHandGoods.setName(goodName);
        secondHandGoods.setPrice(goodPrice);
        secondHandGoods.setDescription(goodDescreption);
        secondHandGoods.setPictureFile(bmobFile);
        secondHandGoods.setCategory(catagary);
        secondHandGoods.setTradeWay("卖出");*/
        DonationRecord donationRecord=new DonationRecord();
        donationRecord.setName(name);
        donationRecord.setReceiveOrganization(organizationi);
        donationRecord.setPublishMan(MyApplication.getCurrentName());
        donationRecord.setPictureFile(bmobFile);
        donationRecord.save(view.getContext(), new SaveListener() {
            @Override
            public void onSuccess() {
                progressDialog.dismiss();
                Toast.makeText(view.getContext(), "商品发布成功", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(view.getContext(), DonateSuccessActivity.class);
                startActivity(intent);
            }

            @Override
            public void onFailure(int i, String s) {
                progressDialog.dismiss();
                Toast.makeText(view.getContext(), s, Toast.LENGTH_SHORT).show();
            }
        });
    }

}
