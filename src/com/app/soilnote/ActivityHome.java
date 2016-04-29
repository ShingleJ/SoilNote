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
	
	private final String[] photoSelect_itemStrings = { "������Ƭ", "���ļ���ѡ��" };
	private static final int TAKE_PHOTO = 1;
	private static final int CHOOSE_PHOTO = 2;
//	private static final int CROP_PHOTO = 3;
	
	private Uri imageUri;
	private String imageFilePath;
	
	// ���ն�λ��أ�ʹ�ðٶȵ�ͼ��λ���ܣ���ʹ��Android�Դ���locationManager��
	private LocationClient mLocationClient;
	private MyLocationListener myLocationListener;
	private double mLatitude, mLongtitude;
	private String humanPosition;
	
	// ���ݿ����
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
	
	// ���ջش�������ڶ���ҳ�����õ���setResult������
	// requestCode ������
	// resultCode ����루�ڵڶ���ҳ����Ҳ�У�
	// data �ش�������
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case CHOOSE_PHOTO:
			if (resultCode == RESULT_OK) {
				try {
					Uri selectedImageUri = data.getData();
					String[] filePathColumns={MediaStore.Images.Media.DATA};
					Cursor c = this.getContentResolver().query(selectedImageUri, filePathColumns, null,null, null);
					//�����������ͷ ���������Ҫ����С�ĺ���������Խ��
					c.moveToFirst();
					int columnIndex = c.getColumnIndex(filePathColumns[0]);
					//����������ֵ��ȡͼƬ·��
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
				//����ϵͳ�ü�
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
	
    //���������saveInfoGeo�ڲ�����ֱ�ӷ��ڵ���¼����棬��γ�ȵ���Ϣ���ǿյģ�����д����������
	private void saveInfoGeo() {
		InfoGeo infoGeo = new InfoGeo();
		infoGeo.setImagePath(imageFilePath);
		infoGeo.setLatitude(mLatitude);
		infoGeo.setLongtitude(mLongtitude);
		infoGeo.setPosition(humanPosition);
		soilNoteDB.saveInfoGeo(infoGeo);
	}
	
	// ���ջ�ȡͼƬ
	protected void doTakePhoto() {
		try {
			Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); // ����ϵͳ���
			// ����ļ�����·��
			imageFilePath = getPhotoPath();
			// ����·��
			imageUri = Uri.fromFile(new File(imageFilePath));
			// ָ���洢·���������Ϳ��Ա���ԭͼ��
			cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
			startActivityForResult(cameraIntent, TAKE_PHOTO);
		} catch (ActivityNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	// �ӱ���ѡ��ͼƬ
	private void doSelectImageFromLoacal() {
		Intent localIntent = new Intent();
		localIntent.setType("image/*");
		localIntent.setAction("android.intent.action.GET_CONTENT");
//		Intent localIntent2 = Intent.createChooser(localIntent, "ѡ��ͼƬ");
		startActivityForResult(localIntent, CHOOSE_PHOTO);
	}
	
	@SuppressLint("SimpleDateFormat") 
	private String getPhotoPath() {
		String mFilePath = "";
		// �������ͼƬ�����·��
		String pathStorage = Environment.getExternalStorageDirectory()
				.getPath() + "/SoilNote/";
		// �����ļ���
		File file = new File(pathStorage);
		if (!file.exists()) {
			file.mkdirs();
		}
		// �ļ�������������
		String name = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
		// �����ļ�·��
		mFilePath = pathStorage + "/" + name + ".jpg";
		return mFilePath;
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
}
