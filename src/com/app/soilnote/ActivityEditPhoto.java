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
import utils.MontainBrownSoil;
import utils.MontainYelloBrownSoil;
import utils.MontainYelloSoilOne;
import utils.MontainYelloSoilThree;
import utils.MontainYelloSoilTwo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class ActivityEditPhoto extends Activity implements OnClickListener{

	private Bitmap bitMap; // ��������ͼƬ
	private Bitmap alterBitmap;
	private boolean hasImage; // �Ƿ��Ѿ�ѡ����ͼƬ

	private String imageFilePath;
	private Bitmap bm;

	private Button saveButton, chsModelButton, cusDrawButton, saveEditButton, cusDrawBackButton;
	private ImageView img;
	private CustomDrawMdl cusDrawMdl;
	private MontainYelloSoilOne montainYelloSoilOne;
	private MontainYelloSoilTwo montainYelloSoilTwo ;
	private MontainBrownSoil montainBrownSoil;
	private MontainYelloSoilThree montainYelloSoilThree;
	private MontainYelloBrownSoil montainYelloBrownSoil;
//	private  ;
	
	private final int CHOOSE_MODEL = 0;
	private final int CUSTOM_DRAW = 1;
	private int flag;

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
//		this.context = this;
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_edit_photo);
		Intent intent = getIntent();
		imageFilePath = intent.getStringExtra("imageFilePath");
		
		initView();//��ʼ���ؼ�
		
		soilNoteDB = SoilNoteDB.getInstance(this);
		
		initLocation();
	}

	@SuppressLint("CutPasteId") 
	private void initView() {
		saveButton = (Button) findViewById(R.id.id_save);
		chsModelButton = (Button) findViewById(R.id.choose_model);
		cusDrawButton = (Button) findViewById(R.id.custom_draw);
		saveEditButton = (Button) findViewById(R.id.save_edit);
		cusDrawBackButton = (Button) findViewById(R.id.save_edit_back);
		
	    img = (ImageView) findViewById(R.id.id_orig_img);
	    ViewTreeObserver vto = img.getViewTreeObserver();  
		vto.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {  
			@Override  
			public void onGlobalLayout() {  
				img.getViewTreeObserver().removeGlobalOnLayoutListener(this);  
				//TODO �ж�id�Ƿ���ͬ
				bm = BitmapUtils.decodeSampledBitmapFromFile(imageFilePath, img.getWidth(),img.getHeight());
				img.setImageBitmap(bm);
			}  
		}); 
//		montainYelloSoilThree = (montainYelloSoilThree) findViewById(R.id.id_chs_mdl);
		montainBrownSoil = (MontainBrownSoil) findViewById(R.id.id_chs_mdl);
		cusDrawMdl = (CustomDrawMdl) findViewById(R.id.id_cus_draw);
		
		saveButton.setOnClickListener(this);
		chsModelButton.setOnClickListener(this);
		cusDrawButton.setOnClickListener(this);
		saveEditButton.setOnClickListener(this);
		cusDrawBackButton.setOnClickListener(this);
	}

	@SuppressLint("NewApi") @Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.id_save:
			saveInfoGeo();
			break;
		case R.id.choose_model:
			flag = CHOOSE_MODEL;
			img.setVisibility(View.GONE);
			cusDrawMdl.setVisibility(View.GONE);
			montainBrownSoil.setVisibility(View.VISIBLE);
			ViewTreeObserver vto = montainBrownSoil.getViewTreeObserver();  
			vto.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {  
				@SuppressLint("NewApi") @Override  
				public void onGlobalLayout() {  
					montainBrownSoil.getViewTreeObserver().removeGlobalOnLayoutListener(this);  
					//TODO �ж�id�Ƿ���ͬ
					bm = BitmapUtils.decodeSampledBitmapFromFile(imageFilePath, montainBrownSoil.getWidth(),montainBrownSoil.getHeight());
//					montainBrownSoil.setImageBitmap(bm);
					//��ͼƬ��Ϊ����
					Drawable drawable = new BitmapDrawable(bm); 
					montainBrownSoil.setBackground(drawable);      
//					montainBrownSoil.setImageResource(R.drawable.transparent_backgroud_frame);
				}  
			}); 
			break;
		case R.id.custom_draw:
			flag = CUSTOM_DRAW;
			img.setVisibility(View.GONE);
			montainBrownSoil.setVisibility(View.GONE);
			cusDrawMdl.setVisibility(View.VISIBLE);
			 ViewTreeObserver vto1 = cusDrawMdl.getViewTreeObserver();  
			 vto1.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {  
					@Override  
					public void onGlobalLayout() {  
						cusDrawMdl.getViewTreeObserver().removeGlobalOnLayoutListener(this);  
						//TODO �ж�id�Ƿ���ͬ
						bm = BitmapUtils.decodeSampledBitmapFromFile(imageFilePath, cusDrawMdl.getWidth(),cusDrawMdl.getHeight());
//						cusDrawMdl.setImageBitmap(bm);
						//��ͼƬ��Ϊ����
						Drawable drawable = new BitmapDrawable(bm); 
						cusDrawMdl.setBackground(drawable);    
						cusDrawMdl.setImageResource(R.drawable.transparent_backgroud_frame);
					}  
				}); 
			break;
		case R.id.save_edit:
			saveBitmap();
			break;
		case R.id.save_edit_back:
			cusDrawMdl.unDo();
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

	 /**  
     * ����ͼƬ��SD����  
     */ 
    protected void saveBitmap() {  
        try {  
            // ����ͼƬ��SD����  
            File file = new File(imageFilePath + "pro.PNG");  
            FileOutputStream stream = new FileOutputStream(file);  
            //��ImageView�е�ͼƬת����Bitmap
            Bitmap bitmap = null;
            if (flag == CHOOSE_MODEL) {
//            	montainBrownSoil.buildDrawingCache();
//                bitmap = montainBrownSoil.getDrawingCache();
            	bitmap = montainBrownSoil.getProModelBitmap();
			}else if (flag == CUSTOM_DRAW) {
				cusDrawMdl.buildDrawingCache();
				bitmap = cusDrawMdl.getDrawingCache();
			}else {
				img.buildDrawingCache();
				bitmap = img.getDrawingCache();
			}
            bitmap.compress(CompressFormat.PNG, 100, stream);  
            Toast.makeText(this, "����ͼƬ�ɹ�", Toast.LENGTH_SHORT).show();  
              
            // Android�豸GalleryӦ��ֻ����������ʱ��ɨ��ϵͳ�ļ���  
            // ����ģ��һ��ý��װ�صĹ㲥������ʹ�����ͼƬ������Gallery�в鿴  
            Intent intent = new Intent();  
            intent.setAction(Intent.ACTION_MEDIA_MOUNTED);  
            intent.setData(Uri.fromFile(Environment  
                    .getExternalStorageDirectory()));  
            sendBroadcast(intent);  
        } catch (Exception e) {  
            Toast.makeText(this, "����ͼƬʧ��", 0).show();  
            e.printStackTrace();  
        }  
    }
}
