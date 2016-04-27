package utils;

import com.app.soilnote.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;

public class MontainYelloSoilOne extends ImageView{

	private Paint paint = new Paint();
	private Bitmap bitmap = null;   //用于存储模板
    private Canvas canvasBitmap;
    
    private int width, height;
	
	private float one = (float)1038*5/90;
	private float two = (float)1038*10/90;
	private float three = (float)1038*25/90;
	private float four = (float)1038*75/90;
	
	private float fmlOne = one;
	private float fmlTwo = two;
	private float fmlThree = three;
	private float fmlFour = four;
	
	private int flag;
	
	public MontainYelloSoilOne(Context context) {
		super(context);
	}
	
	public MontainYelloSoilOne(Context context, AttributeSet attrs,  
            int defStyle) {  
        super(context, attrs, defStyle);  
    }  
  
	//这一个构造函数是必须的
    public MontainYelloSoilOne(Context context, AttributeSet attrs) {  
        super(context, attrs);  
        paint.setColor(Color.WHITE);
		paint.setStyle(Paint.Style.STROKE);//设置空心
		paint.setStrokeWidth(3);  
		paint.setTextSize(30);
    }  
	
	@SuppressLint({ "UseValueOf", "ClickableViewAccessibility" }) 
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (fmlFour-10 < event.getY() && event.getY() < fmlFour + 10) {
			flag = 4;
		}else if (fmlThree-10 < event.getY() && event.getY() < fmlThree + 10) {
			flag = 3;
		}else if (fmlTwo-10 < event.getY() && event.getY() < fmlTwo + 10) {
			flag = 2;
		}else if (fmlOne-10 < event.getY() && event.getY() < fmlOne + 10){
			flag = 1;
		}
		switch (flag) {
		case 4:
			if (event.getY()> fmlThree+20 && event.getY()<1280) {
				fmlFour = event.getY();
				invalidate(); //重新绘制区域
			}
			break;
		case 3:
			if (event.getY()> fmlTwo+20 && event.getY()<fmlFour-20) {
				fmlThree = event.getY();
				invalidate(); //重新绘制区域
			}
			break;
		case 2:
			if (event.getY()> fmlOne+20 && event.getY()<fmlThree-20) {
				fmlTwo = event.getY();
				invalidate(); //重新绘制区域
			}
			break;
		case 1:
			if (event.getY()> 20 && event.getY()<fmlTwo-20) {
				fmlOne = event.getY();
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
        
		width = 600;
		height = canvas.getHeight();

		setImageResource(R.drawable.transparent_backgroud_frame);
		 
		//枯枝落叶层——O
		DrawLegend.DrawProfileO(canvasBitmap, paint, width, fmlOne);
		
		//腐殖质层——A
		DrawLegend.DrawProfileA(canvasBitmap, paint, width, fmlOne, fmlTwo);
		
		//过渡层——AB
		DrawLegend.DrawProfileAB(canvasBitmap, paint, width, fmlTwo, fmlThree);
		
		//沉淀层——B
		DrawLegend.DrawProfileB(canvasBitmap, paint, width, fmlThree, fmlFour);
		
		//母质质层——C
		DrawLegend.DrawProfileC(canvasBitmap, paint, width, fmlFour, height);
		
		canvasBitmap.drawText("O", 650, fmlOne/2, paint);
		canvasBitmap.drawText("A", 650, (fmlOne+fmlTwo)/2, paint);
		canvasBitmap.drawText("AB", 650, (fmlThree+fmlTwo)/2, paint);
		canvasBitmap.drawText("B", 650, (fmlThree+fmlFour)/2, paint);
		canvasBitmap.drawText("C", 650, (fmlFour+1000)/2, paint);
		
		canvasBitmap.drawLine(0f, fmlOne, width, fmlOne, paint);
		canvasBitmap.drawLine(0f, fmlTwo, width, fmlTwo, paint);
		canvasBitmap.drawLine(0f, fmlThree, width, fmlThree, paint);
		canvasBitmap.drawLine(0f, fmlFour, width, fmlFour, paint);  
		
		canvasBitmap.drawLine(600, 0, 600, 1038, paint);
		
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
