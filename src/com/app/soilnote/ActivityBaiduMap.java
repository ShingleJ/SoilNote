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
	// ��λ���
	private LocationClient mLocationClient;
	private MyLocationListener myLocationListener;
	private boolean isFirstIn = true;
	private double mLatitude;
	private double mLongtitude;

	private LocationMode mLocationMode;

	// �Զ��嶨λͼ��
	private BitmapDescriptor mIconLocation;
	private MyOrientationListener myOrientationListener;
	private float mCurrentX;

	
	//���������
	private BitmapDescriptor mMarker;
	List<InfoGeo> infoGeos;
	
	
	// ���ݿ����
	private SoilNoteDB soilNoteDB;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO �Զ����ɵķ������
		super.onCreate(savedInstanceState);

		this.context = this;
		// ��ʹ��SDK�����֮ǰ��ʼ��context��Ϣ������applicationContext
		// ע��÷�����setContentView����֮ǰʵ��
		SDKInitializer.initialize(getApplicationContext());
		setContentView(R.layout.map);
		soilNoteDB = SoilNoteDB.getInstance(this);
		initView();
		// ��ʼ����λ
		initLocation();
		//��ʼ��������
		initMarker();
		//���ø��������¼�
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
		mLocationClient.registerLocationListener(myLocationListener);// ע�ᶨλ������
		// ���ö�λ��һЩ����
		LocationClientOption option = new LocationClientOption();
		option.setCoorType("bd09ll");// ��������
		option.setIsNeedAddress(true);// ����λ��
		option.setOpenGps(true);// ��GPS
		option.setScanSpan(1000);// ÿ��1000�����һ������
		mLocationClient.setLocOption(option);

		// ��ʼ��ͼ��
		mIconLocation = BitmapDescriptorFactory
				.fromResource(R.drawable.navigation);
		myOrientationListener = new MyOrientationListener(context);

		myOrientationListener
				.setOnOrientationListener(new OnOrientationListener() {

					@Override
					public void onOrientationChanged(float x) {
						// TODO �Զ����ɵķ������
						mCurrentX = x;
					}
				});
	}

	private void initView() {
		mapView = (MapView) findViewById(R.id.bmapview);
		mBaiduMap = mapView.getMap();
		// ��ʼ��ʱ�Ŵ��ͼ�ı���
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
		mBaiduMap.setMyLocationEnabled(true);// �ȿ�����ͼ
		if (!mLocationClient.isStarted()) {
			mLocationClient.start();// ������λ
		}
		// �������򴫸���
		myOrientationListener.start();
	}

	@Override
	protected void onStop() {
		super.onStop();
		mBaiduMap.setMyLocationEnabled(false);
		mLocationClient.stop();
		// �رշ��򴫸���
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
				item.setTitle("ʵʱ��ͨ(off)");
			} else {
				mBaiduMap.setTrafficEnabled(true);
				item.setTitle("ʵʱ��ͨ(on)");
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
	 * ��Ӹ�����
	 */
	private void addOverlays(List<InfoGeo> infoGeos) {
		mBaiduMap.clear();
		LatLng latLng = null;
		Marker marker = null;
		Bitmap bitmap;
		BitmapDescriptor mMarker;
		OverlayOptions options;
		for (InfoGeo info:infoGeos) {
			//��γ��
			latLng = new LatLng(info.getLatitude(), info.getLongtitude());
			//ͼ��
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
	 * ��λ���ҵ�λ��
	 */
	private void centerToMyLocation() {
		LatLng latLng = new LatLng(mLatitude, mLongtitude);
		MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(latLng);
		mBaiduMap.setMapStatus(msu);
	}

	private class MyLocationListener implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) { // ��λ�ɹ��Ժ�Ļص�
			// builderģʽ���������Ƚ϶��ʱ�����ͨ����������ָ�ʽ��д
			// ���location�����а����˾��ȡ�γ�ȡ����ε�һϵ�е�λ����Ϣ��
			MyLocationData data = new MyLocationData.Builder()//
					.direction(mCurrentX)//
					.accuracy(location.getRadius())//
					.latitude(location.getLatitude())//
					.longitude(location.getLongitude()).build();

			mBaiduMap.setMyLocationData(data);

			// ���¾�γ��
			mLatitude = location.getLatitude();
			mLongtitude = location.getLongitude();

			// �����Զ���ͼ��
			MyLocationConfiguration config = new MyLocationConfiguration(
			// mLocationMode��λģʽ
					mLocationMode, true, mIconLocation);

			mBaiduMap.setMyLocationConfigeration(config);

			// ���ó�ʼ��ͼ��ʾ����λ��
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
