package com.dbms_v2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.fima.cardsui.views.*;
import com.fima.cardsui.objects.*;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;

public class Information_activity extends Activity {

	private CardUI mCardView;
	List<Map<String, Object>> class_list = new ArrayList<Map<String, Object>>();
	List<Map<String, Object>> course_list = new ArrayList<Map<String, Object>>();
	Button back = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.information_activity_main);
		Intent FromMainActivity = this.getIntent();
		Bundle bundle = FromMainActivity.getExtras();
		int User_id = Integer.parseInt(bundle.getString("User_id"));
		int Class_id = Integer.parseInt(bundle.getString("Class_id"));
		int Course_id = Integer.parseInt(bundle.getString("Course_id"));
		Log.v("InformationActivity", "User_id = " + User_id + " Class_id = " + Class_id + " Course_id = " + Course_id);
		mCardView = (CardUI) findViewById(R.id.cardsview);
		class_list = QueryFromInformationForClass(Class_id);
		String Class_Name = QueryClass_NameFromClasses(Class_id);
		CardStack stack = new CardStack();
		stack.setTitle("来自 " + Class_Name + " 的消息");
		for(Map<String, Object> listItem : class_list) {
			String Title = (String) listItem.get("Title");
			String Editer = (String) listItem.get("Editer");
			String add_Date = (String) listItem.get("add_Date");
			String Date = (String) listItem.get("Date");
			String Text = (String) listItem.get("Text");
			String Show = "发件人: " + Editer + "\n发布日期: " + add_Date + "\n截止日期: " + Date + "\n正文:\n" + Text;
			stack.add(new MyCard(Title, Show));
		}
		//mCardView.addStack(stack);
		CardStack stack_2 = new CardStack();
		String Course_Name = QueryCourse_NameFromCourses(Course_id);
		stack_2.setTitle("来自 " + Course_Name + " 的消息");
		course_list = QueryFromInformationForCourse(Course_id);
		for(Map<String, Object> listItem : course_list) {
			String Title = (String) listItem.get("Title");
			String Editer = (String) listItem.get("Editer");
			String add_Date = (String) listItem.get("add_Date");
			String Date = (String) listItem.get("Date");
			String Text = (String) listItem.get("Text");
			String Show = "发件人: " + Editer + "\n发布日期: " + add_Date + "\n截止日期: " + Date + "\n正文:\n" + Text;
			stack_2.add(new MyCard(Title, Show));
		}
		mCardView.addStack(stack_2);
		mCardView.addStack(stack);
		mCardView.refresh();
		back = (Button) findViewById(R.id.btn_back);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
		    finish();	
			}
			
		});
	}
	
	private List<Map<String, Object>> QueryFromInformationForClass(int Class_id) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		SQLiteDatabase sld=LoadUtil.createOrOpenDatabase();//得到连接数据库的连接
		String sql = "select Title, Editer, Date, add_Date, Text from Information where Class_id = " + Class_id;
		try {
			Cursor cur=sld.rawQuery(sql, new String[]{});//得到结果集
			while(cur.moveToNext())//如果存在下一条
			{
			Map<String, Object> listItem = new HashMap<String, Object>();
			String Title = cur.getString(0);
			String Editer = cur.getString(1);
			String Date = cur.getString(2);
			String add_Date = cur.getString(3);
			String Text = cur.getString(4);
			Log.v("Information_activity", "Query Success From InformationClass Title = " + Title + " Editer = " + Editer + " Date = " + Date + " add_Date = " + add_Date);
			listItem.put("Title", Title);
			listItem.put("Editer", Editer);
			listItem.put("add_Date", add_Date);
			listItem.put("Date", Date);
			listItem.put("Text", Text);
			list.add(listItem);
			}
			} catch(Exception e)
			{
				e.printStackTrace();		
				Log.d("Information_activity", "Query Wrong From Information");
			}
		return list;
	}
	private List<Map<String, Object>> QueryFromInformationForCourse(int Course_id) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		SQLiteDatabase sld=LoadUtil.createOrOpenDatabase();//得到连接数据库的连接
		String sql = "select Title, Editer, Date, add_Date, Text from Information where Course_id = " + Course_id;
		try {
			Cursor cur=sld.rawQuery(sql, new String[]{});//得到结果集
			while(cur.moveToNext())//如果存在下一条
			{
			Map<String, Object> listItem = new HashMap<String, Object>();
			String Title = cur.getString(0);
			String Editer = cur.getString(1);
			String Date = cur.getString(2);
			String add_Date = cur.getString(3);
			String Text = cur.getString(4);
			Log.v("Information_activity", "Query Success From InformationClass Title = " + Title + " Editer = " + Editer + " Date = " + Date + " add_Date = " + add_Date);
			listItem.put("Title", Title);
			listItem.put("Editer", Editer);
			listItem.put("add_Date", add_Date);
			listItem.put("Date", Date);
			listItem.put("Text", Text);
			list.add(listItem);
			}
			} catch(Exception e)
			{
				e.printStackTrace();		
				Log.d("Information_activity", "Query Wrong From Information");
			}
		return list;
	}
	
	private String QueryClass_NameFromClasses(int Class_id) {
		SQLiteDatabase sld=LoadUtil.createOrOpenDatabase();//得到连接数据库的连接
		String Class_Name = null;
		String sql = "select Class_Name from Classes where Class_id = " + Class_id;
		try {
			Cursor cur=sld.rawQuery(sql, new String[]{});//得到结果集
			while(cur.moveToNext()) {
				Class_Name = cur.getString(0);
			}
		} catch(Exception e) {
				e.printStackTrace();
		}
		return Class_Name;
	}
	
	private String QueryCourse_NameFromCourses(int Course_id) {
		SQLiteDatabase sld=LoadUtil.createOrOpenDatabase();//得到连接数据库的连接
		String Course_Name = null;
		String sql = "select Course_Name from Courses where Course_id = " + Course_id;
		try {
			Cursor cur=sld.rawQuery(sql, new String[]{});//得到结果集
			while(cur.moveToNext()) {
				Course_Name = cur.getString(0);
			}
		} catch(Exception e) {
				e.printStackTrace();
		}
		return Course_Name;
	}

	/*@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		MyApplication mApp = (MyApplication)getApplication(); 
		if (mApp.isExit()) { 
		finish(); 
		} 
	}*/
	
	
}
