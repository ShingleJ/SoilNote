package db;

import java.util.ArrayList;
import java.util.List;

import model.InfoGeo;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/*
 * SoilNoteDB�����ڷ�װһЩ���õ����ݿ��������
 * ��InfoGeoʵ���������ݿ�
 * �����ݿ��ȡ����InfoGeo��Ϣ
 */

public class SoilNoteDB {
	/*
	 * ���ݿ���
	 */
	public static final String DB_NAME = "soil_note";

	/*
	 * ���ݿ�汾
	 */
	public static final int VERSION = 1;

	/*
	 * ����SoilNoteDB����
	 */
	private static SoilNoteDB soilNoteDB;

	private SQLiteDatabase db;

	/*
	 * �����췽��˽�л� ��ʵ����������ʱ��Ҳͬʱʵ����SoilNoteOpenHelper�࣬���Ҵ��������ݿ�
	 */
	private SoilNoteDB(Context context) {
		SoilNoteOpenHelper dbHelper = new SoilNoteOpenHelper(context, DB_NAME,
				null, VERSION);// ʵ����
		db = dbHelper.getWritableDatabase();// �������ݿ��еı�
	}

	/*
	 * ��ȡSoilNoteDB��ʵ��
	 */
	public synchronized static SoilNoteDB getInstance(Context context) {
		if (soilNoteDB == null) {
			soilNoteDB = new SoilNoteDB(context);
		}
		return soilNoteDB;
	}

	/*
	 * ��InfoGeoʵ���洢�����ݿ�
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
	 * �����ݿ��ȡ������Ƭ�ĵ�����Ϣ
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
