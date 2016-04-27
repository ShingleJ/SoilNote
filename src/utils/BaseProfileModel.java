package utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.ImageView;

public class BaseProfileModel extends ImageView{
  
	//protected�����Ա�������ʣ�Ҳ��Ĭ�����η�
	protected Paint paint;
	protected Bitmap bitmap = null;   //���ڴ洢ģ��
	protected Canvas canvasBitmap;
	
	protected int width, height;
    
	float[] fmlLines;
	boolean[] isDraw;
    
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
		paint.setStyle(Paint.Style.STROKE);//���ÿ���
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
