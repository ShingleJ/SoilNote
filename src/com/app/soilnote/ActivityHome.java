package com.app.soilnote;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.baidu.location.LocationClientOption.LocationMode;

import utils.BitmapUtils;

import android.R.integer;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
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


public class ActivityHome extends Activity implements OnClickListener, OnTouchListener{

	private final String[]photoSelect_itemStrings={"������Ƭ","���ļ���ѡ��"};
	private static final int CAMERA_WITH_DATA = 1001;
	private static final int PHOTO_PICKED_WITH_DATA = 1002;
    
	private Bitmap bitMap;       //��������ͼƬ
    private Bitmap alterBitmap;
	private boolean hasImage;    //�Ƿ��Ѿ�ѡ����ͼƬ
	
    private Paint paint;
    private Canvas canvas;
	
	private String filePath;
	
	private Button button, mapButton;
	private ImageView img;
	
	//���ն�λ���
	private LocationManager mLocationManager;
	private String mProvider; 
	private TextView positionTextView;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        button = (Button) findViewById(R.id.button1);
        mapButton = (Button) findViewById(R.id.map);
        img = (ImageView) findViewById(R.id.imageView1);

//        img.setOnTouchListener(this);
        button.setOnClickListener(this);
        mapButton.setOnClickListener(this);
        
        initLocation();

    }
    
	private void initLocation() {
		positionTextView = (TextView) findViewById(R.id.id_show_latlng);
		mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		//��ȡ���п��õ�λ���ṩ��
		List<String> providerList = mLocationManager.getProviders(true);
		if (providerList.contains(LocationManager.GPS_PROVIDER)) {
			mProvider = LocationManager.GPS_PROVIDER;
		}else if (providerList.contains(LocationManager.NETWORK_PROVIDER)) {
			mProvider = LocationManager.NETWORK_PROVIDER;
		}else {
			//��û�п��õ�λ���ṩ��ʱ������Toast��ʾ�û�
			Toast.makeText(this, "No location provider to use", Toast.LENGTH_SHORT).show();
			return;
		}
	}

	@Override
	public void onClick(View v) {
		// TODO �Զ����ɵķ������
		switch (v.getId()) {
		case R.id.button1:
			final AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setItems(photoSelect_itemStrings, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO �Զ����ɵķ������
					if (which==0)
					{
						doTakePhoto();
					}
					else {
						doSelectImageFromLoacal();
					}
				}
			});
			AlertDialog adDialog=builder.create();
			adDialog.show();
			break;
		case R.id.map:
			Intent intent = new Intent(this, ActivityBaiduMap.class);
			startActivity(intent);
			break;
		default:
			break;
		}
		
	}

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
            downx = event.getX();
            downy = event.getY();
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
	
    //���ջش�������ڶ���ҳ�����õ���setResult������
    //requestCode  ������
    //resultCode   ����루�ڵڶ���ҳ����Ҳ�У�
    //data �ش�������
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
		case PHOTO_PICKED_WITH_DATA:
			if (resultCode == RESULT_OK) {
				if (bitMap != null && !bitMap.isRecycled()) {      
					bitMap.recycle();      
				}      
				Uri selectedImageUri = data.getData();   			
				if(selectedImageUri != null){           
//						bitMap = BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImageUri));      
					bitMap = BitmapUtils.decodeSampledBitmapFromFile(getRealPathFromURI(selectedImageUri), img.getWidth(), img.getHeight());
					img.setImageBitmap(bitMap);
              	}
			}
			break;
		case CAMERA_WITH_DATA:
			if (resultCode == RESULT_OK) {
            	Bitmap bitmap = BitmapUtils.decodeSampledBitmapFromFile(filePath, img.getWidth(), img.getHeight());
                // ����ͼƬ
//                img.setImageBitmap(bitmap);
                
//              Bitmap bm = (Bitmap) data.getExtras().get("data");//ͨ�����ַ�ʽ�õ���ͼ�������Ǿ���ѹ���ģ�
//              														//���Կ�����ͼ����ģ����
//              img.setImageBitmap(bm);//��ͼ����ʾ��ImageView��ͼ��

	            filePath = "";
	            //alterBitmap�������½���һ����bitmapͼƬ����һ�µĿհ�BitMap���ͣ����ڴ洢���Ƶ�����
	            alterBitmap = Bitmap.createBitmap(img.getWidth(),
	                    img.getHeight(), bitmap.getConfig());
	            canvas = new Canvas(alterBitmap);
	            paint = new Paint();
	            //paint��ָ����
	            paint.setColor(Color.BLACK);
	            paint.setStrokeWidth(3);
	            Matrix matrix = new Matrix();
	            //ʹ��ָ����matrix����λͼbitmap��ʹ��canvas�滭�����Ķ������洢����alterBitmap��
	            canvas.drawBitmap(bitmap, matrix, paint);
	            canvas.drawLine(0, bitmap.getHeight()*0.25f, bitmap.getWidth(), bitmap.getHeight()*0.25f, paint);
                canvas.drawLine(0, bitmap.getHeight()*0.5f, bitmap.getWidth(), bitmap.getHeight()*0.5f, paint);
                canvas.drawLine(0, bitmap.getHeight()*0.75f, bitmap.getWidth(), bitmap.getHeight()*0.75f, paint);
	            img.setImageBitmap(alterBitmap);
	            img.setOnTouchListener(this);
			}
			break;
		default:
			break;
		} 
    }
	
    //���ջ�ȡͼƬ
	protected void doTakePhoto() {      
        try {      
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); //����ϵͳ���  
            Location mLocation = mLocationManager.getLastKnownLocation(mProvider);
            if (mLocation != null) {
				//��ʾ��ǰ����ʱ��λ����Ϣ
            	showLocation(mLocation);
			}
            mLocationManager.requestLocationUpdates(mProvider, 5000, 1, mLocationListener);
            
            //����ļ�����·��
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
	
	LocationListener mLocationListener = new LocationListener() {
		
		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO �Զ����ɵķ������
			
		}
		
		@Override
		public void onProviderEnabled(String provider) {
			// TODO �Զ����ɵķ������
			
		}
		
		@Override
		public void onProviderDisabled(String provider) {
			// TODO �Զ����ɵķ������
			
		}
		
		@Override
		public void onLocationChanged(Location location) {
			//���µ�ǰ�豸��λ����Ϣ
			showLocation(location);
		}
	};
	
	protected void onDestroy() {
		super.onDestroy();
		if (mLocationManager != null) {
			//�رճ���ʱ���������Ƴ�
			mLocationManager.removeUpdates(mLocationListener);
		}
	};
		
	private void showLocation(Location mLocation) {
		String currentPosition = "�����ǣ�" + mLocation.getLatitude() + 
				"\n" + "γ���ǣ�" + mLocation.getLongitude();
		positionTextView.setText(currentPosition);
		
	}

	//�ӱ���ѡ��ͼƬ
	private void doSelectImageFromLoacal(){      
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
        String pathStorage = Environment.getExternalStorageDirectory().getPath() + "/SoilNote/";
        //�����ļ���
        File file = new File(pathStorage);
        if(!file.exists()){
        	file.mkdirs();
        }
        //�ļ�������������
        String name = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
        //�����ļ�·��
        mFilePath = pathStorage + "/" + name+ ".jpg";
		return mFilePath;
	}
	
    
    //����uri����ļ�·��
	private String getRealPathFromURI(Uri contentUri) {
		// TODO �Զ����ɵķ������
		String res = null;
	    String[] proj = { MediaStore.Images.Media.DATA };
	    Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
	    if(cursor.moveToFirst()){;
	       int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
	       res = cursor.getString(column_index);
	    }
	    cursor.close();
	    return res;
	}
	


}
