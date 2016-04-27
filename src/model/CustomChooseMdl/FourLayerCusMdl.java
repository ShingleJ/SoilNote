package model.CustomChooseMdl;

import model.BaseProfileModel;
import utils.DrawLegend;

import com.app.soilnote.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class FourLayerCusMdl extends BaseProfileModel{
	private int[] pro_name;
	private static float[] lines = {0, 1038*1/4, 1038*2/4, 1038*3/4, 1038};
	
    public FourLayerCusMdl(Context context, AttributeSet attrs) {  
        super(context, attrs, lines); 
    }  
    
    public int[] getPro_name() {
    	return pro_name;
    }
    
    public void setPro_name(int[] pro_name) {
    	this.pro_name = new int[pro_name.length];
    	for (int i = 0; i < pro_name.length; i++) {
    		this.pro_name[i] = pro_name[i];
    	}
    }
    
    @SuppressLint({ "UseValueOf", "ClickableViewAccessibility" }) 
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (fmlLines[3]-10 < event.getY() && event.getY() < fmlLines[3] + 10) {
			flag = 3;
		}else if (fmlLines[2]-10 < event.getY() && event.getY() < fmlLines[2] + 10) {
			flag = 2;
		}else if (fmlLines[1]-10 < event.getY() && event.getY() < fmlLines[1] + 10){
			flag = 1;
		}
		switch (flag) {
		case 3:
			if (event.getY()> fmlLines[2]+20 && event.getY()<1038-20) {
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
		
		for (int i = 0; i < pro_name.length; i++) {
			if (isDraw[i]) {
				DrawLegend.drawCustomChoose(pro_name[i], canvas, paint, linePaint, width, fmlLines[i], fmlLines[i+1]);
			}
		}
		canvasBitmap.drawLine(600, 0, 600, 1038, paint);
		
		canvas.drawBitmap(bitmap, 0, 0 , paint);
	}
}
