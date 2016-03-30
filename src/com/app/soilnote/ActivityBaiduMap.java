package com.app.soilnote;


import com.app.soilnote.MyOrientationListener.OnOrientationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class ActivityBaiduMap extends Activity{
	private MapView mapView;
	private BaiduMap mBaiduMap;
	
	private Context context;
	//定位相关
	private LocationClient mLocationClient;
	private MyLocationListener myLocationListener;
	private boolean isFirstIn = true;
	private double mLatitude;
	private double mLongtitude;
	
	private LocationMode mLocationMode;
	
	//自定义定位图标
	private BitmapDescriptor mIconLocation;
	private MyOrientationListener myOrientationListener;
	private float mCurrentX;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		
		this.context = this;
		//在使用SDK各组件之前初始化context信息，传入applicationContext
		//注意该方法在setContentView方法之前实现
		SDKInitializer.initialize(getApplicationContext());
		setContentView(R.layout.map);
		initView();
		
		//初始化定位
		initLocation();
	}
	
	private void initLocation() {
		// TODO 自动生成的方法存根
		mLocationMode = LocationMode.NORMAL;
		mLocationClient = new LocationClient(this);
		myLocationListener = new MyLocationListener();
		mLocationClient.registerLocationListener(myLocationListener);//注册定位监听器
		//设置定位的一些属性
		LocationClientOption option = new LocationClientOption();
		option.setCoorType("bd09ll");//坐标类型
		option.setIsNeedAddress(true);//返回位置
		option.setOpenGps(true);//打开GPS
		option.setScanSpan(1000);//每隔1000秒进行一次请求
		mLocationClient.setLocOption(option);
		
		//初始化图标
		mIconLocation = BitmapDescriptorFactory.fromResource(R.drawable.navigation);
		myOrientationListener = new MyOrientationListener(context);
		
		myOrientationListener.setOnOrientationListener(new OnOrientationListener() {
			
			@Override
			public void onOrientationChanged(float x) {
				// TODO 自动生成的方法存根
				mCurrentX = x;
			}
		});
	}

	private void initView() {
		// TODO 自动生成的方法存根
		mapView = (MapView) findViewById(R.id.bmapview);
		mBaiduMap = mapView.getMap();
		//初始化时放大地图的比例
		MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(15.0f);
		mBaiduMap.setMapStatus(msu);
	}

	@Override
	protected void onDestroy() {
		// TODO 自动生成的方法存根
		super.onDestroy();
		mapView.onDestroy();
	}
	
	@Override
	protected void onStart() {
		// TODO 自动生成的方法存根
		super.onStart();
		mBaiduMap.setMyLocationEnabled(true);//先开启地图
		if (!mLocationClient.isStarted()) {
			mLocationClient.start();//开启定位
		}
		//开启方向传感器
		myOrientationListener.start();
	}
	
	@Override
	protected void onStop() {
		// TODO 自动生成的方法存根
		super.onStop();
		mBaiduMap.setMyLocationEnabled(false);
		mLocationClient.stop();
		//关闭方向传感器
	    myOrientationListener.stop();
	}
	@Override
	protected void onPause() {
		// TODO 自动生成的方法存根
		super.onPause();
		mapView.onPause();
	}
	
	@Override
	protected void onResume() {
		// TODO 自动生成的方法存根
		super.onResume();
		mapView.onResume();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO 自动生成的方法存根
		getMenuInflater().inflate(R.menu.home, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO 自动生成的方法存根
		switch (item.getItemId()) {
		case R.id.id_map_common:
			mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
			break;
		case R.id.id_map_site:
			mBaiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
			break;
		case R.id.id_map_trafic:
			if (mBaiduMap.isTrafficEnabled()) {
				mBaiduMap.setTrafficEnabled(false);
				item.setTitle("实时交通(off)");
			}else {
				mBaiduMap.setTrafficEnabled(true);
				item.setTitle("实时交通(on)");
			}
			break;
		case R.id.id_map_location:
			centerToMyLocation();
			break;
		case R.id.id_map_mode_common:
			mLocationMode = LocationMode.NORMAL;
			break;
		case R.id.id_map_mode_follow:
			mLocationMode = LocationMode.FOLLOWING;
			break;
		case R.id.id_map_mode_campass:
			mLocationMode = LocationMode.COMPASS;
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	/*
	 * 定位到我的位置
	 */
	private void centerToMyLocation() {
		LatLng latLng = new LatLng(mLatitude, mLongtitude);
		MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(latLng);
		mBaiduMap.setMapStatus(msu);
	}
	
	private class MyLocationListener implements BDLocationListener{

		@Override
		public void onReceiveLocation(BDLocation location) { //定位成功以后的回调
			// TODO 自动生成的方法存根
			//builder模式，当参数比较多的时候可以通过下面的这种格式来写
			//这个location对象中包含了经度、纬度、海拔等一系列的位置信息，
			MyLocationData data = new MyLocationData.Builder()//
			.direction(mCurrentX)//
			.accuracy(location.getRadius())//
			.latitude(location.getLatitude())//
			.longitude(location.getLongitude()).build();
			
			mBaiduMap.setMyLocationData(data);
			
			//更新经纬度
			mLatitude = location.getLatitude();
			mLongtitude = location.getLongitude();
			
			//设置自定义图标
			MyLocationConfiguration config = new MyLocationConfiguration(
					//mLocationMode定位模式
					mLocationMode, true, mIconLocation);
			
			mBaiduMap.setMyLocationConfigeration(config);
			
			//设置初始地图显示到定位点
			if(isFirstIn){
				LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
				MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(latLng);
				mBaiduMap.animateMapStatus(msu);
				isFirstIn = false;
				
				Toast.makeText(context, location.getAddrStr(),Toast.LENGTH_SHORT).show();
			}
		}
		
	}
}
