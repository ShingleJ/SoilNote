package com.app.soilnote;

import utils.BitmapUtils;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.ImageView;
import android.widget.Toast;

public class ActivityMapImageDetail extends Activity {

	private ImageView iv;
	Bitmap bm;
	String imagePath;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map_image_detail);
		iv = (ImageView) findViewById(R.id.id_map_img_detail);
		Intent intent = getIntent();
		int id = intent.getIntExtra("id", -1);
		imagePath = intent.getStringExtra("imagePath");
		Toast.makeText(this, imagePath, Toast.LENGTH_LONG).show();
	    ViewTreeObserver vto = iv.getViewTreeObserver();  
        vto.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {  
            @Override  
            public void onGlobalLayout() {  
                iv.getViewTreeObserver().removeGlobalOnLayoutListener(this);  
                //TODO ≈–∂œid «∑Òœ‡Õ¨
                bm = BitmapUtils.decodeSampledBitmapFromFile(imagePath, iv.getWidth(),iv.getHeight());
                iv.setImageBitmap(bm);
            }  
        });  
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_map_image_detail, menu);
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
