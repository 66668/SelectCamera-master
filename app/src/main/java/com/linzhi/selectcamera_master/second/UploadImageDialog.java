package com.linzhi.selectcamera_master.second;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;

import com.linzhi.selectcamera_master.R;


/**
 * @Title: UploadImageDialog.java
 * @Description: 弹出上传图片选择对话框 相机或者照相
 * @author lanhaizhong
 * @date 2013年7月23日 下午4:08:15
 * @version V1.0 Copyright (c) 2013 Company,Inc. All Rights Reserved.
 * 
 */
public class UploadImageDialog extends Dialog implements View.OnClickListener {

	private LinearLayout layout_photo;
	private LinearLayout layout_gallery;

	private ClickCallback callback;

	public UploadImageDialog(Context context, ClickCallback callback) {
		super(context, R.style.custom_dialog);
		setContentView(R.layout.dialog_upload);
		layout_photo = (LinearLayout) this.findViewById(R.id.ll_photo);
		layout_photo.setOnClickListener(this);
		layout_gallery = (LinearLayout) this.findViewById(R.id.ll_gallery);
		layout_gallery.setOnClickListener(this);
		this.callback = callback;

		setCanceledOnTouchOutside(false);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setGravity(Gravity.CENTER);

	}

	public interface ClickCallback {
		/**
		 * 点击拍照条目的回调
		 */
		public void PhotoCallback();

		/**
		 * 点击从相册选择的回调
		 */
		public void galleryCallback();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ll_photo:
			callback.PhotoCallback();
			break;
		case R.id.ll_gallery:
			callback.galleryCallback();
			break;

		default:
			break;
		}
	}
}
