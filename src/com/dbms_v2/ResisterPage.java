package com.dbms_v2;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

public class ResisterPage extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.register);
        Spinner s1 = (Spinner) findViewById(R.id.spinner01);
        Spinner s2 = (Spinner) findViewById(R.id.spinner02);
		ArrayAdapter adapter1 = ArrayAdapter.createFromResource(
	             this, R.array.grades, android.R.layout.simple_spinner_item);
	    //ArrayAdapter adapter2 = ArrayAdapter.createFromResource(
	             //this, R.array.direction0, android.R.layout.simple_spinner_item);
		//ArrayAdapter adapter3 =  ArrayAdapter.createFromResource(
	             //this, R.array.direction1, android.R.layout.simple_spinner_item);
		adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    s1.setAdapter(adapter1);
	    //adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    //adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    //s2.setAdapter(adapter2);
		
	    s1.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				if(arg2 == 0 || arg2 == 1) {
					updateSpinner2(0);
				}
				else {
					updateSpinner2(1);
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
	    	
	    });
	    		
	    Button Back = (Button) findViewById(R.id.btn_title_left);
	    Back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
	    	
	    });
	    
	    Button register = (Button) findViewById(R.id.btn_register); 
	    register.setOnClickListener(new OnClickListener() {

	    	EditText new_user_id = (EditText) findViewById(R.id.Ed_New_User_id);
		    EditText new_user_password = (EditText) findViewById(R.id.Ed_New_User_PassWord);
		    EditText new_user_password_comfirm = (EditText) findViewById(R.id.Ed_New_User_Password_Comfirm);
		    EditText new_user_name = (EditText) findViewById(R.id.et_username);
		    RadioButton new_user_man = (RadioButton) findViewById(R.id.man);
		    RadioButton new_user_women = (RadioButton) findViewById(R.id.woman);
	    	Spinner s1 = (Spinner) findViewById(R.id.spinner01);
	    	Spinner s2 = (Spinner) findViewById(R.id.spinner02);
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(new_user_id.getText().toString().trim().equals("")) {
					Toast.makeText(ResisterPage.this, "请输入您的用户名",Toast.LENGTH_LONG).show();
					return;
				}
				if(new_user_password.getText().toString().trim().equals("")) {
					Toast.makeText(ResisterPage.this, "请输入您的密码",Toast.LENGTH_LONG).show();
					return;
			    }
				else if(new_user_password_comfirm.getText().toString().trim().equals("")) {
					Toast.makeText(ResisterPage.this, "请再次确认您的密码", Toast.LENGTH_LONG).show();
					return;
				}
				else if( !new_user_password.getText().toString().trim().equals(new_user_password_comfirm.getText().toString().trim())){
	    	        Toast.makeText(ResisterPage.this, "密码与确认密码不一致，请再次输入", Toast.LENGTH_LONG).show();
	    	        new_user_password.setText("");
	    	        new_user_password_comfirm.setText("");
	    	        return;
	            } else {
	                int user_id = Integer.valueOf(new_user_id.getText().toString());
	                String user_password = new_user_password.getText().toString().trim();
	                String Name = new_user_name.getText().toString().trim();
	                int new_user_sex;
	                if(new_user_man.isChecked())new_user_sex = 0;
	                else new_user_sex = 1;
	                SQLiteDatabase db = LoadUtil.createOrOpenDatabase();
	                Log.v("ResisterPage", "Username = " + Name + new_user_sex);
	                if(QueryIfUserExist(user_id)) {
	                try {
	                String sql = "insert into Users(User_id, User_Name, User_Sex, Password) values(" + user_id + "," + "'" + Name + "'" + "," + new_user_sex + "," + "'" + user_password + "'" + ")";
	                Log.v("ResisterPage", sql);
	                db.execSQL(sql);
	                
	                int Grade = 0, Class = 0, Class_id;
	                switch(s1.getSelectedItemPosition()) {
	                case 0:Grade = 13;break;
	                case 1:Grade = 12;break;
	                case 2:Grade = 11;break;
	                case 3:Grade = 10;break;
	                }
	                
	                switch(s2.getSelectedItemPosition()) {
	                case 0:Class = 01;break;
	                case 1:Class = 02;break;
	                case 2:Class = 03;break;
	                case 3:Class = 04;break;
	                case 4:Class = 05;break;
	                }
	                Class_id = Grade * 100 + Class;
	                String sqll = "insert into in_Class(User_id, Class_id, Roll) values(" + user_id + "," + Class_id + "," + 0 + ")";
	                db.execSQL(sqll);
	                db.close();
	                finish();
	                Log.v("ResisterPage", "Insert Success");
	                } catch(Exception e) {
	                Log.v("ResisterPage", "Insert Failed");
	                }
	                } else {
	                	Toast.makeText(ResisterPage.this, "用户名重复!", Toast.LENGTH_SHORT).show();
	                	return;
	                }
	            }
	    }
	});
}
	private boolean QueryIfUserExist(int user_id) {
		SQLiteDatabase db = LoadUtil.createOrOpenDatabase();
		try {
		String sql = "select User_id from Users where User_id = " + user_id;
		Cursor cur=db.rawQuery(sql, new String[]{});
		if(cur.moveToNext()) return false;
		else return true;
		} catch(Exception e) {
			return false;
		}
	}
	
	private void updateSpinner2(int id) {
		Spinner s2 = (Spinner) findViewById(R.id.spinner02);
		ArrayAdapter adapter2 = ArrayAdapter.createFromResource(
	             this, R.array.direction0, android.R.layout.simple_spinner_item);
		ArrayAdapter adapter3 =  ArrayAdapter.createFromResource(
	             this, R.array.direction1, android.R.layout.simple_spinner_item);
		adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		if(id == 0) s2.setAdapter(adapter2);
		else s2.setAdapter(adapter3);
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