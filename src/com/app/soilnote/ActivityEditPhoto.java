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

public class ActivityEditPhoto extends Activity implements OnClickListener,
		OnTouchListener {

	private Bitmap bitMap; // ��������ͼƬ
	private Bitmap alterBitmap;
	private boolean hasImage; // �Ƿ��Ѿ�ѡ����ͼƬ

	private Paint paint;
	private Canvas canvas;

	private String imageFilePath;

	private Button saveButton, chsModelButton, cusDrawButton;
	private ImageView img;

	private Context context;

	// ���ն�λ��أ�ʹ�ðٶȵ�ͼ��λ���ܣ���ʹ��Android�Դ���locationManager��
	private LocationClient mLocationClient;
	private MyLocationListener myLocationListener;
	private double mLatitude, mLongtitude;
	private String humanPosition;
	private int photo_id = 0;

	// ���ݿ����
	private SoilNoteDB soilNoteDB;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.context = this;
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_edit_photo);
		
		initView();//��ʼ���ؼ�
		
		soilNoteDB = SoilNoteDB.getInstance(this);
		
		Intent intent = getIntent();
		imageFilePath = intent.getStringExtra("imageFilePath");

		initLocation();
	}

	private void initView() {
		saveButton = (Button) findViewById(R.id.id_save);
		chsModelButton = (Button) findViewById(R.id.choose_model);
		cusDrawButton = (Button) findViewById(R.id.custom_draw);
	
		img = (ImageView) findViewById(R.id.imageView1);
		
		ViewTreeObserver vto = img.getViewTreeObserver();  
		vto.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {  
			@Override  
			public void onGlobalLayout() {  
				img.getViewTreeObserver().removeGlobalOnLayoutListener(this);  
				//TODO �ж�id�Ƿ���ͬ
				Bitmap bm = BitmapUtils.decodeSampledBitmapFromFile(imageFilePath, img.getWidth(),img.getHeight());
				img.setImageBitmap(bm);
			}  
		});  
		
		saveButton.setOnClickListener(this);
		chsModelButton.setOnClickListener(this);
		cusDrawButton.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.id_save:
			saveInfoGeo();
			break;
		case R.id.choose_model:
			
			break;
		case R.id.custom_draw:
			img.setOnTouchListener(this);
			break;
		default:
			break;
		}
	}
    //���������saveInfoGeo�ڲ�����ֱ�ӷ��ڵ���¼����棬��γ�ȵ���Ϣ���ǿյģ�����д����������
	private void saveInfoGeo() {
		photo_id++;
		InfoGeo infoGeo = new InfoGeo();
		infoGeo.setId(photo_id);
		infoGeo.setImagePath(imageFilePath);
		infoGeo.setLatitude(mLatitude);
		infoGeo.setLongtitude(mLongtitude);
		infoGeo.setPosition(humanPosition);
		soilNoteDB.saveInfoGeo(infoGeo);
//		List<InfoGeo> list = new ArrayList<InfoGeo>();
//		list = soilNoteDB.loadInfoGeo();
//		InfoGeo line = list.get(photo_id);
//		Toast.makeText(this, "��һ�������ǣ�id:"+line.getId()+"�ļ�·����"+line.getImagePath()+
//				"���ȣ�"+line.getLatitude()+"γ�ȣ�"+line.getLongtitude()+
//				"��ַ��"+line.getPosition(),Toast.LENGTH_LONG).show();
		Toast.makeText(context, photo_id+"", Toast.LENGTH_LONG).show();
	}

	/*
	 * ��λ���
	 */
	private void initLocation() {
		mLocationClient = new LocationClient(this);
		myLocationListener = new MyLocationListener();
		mLocationClient.registerLocationListener(myLocationListener);// ע�ᶨλ������
		// ���ö�λ��һЩ����
		LocationClientOption option = new LocationClientOption();
		option.setCoorType("bd09ll");// ��������
		option.setIsNeedAddress(true);// ����λ��
		option.setOpenGps(true);// ��GPS
		option.setScanSpan(1000);// ÿ��1000�����һ������
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
		// TODO �Զ����ɵķ������
		super.onStart();
		if (!mLocationClient.isStarted()) {
			mLocationClient.start();
		}
	}
	
	@Override
	protected void onStop() {
		// TODO �Զ����ɵķ������
		super.onStop();
		mLocationClient.stop();
	}

	/*
	 * ��ͼ���
	 */
	private float downx = 0;
	private float downy = 0;
	private float upx = 0;
	private float upy = 0;

	@Override
	public boolean onTouch(View v, MotionEvent event) {
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
			// ·������
			upx = event.getX();
			upy = event.getY();
			canvas.drawLine(downx, downy, upx, upy, paint);
			img.invalidate();
			downx = upx;
			downy = upy;
			break;
		case MotionEvent.ACTION_UP:
			// ֱ�߻���

			upx = event.getX();
			upy = event.getY();
			canvas.drawLine(downx, downy, upx, upy, paint);
			img.invalidate();// ˢ�£�ǿ���ػ�
			break;

		default:
			break;
		}
		return true;
	}
//				alterBitmap = Bitmap.createBitmap(img.getWidth(),
//						img.getHeight(), bitmap.getConfig());
//				canvas = new Canvas(alterBitmap);
//				paint = new Paint();
//				// paint��ָ����
//				paint.setColor(Color.WHITE);
//				paint.setStrokeWidth(3);
//				Matrix matrix = new Matrix();
//				// ʹ��ָ����matrix����λͼbitmap��ʹ��canvas�滭�����Ķ������洢����alterBitmap��
//				canvas.drawBitmap(bitmap, matrix, paint);
//				canvas.drawLine(0, bitmap.getHeight() * 0.25f,
//						bitmap.getWidth(), bitmap.getHeight() * 0.25f, paint);
//				canvas.drawLine(0, bitmap.getHeight() * 0.5f,
//						bitmap.getWidth(), bitmap.getHeight() * 0.5f, paint);
//				canvas.drawLine(0, bitmap.getHeight() * 0.75f,
//						bitmap.getWidth(), bitmap.getHeight() * 0.75f, paint);
//				img.setImageBitmap(alterBitmap);
//				img.setOnTouchListener(this);

	// ����uri����ļ�·��
	private String getRealPathFromURI(Uri contentUri) {
		// TODO �Զ����ɵķ������
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
