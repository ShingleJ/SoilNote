package com.app.soilnote;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;

public class ActivityAttribute extends Activity {

	String[] arr_data ={"������", "����", "����", "������", "�ص�", "����ͼ��/��������Ƭ��",
			"��������", "��ʽ����", "����", "���θ߶�", "ĸ������", "��Ȼֲ��", "��ʴ���", 
			"Ǳˮλ��ˮ��", "��������", "�Ź�����", "ʩ�����", "��ΪӰ��", "����״��",
			"һ�����������/Ķ��", "���������ۺ�������"};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_attribute);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_attribute, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
