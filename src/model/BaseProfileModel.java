package model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.ImageView;

public class BaseProfileModel extends ImageView{
  
	//protected：可以被子类访问
	protected Paint paint;
	protected Paint linePaint;
	protected Bitmap bitmap = null;   //用于存储模板
	protected Canvas canvasBitmap;
	
	protected int width, height;
    
	protected float[] fmlLines;
	protected boolean[] isDraw;
    
    protected int flag;
    
    public BaseProfileModel(Context context, AttributeSet attrs, float[] lines) {  
        super(context, attrs);  
        fmlLines = new float[lines.length];
        isDraw = new boolean[lines.length-1];
        for (int i = 0; i < lines.length; i++) {
        	fmlLines[i] = lines[i];
		}
        for (int i = 0; i < lines.length-1; i++) {
        	isDraw[i] = true;
		}
        paint = new Paint();
		paint.setColor(Color.WHITE);
		paint.setStyle(Paint.Style.STROKE);//设置空心
		paint.setStrokeWidth(3);  
		paint.setTextSize(30);
		
		linePaint = new Paint();
		linePaint.setColor(Color.WHITE);
		linePaint.setStyle(Paint.Style.STROKE);//设置空心
		linePaint.setStrokeWidth(6);  
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
