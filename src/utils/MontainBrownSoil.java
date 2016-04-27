package utils;

import java.util.ArrayList;
import java.util.List;

import com.app.soilnote.R;

import android.R.integer;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;

public class MontainBrownSoil extends ImageView{

	private Paint paint = new Paint();
	private Bitmap bitmap = null;   //用于存储模板
    private Canvas canvasBitmap;
    
    private int width, height;
	
	private float one = (float)1038*5/60;
	private float two = (float)1038*10/60;
	private float three = (float)1038*20/60;
	private float four = (float)1038*30/60;
	
	private float fmlOne = one;
	private float fmlTwo = two;
	private float fmlThree = three;
	private float fmlFour = four;
	
	private List<Boolean> isDraw = new ArrayList<Boolean>();
	
	private int flag;
	
	public MontainBrownSoil(Context context) {
		super(context);
	}
	
	public MontainBrownSoil(Context context, AttributeSet attrs,  
            int defStyle) {  
        super(context, attrs, defStyle);  
    }  
  
	//这一个构造函数是必须的
    public MontainBrownSoil(Context context, AttributeSet attrs) {  
        super(context, attrs);  
		paint.setColor(Color.WHITE);
		paint.setStyle(Paint.Style.STROKE);//设置空心
		paint.setStrokeWidth(3);  
		paint.setTextSize(30);
		
		isDraw.add(true);
		isDraw.add(true);
		isDraw.add(true);
		isDraw.add(true);
		isDraw.add(true);
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
		
		if (isDraw.get(0)) {
			//枯枝落叶层——O
			DrawLegend.DrawProfileO(canvasBitmap, paint, width, fmlOne);
		}
		
		if (isDraw.get(1)) {
			//腐殖质层——A
			DrawLegend.DrawProfileA(canvasBitmap, paint, width, fmlOne, fmlTwo);
			canvasBitmap.drawLine(0f, fmlOne, width, fmlOne, paint);
		}
		
		if (isDraw.get(2)) {
			//淋溶层——E
			DrawLegend.DrawProfileE(canvasBitmap, paint, width, fmlTwo, fmlThree);
			canvasBitmap.drawLine(0f, fmlTwo, width, fmlTwo, paint);
		}
		
		if (isDraw.get(3)) {
			//沉淀层——B
			DrawLegend.DrawProfileB(canvasBitmap, paint, width, fmlThree, fmlFour);
			canvasBitmap.drawLine(0f, fmlThree, width, fmlThree, paint);
		}
		
		if (isDraw.get(4)) {
			//母质质层——C
			DrawLegend.DrawProfileC(canvasBitmap, paint, width, fmlFour, height);
			canvasBitmap.drawLine(0f, fmlFour, width, fmlFour, paint);  
		}
		
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
	
	int num =  0;
	public void deleteProfile(int n) {
		isDraw.set(n-1+num, false);
		if (num<isDraw.size()-1) {
			num++;
		}
		float temp = fmlOne;
		float temp1 = fmlTwo;
		float temp2 = fmlThree;
		float temp3 = fmlFour;
		fmlOne = 0;
		fmlTwo = temp;
		fmlThree = temp1;
		fmlFour = temp2;
		height = (int) temp3;
		invalidate();
	}

}
