package db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class SoilNoteOpenHelper extends SQLiteOpenHelper {

	/*
	 * GeoInfo�������
	 */
	public static final String CREATE_GEO_INFO = "create table InfoGeo ("
			+ "id integer primary key autoincrement, " 
			+ "imagePath text, "
			+ "latitude real, "
			+ "longtitude real, " + "position text)";

	public SoilNoteOpenHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_GEO_INFO);// ����GeoInfo��
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO �Զ����ɵķ������

	}

}
