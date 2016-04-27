package utils;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;

public class DrawLegend {
	public static final int O = 10;
	public static final int A = 20;
	public static final int AE = 201;
	public static final int E = 30;
	public static final int EB = 301;
	public static final int B = 40;
	public static final int BC = 401;
	public static final int C = 50;
	public static final int AB = 202;
	public static final int R = 60;
	public static final int C1 = 51;
	public static final int C2 = 52;
	public static final int A1 = 21;
	public static final int A2 = 22;
	
	public static void drawProfileO(Canvas canvas, Paint paint, Paint linePaint, float width, float height) {
		//枯枝落叶层——O
		//TODO 这一层图例重新设计
		for(int i = 0; i<100;i++){
			float x = (float) (Math.random()*width);
			float y = (float) (Math.random()*height);
			canvas.drawPoint(x, y, paint);
		}
		canvas.drawText("O", width+50, height/2, paint);
		canvas.drawLine(0, height, width, height, linePaint);
	}
	
	public static void drawProfileA(Canvas canvas, Paint paint, Paint linePaint, float width, float height1, float height2) {
		//腐殖质层——A
		for(int i = (int) (height1 + 25); i<height2;){
			for(int j = 1;j<width;){
				canvas.drawText("×", j, i, paint);
				j += 40;
			}
			i += 30;
		}
		canvas.drawText("A", width+50, (height1+height2)/2, paint);
		canvas.drawLine(0, height2, width, height2, linePaint);
	}
	
	public static void drawProfileA1(Canvas canvas, Paint paint, Paint linePaint, float width, float height1, float height2) {
		//腐殖质层——A1
		for(int i = (int) (height1 + 25); i<height2;){
			for(int j = 40;j<width-70;){
				canvas.drawLine(j, i-10, j+15, i, paint);
				canvas.drawLine(j+15, i, j+45, i, paint);
				canvas.drawLine(j+45, i, j+60, i+10, paint);
				j += 90;
			}
			i += 60;
		}
		canvas.drawText("A1", width+50, (height1+height2)/2, paint);
		canvas.drawLine(0, height2, width, height2, linePaint);
	}
	
	public static void drawProfileAE(Canvas canvas, Paint paint, Paint linePaint, float width, float height1, float height2){
		//AE过渡层
		for(int i = (int) (height1+25); i<height2;){
			for(int j = 50;j<width-70;){
				canvas.drawLine(j-10, i-10, j-10, i, paint);
				canvas.drawLine(j-20, i, j+20, i, paint);
				canvas.drawLine(j+10, i, j+10, i+10, paint);
				j += 90;
			}
			i += 60;
		}
		canvas.drawText("AE", width+50, (height1+height2)/2, paint);
		canvas.drawLine(0, height2, width, height2, linePaint);
	}
	
	public static void drawProfileAB(Canvas canvas, Paint paint, Paint linePaint, float width, float height1, float height2) {
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
		canvas.drawText("AB", width+50, (height1+height2)/2, paint);
		canvas.drawLine(0, height2, width, height2, linePaint);
	}
	
	public static void drawProfileB(Canvas canvas, Paint paint, Paint linePaint, float width, float height1, float height2) {
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
		canvas.drawText("B", width+50, (height1+height2)/2, paint);
		canvas.drawLine(0, height2, width, height2, linePaint);
	}
	
	public static void drawProfileC(Canvas canvas, Paint paint, Paint linePaint, float width, float height1, float height2){
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
		canvas.drawText("C", width+50, (height1+height2)/2, paint);
		canvas.drawLine(0, height2, width, height2, linePaint);
	}
	
	public static void drawProfileC2(Canvas canvas, Paint paint, Paint linePaint, float width, float height1, float height2){
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
		canvas.drawText("C2", width+50, (height1+height2)/2, paint);
		canvas.drawLine(0, height2, width, height2, linePaint);
	}
	
	public static void drawProfileC1(Canvas canvas, Paint paint, Paint linePaint, float width, float height1, float height2){
		//母质层——C1
		int gap = (int) (width/10);
		for(int i = 0;i<=width-gap;){
			canvas.drawLine(i+gap, height1, i, height2, paint);
			i += gap;
		}
		canvas.drawText("C1", width+50, (height1+height2)/2, paint);
		canvas.drawLine(0, height2, width, height2, linePaint);
	}
	
	public static void drawProfileBC(Canvas canvas, Paint paint, Paint linePaint, float width, float height1, float height2){
		//过渡层——BC
		for (int i = (int) height1; i < height2; ) {
			canvas.drawLine(0f, i, width, i, paint);
			i += 30;
		}
		canvas.drawText("BC", width+50, (height1+height2)/2, paint);
		canvas.drawLine(0, height2, width, height2, linePaint);
	}
	
	public static void drawProfileE(Canvas canvas, Paint paint, Paint linePaint, float width, float height1, float height2) {
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
		canvas.drawText("E", width+50, (height1+height2)/2, paint);
		canvas.drawLine(0, height2, width, height2, linePaint);
	}
	
	public static void drawProfileEB(Canvas canvas, Paint paint, Paint linePaint, float width, float height1, float height2){
		//EB过渡层
		for(int i = (int) (height1);i<height2-40;){
			for(int j = 40;j<width-50;){
				canvas.drawLine(j, i, j+20, i+30, paint);
				canvas.drawLine(j+50, i, j+30, i+30, paint);
				j+=100;
			}
			i+=40;
		}
		canvas.drawText("EB", width+50, (height1+height2)/2, paint);
		canvas.drawLine(0, height2, width, height2, linePaint);
	}
	
	public static void drawProfileR(Canvas canvas, Paint paint, Paint linePaint, float width, float height1, float height2) {
		canvas.drawLine(0, height2, width, height2, linePaint);
	}
	
	public static void drawCustomChoose(int code, Canvas canvas, Paint paint, Paint linePaint, float width, float height1, float height2) {
		switch (code) {
		case O:
			drawProfileO(canvas, paint, linePaint, width, height1);
			break;
		case A:
			drawProfileA(canvas, paint, linePaint, width, height1, height2);
			break;
		case A1:
			drawProfileA1(canvas, paint, linePaint, width, height1, height2);
			break;
		case A2:
			drawProfileA(canvas, paint, linePaint, width, height1, height2);
			break;
		case AE:
			drawProfileAE(canvas, paint, linePaint, width, height1, height2);
			break;
		case AB:
			drawProfileAB(canvas, paint, linePaint, width, height1, height2);
			break;
		case E:
			drawProfileE(canvas, paint, linePaint, width, height1, height2);
			break;
		case EB:
			drawProfileEB(canvas, paint, linePaint, width, height1, height2);
			break;
		case B:
			drawProfileB(canvas, paint, linePaint, width, height1, height2);
			break;
		case BC:
			drawProfileBC(canvas, paint, linePaint, width, height1, height2);
			break;
		case C:
			drawProfileC(canvas, paint, linePaint, width, height1, height2);
			break;
		case C1:
			drawProfileC1(canvas, paint, linePaint, width, height1, height2);
			break;
		case C2:
			drawProfileC2(canvas, paint, linePaint, width, height1, height2);
			break;
		case R:
			drawProfileR(canvas, paint, linePaint, width, height1, height2);
			break;
		default:
			break;
		}
	}
}
