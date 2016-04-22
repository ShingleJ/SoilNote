package com.app.soilnote;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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
import utils.CustomDrawMdl;
import utils.DrawModel;

import android.R.integer;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
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
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TextView.OnEditorActionListener;

public class ActivityEditPhoto extends Activity implements OnClickListener{

	private Bitmap bitMap; // 用来保存图片
	private Bitmap alterBitmap;
	private boolean hasImage; // 是否已经选择了图片

	private Paint paint;
	private Canvas canvas;

	private String imageFilePath;
	private Bitmap bm;

	private Button saveButton, chsModelButton, cusDrawButton, saveEditButton;
	private ImageView img;
	private CustomDrawMdl cusDrawMdl;
	private DrawModel drawModel;

	private Context context;
	
	private final int CHOOSE_MODEL = 0;
	private final int CUSTOM_DRAW = 1;
	private int flag;

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
		this.context = this;
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_edit_photo);
		Intent intent = getIntent();
		imageFilePath = intent.getStringExtra("imageFilePath");
		
		initView();//初始化控件
		
		soilNoteDB = SoilNoteDB.getInstance(this);
		
		initLocation();
	}

	@SuppressLint("CutPasteId") 
	private void initView() {
		saveButton = (Button) findViewById(R.id.id_save);
		chsModelButton = (Button) findViewById(R.id.choose_model);
		cusDrawButton = (Button) findViewById(R.id.custom_draw);
		saveEditButton = (Button) findViewById(R.id.save_edit);
	    img = (ImageView) findViewById(R.id.id_orig_img);
	    ViewTreeObserver vto = img.getViewTreeObserver();  
		vto.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {  
			@Override  
			public void onGlobalLayout() {  
				img.getViewTreeObserver().removeGlobalOnLayoutListener(this);  
				//TODO 判断id是否相同
				bm = BitmapUtils.decodeSampledBitmapFromFile(imageFilePath, img.getWidth(),img.getHeight());
				img.setImageBitmap(bm);
			}  
		}); 
		drawModel = (DrawModel) findViewById(R.id.id_chs_mdl);
		cusDrawMdl = (CustomDrawMdl) findViewById(R.id.id_cus_draw);
		
		saveButton.setOnClickListener(this);
		chsModelButton.setOnClickListener(this);
		cusDrawButton.setOnClickListener(this);
		saveEditButton.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.id_save:
			saveInfoGeo();
			break;
		case R.id.choose_model:
			flag = CHOOSE_MODEL;
			img.setVisibility(View.GONE);
			cusDrawMdl.setVisibility(View.GONE);
			drawModel.setVisibility(View.VISIBLE);
			ViewTreeObserver vto = drawModel.getViewTreeObserver();  
			vto.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {  
				@Override  
				public void onGlobalLayout() {  
					drawModel.getViewTreeObserver().removeGlobalOnLayoutListener(this);  
					//TODO 判断id是否相同
					bm = BitmapUtils.decodeSampledBitmapFromFile(imageFilePath, drawModel.getWidth(),drawModel.getHeight());
					drawModel.setImageBitmap(bm);
				}  
			}); 
			break;
		case R.id.custom_draw:
			flag = CUSTOM_DRAW;
			img.setVisibility(View.GONE);
			drawModel.setVisibility(View.GONE);
			cusDrawMdl.setVisibility(View.VISIBLE);
			 ViewTreeObserver vto1 = cusDrawMdl.getViewTreeObserver();  
			 vto1.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {  
					@Override  
					public void onGlobalLayout() {  
						cusDrawMdl.getViewTreeObserver().removeGlobalOnLayoutListener(this);  
						//TODO 判断id是否相同
						bm = BitmapUtils.decodeSampledBitmapFromFile(imageFilePath, cusDrawMdl.getWidth(),cusDrawMdl.getHeight());
						cusDrawMdl.setImageBitmap(bm);
					}  
				}); 
//			try {
//				bitMap = BitmapFactory.decodeStream(getContentResolver().openInputStream(Uri.fromFile(new File(imageFilePath))));
//			} catch (FileNotFoundException e) {
//				// TODO 自动生成的 catch 块
//				e.printStackTrace();
//			}
////			alterBitmap = Bitmap.createBitmap(cusDrawMdl.getWidth(),
////					cusDrawMdl.getHeight(), bitMap.getConfig());
//			canvas = new Canvas(bm);
//			paint = new Paint();
//			paint.setColor(Color.WHITE);
//			paint.setStrokeWidth(3);
//			Matrix matrix = new Matrix();
//			// 使用指定的matrix绘制位图bitmap，使用canvas绘画，画的东西都存储在了alterBitmap中
//			canvas.drawBitmap(bm, matrix, paint);
//			cusDrawMdl.setImageBitmap(bm);
//			cusDrawMdl.setOnTouchListener(this);
			break;
		case R.id.save_edit:
			saveBitmap();
			break;
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

//	/*
//	 * 绘图相关
//	 */
//	private float downx = 0;
//	private float downy = 0;
//	private float upx = 0;
//	private float upy = 0;
//
//	@Override
//	public boolean onTouch(View v, MotionEvent event) {
//		int action = event.getAction();
//		switch (action) {
//		case MotionEvent.ACTION_DOWN:
//			v.performClick();
//			downx = event.getX();
//			downy = event.getY();
//			canvas.drawPoint(downx, downy, paint);
//			cusDrawMdl.invalidate();
//			break;
//		case MotionEvent.ACTION_MOVE:
//			// 路径画板
//			upx = event.getX();
//			upy = event.getY();
//			canvas.drawLine(downx, downy, upx, upy, paint);
//			cusDrawMdl.invalidate();
//			downx = upx;
//			downy = upy;
//			break;
//		case MotionEvent.ACTION_UP:
//			// 直线画板
//			upx = event.getX();
//			upy = event.getY();
//			canvas.drawLine(downx, downy, upx, upy, paint);
//			cusDrawMdl.invalidate();// 刷新，强制重绘
//			break;
//
//		default:
//			break;
//		}
//		return true;
//	}
	
	 /**  
     * 保存图片到SD卡上  
     */ 
    protected void saveBitmap() {  
        try {  
            // 保存图片到SD卡上  
            File file = new File(imageFilePath + "pro.PNG");  
            FileOutputStream stream = new FileOutputStream(file);  
            //将ImageView中的图片转换成Bitmap
            Bitmap bitmap = null;
            if (flag == CHOOSE_MODEL) {
            	drawModel.buildDrawingCache();
                bitmap = drawModel.getDrawingCache();
			}else if (flag == CUSTOM_DRAW) {
				cusDrawMdl.buildDrawingCache();
				bitmap = cusDrawMdl.getDrawingCache();
			}else {
				img.buildDrawingCache();
				bitmap = img.getDrawingCache();
			}
            bitmap.compress(CompressFormat.PNG, 100, stream);  
            Toast.makeText(this, "保存图片成功", Toast.LENGTH_SHORT).show();  
              
            // Android设备Gallery应用只会在启动的时候扫描系统文件夹  
            // 这里模拟一个媒体装载的广播，用于使保存的图片可以在Gallery中查看  
            Intent intent = new Intent();  
            intent.setAction(Intent.ACTION_MEDIA_MOUNTED);  
            intent.setData(Uri.fromFile(Environment  
                    .getExternalStorageDirectory()));  
            sendBroadcast(intent);  
        } catch (Exception e) {  
            Toast.makeText(this, "保存图片失败", 0).show();  
            e.printStackTrace();  
        }  
    }
}
