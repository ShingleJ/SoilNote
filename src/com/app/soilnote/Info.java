package com.app.soilnote;

import java.util.ArrayList;
import java.util.List;

public class Info {
	private double latitude;// 经度
	private double longtitude;// 纬度
	// 这里用的图片是id，实际项目中用的肯定是URL
	// 用户搜索的时候，从服务器下载字符串，然后进行解析，转化为实体
	// 这里直接把图片放到资源文件下
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
