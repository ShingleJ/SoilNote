package com.app.soilnote;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import model.DatabaseMdl.InfoGeo;

import org.apache.http.client.entity.UrlEncodedFormEntity;

import utils.BitmapUtils;
import utils.RoundBitmap;
import utils.UriAndString;

import com.app.soilnote.R.attr;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BitmapDescriptorFactory;

import db.SoilNoteDB;

import android.R.integer;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class ActivityHome extends Activity implements OnClickListener{

	private Button takePhotoBn, albumBn, baiduMapBn, learnMoreBn;
	private Bitmap bmp;
	
	private final String[] photoSelect_itemStrings = { "拍摄照片", "从文件中选择" };
	private static final int TAKE_PHOTO = 1;
	private static final int CHOOSE_PHOTO = 2;
//	private static final int CROP_PHOTO = 3;
	
	private Uri imageUri;
	private String imageFilePath;
	
	// 拍照定位相关（使用百度地图定位功能，不使用Android自带的locationManager）
	private LocationClient mLocationClient;
	private MyLocationListener myLocationListener;
	private double mLatitude, mLongtitude;
	private String humanPosition;
	
	// 数据库相关
	private SoilNoteDB soilNoteDB;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_home);
		soilNoteDB = SoilNoteDB.getInstance(this);
		initView();
		initLocation();
	}

	private void initView() {
		bmp = BitmapFactory.decodeResource(getResources(), R.drawable.round);
		takePhotoBn = (Button) findViewById(R.id.take_photo);
		albumBn = (Button) findViewById(R.id.album);
		baiduMapBn = (Button) findViewById(R.id.baidu_map);
		learnMoreBn = (Button) findViewById(R.id.learn_more);
		takePhotoBn.setOnClickListener(this);
		albumBn.setOnClickListener(this);
		baiduMapBn.setOnClickListener(this);
		learnMoreBn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.take_photo:
			doTakePhoto();
			break;
		case R.id.album:
			doSelectImageFromLoacal();
			break;
		case R.id.baidu_map:
			Intent intent = new Intent(this, ActivityBaiduMap.class);
			startActivity(intent);
			break;
		case R.id.learn_more:
			Intent intent2 = new Intent(this, Attribute2Activity.class);
			startActivity(intent2);
			break;
		default:
			break;
		}
	}
	
	// 接收回传结果（第二个页面中用的是setResult方法）
	// requestCode 请求码
	// resultCode 结果码（在第二个页面中也有）
	// data 回传的数据
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case CHOOSE_PHOTO:
			if (resultCode == RESULT_OK) {
				try {
					Uri selectedImageUri = data.getData();
					String[] filePathColumns={MediaStore.Images.Media.DATA};
					Cursor c = this.getContentResolver().query(selectedImageUri, filePathColumns, null,null, null);
					//将光标移至开头 ，这个很重要，不小心很容易引起越界
					c.moveToFirst();
					int columnIndex = c.getColumnIndex(filePathColumns[0]);
					//最后根据索引值获取图片路径
					String selectedImagePath= c.getString(columnIndex);
					c.close();
					Intent intent = new Intent(ActivityHome.this, ActivityEditPhoto.class);
					intent.putExtra("imageFilePath", selectedImagePath);
					startActivity(intent);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			break;
		case TAKE_PHOTO:
			if (resultCode == RESULT_OK) {
				saveInfoGeo();
				Intent intent = new Intent(ActivityHome.this, ActivityEditPhoto.class);
				intent.putExtra("imageFilePath", imageFilePath);
				startActivity(intent);
				//调用系统裁剪
//				Intent intent = new Intent("com.android.camera.action.CROP");
//				intent.setDataAndType(imageUri, "image/*");
//				intent.putExtra("scale", true);
//				intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
//				startActivityForResult(intent, CROP_PHOTO);

			}
			break;
//		case CROP_PHOTO:
//			if (resultCode == RESULT_OK) {
//				Intent intent = new Intent(ActivityHome.this, ActivityEditPhoto.class);
//				intent.putExtra("imageFilePath", imageFilePath);
//				startActivity(intent);
//			}
//			break;
		default:
			break;
		}
	}
	
    //好像如果把saveInfoGeo内部代码直接放在点击事件下面，经纬度等信息都是空的，这样写就能有数据
	private void saveInfoGeo() {
		InfoGeo infoGeo = new InfoGeo();
		infoGeo.setImagePath(imageFilePath);
		infoGeo.setLatitude(mLatitude);
		infoGeo.setLongtitude(mLongtitude);
		infoGeo.setPosition(humanPosition);
		soilNoteDB.saveInfoGeo(infoGeo);
	}
	
	// 拍照获取图片
	protected void doTakePhoto() {
		try {
			Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); // 调用系统相机
			// 获得文件保存路径
			imageFilePath = getPhotoPath();
			// 加载路径
			imageUri = Uri.fromFile(new File(imageFilePath));
			// 指定存储路径，这样就可以保存原图了
			cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
			startActivityForResult(cameraIntent, TAKE_PHOTO);
		} catch (ActivityNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	// 从本地选择图片
	private void doSelectImageFromLoacal() {
		Intent localIntent = new Intent();
		localIntent.setType("image/*");
		localIntent.setAction("android.intent.action.GET_CONTENT");
//		Intent localIntent2 = Intent.createChooser(localIntent, "选择图片");
		startActivityForResult(localIntent, CHOOSE_PHOTO);
	}
	
	@SuppressLint("SimpleDateFormat") 
	private String getPhotoPath() {
		String mFilePath = "";
		// 获得最终图片保存的路径
		String pathStorage = Environment.getExternalStorageDirectory()
				.getPath() + "/SoilNote/";
		// 创建文件夹
		File file = new File(pathStorage);
		if (!file.exists()) {
			file.mkdirs();
		}
		// 文件名以日期命名
		String name = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
		// 返回文件路径
		mFilePath = pathStorage + "/" + name + ".jpg";
		return mFilePath;
	}
	
	/*
	 * 定位相关
	 */
	private void initLocation() {
		mLocationClient = new LocationClient(this);
		myLocationListener = new MyLocationListener();
		mLocationClient.registerLocationListener(myLocationListener);// 注册定位监听器
		// 设置定位的一些属性
		LocationClientOption option = new LocationClientOption();
		option.setCoorType("bd09ll");// 坐标类型
		option.setIsNeedAddress(true);// 返回位置
		option.setOpenGps(true);// 打开GPS
		option.setScanSpan(1000);// 每隔1000秒进行一次请求
		mLocationClient.setLocOption(option);
	}
	
	private class MyLocationListener implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			mLatitude = location.getLatitude();
			mLongtitude = location.getLongitude();
			humanPosition = location.getAddrStr();
		}
	}
	
	@Override
	protected void onStart() {
		// TODO 自动生成的方法存根
		super.onStart();
		if (!mLocationClient.isStarted()) {
			mLocationClient.start();
		}
	}
	
	@Override
	protected void onStop() {
		// TODO 自动生成的方法存根
		super.onStop();
		mLocationClient.stop();
	}
}
