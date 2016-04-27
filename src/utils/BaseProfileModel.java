package utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.ImageView;

public class BaseProfileModel extends ImageView{
  
	//protected：可以被子类访问，也是默认修饰符
	protected Paint paint = new Paint();
	protected Bitmap bitmap = null;   //用于存储模板
	protected Canvas canvasBitmap;
	
	protected int width, height;
    
	protected float one = (float)1038*5/60;
	protected float two = (float)1038*10/60;
	protected float three = (float)1038*20/60;
	protected float four = (float)1038*30/60;
	
	float[] fmlLines = {0, one, two, three, four, 1038};
    boolean[] isDraw = {true, true, true, true, true};
    
    protected int flag;
    
    public BaseProfileModel(Context context, AttributeSet attrs) {  
        super(context, attrs);  
		paint.setColor(Color.WHITE);
		paint.setStyle(Paint.Style.STROKE);//设置空心
		paint.setStrokeWidth(3);  
		paint.setTextSize(30);
    }

	public Bitmap getProModelBitmap() {
		if (bitmap != null) {
			return bitmap;
		}else {
			return null;
		}
	}
	
	int num =  0;
	public void deleteProfile(int n) {
		isDraw[n-1+num] = false;
		if (num<isDraw.length-1) {
			num++;
		}
		for(int i = fmlLines.length-1; i>=n;i--){
			fmlLines[i] = fmlLines[i-1];
		}
		invalidate();
	}
	
}
