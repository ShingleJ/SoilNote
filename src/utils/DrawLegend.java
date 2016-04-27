package utils;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;

public class DrawLegend {
	public static void DrawProfileO(Canvas canvas, Paint paint, float width, float height) {
		//��֦��Ҷ�㡪��O
		//TODO ��һ��ͼ���������
		for(int i = 0; i<100;i++){
			float x = (float) (Math.random()*width);
			float y = (float) (Math.random()*height);
			canvas.drawPoint(x, y, paint);
		}
		canvas.drawText("O", width+50, height/2, paint);
	}
	
	public static void DrawProfileA(Canvas canvas, Paint paint, float width, float height1, float height2) {
		//��ֳ�ʲ㡪��A
		for(int i = (int) (height1 + 25); i<height2;){
			for(int j = 1;j<width;){
				canvas.drawText("��", j, i, paint);
				j += 40;
			}
			i += 30;
		}
		canvas.drawText("A", width+50, (height1+height2)/2, paint);
	}
	
	public static void DrawProfileAB(Canvas canvas, Paint paint, float width, float height1, float height2) {
		//���ɲ㡪��AB
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
	}
	
	public static void DrawProfileB(Canvas canvas, Paint paint, float width, float height1, float height2) {
		//����㡪��B
		Path path = new Path(); //����һ��·��
		float len = (height2 - height1 - 60)/4;
		for (int i = (int) height1; i < height2; ) {
			for(int j = 40;j<width;){
				canvas.drawLine(j, i, j, i+len, paint);
				path.moveTo(j-10, i+len-10); //�ƶ��� ����   
		        path.lineTo(j, i+len);   
		        path.lineTo(j+10, i+len-10);   
		        canvas.drawPath(path, paint);
				path.reset();
				j += 50;
			}
			i += len+20;
		}
		canvas.drawText("B", width+50, (height1+height2)/2, paint);
	}
	
	public static void DrawProfileC(Canvas canvas, Paint paint, float width, float height1, float height2){
		//ĸ���ʲ㡪��C
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
	}
	
	public static void DrawProfileC2(Canvas canvas, Paint paint, float width, float height1, float height2){
		//ĸ���ʲ㡪��C
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
	}
	
	public static void DrawProfileC1(Canvas canvas, Paint paint, float width, float height1, float height2){
		//ĸ�ʲ㡪��C1
		int gap = (int) (width/10);
		for(int i = 0;i<=width-gap;){
			canvas.drawLine(i+gap, height1, i, height2, paint);
			i += gap;
		}
		canvas.drawText("C1", width+50, (height1+height2)/2, paint);
	}
	
	public static void DrawProfileBC(Canvas canvas, Paint paint, float width, float height1, float height2){
		//���ɲ㡪��BC
		for (int i = (int) height1; i < height2; ) {
			canvas.drawLine(0f, i, width, i, paint);
			i += 30;
		}
		canvas.drawText("BC", width+50, (height1+height2)/2, paint);
	}
	
	public static void DrawProfileE(Canvas canvas, Paint paint, float width, float height1, float height2) {
		//���ܲ㡪��E
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
	}
}
