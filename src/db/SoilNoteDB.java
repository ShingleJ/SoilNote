package db;

import java.util.ArrayList;
import java.util.List;

import model.InfoGeo;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

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
			values.put("id", infoGeo.getId());
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
}
