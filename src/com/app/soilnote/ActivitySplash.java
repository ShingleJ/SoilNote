package com.app.soilnote;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

public class ActivitySplash extends Activity {

	private final int SPLASH_DISPLAY_LENGHT = 3000; //�ӳ����� 
	private static final int SHOW_TIME_MIN = 2000;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//���ر�����
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//����״̬��
        //����ȫ������
        int flag=WindowManager.LayoutParams.FLAG_FULLSCREEN;
        //��õ�ǰ�������
        Window window=this.getWindow();
        //���õ�ǰ����Ϊȫ����ʾ
        window.setFlags(flag, flag);
		setContentView(R.layout.activity_splash);
		//����һ
//        new Handler().postDelayed(new Runnable(){ 
//        	 
//            @Override
//            public void run() { 
//                Intent mainIntent = new Intent(ActivitySplash.this,ActivityHome.class); 
//                startActivity(mainIntent); 
//                finish(); 
//                //���������ֱ��ʾ����Ķ���,�˳��Ķ���
//                overridePendingTransition(R.anim.head_in, R.anim.fade_out);
//            } 
//                
//           }, SPLASH_DISPLAY_LENGHT); 
		//������
		new AsyncTask<Void, Void, Integer>() {
			 
            @Override
            protected Integer doInBackground(Void... params) {
                int result = 0;
                long startTime = System.currentTimeMillis();
//                result = loadingCache();
                long loadingTime = System.currentTimeMillis() - startTime;
                if (loadingTime < SHOW_TIME_MIN) {
                    try {
                        Thread.sleep(SHOW_TIME_MIN - loadingTime);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                return result;
            }
 
            @Override
            protected void onPostExecute(Integer result) {
            	Intent intent = new Intent(ActivitySplash.this,ActivityHome.class);
                startActivity(intent);
                finish();
                //���������ֱ��ʾ����Ķ���,�˳��Ķ���
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            };
        }.execute(new Void[]{});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_splash, menu);
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
