package com.dbms_v2;

import android.util.Log;

public class CreateTable {

	public static void createTable() {
		String[] sqll = new String[] {
				"create table if not exists Users " +
				"(User_id int primary key, User_Name String, User_Sex int, Password String)",
				"create table if not exists Courses " + 
				"(Course_id Integer primary key, Course_Name String, Course_Grade int, Course_Class_id int, Course_teacher String, Course_position String, Course_status int)",
				"create table if not exists Classes " + 
				"(Class_id Integer primary key, Class_Name String, Class_Grade int)",
				"create table if not exists Information " + 
				"(Information_id int primary key, Editer String, Class_id int, Course_id int, Title String, Year int, Month int, Day int, Hour int, Minute int, Date String, add_Date, Text String)",
				"insert into Users(User_id, User_Name, User_Sex, Password) values(12330000,'Admin', 0, '12330000')",
				"insert into in_Class(User_id, Class_id, Roll) values(12330000, 1202, 1);",
				"create table if not exists in_Class " + 
				"(User_id int, Class_id int, Roll int, primary key(User_id, Class_id))", 
				"create table if not exists in_Course " + 
				"(User_id int, Course_id int, Roll int, Course_status int, primary key(User_id, Course_id))",
				"create table if not exists School_News " + 
                "(URL String primary key, Title String, Editer String, Date String)"
		};
		
		for(int i = 0; i < sqll.length; i++) {
			Log.v("createTable", "createTable_Init");
		    LoadUtil.createTableAndInit(sqll[i]);
	}
	}
}
