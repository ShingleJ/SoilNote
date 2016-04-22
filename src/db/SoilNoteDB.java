package db;

import java.util.ArrayList;
import java.util.List;

import model.InfoAttrFeat;
import model.InfoAttrRec;
import model.InfoGeo;
import android.R.integer;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Point;

/*
 * SoilNoteDB类用于封装一些常用的数据库操作，如
 * 将InfoGeo实例存入数据库
 * 从数据库读取所有InfoGeo信息
 */

public class SoilNoteDB {
	/*
	 * 数据库名
	 */
	public static final String DB_NAME = "soil_note";

	/*
	 * 数据库版本
	 */
	public static final int VERSION = 1;

	/*
	 * 声明SoilNoteDB对象
	 */
	private static SoilNoteDB soilNoteDB;

	private SQLiteDatabase db;

	/*
	 * 将构造方法私有化 在实例化这个类的时候，也同时实例化SoilNoteOpenHelper类，并且创建好数据库
	 */
	private SoilNoteDB(Context context) {
		SoilNoteOpenHelper dbHelper = new SoilNoteOpenHelper(context, DB_NAME,
				null, VERSION);// 实例化
		db = dbHelper.getWritableDatabase();// 创建数据库中的表
	}

	/*
	 * 获取SoilNoteDB的实例
	 */
	public synchronized static SoilNoteDB getInstance(Context context) {
		if (soilNoteDB == null) {
			soilNoteDB = new SoilNoteDB(context);
		}
		return soilNoteDB;
	}

	/*
	 * 将InfoGeo实例存储到数据库
	 */
	public void saveInfoGeo(InfoGeo infoGeo) {
		if (infoGeo != null) {
			ContentValues values = new ContentValues();
			values.put("imagePath", infoGeo.getImagePath());
			values.put("latitude", infoGeo.getLatitude());
			values.put("longtitude", infoGeo.getLongtitude());
			values.put("position", infoGeo.getPosition());
			db.insert("InfoGeo", null, values);
		}
	}

	/*
	 * 从数据库读取所有照片的地理信息
	 */
	public List<InfoGeo> loadInfoGeo() {
		List<InfoGeo> list = new ArrayList<InfoGeo>();
		Cursor cursor = db.query("InfoGeo", null, null, null, null, null, null);
		if (cursor.moveToFirst()) {
			do {
				InfoGeo infoGeo = new InfoGeo();
				infoGeo.setId(cursor.getInt(cursor.getColumnIndex("id")));
				infoGeo.setImagePath(cursor.getString(cursor
						.getColumnIndex("imagePath")));
				infoGeo.setLatitude(cursor.getDouble(cursor
						.getColumnIndex("latitude")));
				infoGeo.setLongtitude(cursor.getDouble(cursor
						.getColumnIndex("longtitude")));
				infoGeo.setPosition(cursor.getString(cursor
						.getColumnIndex("position")));
				list.add(infoGeo);
			} while (cursor.moveToNext());
		}
		if (cursor != null) {
			cursor.close();
		}
		return list;
	}
	
	/*
	 * 将InfoAttrFeat实例存储到数据库
	 */
	public void saveInfoAttrFeat(InfoAttrFeat infoAttrFeat) {
		if (infoAttrFeat != null) {
			ContentValues values = new ContentValues();
//			values.put("id", infoAttrFeat.getId());
			values.put("layer", infoAttrFeat.getLayer());
			values.put("depth", infoAttrFeat.getDepth());
			values.put("color_dry", infoAttrFeat.getColor_dry());
			values.put("color_wet", infoAttrFeat.getColor_wet());
			values.put("humidity", infoAttrFeat.getHumidity());
			values.put("texture", infoAttrFeat.getTexture());
			values.put("structure", infoAttrFeat.getStructure());
			values.put("compactness", infoAttrFeat.getCompactness());
			values.put("porosity", infoAttrFeat.getPorosity());
			values.put("newGrowth_class", infoAttrFeat.getNewGrowth_class());
			values.put("newGrowth_morphology", infoAttrFeat.getNewGrowth_morphology());
			values.put("newGrowth_number", infoAttrFeat.getNewGrowth_number());
			values.put("intrusion", infoAttrFeat.getIntrusion());
			values.put("rootSys", infoAttrFeat.getRootSys());
			values.put("meas_PH", infoAttrFeat.getMeasure_PH());
			values.put("meas_limy_react", infoAttrFeat.getMeasure_limy_reaction());
			db.insert("InfoAttrFeat", null, values);
		}
	}
	
	/*
	 * 从数据库读取所有土壤剖面的性状信息
	 */
	public List<InfoAttrFeat> loadInfoAttrFeat() {
		List<InfoAttrFeat> list = new ArrayList<InfoAttrFeat>();
		Cursor cursor = db.query("InfoAttrFeat", null, null, null, null, null, null);
		if (cursor.moveToFirst()) {
			do {
				InfoAttrFeat infoAttrFeat = new InfoAttrFeat();
//				infoAttrFeat.setId(cursor.getInt(cursor.getColumnIndex("id")));
				infoAttrFeat.setLayer(cursor.getString(cursor
						.getColumnIndex("layer")));
				infoAttrFeat.setDepth(cursor.getFloat(cursor
						.getColumnIndex("depth")));
				infoAttrFeat.setColor_dry(cursor.getString(cursor
						.getColumnIndex("color_dry")));
				infoAttrFeat.setColor_wet(cursor.getString(cursor
						.getColumnIndex("color_wet")));
				infoAttrFeat.setHumidity(cursor.getString(cursor
						.getColumnIndex("humidity")));
				infoAttrFeat.setTexture(cursor.getString(cursor
						.getColumnIndex("texture")));
				infoAttrFeat.setStructure(cursor.getString(cursor
						.getColumnIndex("structure")));
				infoAttrFeat.setCompactness(cursor.getString(cursor
						.getColumnIndex("compactness")));
				infoAttrFeat.setPorosity(cursor.getString(cursor
						.getColumnIndex("porosity")));
				infoAttrFeat.setNewGrowth_class(cursor.getString(cursor
						.getColumnIndex("newGrowth_class")));
				infoAttrFeat.setNewGrowth_morphology(cursor.getString(cursor
						.getColumnIndex("newGrowth_morphology")));
				infoAttrFeat.setNewGrowth_number(cursor.getString(cursor
						.getColumnIndex("newGrowth_number")));
				infoAttrFeat.setIntrusion(cursor.getString(cursor
						.getColumnIndex("intrusion")));
				infoAttrFeat.setRootSys(cursor.getString(cursor
						.getColumnIndex("rootSys")));
				infoAttrFeat.setMeasure_PH(cursor.getString(cursor
						.getColumnIndex("meas_PH")));
				infoAttrFeat.setMeasure_limy_reaction(cursor.getString(cursor
						.getColumnIndex("meas_limy_react")));
				list.add(infoAttrFeat);
			} while (cursor.moveToNext());
		}
		if (cursor != null) {
			cursor.close();
		}
		return list;
	}
	
	/*
	 * 将InfoAttrRec实例存储到数据库
	 */
	public void saveInfoAttrRec(InfoAttrRec infoAttrRec) {
		if (infoAttrRec != null) {
			ContentValues values = new ContentValues();
			values.put("NO", infoAttrRec.getNO());
			values.put("date", infoAttrRec.getDate());
			values.put("weather", infoAttrRec.getWeather());
			values.put("investigator", infoAttrRec.getInvestigator());
			values.put("position", infoAttrRec.getPosition());
			values.put("sheet_NO", infoAttrRec.getSheet_NO());
			values.put("common_name", infoAttrRec.getCommon_name());
			values.put("formal_name", infoAttrRec.getFormal_name());
			values.put("terrain", infoAttrRec.getTerrain());
			values.put("altitude", infoAttrRec.getAltitude());
			values.put("prnt_mat_type", infoAttrRec.getPrnt_mat_type());
			values.put("nat_veg", infoAttrRec.getNat_veg());
			values.put("erode_stat", infoAttrRec.getErode_sitn());
			values.put("phreatic_level", infoAttrRec.getPhreatic_level());
			values.put("water_quality", infoAttrRec.getWater_quality());
			values.put("landuse", infoAttrRec.getLanduse());
			values.put("irrig_drainage", infoAttrRec.getIrrig_drainage());
			values.put("ferti_stat", infoAttrRec.getFerti_stat());
			values.put("human_effect", infoAttrRec.getHuman_effect());
			values.put("crop_rotat_stat", infoAttrRec.getCrop_rotat_stat());
			values.put("yield", infoAttrRec.getYield());
			values.put("review", infoAttrRec.getReview());
			db.insert("InfoAttrRec", null, values);
		}
	}
	
	/*
	 * 从数据库读取所有土壤剖面的记录信息
	 */
	public List<InfoAttrRec> loadInfoAttrRec() {
		List<InfoAttrRec> list = new ArrayList<InfoAttrRec>();
		Cursor cursor = db.query("InfoAttrRec", null, null, null, null, null, null);
		if (cursor.moveToFirst()) {
			do {
				InfoAttrRec infoAttrRec = new InfoAttrRec();
//				infoAttrFeat.setId(cursor.getInt(cursor.getColumnIndex("id")));
				infoAttrRec.setNO(cursor.getString(cursor
						.getColumnIndex("NO")));
				infoAttrRec.setDate(cursor.getInt(cursor
						.getColumnIndex("date")));
				infoAttrRec.setWeather(cursor.getString(cursor
						.getColumnIndex("weather")));
				infoAttrRec.setInvestigator(cursor.getString(cursor
						.getColumnIndex("investigator")));
				infoAttrRec.setPosition(cursor.getString(cursor
						.getColumnIndex("position")));
				infoAttrRec.setSheet_NO(cursor.getString(cursor
						.getColumnIndex("sheet_NO")));
				infoAttrRec.setCommon_name(cursor.getString(cursor
						.getColumnIndex("common_name")));
				infoAttrRec.setFormal_name(cursor.getString(cursor
						.getColumnIndex("formal_name")));
				infoAttrRec.setTerrain(cursor.getString(cursor
						.getColumnIndex("terrain")));
				infoAttrRec.setAltitude(cursor.getString(cursor
						.getColumnIndex("altitude")));
				infoAttrRec.setPrnt_mat_type(cursor.getString(cursor
						.getColumnIndex("prnt_mat_type")));
				infoAttrRec.setNat_veg(cursor.getString(cursor
						.getColumnIndex("nat_veg")));
				infoAttrRec.setErode_sitn(cursor.getString(cursor
						.getColumnIndex("erode_stat")));
				infoAttrRec.setPhreatic_level(cursor.getString(cursor
						.getColumnIndex("phreatic_level")));
				infoAttrRec.setWater_quality(cursor.getString(cursor
						.getColumnIndex("water_quality")));
				infoAttrRec.setLanduse(cursor.getString(cursor
						.getColumnIndex("landuse")));
				infoAttrRec.setIrrig_drainage(cursor.getString(cursor
						.getColumnIndex("irrig_drainage")));
				infoAttrRec.setFerti_stat(cursor.getString(cursor
						.getColumnIndex("ferti_stat")));
				infoAttrRec.setHuman_effect(cursor.getString(cursor
						.getColumnIndex("human_effect")));
				infoAttrRec.setCrop_rotat_stat(cursor.getString(cursor
						.getColumnIndex("crop_rotat_stat")));
				infoAttrRec.setYield(cursor.getString(cursor
						.getColumnIndex("yield")));
				infoAttrRec.setReview(cursor.getString(cursor
						.getColumnIndex("review")));
				list.add(infoAttrRec);
			} while (cursor.moveToNext());
		}
		if (cursor != null) {
			cursor.close();
		}
		return list;
	}	
	
	//将Model_1的屏幕坐标存储到数据库中
	public void saveProfileModel() {
		ContentValues values = new ContentValues();
		values.put("XPoint", 0);
		values.put("YPoint", 50);
		values.put("IsStart", true);
		values = new ContentValues();
		values.put("XPoint", 450);
		values.put("YPoint", 50);
		values.put("IsStart", false);
		db.insert("Model_1", null, values);
	}
	
	public List<Point> loadProfileModel() {
		List<Point> list = new ArrayList<Point>(); 
		Cursor cursor = db.query("Model_1", null, null, null, null, null, null);
		if (cursor.moveToFirst()) {
			do {
				int x = cursor.getInt(cursor.getColumnIndex("XPoint"));
			    int y = cursor.getInt(cursor.getColumnIndex("YPoint"));
				Point point = new Point(x, y);
				list.add(point);
			} while (cursor.moveToNext());
		}
		if (cursor != null) {
			cursor.close();
		}
		return list;
	}
}
