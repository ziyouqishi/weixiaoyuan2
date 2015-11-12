package com.zhimei.fragment;
import android.app.AlertDialog;
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
import android.widget.EditText;
import android.widget.ImageButton;
import com.zhimei.customview.XCicleImageView;
import com.zhimei.weixiaoyuan.R;

import java.io.File;


public class DonationFragment extends Fragment {
    private  View view;
    private ImageButton commit_photo;
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
    private  String description;

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
        name=et_name.getText().toString();
        description=et_description.getText().toString();
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
                Uri uri_photo = data.getData();
                if (res == this.getActivity().RESULT_OK) {
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
        /**
         * 打开选择图片的界面，选择结束后，进入裁剪界面
         */
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");//相片类型
        startActivityForResult(intent, CHOOSE_PHOTO_CROP);

    }

}
