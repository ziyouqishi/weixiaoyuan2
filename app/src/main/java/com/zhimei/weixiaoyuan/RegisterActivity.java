package com.zhimei.weixiaoyuan;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EdgeEffect;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;


public class RegisterActivity extends Activity {
    private ImageButton addPhoto;
    private Bitmap picture;
    private Uri imageUri;
    private ImageView imagetest;
    private EditText et_id;
    private EditText et_password;
    private EditText et_name;
    private EditText et_phone_number;
    private static final int TAKE_PHOTO = 1;
    private static final int CROP_PHOTO = 2;
    private static final int REQUEST_CODE_PICK_IMAGE=3;
    private static final int CHOOSE_PHOTO_CROP=4;
    private  File output;
    private String id;//学号
    private String password;//密码
    private String name;//昵称
    private String phone_number;//电话号码


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
        chooseWays();

    }
    void initView(){
        addPhoto=(ImageButton)findViewById(R.id.register_ib_photo);
        imagetest=(ImageView)findViewById(R.id.imageView_test);
        et_id=(EditText)findViewById(R.id.reg_et_ID);
        et_password=(EditText)findViewById(R.id.reg_et_nickname);
        et_name=(EditText)findViewById(R.id.reg_et_phone);
        et_phone_number=(EditText)findViewById(R.id.reg_et_password);

        id=et_id.getText().toString();
        password=et_password.getText().toString();
        name=et_name.getText().toString();
        phone_number=et_phone_number.getText().toString();




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

                    startActivityForResult(intent, CROP_PHOTO);
                }
                break;
            case CROP_PHOTO:
                if (res == RESULT_OK) {
                    try {
                        Uri uri = data.getData();
                        Bitmap bit = BitmapFactory
                                .decodeStream(getContentResolver().openInputStream(
                                        uri));
                        picture=bit;
                        imagetest.setImageBitmap(picture);
                        imagetest.setVisibility(View.VISIBLE);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            case CHOOSE_PHOTO_CROP:
                /**
                 * 对相册中的图片进行裁剪，首先要得到被选中图片的URI
                 */
                Uri uri_photo = data.getData();
                if (res == RESULT_OK) {
                    Intent intent = new Intent("com.android.camera.action.CROP");
                    intent.setDataAndType(uri_photo, "image/*");
                    intent.putExtra("scale", true);
                    intent.putExtra("MediaStore.EXTRA_OUTPUT", uri_photo);

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
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");//相片类型
        startActivityForResult(intent, CHOOSE_PHOTO_CROP);

    }


}
