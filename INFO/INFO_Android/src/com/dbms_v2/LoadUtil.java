package com.dbms_v2;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class LoadUtil {
	
	public static SQLiteDatabase createOrOpenDatabase() {
		SQLiteDatabase sld=null;
		try{
			sld=SQLiteDatabase.openDatabase//连接并创建数据库，如果不存在则创建
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
	
	public static void createTableAndInit(String sql){//创建表
		Log.v("LoadUtil", "OpenDataBase");
		SQLiteDatabase sld=createOrOpenDatabase();//连接数据库
		try{
			sld.execSQL(sql);//执行SQL语句
			sld.close();//关闭连接
			Log.v("LoadUtil", sql + "Init Success");
		}catch(Exception e){
			Log.v("LoadUtil", sql + "Init Failure");
		}
	}
}
