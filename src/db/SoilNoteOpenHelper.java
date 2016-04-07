package db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class SoilNoteOpenHelper extends SQLiteOpenHelper {

	/*
	 * GeoInfo表建表语句
	 */
	public static final String CREATE_GEO_INFO = "create table InfoGeo ("
			+ "id integer primary key autoincrement, " 
			+ "imagePath text, "
			+ "latitude real, "
			+ "longtitude real, " + "position text)";
	
	public static final String 	CREATE_FEAT_ATTR_INFO = "create table InfoAttrFeat ("
			+"id integer primary key autoincrement, " + "layer text, "
			+"depth real, " + "color_dry text, " + "color_wet text, "
			+"humidity text, " + "texture text, " + "structure text, "
			+"compactness text, " + "porosity text, " + "newGrowth_class text, "
			+"newGrowth_morphology text, " + "newGrowth_number text, " + "intrusion text, "
			+"rootSys text, " + "meas_PH text, " + "meas_limy_react text)";
	
	public static final String 	CREATE_REC_ATTR_INFO = "create table InfoAttrRec ("
			+"id integer primary key autoincrement, " + "NO text" + "date integer, " + "weather text, "
			+"investigator text, " + "position text, " + "sheet_NO text, "
			+"common_name text, " + "formal_name text, " + "terrain text, "
			+"altitude text, " + "prnt_mat_type text, " + "nat_veg text, "
			+"erode_stat text, " + "phreatic_level text, " + "water_quality text, "
			+"landuse text, " + "irrig_drainage text, " + "ferti_stat text, "
			+"human_effect text, " + "crop_rotat_stat text, " + "yield text, " + "review text)";

	public SoilNoteOpenHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_GEO_INFO);// 创建GeoInfo表
		db.execSQL(CREATE_FEAT_ATTR_INFO);//创建InfoAttrFeat表
		db.execSQL(CREATE_REC_ATTR_INFO);//创建InfoAttrRec表
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO 自动生成的方法存根

	}

}
