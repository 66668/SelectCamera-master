package com.linzhi.selectcamera_master;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.linzhi.selectcamera_master.inject.ViewInject;
import com.linzhi.selectcamera_master.other.BaseActivity;
import com.linzhi.selectcamera_master.other.ImageLoadingConfig;
import com.linzhi.selectcamera_master.other.UpdateAvatarUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class MainActivity extends BaseActivity implements UpdateAvatarUtil.ChoosePicCallBack {
    //拍照
    @ViewInject(id = R.id.imgPhoto, click = "imgPhotoClick")
    ImageView imgPhoto;

    @ViewInject(id = R.id.btn, click = "transfer")
    Button btn;

    private UpdateAvatarUtil updateAvatarUtil;//头像上传工具
    //外部jar包：universal-image-loader-1.9.2.jar/异步加载图片
    private DisplayImageOptions imgOption;
    private ImageLoader imgLoader;

    String picpath = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //头像上传的工具类
        updateAvatarUtil = new UpdateAvatarUtil(this, handler, this, false);//实例化
        //全局初始化外部包：个推推送消息使用
        imgLoader = ImageLoader.getInstance();//实例化
        imgLoader.init(ImageLoaderConfiguration.createDefault(this));
        //字义：显示图像生成选项？
        imgOption = ImageLoadingConfig.generateDisplayImageOptionsNoCatchDisc(R.mipmap.photo);

    }

    //点击拍照
    public void imgPhotoClick(View v) {
        //弹窗提示选择拍照类型
        updateAvatarUtil.showChoosePhotoDialog(UpdateAvatarUtil.IMG_TYPE_AVATAR);//-99
    }

    //相机或者相册调用后的回调方法
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        updateAvatarUtil.onActivityResultAction(requestCode, resultCode, data);
    }


    @Override
    //实现UpdateAvatarUtil.ChoosePicCallBack的接口方法（1）
    public void updateAvatarSuccess(int updateType, String picpath, String avatarBase64) {
        if (picpath.toLowerCase().startsWith("http")) {//将字符参数改写成小写的方式
            //外部jar包方法
            ImageLoader.getInstance().displayImage(picpath, //地址
                    imgPhoto, //imageView
                    imgOption);//jar包类
        } else {
            //			imgPhoto.setImageURI(Uri.fromFile(new File(avatar)));
            ImageLoader.getInstance().displayImage("file://" + picpath,
                    imgPhoto, //imageView
                    imgOption);//jar包类
        }
        this.picpath = avatarBase64;
    }

    @Override
    //实现UpdateAvatarUtil.ChoosePicCallBack的接口方法（2）
    public void updateAvatarFailed(int updateType) {
        Toast.makeText(this, "图片上传失败", Toast.LENGTH_SHORT).show();//头像上传失败
    }

    @Override
    //实现UpdateAvatarUtil.ChoosePicCallBack的接口方法（3）
    public void cancel() {

    }

    public void transfer(View view) {
        startActivity(SecondActivity.class);
    }
}
