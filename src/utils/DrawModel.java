package utils;

import java.util.ArrayList;
import java.util.Random;

import com.baidu.platform.comapi.map.x;

import android.R.integer;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

public class DrawModel extends ImageView{

	Paint paint,paint2;
	
	private float one = (float)1000*5/90;
	private float two = (float)1000*10/90;
	private float three = (float)1000*25/90;
	private float four = (float)1000*75/90;
	
	private float fomalOne = one;
	private float fomalTwo = two;
	private float fomalThree = three;
	private float fomalFour = four;
	
	private int flag;
	
	public DrawModel(Context context) {
		super(context);
	}
	
	public DrawModel(Context context, AttributeSet attrs,  
            int defStyle) {  
        super(context, attrs, defStyle);  
        // TODO Auto-generated constructor stub  
    }  
  
    public DrawModel(Context context, AttributeSet attrs) {  
        super(context, attrs);  
        // TODO Auto-generated constructor stub  
    }  
	
	@SuppressLint({ "UseValueOf", "ClickableViewAccessibility" }) 
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (fomalFour-10 < event.getY() && event.getY() < fomalFour + 10) {
			flag = 4;
		}else if (fomalThree-10 < event.getY() && event.getY() < fomalThree + 10) {
			flag = 3;
		}else if (fomalTwo-10 < event.getY() && event.getY() < fomalTwo + 10) {
			flag = 2;
		}else if (fomalOne-10 < event.getY() && event.getY() < fomalOne + 10){
			flag = 1;
		}
		switch (flag) {
		case 4:
			if (event.getY()> fomalThree+20 && event.getY()<1280) {
				fomalFour = event.getY();
				invalidate(); //重新绘制区域
			}
			break;
		case 3:
			if (event.getY()> fomalTwo+20 && event.getY()<fomalFour-20) {
				fomalThree = event.getY();
				invalidate(); //重新绘制区域
			}
			break;
		case 2:
			if (event.getY()> fomalOne+20 && event.getY()<fomalThree-20) {
				fomalTwo = event.getY();
				invalidate(); //重新绘制区域
			}
			break;
		case 1:
			if (event.getY()> 20 && event.getY()<fomalTwo-20) {
				fomalOne = event.getY();
				invalidate(); //重新绘制区域
			}
			break;
		default:
			break;
		}
		return true;
	}
	
	@SuppressLint("DrawAllocation") @Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);  
		paint = new Paint();
		paint.setColor(Color.WHITE);
//		paint.setStrokeCap(Paint.Cap.ROUND);
//		paint.setStrokeJoin(Paint.Join.ROUND);
		paint.setStyle(Paint.Style.STROKE);//设置空心
		paint.setStrokeWidth(3);  
		paint.setTextSize(24);
        
		//枯枝落叶层——O
		//TODO 这一层图例重新设计
		for(int i = 0; i<100;i++){
			float x = (float) (Math.random()*700);
			float y = (float) (Math.random()*fomalOne);
			canvas.drawPoint(x, y, paint);
		}
		//腐殖质层——A
		for(int i = (int) (fomalOne + 25); i<fomalTwo;){
			for(int j = 1;j<720;){
				canvas.drawText("×", j, i, paint);
				j += 40;
			}
			i += 30;
		}
		//过渡层——AB
		RectF rect = new RectF(0, 0, 0, 0);
		for(int i = (int) (fomalTwo);i<fomalThree-50;){
			for(int j = 1;j<720-35;){
				rect.set(j, i, j+50, i+50);
				canvas.drawArc(rect, 45, 90, false, paint);
				j += 35;
			}
			i += 50;
		}  
		//沉淀层——B
		Path path = new Path(); //定义一条路径
		float len = (fomalFour - fomalThree - 60)/4;
		for (int i = (int) fomalThree; i < fomalFour; ) {
			for(int j = 40;j<720;){
				canvas.drawLine(j, i, j, i+len, paint);
				path.moveTo(j-10, i+len-10); //移动到 坐标   
		        path.lineTo(j, i+len);   
		        path.lineTo(j+10, i+len-10);   
				canvas.drawPath(path, paint);
				path.reset();
				j += 50;
			}
			i += len+20;
		}
		//母质质层——C
		int count = 0;
		int j;
		for(int i = (int) (fomalFour);i<1280-20;){
			canvas.drawLine(0,i, 720, i, paint);
			j = (count%2==0)?50:0;
			for(;j<720;){
				canvas.drawLine(j, i, j, i+20, paint);
				j+=100;
			}
			count++;
			i+=40;
		}
		canvas.drawLine(0f, fomalOne, 700f, fomalOne, paint);
		canvas.drawLine(0f, fomalTwo, 700f, fomalTwo, paint);
		canvas.drawLine(0f, fomalThree, 700f, fomalThree, paint);
		canvas.drawLine(0f, fomalFour, 700f, fomalFour, paint);  
 
	}

}
