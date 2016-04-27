package utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;

public class RoundBitmap {
//	 public static Bitmap toRoundBitmap(Bitmap bitmap) {  
//	        //Բ��ͼƬ���  
//	        int width = bitmap.getWidth();  
//	        int height = bitmap.getHeight();  
//	        //�����εı߳�  
//	        int r = 0;  
//	        //ȡ��̱����߳�  
//	        if(width > height) {  
//	            r = height;  
//	        } else {  
//	            r = width;  
//	        }  
//	        //����һ��bitmap  
//	        Bitmap backgroundBmp = Bitmap.createBitmap(width,  
//	                 height, Config.ARGB_8888);  
//	        //newһ��Canvas����backgroundBmp�ϻ�ͼ  
//	        Canvas canvas = new Canvas(backgroundBmp);  
//	        Paint paint = new Paint();  
//	        //���ñ�Ե�⻬��ȥ�����  
//	        paint.setAntiAlias(true);  
//	        //�����ȣ���������  
//	        RectF rect = new RectF(0, 0, r, r);  
//	        //ͨ���ƶ���rect��һ��Բ�Ǿ��Σ���Բ��X�᷽��İ뾶����Y�᷽��İ뾶ʱ��  
//	        //�Ҷ�����r/2ʱ����������Բ�Ǿ��ξ���Բ��  
//	        canvas.drawRoundRect(rect, r/2, r/2, paint);  
//	        //���õ�����ͼ���ཻʱ��ģʽ��SRC_INΪȡSRCͼ���ཻ�Ĳ��֣�����Ľ���ȥ��  
//	        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));  
//	        //canvas��bitmap����backgroundBmp��  
//	        canvas.drawBitmap(bitmap, null, rect, paint);  
//	        //�����Ѿ��滭�õ�backgroundBmp  
//	        return backgroundBmp;  
//	    }
	
	 /**
     * ת��ͼƬ��Բ��
     * @param bitmap ����Bitmap����
     * @return
     */
    public static Bitmap toRoundBitmap(Bitmap bitmap)
    {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float roundPx;
        float left,top,right,bottom,dst_left,dst_top,dst_right,dst_bottom;
        if (width <= height) {
            roundPx = width / 2 -5;
            top = 0;
            bottom = width;
            left = 0;
            right = width;
            height = width;
            dst_left = 0;
            dst_top = 0;
            dst_right = width;
            dst_bottom = width;
        } else {
            roundPx = height / 2 -5;
            float clip = (width - height) / 2;
            left = clip;
            right = width - clip;
            top = 0;
            bottom = height;
            width = height;
            dst_left = 0;
            dst_top = 0;
            dst_right = height;
            dst_bottom = height;
        }

        Bitmap output = Bitmap.createBitmap(width,
                height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect src = new Rect((int)left, (int)top, (int)right, (int)bottom);
        final Rect dst = new Rect((int)dst_left, (int)dst_top, (int)dst_right, (int)dst_bottom);
        final RectF rectF = new RectF(dst_left+15, dst_top+15, dst_right-20, dst_bottom-20);

        paint.setAntiAlias(true);

        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);

        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, src, dst, paint);
        return output;
    }
}
