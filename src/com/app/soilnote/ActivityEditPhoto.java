package com.app.soilnote;

import java.io.File;
import java.io.FileOutputStream;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

import model.BaseProfileModel;
import model.CustomChooseMdl.FiveLayerCusMdl;
import model.CustomChooseMdl.FourLayerCusMdl;
import model.CustomChooseMdl.SevenLayerCusMdl;
import model.CustomChooseMdl.SixLayerCusMdl;
import model.CustomChooseMdl.ThreeLayerCusMdl;
import model.CustomDrawMdl.CustomDrawMdl;
import model.DatabaseMdl.InfoGeo;
import model.InitProfileModel.MontainBrownSoil;
import model.InitProfileModel.MontainYelloBrownSoil;
import model.InitProfileModel.MontainYelloSoilOne;
import model.InitProfileModel.MontainYelloSoilThree;
import model.InitProfileModel.MontainYelloSoilTwo;

import db.SoilNoteDB;

import utils.BitmapUtils;
import utils.ProfileModelFactory;

import android.R.color;
import android.R.integer;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

public class ActivityEditPhoto extends Activity implements OnClickListener,OnItemSelectedListener{

	private String imageFilePath;
	private Bitmap bm;

	private FrameLayout layout;
	private String[] list = {"ѡ��ģ��", "ɽ�ػ���1","ɽ�ػ���2","ɽ�ػ���3","ɽ�ػ�����","ɽ������"};
	private ArrayAdapter<String> adapter;
	private Spinner chsMdlSp;
	private Button cusDrawButton, saveEditButton, cusDrawBackButton;
	private ImageView img;
	private CustomDrawMdl cusDrawMdl;
	private MontainYelloSoilOne montainYelloSoilOne;
	
	private int[] pro_name = {50,201,40,301,401,20,30};
	
	private final int CHOOSE_MODEL = 0;
	private final int CUSTOM_DRAW = 1;
	private int flag;
	
	private static final int CHOOSE_MODEL_ACTIVITY = 10;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		this.context = this;
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_edit_photo);
		Intent intent = getIntent();
		imageFilePath = intent.getStringExtra("imageFilePath");
		
		
		initView();//��ʼ���ؼ�
	}

	@SuppressLint("CutPasteId") 
	private void initView() {
		chsMdlSp = (Spinner) findViewById(R.id.choose_model);
		// ����Adapter���Ұ�����Դ
		adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, list);
		//�� Adapter���ؼ�
		chsMdlSp.setAdapter(adapter);
				
		cusDrawButton = (Button) findViewById(R.id.custom_draw);
		saveEditButton = (Button) findViewById(R.id.save_edit);
		cusDrawBackButton = (Button) findViewById(R.id.save_edit_back);
		
	    img = (ImageView) findViewById(R.id.id_orig_img);
	    ViewTreeObserver vto = img.getViewTreeObserver();  
		vto.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {  
			@SuppressWarnings("deprecation")
			@Override  
			public void onGlobalLayout() {  
				img.getViewTreeObserver().removeGlobalOnLayoutListener(this);  
				//TODO �ж�id�Ƿ���ͬ
				bm = BitmapUtils.decodeSampledBitmapFromFile(imageFilePath, img.getWidth(),img.getHeight());
				img.setImageBitmap(bm);
			}  
		}); 
//		montainYelloSoilThree = (montainYelloSoilThree) findViewById(R.id.id_chs_mdl);
//		sevenLayerCusMdl.setPro_name(pro_name);
		cusDrawMdl = (CustomDrawMdl) findViewById(R.id.id_cus_draw);
		layout = (FrameLayout) findViewById(R.id.layout_chs_mdl);
		
		chsMdlSp.setOnItemSelectedListener(this);
		cusDrawButton.setOnClickListener(this);
		saveEditButton.setOnClickListener(this);
		cusDrawBackButton.setOnClickListener(this);
	}

	@SuppressLint("NewApi") @Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.custom_draw:
			flag = CUSTOM_DRAW;
			img.setVisibility(View.GONE);
			montainYelloSoilOne.setVisibility(View.GONE);
			cusDrawMdl.setVisibility(View.VISIBLE);
			 ViewTreeObserver vto1 = cusDrawMdl.getViewTreeObserver();  
			 vto1.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {  
					@SuppressWarnings("deprecation")
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
//			cusDrawMdl.unDo();
			montainYelloSoilOne.deleteProfile(1);
		default:
			break;
		}
	}

	private void showProfileModel(final BaseProfileModel base) {
		ViewTreeObserver vto = base.getViewTreeObserver();  
		vto.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {  
			@SuppressWarnings("deprecation")
			@SuppressLint("NewApi") @Override  
			public void onGlobalLayout() {  
				base.getViewTreeObserver().removeGlobalOnLayoutListener(this);  
				//TODO �ж�id�Ƿ���ͬ
				bm = BitmapUtils.decodeSampledBitmapFromFile(imageFilePath, base.getWidth(),base.getHeight());
				//��ͼƬ��Ϊ����
				Drawable drawable = new BitmapDrawable(bm); 
				base.setBackground(drawable);      
			}  
		});
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
//            	sevenLayerCusMdl.buildDrawingCache();
//                bitmap = sevenLayerCusMdl.getDrawingCache();
            	bitmap = montainYelloSoilOne.getProModelBitmap();
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

	@SuppressLint("ShowToast") 
	@Override
	public void onItemSelected(AdapterView<?> parent, View arg1, int position,
			long arg3) {
		flag = CHOOSE_MODEL;
		if (position == 0) {
			img.setVisibility(View.VISIBLE);
			cusDrawMdl.setVisibility(View.GONE);
		}else {
			img.setVisibility(View.GONE);
			cusDrawMdl.setVisibility(View.GONE);
			layout.setVisibility(View.VISIBLE);
			switch (position) {
			case 1:
				layout.removeAllViews();
				MontainYelloSoilOne one = new MontainYelloSoilOne(this);
				one.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
				showProfileModel(one);
				layout.addView(one);
				break;
			case 2:
				layout.removeAllViews();
				MontainYelloSoilTwo two = new MontainYelloSoilTwo(this);
				two.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
				showProfileModel(two);
				layout.addView(two);
				break;
			case 3:
				layout.removeAllViews();
				MontainYelloSoilThree three = new MontainYelloSoilThree(this);
				three.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
				showProfileModel(three);
				layout.addView(three);
				break;
			case 4:
				layout.removeAllViews();
				MontainYelloBrownSoil four = new MontainYelloBrownSoil(this);
				four.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
				showProfileModel(four);
				layout.addView(four);
				break;
			case 5:
				layout.removeAllViews();
				MontainBrownSoil five = new MontainBrownSoil(this);
				five.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
				showProfileModel(five);
				layout.addView(five);
				break;
			default:
				break;
			}
			
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO �Զ����ɵķ������
		
	}
}
