package com.app.soilnote;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class MyOrientationListener implements SensorEventListener {

	private SensorManager mSensorManager;
	private Context context;
	private Sensor sensor;

	private float lastX;

	public MyOrientationListener(Context context) {
		// TODO �Զ����ɵĹ��캯�����
		this.context = context;
	}

	public void start() {
		// �õ�ϵͳ����,���е�ϵͳ�����ȡ����������context.getSystemService����ʵ�֣�ֻ�Ǵ���Ĳ�����ͬ
		mSensorManager = (SensorManager) context
				.getSystemService(Context.SENSOR_SERVICE);
		if (mSensorManager != null) {
			// ��÷��򴫸���
			sensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
		}
		if (sensor != null) {
			mSensorManager.registerListener(this, sensor,
					SensorManager.SENSOR_DELAY_UI);
		}
	}

	public void stop() {
		mSensorManager.unregisterListener(this);
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {// ���ȸı�
		// TODO �Զ����ɵķ������

	}

	@Override
	public void onSensorChanged(SensorEvent event) {// �������仯
		// TODO �Զ����ɵķ������
		if (event.sensor.getType() == Sensor.TYPE_ORIENTATION) {
			float x = event.values[SensorManager.DATA_X];
			// ����̫��ظ���UI�������ж�����������仯����1�ȵĻ����Ž���UI����
			if (Math.abs(x - lastX) > 1.0) {
				// ����һ���ص���֪ͨ��������£�����˵��"1��������ˣ��������"��
				if (mOnOrientationListener != null) {
					mOnOrientationListener.onOrientationChanged(x);
				}
			}
			lastX = x;
		}
	}

	private OnOrientationListener mOnOrientationListener;

	public void setOnOrientationListener(
			OnOrientationListener mOnOrientationListener) {
		this.mOnOrientationListener = mOnOrientationListener;
	}

	public interface OnOrientationListener {
		void onOrientationChanged(float x);
	}
}
