package com.app.soilnote;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.http.client.entity.UrlEncodedFormEntity;

import utils.BitmapUtils;
import utils.RoundBitmap;

import com.baidu.mapapi.map.BitmapDescriptorFactory;

import db.SoilNoteDB;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
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

	private Button takePhotoBn, baiduMapBn, learnMoreBn;
	private Bitmap bmp;
	
	private final String[] photoSelect_itemStrings = { "������Ƭ", "���ļ���ѡ��" };
	private static final int TAKE_PHOTO = 1;
	private static final int CHOOSE_PHOTO = 2;
	
	private Uri imageUri;
	private String imageFilePath;
	
	// ���ݿ����
	private SoilNoteDB soilNoteDB;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_home);
		soilNoteDB = SoilNoteDB.getInstance(this);
		init();
	}

	private void init() {
		bmp = BitmapFactory.decodeResource(getResources(), R.drawable.round);
		takePhotoBn = (Button) findViewById(R.id.take_photo);
		baiduMapBn = (Button) findViewById(R.id.baidu_map);
		learnMoreBn = (Button) findViewById(R.id.learn_more);
		takePhotoBn.setOnClickListener(this);
		baiduMapBn.setOnClickListener(this);
		learnMoreBn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.take_photo:
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
		case R.id.baidu_map:
			Intent intent = new Intent(this, ActivityBaiduMap.class);
			startActivity(intent);
			break;
		case R.id.learn_more:
			Intent intent2 = new Intent(this, DrawTestActivity.class);
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
//				if (bitMap != null && !bitMap.isRecycled()) {
//					bitMap.recycle();
//				}
//				Uri selectedImageUri = data.getData();
//				if (selectedImageUri != null) {
//					// bitMap =
//					// BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImageUri));
//					bitMap = BitmapUtils.decodeSampledBitmapFromFile(
//							getRealPathFromURI(selectedImageUri),
//							img.getWidth(), img.getHeight());
//					img.setImageBitmap(bitMap);
//				}
			}
			break;
		case TAKE_PHOTO:
			if (resultCode == RESULT_OK) {
				Intent intent = new Intent(ActivityHome.this, ActivityEditPhoto.class);
				intent.putExtra("imageFilePath", imageFilePath);
				startActivity(intent);
//				Bitmap bitmap = BitmapUtils.decodeSampledBitmapFromFile(
//						filePath, img.getWidth(), img.getHeight());
				// ����ͼƬ
				// img.setImageBitmap(bitmap);

				// Bitmap bm = (Bitmap)
				// data.getExtras().get("data");//ͨ�����ַ�ʽ�õ���ͼ�������Ǿ���ѹ���ģ�
				// //���Կ�����ͼ����ģ����
				// img.setImageBitmap(bm);//��ͼ����ʾ��ImageView��ͼ��

//				filePath = "";
				// alterBitmap�������½���һ����bitmapͼƬ����һ�µĿհ�BitMap���ͣ����ڴ洢���Ƶ�����
				
//				Toast.makeText(this, filePath, Toast.LENGTH_LONG).show();
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
		Intent localIntent2 = Intent.createChooser(localIntent, "ѡ��ͼƬ");
		startActivityForResult(localIntent2, TAKE_PHOTO);
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
}
