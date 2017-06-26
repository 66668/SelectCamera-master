package com.linzhi.selectcamera_master.other;

import android.content.Context;

import org.json.JSONObject;

public class Loading {
	//登录的耗时操作运行，
	public static LoadingDialog run(Context context, boolean  cancelable, final Runnable runnable) {
		//弹窗显示登录状态
		final LoadingDialog loadingDialog = new LoadingDialog(context);//登录弹窗提示
		loadingDialog.setCanceledOnTouchOutside(false);//点击弹窗之外的界面，弹窗不消失
		loadingDialog.setCancelable(cancelable);//true:可以按返回键back取消
		loadingDialog.show();
		//异步线程处理登录状态
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					runnable.run();
				} finally {
					//运行完后，弹窗取消
					loadingDialog.dismiss();
				}
			}
		}).start();
		return loadingDialog;
	} 
	//赋值，跳转上方法运行
	public static LoadingDialog run(Context context, final Runnable runnable) {
		 return run(context, true, runnable);
	} 

	public interface HttpResult {
		void onSuccess(JSONObject result); 
		void onError(Exception e);
	}
}
