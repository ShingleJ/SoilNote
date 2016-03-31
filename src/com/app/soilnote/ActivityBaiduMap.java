package com.app.soilnote;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import utils.BitmapUtils;

import model.InfoGeo;

import com.app.soilnote.MyOrientationListener.OnOrientationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;

import db.SoilNoteDB;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class ActivityBaiduMap extends Activity {
	private MapView mapView;
	private BaiduMap mBaiduMap;

	private Context context;
	// 定位相关
	private LocationClient mLocationClient;
	private MyLocationListener myLocationListener;
	private boolean isFirstIn = true;
	private double mLatitude;
	private double mLongtitude;

	private LocationMode mLocationMode;

	// 自定义定位图标
	private BitmapDescriptor mIconLocation;
	private MyOrientationListener myOrientationListener;
	private float mCurrentX;

	
	//覆盖物相关
	private BitmapDescriptor mMarker;
	List<InfoGeo> infoGeos;
	
	
	// 数据库相关
	private SoilNoteDB soilNoteDB;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);

		this.context = this;
		// 在使用SDK各组件之前初始化context信息，传入applicationContext
		// 注意该方法在setContentView方法之前实现
		SDKInitializer.initialize(getApplicationContext());
		setContentView(R.layout.map);
		soilNoteDB = SoilNoteDB.getInstance(this);
		initView();
		// 初始化定位
		initLocation();
		//初始化覆盖物
		initMarker();
		//设置覆盖物点击事件
		mBaiduMap.setOnMarkerClickListener(new OnMarkerClickListener() {
			
			@Override
			public boolean onMarkerClick(Marker marker) {
				Bundle extraInfo = marker.getExtraInfo();
				InfoGeo infoGeo = (InfoGeo) extraInfo.getSerializable("info");
				String imagePath = infoGeo.getImagePath();
				int id = infoGeo.getId();
				Intent intent = new Intent(ActivityBaiduMap.this, ActivityMapImageDetail.class);
				intent.putExtra("id", id);
				intent.putExtra("imagePath", imagePath);
				startActivity(intent);
				return false;
			}
		});
	}

	private void initMarker() {
//		mMarker = BitmapDescriptorFactory.fromResource(R.drawable.marker);
		infoGeos = new ArrayList<InfoGeo>();
		infoGeos = soilNoteDB.loadInfoGeo();
	}

	private void initLocation() {
		mLocationMode = LocationMode.NORMAL;
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

		// 初始化图标
		mIconLocation = BitmapDescriptorFactory
				.fromResource(R.drawable.navigation);
		myOrientationListener = new MyOrientationListener(context);

		myOrientationListener
				.setOnOrientationListener(new OnOrientationListener() {

					@Override
					public void onOrientationChanged(float x) {
						// TODO 自动生成的方法存根
						mCurrentX = x;
					}
				});
	}

	private void initView() {
		mapView = (MapView) findViewById(R.id.bmapview);
		mBaiduMap = mapView.getMap();
		// 初始化时放大地图的比例
		MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(15.0f);
		mBaiduMap.setMapStatus(msu);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mapView.onDestroy();
	}

	@Override
	protected void onStart() {
		super.onStart();
		mBaiduMap.setMyLocationEnabled(true);// 先开启地图
		if (!mLocationClient.isStarted()) {
			mLocationClient.start();// 开启定位
		}
		// 开启方向传感器
		myOrientationListener.start();
	}

	@Override
	protected void onStop() {
		super.onStop();
		mBaiduMap.setMyLocationEnabled(false);
		mLocationClient.stop();
		// 关闭方向传感器
		myOrientationListener.stop();
	}

	@Override
	protected void onPause() {
		super.onPause();
		mapView.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		mapView.onResume();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.home, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
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
			} else {
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
		case R.id.id_map_add_overlay:
			addOverlays(infoGeos);
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	/*
	 * 添加覆盖物
	 */
	private void addOverlays(List<InfoGeo> infoGeos) {
		mBaiduMap.clear();
		LatLng latLng = null;
		Marker marker = null;
		Bitmap bitmap;
		BitmapDescriptor mMarker;
		OverlayOptions options;
		for (InfoGeo info:infoGeos) {
			//经纬度
			latLng = new LatLng(info.getLatitude(), info.getLongtitude());
			//图标
			bitmap = BitmapUtils.decodeSampledBitmapFromFile(
					info.getImagePath(),80, 80);
			mMarker = BitmapDescriptorFactory.fromBitmap(bitmap);
			options = new MarkerOptions().position(latLng).icon(mMarker).zIndex(5);
			marker = (Marker) mBaiduMap.addOverlay(options);
			Bundle arg0 = new Bundle();
			arg0.putSerializable("info", info);
			marker.setExtraInfo(arg0);
		}
		MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(latLng);
		mBaiduMap.setMapStatus(msu);
	}

	/*
	 * 定位到我的位置
	 */
	private void centerToMyLocation() {
		LatLng latLng = new LatLng(mLatitude, mLongtitude);
		MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(latLng);
		mBaiduMap.setMapStatus(msu);
	}

	private class MyLocationListener implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) { // 定位成功以后的回调
			// builder模式，当参数比较多的时候可以通过下面的这种格式来写
			// 这个location对象中包含了经度、纬度、海拔等一系列的位置信息，
			MyLocationData data = new MyLocationData.Builder()//
					.direction(mCurrentX)//
					.accuracy(location.getRadius())//
					.latitude(location.getLatitude())//
					.longitude(location.getLongitude()).build();

			mBaiduMap.setMyLocationData(data);

			// 更新经纬度
			mLatitude = location.getLatitude();
			mLongtitude = location.getLongitude();

			// 设置自定义图标
			MyLocationConfiguration config = new MyLocationConfiguration(
			// mLocationMode定位模式
					mLocationMode, true, mIconLocation);

			mBaiduMap.setMyLocationConfigeration(config);

			// 设置初始地图显示到定位点
			if (isFirstIn) {
				LatLng latLng = new LatLng(location.getLatitude(),
						location.getLongitude());
				MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(latLng);
				mBaiduMap.animateMapStatus(msu);
				isFirstIn = false;

				Toast.makeText(context, location.getAddrStr(),
						Toast.LENGTH_SHORT).show();
			}
		}

	}
}
