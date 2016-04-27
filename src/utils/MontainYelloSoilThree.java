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

public class MontainYelloSoilThree extends BaseProfileModel{
	
	private static float one = (float)1038*5/60;
	private static float two = (float)1038*15/60;
	private static float three = (float)1038*20/60;
	private static float four = (float)1038*50/60;
	
	private static float[] lines = {0, one, two, three, four, 1038};
	
	//��һ�����캯���Ǳ����
    public MontainYelloSoilThree(Context context, AttributeSet attrs) {  
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
		}
		switch (flag) {
		case 4:
			if (event.getY()> fmlLines[3]+20 && event.getY()<1280) {
				fmlLines[4] = event.getY();
				invalidate(); //���»�������
			}
			break;
		case 3:
			if (event.getY()> fmlLines[2]+20 && event.getY()<fmlLines[4]-20) {
				fmlLines[3] = event.getY();
				invalidate(); //���»�������
			}
			break;
		case 2:
			if (event.getY()> fmlLines[1]+20 && event.getY()<fmlLines[3]-20) {
				fmlLines[2] = event.getY();
				invalidate(); //���»�������
			}
			break;
		case 1:
			if (event.getY()> 20 && event.getY()<fmlLines[2]-20) {
				fmlLines[1] = event.getY();
				invalidate(); //���»�������
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
			//��֦��Ҷ�㡪��O
			DrawLegend.DrawProfileO(canvasBitmap, paint, width, fmlLines[1]);
		}
		if (isDraw[1]) {
			//��ֳ�ʲ㡪��A
			DrawLegend.DrawProfileA(canvasBitmap, paint, width, fmlLines[1], fmlLines[2]);
			canvasBitmap.drawLine(0f, fmlLines[1], width, fmlLines[1], paint);
		}
		if (isDraw[2]) {
			//����㡪��B
			DrawLegend.DrawProfileB(canvasBitmap, paint, width, fmlLines[2], fmlLines[3]);
			canvasBitmap.drawLine(0f, fmlLines[2], width, fmlLines[2], paint);
		}
		if (isDraw[3]) {
			//���ɲ㡪��BC
			DrawLegend.DrawProfileBC(canvasBitmap, paint, width, fmlLines[3], fmlLines[4]);
			canvasBitmap.drawLine(0f, fmlLines[3], width, fmlLines[3], paint);
		}
		if (isDraw[4]) {
			//ĸ���ʲ㡪��C
			DrawLegend.DrawProfileC(canvasBitmap, paint, width, fmlLines[4], height);
			canvasBitmap.drawLine(0f, fmlLines[4], width, fmlLines[4], paint);  
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
