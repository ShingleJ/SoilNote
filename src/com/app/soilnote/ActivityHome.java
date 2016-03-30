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

	private final String[]photoSelect_itemStrings={"拍摄照片","从文件中选择"};
	private static final int CAMERA_WITH_DATA = 1001;
	private static final int PHOTO_PICKED_WITH_DATA = 1002;
    
	private Bitmap bitMap;       //用来保存图片
    private Bitmap alterBitmap;
	private boolean hasImage;    //是否已经选择了图片
	
    private Paint paint;
    private Canvas canvas;
	
	private String filePath;
	
	private Button button, mapButton;
	private ImageView img;
	
	//拍照定位相关
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
		//获取所有可用的位置提供器
		List<String> providerList = mLocationManager.getProviders(true);
		if (providerList.contains(LocationManager.GPS_PROVIDER)) {
			mProvider = LocationManager.GPS_PROVIDER;
		}else if (providerList.contains(LocationManager.NETWORK_PROVIDER)) {
			mProvider = LocationManager.NETWORK_PROVIDER;
		}else {
			//当没有可用的位置提供器时，弹出Toast提示用户
			Toast.makeText(this, "No location provider to use", Toast.LENGTH_SHORT).show();
			return;
		}
	}

	@Override
	public void onClick(View v) {
		// TODO 自动生成的方法存根
		switch (v.getId()) {
		case R.id.button1:
			final AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setItems(photoSelect_itemStrings, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO 自动生成的方法存根
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
		// TODO 自动生成的方法存根
		int action = event.getAction();
        switch (action) {
        case MotionEvent.ACTION_DOWN:
            downx = event.getX();
            downy = event.getY();
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
	
    //接收回传结果（第二个页面中用的是setResult方法）
    //requestCode  请求码
    //resultCode   结果码（在第二个页面中也有）
    //data 回传的数据
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
                // 设置图片
//                img.setImageBitmap(bitmap);
                
//              Bitmap bm = (Bitmap) data.getExtras().get("data");//通过这种方式得到的图像数据是经过压缩的，
//              														//所以看到的图像是模糊的
//              img.setImageBitmap(bm);//将图像显示在ImageView视图上

	            filePath = "";
	            //alterBitmap变量是新建的一个与bitmap图片长宽一致的空白BitMap类型，用于存储绘制的像素
	            alterBitmap = Bitmap.createBitmap(img.getWidth(),
	                    img.getHeight(), bitmap.getConfig());
	            canvas = new Canvas(alterBitmap);
	            paint = new Paint();
	            //paint是指画笔
	            paint.setColor(Color.BLACK);
	            paint.setStrokeWidth(3);
	            Matrix matrix = new Matrix();
	            //使用指定的matrix绘制位图bitmap，使用canvas绘画，画的东西都存储在了alterBitmap中
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
	
    //拍照获取图片
	protected void doTakePhoto() {      
        try {      
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); //调用系统相机  
            Location mLocation = mLocationManager.getLastKnownLocation(mProvider);
            if (mLocation != null) {
				//显示当前拍照时的位置信息
            	showLocation(mLocation);
			}
            mLocationManager.requestLocationUpdates(mProvider, 5000, 1, mLocationListener);
            
            //获得文件保存路径
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
	
	LocationListener mLocationListener = new LocationListener() {
		
		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO 自动生成的方法存根
			
		}
		
		@Override
		public void onProviderEnabled(String provider) {
			// TODO 自动生成的方法存根
			
		}
		
		@Override
		public void onProviderDisabled(String provider) {
			// TODO 自动生成的方法存根
			
		}
		
		@Override
		public void onLocationChanged(Location location) {
			//更新当前设备的位置信息
			showLocation(location);
		}
	};
	
	protected void onDestroy() {
		super.onDestroy();
		if (mLocationManager != null) {
			//关闭程序时将监听器移除
			mLocationManager.removeUpdates(mLocationListener);
		}
	};
		
	private void showLocation(Location mLocation) {
		String currentPosition = "经度是：" + mLocation.getLatitude() + 
				"\n" + "纬度是：" + mLocation.getLongitude();
		positionTextView.setText(currentPosition);
		
	}

	//从本地选择图片
	private void doSelectImageFromLoacal(){      
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
        String pathStorage = Environment.getExternalStorageDirectory().getPath() + "/SoilNote/";
        //创建文件夹
        File file = new File(pathStorage);
        if(!file.exists()){
        	file.mkdirs();
        }
        //文件名以日期命名
        String name = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
        //返回文件路径
        mFilePath = pathStorage + "/" + name+ ".jpg";
		return mFilePath;
	}
	
    
    //根据uri获得文件路径
	private String getRealPathFromURI(Uri contentUri) {
		// TODO 自动生成的方法存根
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
