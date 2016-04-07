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

	private final String[] photoSelect_itemStrings = { "������Ƭ", "���ļ���ѡ��" };
	private static final int CAMERA_WITH_DATA = 1001;
	private static final int PHOTO_PICKED_WITH_DATA = 1002;

	private Bitmap bitMap; // ��������ͼƬ
	private Bitmap alterBitmap;
	private boolean hasImage; // �Ƿ��Ѿ�ѡ����ͼƬ

	private Paint paint;
	private Canvas canvas;

	private String filePath;

	private Button tkPhotoButton, saveButton, mapButton;
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
		// TODO �Զ����ɵķ������
		switch (v.getId()) {
		case R.id.id_take_photo:
			final AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setItems(photoSelect_itemStrings,
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO �Զ����ɵķ������
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
    //���������saveInfoGeo�ڲ�����ֱ�ӷ��ڵ���¼����棬��γ�ȵ���Ϣ���ǿյģ�����д����������
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
		// TODO �Զ����ɵķ������
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

	// ���ջش�������ڶ���ҳ�����õ���setResult������
	// requestCode ������
	// resultCode ����루�ڵڶ���ҳ����Ҳ�У�
	// data �ش�������
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
				// ����ͼƬ
				// img.setImageBitmap(bitmap);

				// Bitmap bm = (Bitmap)
				// data.getExtras().get("data");//ͨ�����ַ�ʽ�õ���ͼ�������Ǿ���ѹ���ģ�
				// //���Կ�����ͼ����ģ����
				// img.setImageBitmap(bm);//��ͼ����ʾ��ImageView��ͼ��

//				filePath = "";
				// alterBitmap�������½���һ����bitmapͼƬ����һ�µĿհ�BitMap���ͣ����ڴ洢���Ƶ�����
				Toast.makeText(this, filePath, Toast.LENGTH_LONG).show();
				alterBitmap = Bitmap.createBitmap(img.getWidth(),
						img.getHeight(), bitmap.getConfig());
				canvas = new Canvas(alterBitmap);
				paint = new Paint();
				// paint��ָ����
				paint.setColor(Color.WHITE);
				paint.setStrokeWidth(3);
				Matrix matrix = new Matrix();
				// ʹ��ָ����matrix����λͼbitmap��ʹ��canvas�滭�����Ķ������洢����alterBitmap��
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

	// ���ջ�ȡͼƬ
	protected void doTakePhoto() {
		try {
			Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); // ����ϵͳ���
			// ����ļ�����·��
			filePath = getPhotoPath();
			// ����·��
			Uri uri = Uri.fromFile(new File(filePath));
			// ָ���洢·���������Ϳ��Ա���ԭͼ��
			cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
			startActivityForResult(cameraIntent, CAMERA_WITH_DATA);
		} catch (ActivityNotFoundException e) {
			e.printStackTrace();
		}
	}

	// �ӱ���ѡ��ͼƬ
	private void doSelectImageFromLoacal() {
		Intent localIntent = new Intent();
		localIntent.setType("image/*");
		localIntent.setAction("android.intent.action.GET_CONTENT");
		Intent localIntent2 = Intent.createChooser(localIntent, "ѡ��ͼƬ");
		startActivityForResult(localIntent2, PHOTO_PICKED_WITH_DATA);
	}

	private String getPhotoPath() {
		// TODO �Զ����ɵķ������
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
