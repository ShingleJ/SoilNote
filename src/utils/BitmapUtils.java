package utils;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class BitmapUtils {
	 /**
	   * @description ����ͼƬ��ѹ������
	   *
	   * @param options ����
	   * @param reqWidth Ŀ��Ŀ��
	   * @param reqHeight Ŀ��ĸ߶�
	   * @return
	   */
	  private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
	    // ԴͼƬ�ĸ߶ȺͿ��
	    final int height = options.outHeight;
	    final int width = options.outWidth;
	    int inSampleSize = 1;
	    if (height > reqHeight || width > reqWidth) {
	      // �����ʵ�ʿ�ߺ�Ŀ���ߵı���
	      final int halfHeight = height / 2;
	      final int halfWidth = width / 2;
	      while ((halfHeight / inSampleSize) > reqHeight && (halfWidth / inSampleSize) > reqWidth) {
	        inSampleSize *= 2;
	      }
	    }
	    return inSampleSize;
	  }
	  /**
	   * @description ͨ�������bitmap������ѹ�����õ����ϱ�׼��bitmap
	   *
	   * @param src
	   * @param dstWidth
	   * @param dstHeight
	   * @return
	   */
	  private static Bitmap createScaleBitmap(Bitmap src, int dstWidth, int dstHeight, int inSampleSize) {
	    //���inSampleSize��2�ı�����Ҳ��˵���src�Ѿ���������Ҫ������ͼ�ˣ�ֱ�ӷ��ؼ��ɡ�
	    if (inSampleSize % 2 == 0) {
	      return src;
	    }
	    // ����ǷŴ�ͼƬ��filter�����Ƿ�ƽ�����������СͼƬ��filter��Ӱ�죬������������СͼƬ������ֱ������Ϊfalse
	    Bitmap dst = Bitmap.createScaledBitmap(src, dstWidth, dstHeight, false);
	    if (src != dst) { // ���û�����ţ���ô������
	      src.recycle(); // �ͷ�Bitmap��native��������
	    }
	    return dst;
	  }
	  /**
	   * @description ��Resources�м���ͼƬ
	   *
	   * @param res
	   * @param resId
	   * @param reqWidth
	   * @param reqHeight
	   * @return
	   */
	  public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId, int reqWidth, int reqHeight) {
	    final BitmapFactory.Options options = new BitmapFactory.Options();
	    options.inJustDecodeBounds = true; // ���ó���true,��ռ���ڴ棬ֻ��ȡbitmap���
	    BitmapFactory.decodeResource(res, resId, options); // ��ȡͼƬ����
	    options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight); // �������涨��ķ�������inSampleSizeֵ
	    // ʹ�û�ȡ����inSampleSizeֵ�ٴν���ͼƬ
	    options.inJustDecodeBounds = false;
	    Bitmap src = BitmapFactory.decodeResource(res, resId, options); // ����һ���Դ������ͼ
	    return createScaleBitmap(src, reqWidth, reqHeight, options.inSampleSize); // ��һ���õ�Ŀ���С������ͼ
	  }
	  /**
	   * @description ��SD���ϼ���ͼƬ
	   *
	   * @param pathName
	   * @param reqWidth
	   * @param reqHeight
	   * @return
	   */
	  public static Bitmap decodeSampledBitmapFromFile(String pathName, int reqWidth, int reqHeight) {
	    final BitmapFactory.Options options = new BitmapFactory.Options();
	    options.inJustDecodeBounds = true;
	    BitmapFactory.decodeFile(pathName, options);
	    options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
	    options.inJustDecodeBounds = false;
	    Bitmap src = BitmapFactory.decodeFile(pathName, options);
	    return createScaleBitmap(src, reqWidth, reqHeight, options.inSampleSize);
	  }
}
