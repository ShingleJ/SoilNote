package com.app.soilnote;

import java.util.ArrayList;
import java.util.List;

public class Info {
	private double latitude;// ����
	private double longtitude;// γ��
	// �����õ�ͼƬ��id��ʵ����Ŀ���õĿ϶���URL
	// �û�������ʱ�򣬴ӷ����������ַ�����Ȼ����н�����ת��Ϊʵ��
	// ����ֱ�Ӱ�ͼƬ�ŵ���Դ�ļ���
	private int imgId;
	private String name;
	private String distance;
	private int zan;

	public static List<Info> infos = new ArrayList<Info>();

	static {
		// Infos.add(new Info(latitude, longtitude, imgId, name, distance, zan))
	}

	public Info(double latitude, double longtitude, int imgId, String name,
			String distance, int zan) {
		this.latitude = latitude;
		this.longtitude = longtitude;
		this.imgId = imgId;
		this.name = name;
		this.distance = distance;
		this.zan = zan;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongtitude() {
		return longtitude;
	}

	public void setLongtitude(double longtitude) {
		this.longtitude = longtitude;
	}

	public int getImgId() {
		return imgId;
	}

	public void setImgId(int imgId) {
		this.imgId = imgId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDistance() {
		return distance;
	}

	public void setDistance(String distance) {
		this.distance = distance;
	}

	public int getZan() {
		return zan;
	}

	public void setZan(int zan) {
		this.zan = zan;
	}

}
