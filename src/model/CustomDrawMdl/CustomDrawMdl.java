package model.CustomDrawMdl;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;

public class CustomDrawMdl extends ImageView{

	private Bitmap bitmap = null;   //���ڴ洢ģ��
    private Canvas canvasBitmap;
    
	private float downx = 0;
	private float downy = 0;
	private float upx = 0;
	private float upy = 0;
	
	private List<Float> pointsX = new ArrayList<Float>();
	private List<Float> pointsY = new ArrayList<Float>();
	private List<Boolean> flagList = new ArrayList<Boolean>();
	
	private Paint paint;
	
	//�������캯��������Ҫд
	public CustomDrawMdl(Context context) {
		super(context);
	}
	
	public CustomDrawMdl(Context context, AttributeSet attrs,  
            int defStyle) {  
        super(context, attrs, defStyle);  
    }  
  
    public CustomDrawMdl(Context context, AttributeSet attrs) {  
        super(context, attrs);  
        paint = new Paint();
		paint.setColor(0xff00FECC);
		paint.setStrokeWidth(3);
		paint.setAntiAlias(true);
    }  
	
	@SuppressLint("ClickableViewAccessibility") 
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int action = event.getAction();
		switch (action) {
		case MotionEvent.ACTION_DOWN:
//			performClick();
			downx = event.getX();
			downy = event.getY();
			pointsX.add(downx);
			pointsY.add(downy);
			flagList.add(false);
			break;
		case MotionEvent.ACTION_MOVE:
			upx = event.getX();
			upy = event.getY();
			pointsX.add(upx);
			pointsY.add(upy);
			flagList.add(false);
			invalidate();
			break;
			
		case MotionEvent.ACTION_UP:
			upx = event.getX();
			upy = event.getY();
			pointsX.add(upx);
			pointsY.add(upy);
			flagList.add(true);
			invalidate();// ˢ�£�ǿ���ػ�
			break;

		default:
			break;
		}
		return true;
	}
	
	@SuppressLint("DrawAllocation") 
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas); 

		bitmap = Bitmap.createBitmap(720, 1038, Bitmap.Config.ARGB_8888);
        canvasBitmap = new Canvas(bitmap);
        
		for (int i = 0; i < pointsX.size()-1; i++) {//ע�������size-1
			if (flagList.get(i)) {
				i++;
			}
			canvasBitmap.drawLine(pointsX.get(i), pointsY.get(i), pointsX.get(i+1), pointsY.get(i+1), paint);
		}
		
		canvas.drawBitmap(bitmap, 0, 0 , paint);
	}
	
	public void unDo() {
		for (int i = pointsX.size()-1;i>=0; i--) {
			pointsX.remove(i);
			pointsY.remove(i);
			flagList.remove(i);
			if (i>0&&flagList.get(i-1)) {
				break;
			}
		}
		invalidate();
	}
	
	public void clearDrawMdl() {
		pointsX.clear();
		pointsY.clear();
		flagList.clear();
		invalidate();
	}
}
