package com.app.soilnote;

import utils.DrawModel;
import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class DrawTestActivity extends Activity {

	private ImageView imageView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_draw_test);
//		imageView = (ImageView) findViewById(R.id.iv);
//		imageView.setImageResource(R.id.start_img);
		
		drawTest();
	}

	private void drawTest() {
        RelativeLayout layout=(RelativeLayout) findViewById(R.id.root);  
        final DrawModel view = new DrawModel(this);  
        view.setMinimumHeight(500);  
        view.setMinimumWidth(300);  
        //通知view组件重绘    
        view.invalidate();  
        layout.addView(view); 
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.draw_test, menu);
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
