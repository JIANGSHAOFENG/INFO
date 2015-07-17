package com.dbms_v2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Main extends Activity {

	private long exitTime = 0;
    InputStream Class_in = null;
    InputStream Course_in = null;
    private boolean HasClass = false;
    private boolean HasCourse = false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.login);
		CreateTable.createTable();
		//if(!HasClass)
			ReadAndInsertForClass();
		//if(!HasCourse)
			ReadAndInsertForCourse();
		TextView reg = (TextView) findViewById(R.id.register);
		reg.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent_login_resisterPage = new Intent(Main.this, ResisterPage.class);
				startActivity(intent_login_resisterPage);
				//setContentView(R.layout.register);
			}
		});
		
		final EditText User_id = (EditText) findViewById(R.id.et_User_id);
		final EditText User_PassWord = (EditText) findViewById(R.id.et_User_Pwd);
		Button Login = (Button) findViewById(R.id.btn_login);
		
		Login.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(User_id.getText().toString().trim().equals("")) {
					Toast.makeText(Main.this, "请输入您的用户名",Toast.LENGTH_LONG).show();
					return;
				}
				else if(User_PassWord.getText().toString().trim().equals("")) {
					Toast.makeText(Main.this, "请输入您的密码",Toast.LENGTH_LONG).show();
					return;
				} else {
					Log.v("Main", "1");
					int id = Integer.valueOf((User_id.getText().toString()));
					String password = User_PassWord.getText().toString();
					String password_from_query = QueryIfPasswordexist(id);
					Log.v("Main", "password = " + password);
					Log.v("Main", "password_from_query = " + password_from_query);
					if(password_from_query != null) {
						 if(password_from_query.equals(password)) {
							 Toast.makeText(Main.this, "你已经成功登陆!", Toast.LENGTH_SHORT).show();
							 Intent main_mainactivity = new Intent(Main.this, MainActivity.class);
							 main_mainactivity.putExtra("UserId", id);
							 startActivity(main_mainactivity);
						 }
						 else {
						 Toast.makeText(Main.this, "对不起,登录失败!", Toast.LENGTH_SHORT).show();
						 User_id.setText("");
						 User_PassWord.setText("");
						 }
					} else {
					Toast.makeText(Main.this, "对不起，登录失败！",Toast.LENGTH_LONG).show();
					User_PassWord.setText("");
					}
				}
			}
		});
	}
	
	public  static String  QueryIfPasswordexist(int User_id)//查询
	{
		String Password = null;
		SQLiteDatabase sld=LoadUtil.createOrOpenDatabase();//得到连接数据库的连接
		String sql = "select Password from Users where User_id = " + User_id;
		try{
			Log.v("Main", "Query Success_0");
			Cursor cur=sld.rawQuery(sql, new String[]{});//得到结果集
			Log.v("Main", "Query Success_1");
			while(cur.moveToNext())//如果存在下一条
			{
				Password = cur.getString(0);
				Log.v("LoginPage", "Query Success_2" + cur.getString(0));
			}
			cur.close();//关闭结果集
			sld.close();//关闭连接
		}catch(Exception e)
		{
			e.printStackTrace();		
			Log.v("Main", "Query Wrong");
		}
		return Password;
	}
	
	@Override 
	public boolean onKeyDown(int keyCode, KeyEvent event) { 
	if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){ 
	if((System.currentTimeMillis()-exitTime) > 2000){ 
	Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show(); 
	exitTime = System.currentTimeMillis(); 
	} else {
		/*MyApplication mApp = (MyApplication)getApplication(); 
		mApp.setExit(true); */
		//finish();
		
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.addCategory(Intent.CATEGORY_HOME);
		startActivity(intent);//相当于把你手机桌面当成一个Activity跳转
	} 
	return true; 
	} 
	return super.onKeyDown(keyCode, event);
	}
	private void ReadAndInsertForClass() {
		 
		try {
				Class_in = getResources().getAssets().open("Class.csv");
				BufferedReader Class_in_buffer = new BufferedReader(new InputStreamReader(Class_in));
				Log.v("Main", "Read Success" + Class_in);
				String line = null;
				 try {   

			     while ((line = Class_in_buffer.readLine()) != null) {   

			                String data[] = new String[3];
			                data = line.split(",");
			                Log.v("Main", "String = " + line);
			                SQLiteDatabase sld=LoadUtil.createOrOpenDatabase();//得到连接数据库的连接
			       		    String sql = "insert into Classes(Class_id, Class_Name, Class_Grade) values( " + data[0] + ", " + "'" + data[1] +"'" + ", " + data[2] + ")";
			       		    try {
			   		        sld.execSQL(sql);
			   		        Log.v("Main", "Insert Classes Success String = " + line);
			   			    sld.close();//关闭连接
			       		    } catch(Exception e)
			    			{
			    				e.printStackTrace();
			    				Log.v("Main", "Insert Classes Failed");
			    			}
			            }   
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Log.v("Main", "Failed");
			}
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		Log.v("Main", "Failed");
	}
	HasClass = true;
	}
	
	private void ReadAndInsertForCourse() {
		try {
			Course_in = getResources().getAssets().open("Course.csv");
			BufferedReader Course_in_buffer = new BufferedReader(new InputStreamReader(Course_in));
			Log.v("Main", "Read Success" + Course_in);
			String line = null;
			 try {   

		     while ((line = Course_in_buffer.readLine()) != null) {   

		                String data[] = new String[7];
		                data = line.split(",");
		                SQLiteDatabase sld=LoadUtil.createOrOpenDatabase();//得到连接数据库的连接
		       		    String sql = "insert into Courses(Course_id, Course_Name, Course_Grade, Course_Class_id, Course_teacher, Course_position, Course_status) values( " + data[0] + ", " + "'" + data[1] + "'" + ", " + data[2] + ", " + data[3] + ", " + "'" + data[4] + "'" + ", " + "'" + data[5] + "'" + ", " + data[6]+ ")";
		       		    try {
		   		        sld.execSQL(sql);
		   		        Log.v("Main", "Insert Course Success");
		   			    sld.close();//关闭连接
		       		    } catch(Exception e)
		    			{
		    				e.printStackTrace();
		    				Log.v("Main", "Insert Course Failed");
		    			}
		            }   
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.v("Main", "Failed");
		}
} catch (IOException e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
	Log.v("Main", "Failed");
}
		HasCourse = true;
	}
	
}