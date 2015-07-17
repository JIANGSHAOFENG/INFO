package com.dbms_v2;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class LoadUtil {
	
	public static SQLiteDatabase createOrOpenDatabase() {
		SQLiteDatabase sld=null;
		try{
			sld=SQLiteDatabase.openDatabase//���Ӳ��������ݿ⣬����������򴴽�
			(
					"/data/data/com.dbms_v2/mydatabase.db", 
					null,
					SQLiteDatabase.OPEN_READWRITE|SQLiteDatabase.CREATE_IF_NECESSARY);
			        Log.v("LoadUtilPage", "OpenDatabase Success");

		}catch(Exception e)
		{
			e.printStackTrace();
			Log.v("LoadUtilPage", "OpenDatabase Failure");
		}
		return sld;
	}
	
	public static void createTableAndInit(String sql){//������
		Log.v("LoadUtil", "OpenDataBase");
		SQLiteDatabase sld=createOrOpenDatabase();//�������ݿ�
		try{
			sld.execSQL(sql);//ִ��SQL���
			sld.close();//�ر�����
			Log.v("LoadUtil", sql + "Init Success");
		}catch(Exception e){
			Log.v("LoadUtil", sql + "Init Failure");
		}
	}
}
