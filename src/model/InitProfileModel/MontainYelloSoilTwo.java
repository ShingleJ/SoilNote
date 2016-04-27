package model.InitProfileModel;

import model.BaseProfileModel;
import utils.DrawLegend;

import com.app.soilnote.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class MontainYelloSoilTwo extends BaseProfileModel{

	private static float one = (float)1038*5/90;
	private static float two = (float)1038*10/90;
	private static float three = (float)1038*20/90;
	private static float four = (float)1038*40/90;
	private static float five = (float)1038*50/90;
	
	private static float[] lines = {0, one, two, three, four, five, 1038};
	
    public MontainYelloSoilTwo(Context context, AttributeSet attrs) {  
        super(context, attrs, lines);  
    }  
	
	@SuppressLint({ "UseValueOf", "ClickableViewAccessibility" }) 
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (fmlLines[4]-10 < event.getY() && event.getY() < fmlLines[4] + 10) {
			flag = 4;
		}else if (fmlLines[3]-10 < event.getY() && event.getY() < fmlLines[3] + 10) {
			flag = 3;
		}else if (fmlLines[2]-10 < event.getY() && event.getY() < fmlLines[2] + 10) {
			flag = 2;
		}else if (fmlLines[1]-10 < event.getY() && event.getY() < fmlLines[1] + 10){
			flag = 1;
		}else if (fmlLines[5]-10<event.getY() && event.getY() <fmlLines[5] +10){
			flag = 5;
		}
		switch (flag) {
		case 5:
			if (event.getY()>fmlLines[4]+20 && event.getY()<1038) {
				fmlLines[5] = event.getY();
				invalidate();
			}
		case 4:
			if (event.getY()> fmlLines[3]+20 && event.getY()<fmlLines[5]-20) {
				fmlLines[4] = event.getY();
				invalidate(); //重新绘制区域
			}
			break;
		case 3:
			if (event.getY()> fmlLines[2]+20 && event.getY()<fmlLines[4]-20) {
				fmlLines[3] = event.getY();
				invalidate(); //重新绘制区域
			}
			break;
		case 2:
			if (event.getY()> fmlLines[1]+20 && event.getY()<fmlLines[3]-20) {
				fmlLines[2] = event.getY();
				invalidate(); //重新绘制区域
			}
			break;
		case 1:
			if (event.getY()> 20 && event.getY()<fmlLines[2]-20) {
				fmlLines[1] = event.getY();
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
		 
		if (isDraw[0]) {
			//枯枝落叶层——O
			DrawLegend.drawProfileO(canvasBitmap, paint, linePaint, width, fmlLines[1]);
		}
		if (isDraw[1]) {
			//腐殖质层——A
			DrawLegend.drawProfileA(canvasBitmap, paint, linePaint, width, fmlLines[1], fmlLines[2]);
//			canvasBitmap.drawLine(0f, fmlLines[1], width, fmlLines[1], paint);
		}
		if (isDraw[2]) {
			//过渡层——AB
			DrawLegend.drawProfileAB(canvasBitmap, paint, linePaint, width, fmlLines[2], fmlLines[3]);
//			canvasBitmap.drawLine(0f, fmlLines[2], width, fmlLines[2], paint);
		}
		if (isDraw[3]) {
			//沉淀层——B
			DrawLegend.drawProfileB(canvasBitmap, paint, linePaint, width, fmlLines[3], fmlLines[4]);
//			canvasBitmap.drawLine(0f, fmlLines[3], width, fmlLines[3], paint);
		}
		if (isDraw[4]) {
			//母质质层——C1
			DrawLegend.drawProfileC1(canvasBitmap, paint, linePaint, width, fmlLines[4], fmlLines[5]);
//			canvasBitmap.drawLine(0f, fmlLines[4], width, fmlLines[4], paint);  
		}
		if (isDraw[5]) {
			//母质质层——C2
//			DrawLegend.drawProfileC(canvasBitmap, paint, width, fmlLines[5], height);
			DrawLegend.drawProfileAE(canvasBitmap, paint, linePaint, width, fmlLines[5], height);
//			canvasBitmap.drawLine(0f, fmlLines[5], width, fmlLines[5], paint);
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

}
