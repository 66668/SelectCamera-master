package com.linzhi.selectcamera_master;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.linzhi.selectcamera_master.inject.ViewInject;
import com.linzhi.selectcamera_master.other.BaseActivity;
import com.linzhi.selectcamera_master.second.CustomDialog;
import com.linzhi.selectcamera_master.second.EditPictureUtil;
import com.linzhi.selectcamera_master.second.UploadImageDialog;

/**
 * Created by sjy on 2017/6/29.
 */

public class SecondActivity extends BaseActivity {
    //拍照
    @ViewInject(id = R.id.imgPhoto, click = "imgUpload")
    ImageView imgPhoto;

    @ViewInject(id = R.id.btn, click = "imgPhotoClick")
    Button btn;

    private UploadImageDialog dialog;
    private CustomDialog customDialog;
    Uri uri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn.setText("拍照");
    }

    public void imgPhotoClick(View view) {
        showUploadeDialog();
    }

    public void imgUpload(View view) {
        showCustomDialog();
    }

    private void showUploadeDialog() {
        if (dialog == null) {
            dialog = new UploadImageDialog(SecondActivity.this, new UploadImageDialog.ClickCallback() {

                @Override
                public void galleryCallback() {
                    Intent intent = EditPictureUtil.getGalleryIntent(200, 200, EditPictureUtil.createTempCropImageFile(SecondActivity.this));
                    startActivityForResult(intent, 2);
                    dialog.dismiss();
                }

                @Override
                public void PhotoCallback() {
                    dialog.dismiss();
                    Intent intent = EditPictureUtil.getCaptureIntent(SecondActivity.this);
                    startActivityForResult(intent, 1);
                }
            });
        }
        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 1) {
                // 启动修剪相片功能
                Intent intent = EditPictureUtil.getCropImageIntent(EditPictureUtil.getCaptureTempFileUri(SecondActivity.this), 200, 200,
                        EditPictureUtil.createTempCropImageFile(SecondActivity.this));
                startActivityForResult(intent, 2);
            }
            if (requestCode == 2) {// 剪切完成

                uri = EditPictureUtil.getCropImageTempFileUri(SecondActivity.this);
                Bitmap bitmap = EditPictureUtil.getBitmapFromUri(SecondActivity.this, uri);
                imgPhoto.setImageBitmap(bitmap);


            }
        }

    }

    /**
     * 显示图片 点击上传
     */
    // 预览剪切后的图片
    ImageView imageView;
    private void showCustomDialog() {
        if (customDialog == null) {
            customDialog = new CustomDialog(SecondActivity.this, new CustomDialog.ButtonRespond() {

                @Override
                public void buttonRightRespond() {
                    customDialog.dismiss();
                    Toast.makeText(SecondActivity.this, "实现上传",Toast.LENGTH_SHORT).show();
                }

                @Override
                public void buttonLeftRespond() {
                    customDialog.dismiss();
                }
            });

            View view = LayoutInflater.from(SecondActivity.this).inflate(R.layout.identity, null);
            imageView = (ImageView) view.findViewById(R.id.iv_usericon);
            customDialog.setDialogTitle("剪切图片预览");
            customDialog.setRightButtonText("上传");
            customDialog.setLeftButtonText("取消");
            customDialog.addView2Frame(view);
            customDialog.setMagssageViewVisibility(View.GONE);
            customDialog.setFrameViewVisibility(View.VISIBLE);
        }

        imageView.setImageBitmap(EditPictureUtil.getBitmapFromUri(SecondActivity.this, uri));
        customDialog.show();
    }
}
