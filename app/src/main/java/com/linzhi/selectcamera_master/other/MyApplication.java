package com.linzhi.selectcamera_master.other;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Environment;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import java.io.File;

public class MyApplication extends android.app.Application {

	private static MyApplication instance;
	private Context currentContext;
	private final static String sdcardDirName = "VFACE";
	boolean isLogin = false;
	// MainActivity--MyApplication该方法

	public MyApplication() {
		super();
	}

	public void onCreate() {
		super.onCreate();
		instance = this;
		currentContext = this.getApplicationContext();
		initImageLoader(this);
	}

	public void onLowMemory() {
		super.onLowMemory();
		System.gc();
	}

	@Override
	public void onTerminate() {
		super.onTerminate();
	}

	public static String getAppFilesPath() {
		return instance.getFilesDir().getAbsolutePath();
	}

	// LoginActivity/GetBackPwdActivity/MainActivity
	public static MyApplication getInstance() {
		return instance;
	}

	public Context getCurrentContext() {
		return currentContext;
	}
	private void initImageLoader(Context context) {

		File cacheDir = new File(getPicCachePath(context));

		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
				.threadPriority(Thread.NORM_PRIORITY - 2).denyCacheImageMultipleSizesInMemory()
				.diskCacheFileNameGenerator(new Md5FileNameGenerator()).tasksProcessingOrder(QueueProcessingType.LIFO)
				.diskCache(new UnlimitedDiscCache(cacheDir))
				// .writeDebugLogs() // Remove for release app
				.build();
		ImageLoader.getInstance().init(config);
	}
	public int getVersionCode() {
		int i;
		try {
			i = getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;

		} catch (PackageManager.NameNotFoundException localNameNotFoundException) {
			i = -1;
		}
		return i;
	}

	public String getVersionName() {
		try {
			return getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
		} catch (PackageManager.NameNotFoundException localNameNotFoundException) {
			return "";
		}
	}



	//
	public static String getBaseDir(Context context) {
		// (4)获取sd卡根路径
		String sdcard_base_path = null;
		long availableSDCardSpace = Utils.getExternalStorageSpace();// 获取SD卡可用空间
		if (availableSDCardSpace != -1L) {// 如果存在SD卡/-1L:没有SD卡
			// sd/VFACE
			sdcard_base_path = Environment.getExternalStorageDirectory() + File.separator + sdcardDirName;
		} else if (Utils.getInternalStorageSpace() != -1L) {
			// VFACE
			sdcard_base_path = context.getFilesDir().getPath() + File.separator + sdcardDirName;
		} else {// sd卡不存在
			// 没有可写入位置
		}
		if (sdcard_base_path != null) {
			// 初始化根目录
			File basePath = new File(sdcard_base_path);
			if (!basePath.exists()) {
				basePath.mkdir();
				// sjy
				// 创建.nomedia文件,应用中的图片不被系统图库扫描
				// File nomedia = new File(sdcard_base_path, ".nomedia");
				// if (!nomedia.exists()) {
				// try {
				// nomedia.createNewFile();
				// } catch (IOException e) {
				// e.printStackTrace();
				// }
				// }
			}
		}
		return sdcard_base_path;
	}

	// 获取图片缓存目录
	//MainActivity--MyApplication该方法
	public static String getPicCachePath(Context context) {
		// (3)VFACE/tempPics
		String cachePicPath = getBaseDir(context) + File.separator + "tempPics";
		File cachePath = new File(cachePicPath);
		if (!cachePath.exists()) {
			cachePath.mkdir();
		}
		return cachePicPath;
	}

	// 图片上传目录
	public static String getUploadPicPath(Context context) {
		// (2)VFACE/tempPics/uploadTemp
		String uploadPath = getPicCachePath(context) + File.separator + "uploadTemp";
		File uploadDir = new File(uploadPath);
		if (!uploadDir.exists()) {
			uploadDir.mkdir();
		}
		return uploadPath;
	}

	// 头像处理路径
	// CreateUserActivity--UpdateAvatar--MyApplication该方法：拍照图片选择
	public static String getUnhandledUserPhotoPath(Context context) {
		// （1）VFACE/tempPics/uploadTemp/unhandled.jpg
		String path = getUploadPicPath(context) + File.separator + "unhandled.jpg";
		return path;
	}

	// 头像上传路径
	public static String getHandledUserPhotoPath(Context context) {

		return getUploadPicPath(context) + File.separator + "handled.jpg";
	}

	// MainActivity--MyApplication该方法，判断是否登录
	public boolean isLogin() {
		return isLogin;
	}

	// 登录成功赋值 true LoginActivity---该方法
	public void setIsLogin(boolean b) {
		isLogin = b;
	}

}
