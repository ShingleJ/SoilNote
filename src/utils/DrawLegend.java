package utils;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;

public class DrawLegend {
	public static void DrawProfileO(Canvas canvas, Paint paint, float width, float height) {
		//枯枝落叶层——O
		//TODO 这一层图例重新设计
		for(int i = 0; i<100;i++){
			float x = (float) (Math.random()*width);
			float y = (float) (Math.random()*height);
			canvas.drawPoint(x, y, paint);
		}
	}
	
	public static void DrawProfileA(Canvas canvas, Paint paint, float width, float height1, float height2) {
		//腐殖质层——A
		for(int i = (int) (height1 + 25); i<height2;){
			for(int j = 1;j<width;){
				canvas.drawText("×", j, i, paint);
				j += 40;
			}
			i += 30;
		}
	}
	
	public static void DrawProfileAB(Canvas canvas, Paint paint, float width, float height1, float height2) {
		//过渡层——AB
		RectF rect = new RectF(0, 0, 0, 0);
		for(int i = (int) (height1);i<height2-50;){
			for(int j = 1;j<width-35;){
				rect.set(j, i, j+50, i+50);
				canvas.drawArc(rect, 45, 90, false, paint);
				j += 35;
			}
			i += 50;
		}
	}
	
	public static void DrawProfileB(Canvas canvas, Paint paint, float width, float height1, float height2) {
		//沉淀层——B
		Path path = new Path(); //定义一条路径
		float len = (height2 - height1 - 60)/4;
		for (int i = (int) height1; i < height2; ) {
			for(int j = 40;j<width;){
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
	}
	
	public static void DrawProfileC(Canvas canvas, Paint paint, float width, float height1, float height2){
		//母质质层——C
		int count = 0;
		int j;
		for(int i = (int) (height1);i<height2-20;){
			canvas.drawLine(0,i, width, i, paint);
			j = (count%2==0)?50:0;
			for(;j<width;){
				canvas.drawLine(j, i, j, i+20, paint);
				j+=100;
			}
			count++;
			i+=40;
		}
	}
	
	public static void DrawProfileC1(Canvas canvas, Paint paint, float width, float height1, float height2){
		//母质层——C1
		int gap = (int) (width/10);
		for(int i = 0;i<=width-gap;){
			canvas.drawLine(i+gap, height1, i, height2, paint);
			i += gap;
		}
	}
	
	public static void DrawProfileBC(Canvas canvas, Paint paint, float width, float height1, float height2){
		//过渡层——BC
		for (int i = (int) height1; i < height2; ) {
			canvas.drawLine(0f, i, width, i, paint);
			i += 30;
		}
	}
	
	public static void DrawProfileE(Canvas canvas, Paint paint, float width, float height1, float height2) {
		//淋溶层——E
		int n = 0;
		int k;
		for(int i = (int) (height1);i<height2-40;){
			k = (n%2==0)?50:0;
			for(;k<width;){
				canvas.drawLine(k, i, k, i+40, paint);
				k+=100;
			}
			n++;
			i+=40;
		}
	}
}
