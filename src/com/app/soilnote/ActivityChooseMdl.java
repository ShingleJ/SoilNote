package com.app.soilnote;

import android.R.integer;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ActivityChooseMdl extends Activity implements OnItemClickListener{

	public static int modelNameNum;
	private ListView lv;
	private ArrayAdapter<String> adapter;
	private String[] list = {"山地黄壤1","山地黄壤2","山地黄壤3","山地黄棕壤","山地棕壤"};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_choose_mdl);
		
		lv = (ListView) findViewById(R.id.list_choose_mdl);
		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list);
		lv.setAdapter(adapter);
		lv.setOnItemClickListener(this);
	}
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		modelNameNum = position;
		Intent intent = new Intent();
		intent.putExtra("model_name_num", modelNameNum);
		setResult(RESULT_OK, intent);
		finish();
	}

}
