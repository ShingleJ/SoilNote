package utils;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;

public class BaseProfileModel extends ImageView{
	
	private float one;
	private float two;
	private float three;
	private float four;
	
	private float fomalOne = one;
	private float fomalTwo = two;
	private float fomalThree = three;
	private float fomalFour = four;
	
	private int flag;
	
	public BaseProfileModel(Context context) {
		super(context);
	}
	
	public BaseProfileModel(Context context, AttributeSet attrs,  
            int defStyle) {  
        super(context, attrs, defStyle);  
    }  
  
    public BaseProfileModel(Context context, AttributeSet attrs) {  
        super(context, attrs);  
    }
    
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
    
	
}
