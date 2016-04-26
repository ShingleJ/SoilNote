package utils;

import com.app.soilnote.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;

public class DrawModel extends ImageView{

	private Paint paint = new Paint();
	private Bitmap bitmap = null;   //用于存储模板
    private Canvas canvasBitmap;
    
    private int width, height;
	
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
    }  
  
    public DrawModel(Context context, AttributeSet attrs) {  
        super(context, attrs);  
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
		
		bitmap = Bitmap.createBitmap(720, 1038, Bitmap.Config.ARGB_8888);
        canvasBitmap = new Canvas(bitmap);
        
		width = canvas.getWidth();
		height = canvas.getHeight();

		paint.setColor(Color.WHITE);
		paint.setStyle(Paint.Style.STROKE);//设置空心
		paint.setStrokeWidth(3);  
		paint.setTextSize(24);
		
		setImageResource(R.drawable.transparent_backgroud_frame);
		 
		//枯枝落叶层——O
		//TODO 这一层图例重新设计
		for(int i = 0; i<100;i++){
			float x = (float) (Math.random()*width);
			float y = (float) (Math.random()*fomalOne);
			canvasBitmap.drawPoint(x, y, paint);
		}
		//腐殖质层——A
		for(int i = (int) (fomalOne + 25); i<fomalTwo;){
			for(int j = 1;j<width;){
				canvasBitmap.drawText("×", j, i, paint);
				j += 40;
			}
			i += 30;
		}
		//过渡层——AB
		RectF rect = new RectF(0, 0, 0, 0);
		for(int i = (int) (fomalTwo);i<fomalThree-50;){
			for(int j = 1;j<width-35;){
				rect.set(j, i, j+50, i+50);
				canvasBitmap.drawArc(rect, 45, 90, false, paint);
				j += 35;
			}
			i += 50;
		}  
		//沉淀层——B
		Path path = new Path(); //定义一条路径
		float len = (fomalFour - fomalThree - 60)/4;
		for (int i = (int) fomalThree; i < fomalFour; ) {
			for(int j = 40;j<width;){
				canvasBitmap.drawLine(j, i, j, i+len, paint);
				path.moveTo(j-10, i+len-10); //移动到 坐标   
		        path.lineTo(j, i+len);   
		        path.lineTo(j+10, i+len-10);   
		        canvasBitmap.drawPath(path, paint);
				path.reset();
				j += 50;
			}
			i += len+20;
		}
		//母质质层——C
		int count = 0;
		int j;
		for(int i = (int) (fomalFour);i<1280-20;){
			canvasBitmap.drawLine(0,i, width, i, paint);
			j = (count%2==0)?50:0;
			for(;j<width;){
				canvasBitmap.drawLine(j, i, j, i+20, paint);
				j+=100;
			}
			count++;
			i+=40;
		}
		canvasBitmap.drawLine(0f, fomalOne, width, fomalOne, paint);
		canvasBitmap.drawLine(0f, fomalTwo, width, fomalTwo, paint);
		canvasBitmap.drawLine(0f, fomalThree, width, fomalThree, paint);
		canvasBitmap.drawLine(0f, fomalFour, width, fomalFour, paint);  
		
		canvas.drawBitmap(bitmap, 0, 0 , paint);
	}
	
	public Bitmap getProModelBitmap() {
		if (bitmap != null) {
			return bitmap;
		}else {
			return null;
		}
	}
}
