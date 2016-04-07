package com.app.soilnote;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

import model.InfoGeo;

import db.SoilNoteDB;

import utils.BitmapUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TextView.OnEditorActionListener;

public class ActivityHome extends Activity implements OnClickListener,
		OnTouchListener {

	private final String[] photoSelect_itemStrings = { "拍摄照片", "从文件中选择" };
	private static final int CAMERA_WITH_DATA = 1001;
	private static final int PHOTO_PICKED_WITH_DATA = 1002;

	private Bitmap bitMap; // 用来保存图片
	private Bitmap alterBitmap;
	private boolean hasImage; // 是否已经选择了图片

	private Paint paint;
	private Canvas canvas;

	private String filePath;

	private Button tkPhotoButton, saveButton, mapButton;
	private ImageView img;

	private Context context;

	// 拍照定位相关（使用百度地图定位功能，不使用Android自带的locationManager）
	private LocationClient mLocationClient;
	private MyLocationListener myLocationListener;
	private double mLatitude, mLongtitude;
	private String humanPosition;
	private int photo_id = 0;

	// 数据库相关
	private SoilNoteDB soilNoteDB;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.context = this;
		setContentView(R.layout.activity_home);

		tkPhotoButton = (Button) findViewById(R.id.id_take_photo);
		saveButton = (Button) findViewById(R.id.id_save);
		mapButton = (Button) findViewById(R.id.map);
		img = (ImageView) findViewById(R.id.imageView1);

		// img.setOnTouchListener(this);
		tkPhotoButton.setOnClickListener(this);
		saveButton.setOnClickListener(this);
		mapButton.setOnClickListener(this);
		soilNoteDB = SoilNoteDB.getInstance(this);

		initLocation();
	}

	@Override
	public void onClick(View v) {
		// TODO 自动生成的方法存根
		switch (v.getId()) {
		case R.id.id_take_photo:
			final AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setItems(photoSelect_itemStrings,
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO 自动生成的方法存根
							if (which == 0) {
								doTakePhoto();
							} else {
								doSelectImageFromLoacal();
							}
						}
					});
			AlertDialog adDialog = builder.create();
			adDialog.show();
			break;
		case R.id.map:
			Intent intent = new Intent(this, ActivityBaiduMap.class);
			startActivity(intent);
			break;
		case R.id.id_save:
			saveInfoGeo();
			break;
		default:
			break;
		}
	}
    //好像如果把saveInfoGeo内部代码直接放在点击事件下面，经纬度等信息都是空的，这样写就能有数据
	private void saveInfoGeo() {
		photo_id++;
		InfoGeo infoGeo = new InfoGeo();
		infoGeo.setId(photo_id);
		infoGeo.setImagePath(filePath);
		infoGeo.setLatitude(mLatitude);
		infoGeo.setLongtitude(mLongtitude);
		infoGeo.setPosition(humanPosition);
		soilNoteDB.saveInfoGeo(infoGeo);
//		List<InfoGeo> list = new ArrayList<InfoGeo>();
//		list = soilNoteDB.loadInfoGeo();
//		InfoGeo line = list.get(photo_id);
//		Toast.makeText(this, "第一条数据是：id:"+line.getId()+"文件路径："+line.getImagePath()+
//				"经度："+line.getLatitude()+"纬度："+line.getLongtitude()+
//				"地址："+line.getPosition(),Toast.LENGTH_LONG).show();
		Toast.makeText(context, photo_id+"", Toast.LENGTH_LONG).show();
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

	/*
	 * 绘图相关
	 */
	private float downx = 0;
	private float downy = 0;
	private float upx = 0;
	private float upy = 0;

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO 自动生成的方法存根
		int action = event.getAction();
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			v.performClick();
			downx = event.getX();
			downy = event.getY();
			canvas.drawPoint(downx, downy, paint);
			img.invalidate();
			break;
		case MotionEvent.ACTION_MOVE:
			// 路径画板
			upx = event.getX();
			upy = event.getY();
			canvas.drawLine(downx, downy, upx, upy, paint);
			img.invalidate();
			downx = upx;
			downy = upy;
			break;
		case MotionEvent.ACTION_UP:
			// 直线画板

			upx = event.getX();
			upy = event.getY();
			canvas.drawLine(downx, downy, upx, upy, paint);
			img.invalidate();// 刷新，强制重绘
			break;

		default:
			break;
		}
		return true;
	}

	// 接收回传结果（第二个页面中用的是setResult方法）
	// requestCode 请求码
	// resultCode 结果码（在第二个页面中也有）
	// data 回传的数据
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case PHOTO_PICKED_WITH_DATA:
			if (resultCode == RESULT_OK) {
				if (bitMap != null && !bitMap.isRecycled()) {
					bitMap.recycle();
				}
				Uri selectedImageUri = data.getData();
				if (selectedImageUri != null) {
					// bitMap =
					// BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImageUri));
					bitMap = BitmapUtils.decodeSampledBitmapFromFile(
							getRealPathFromURI(selectedImageUri),
							img.getWidth(), img.getHeight());
					img.setImageBitmap(bitMap);
				}
			}
			break;
		case CAMERA_WITH_DATA:
			if (resultCode == RESULT_OK) {
				Bitmap bitmap = BitmapUtils.decodeSampledBitmapFromFile(
						filePath, img.getWidth(), img.getHeight());
				// 设置图片
				// img.setImageBitmap(bitmap);

				// Bitmap bm = (Bitmap)
				// data.getExtras().get("data");//通过这种方式得到的图像数据是经过压缩的，
				// //所以看到的图像是模糊的
				// img.setImageBitmap(bm);//将图像显示在ImageView视图上

//				filePath = "";
				// alterBitmap变量是新建的一个与bitmap图片长宽一致的空白BitMap类型，用于存储绘制的像素
				Toast.makeText(this, filePath, Toast.LENGTH_LONG).show();
				alterBitmap = Bitmap.createBitmap(img.getWidth(),
						img.getHeight(), bitmap.getConfig());
				canvas = new Canvas(alterBitmap);
				paint = new Paint();
				// paint是指画笔
				paint.setColor(Color.WHITE);
				paint.setStrokeWidth(3);
				Matrix matrix = new Matrix();
				// 使用指定的matrix绘制位图bitmap，使用canvas绘画，画的东西都存储在了alterBitmap中
				canvas.drawBitmap(bitmap, matrix, paint);
				canvas.drawLine(0, bitmap.getHeight() * 0.25f,
						bitmap.getWidth(), bitmap.getHeight() * 0.25f, paint);
				canvas.drawLine(0, bitmap.getHeight() * 0.5f,
						bitmap.getWidth(), bitmap.getHeight() * 0.5f, paint);
				canvas.drawLine(0, bitmap.getHeight() * 0.75f,
						bitmap.getWidth(), bitmap.getHeight() * 0.75f, paint);
				img.setImageBitmap(alterBitmap);
				img.setOnTouchListener(this);
			}
			break;
		default:
			break;
		}
	}

	// 拍照获取图片
	protected void doTakePhoto() {
		try {
			Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); // 调用系统相机
			// 获得文件保存路径
			filePath = getPhotoPath();
			// 加载路径
			Uri uri = Uri.fromFile(new File(filePath));
			// 指定存储路径，这样就可以保存原图了
			cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
			startActivityForResult(cameraIntent, CAMERA_WITH_DATA);
		} catch (ActivityNotFoundException e) {
			e.printStackTrace();
		}
	}

	// 从本地选择图片
	private void doSelectImageFromLoacal() {
		Intent localIntent = new Intent();
		localIntent.setType("image/*");
		localIntent.setAction("android.intent.action.GET_CONTENT");
		Intent localIntent2 = Intent.createChooser(localIntent, "选择图片");
		startActivityForResult(localIntent2, PHOTO_PICKED_WITH_DATA);
	}

	private String getPhotoPath() {
		// TODO 自动生成的方法存根
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

	// 根据uri获得文件路径
	private String getRealPathFromURI(Uri contentUri) {
		// TODO 自动生成的方法存根
		String res = null;
		String[] proj = { MediaStore.Images.Media.DATA };
		Cursor cursor = getContentResolver().query(contentUri, proj, null,
				null, null);
		if (cursor.moveToFirst()) {
			;
			int column_index = cursor
					.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			res = cursor.getString(column_index);
		}
		cursor.close();
		return res;
	}

}
