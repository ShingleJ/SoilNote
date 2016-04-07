package com.app.soilnote;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class Attribute2Activity extends Activity {

	String[] arr_data = {"层次","深度","颜色-干","颜色-润","干湿度","质地","结构",
			"松紧度","空隙","新生体-种类","新生体-形态","新生体-数量","侵入体","根系"
			,"野外测定-PH","野外测定-石灰反应"}; 
	
	private List<Map<String, Object>> dataList;
	private ListView listView;
	private SimpleAdapter simp_adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_attribute2);
		
		listView = (ListView) findViewById(R.id.list);
		dataList = new ArrayList<Map<String,Object>>();
        simp_adapter = new SimpleAdapter(this, getTestData(), R.layout.item_attr, 
        		new String[]{"label","content", "pic"}, new int[]{R.id.label, R.id.content, R.id.arrow_right});
        listView.setAdapter(simp_adapter);
	}

	private List<Map<String, Object>> getTestData() {
		for(int i=0;i<arr_data.length;i++)
    	{
    		Map<String, Object> map = new HashMap<String, Object>();
    		map.put("label", arr_data[i]);
    		map.put("content", i);
    		map.put("pic", R.drawable.arrow_right);
    		dataList.add(map);
    	}
    	return dataList;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.attribute2, menu);
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
