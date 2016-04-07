package com.app.soilnote;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;

public class ActivityAttribute extends Activity {

	String[] arr_data ={"剖面编号", "日期", "天气", "调查人", "地点", "地形图幅/航（卫）片号",
			"土壤俗名", "正式定名", "地形", "海拔高度", "母质类型", "自然植被", "侵蚀情况", 
			"潜水位及水质", "土地利用", "排灌条件", "施肥情况", "人为影响", "轮作状况",
			"一般产量（公斤/亩）", "土壤剖面综合评述："};
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
