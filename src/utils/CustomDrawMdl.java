package utils;

import java.util.ArrayList;
import java.util.List;

import android.R.integer;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.Toast;

public class CustomDrawMdl extends ImageView{

	private float downx = 0;
	private float downy = 0;
	private float upx = 0;
	private float upy = 0;
	
	private List<Float> pointsX = new ArrayList<Float>();
	private List<Float> pointsY = new ArrayList<Float>();
	private List<Boolean> flagList = new ArrayList<Boolean>();
	
	private Paint paint;
	
	//三个构造函数都必须要写
	public CustomDrawMdl(Context context) {
		super(context);
	}
	
	public CustomDrawMdl(Context context, AttributeSet attrs,  
            int defStyle) {  
        super(context, attrs, defStyle);  
        // TODO Auto-generated constructor stub  
    }  
  
    public CustomDrawMdl(Context context, AttributeSet attrs) {  
        super(context, attrs);  
        // TODO Auto-generated constructor stub  
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
			invalidate();// 刷新，强制重绘
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

		paint = new Paint();
		paint.setColor(Color.RED);
		paint.setStrokeWidth(3);

		for (int i = 0; i < pointsX.size()-1; i++) {//注意这边是size-1
			if (flagList.get(i)) {
				i++;
			}
			canvas.drawLine(pointsX.get(i), pointsY.get(i), pointsX.get(i+1), pointsY.get(i+1), paint);
		}
	}
}
