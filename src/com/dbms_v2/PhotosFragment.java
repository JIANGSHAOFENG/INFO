package com.dbms_v2;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Dialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.selector.*;


public class PhotosFragment extends Fragment {
	
	public PhotosFragment(){} 
	private Dialog dialog;
    private static int START_YEAR = 1990, END_YEAR = 2100;
    TextView date_selector = null;
    Button information_comfirm = null;
    EditText information_theme = null;
    EditText information_text = null;
    
    Spinner receiver = null;
    List<String> receiver_list = new ArrayList<String>();
    List<Integer> receiver_list_id = new ArrayList<Integer>();
    boolean if_exist_receiver_class = false;
    int cur_year, cur_month, cur_day, cur_hour, cur_minute;
    int User_id;
    public void setUserId(int Id) {
		User_id = Id;
	}
	
	@Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
            View rootView = inflater.inflate(R.layout.fragment_photos, container, false);
            date_selector = (TextView) rootView.findViewById(R.id.information_deadline);
            receiver = (Spinner) rootView.findViewById(R.id.information_receiver);
            receiver_list = QueryCourse_nameFromIn_CourseAndIn_Class();
            String name[] = (String[])receiver_list.toArray(new String[receiver_list.size()]);
            Log.v("PhotosFragment", "Here");
			ArrayAdapter<String> receiver_name_adapter= new ArrayAdapter<String>( 
					PhotosFragment.this.getActivity(), android.R.layout.simple_spinner_item, 
			        name); 
			receiver_name_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			receiver.setAdapter(receiver_name_adapter);
            date_selector.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					showDateTimePicker();
				}
            	
            });
            Calendar calendar = Calendar.getInstance();
    		cur_year = calendar.get(Calendar.YEAR);
    		cur_month = calendar.get(Calendar.MONTH) + 1;
    		cur_day = calendar.get(Calendar.DATE);
    		cur_hour = calendar.get(Calendar.HOUR_OF_DAY);
    		cur_minute = calendar.get(Calendar.MINUTE);
            date_selector.setText(cur_year + "年" + cur_month + "月" + cur_day + "日 " + cur_hour + ":" + cur_minute);
            information_comfirm = (Button) rootView.findViewById(R.id.information_comfirm);
            information_theme = (EditText) rootView.findViewById(R.id.information_theme);
            information_text = (EditText) rootView.findViewById(R.id.information_text);
            
            information_comfirm.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
				    String theme = information_theme.getText().toString().trim();
				    String text = information_text.getText().toString().trim();
				    if(receiver_list.size() == 0) {
				    	Toast.makeText(PhotosFragment.this.getActivity(), "对不起，你没有这个权限！",Toast.LENGTH_LONG).show();
				    	return;
				    }
				    if(theme.equals("")) {
				    	Toast.makeText(PhotosFragment.this.getActivity(), "请输入信息主题！",Toast.LENGTH_LONG).show();
				    	return;
				    }
				    if(text.equals("")) {
				    	Toast.makeText(PhotosFragment.this.getActivity(), "请输入正文！",Toast.LENGTH_LONG).show();
				    	return;
				    }
				    int year = cur_year;
				    int month = cur_month;
				    int day = cur_day;
				    int hour = cur_hour;
				    int minute = cur_minute;
				    String Date = year + "-" + month + "-" + day + " " + hour + ":" + minute;
				    int Information_id = QuerycountfromInformation() + 1;
				    int Id = receiver.getSelectedItemPosition();
				    String Editer = QueryUser_NameFromUsers(User_id);
				    int add_year, add_month, add_day, add_hour, add_minute;
				    Calendar calendar = Calendar.getInstance();
		    		add_year = calendar.get(Calendar.YEAR);
		    		add_month = calendar.get(Calendar.MONTH) + 1;
		    		add_day = calendar.get(Calendar.DATE);
		    		add_hour = calendar.get(Calendar.HOUR_OF_DAY);
		    		add_minute = calendar.get(Calendar.MINUTE);
		    		String add_Date = add_year + "-" + add_month + "-" + add_day + " " + add_hour + ":" + add_minute;
				    if(if_exist_receiver_class) {
				    	if(Id == 0) {
				    		int Class_id = receiver_list_id.get(Id);
				    		if(insertToInformationForClass(Information_id, Editer, Class_id, theme, year, month, day, hour, minute, Date, add_Date, text)) {
				    			Toast.makeText(PhotosFragment.this.getActivity(), "恭喜你，发送消息成功",Toast.LENGTH_LONG).show();
				    			information_theme.setText("");
				    			information_text.setText("");
				    		} else {
				    			Toast.makeText(PhotosFragment.this.getActivity(), "对不起，发送消息失败",Toast.LENGTH_LONG).show();
				    		}
				    	} else {
				    		int Course_id = receiver_list_id.get(Id);
				    		if(insertToInformationForCourse(Information_id, Editer, Course_id, theme, year, month, day, hour, minute, Date, add_Date, text)) {
				    			Toast.makeText(PhotosFragment.this.getActivity(), "恭喜你，发送消息成功",Toast.LENGTH_LONG).show();
				    			information_theme.setText("");
				    			information_text.setText("");
				    		} else {
				    			Toast.makeText(PhotosFragment.this.getActivity(), "对不起，发送消息失败",Toast.LENGTH_LONG).show();
				    		}
				    	}
				} else {
					int Course_id = receiver_list_id.get(Id);
		    		if(insertToInformationForCourse(Information_id, Editer, Course_id, theme, year, month, day, hour, minute, Date, add_Date, text)) {
		    			Toast.makeText(PhotosFragment.this.getActivity(), "恭喜你，发送消息成功",Toast.LENGTH_LONG).show();
		    			information_theme.setText("");
		    			information_text.setText("");
		    		} else {
		    			Toast.makeText(PhotosFragment.this.getActivity(), "对不起，发送消息失败",Toast.LENGTH_LONG).show();
		    		}
				}
				}
            });
            
            return rootView;
	}
	
	
	private void showDateTimePicker() {
		//Calendar calendar = Calendar.getInstance();
		int year = cur_year;
		int month = cur_month - 1;
		int day = cur_day;
		int hour = cur_hour;
		int minute = cur_minute;

		// 添加大小月月份并将其转换为list,方便之后的判断
		String[] months_big = { "1", "3", "5", "7", "8", "10", "12" };
		String[] months_little = { "4", "6", "9", "11" };

		final List<String> list_big = Arrays.asList(months_big);
		final List<String> list_little = Arrays.asList(months_little);

		dialog = new Dialog(PhotosFragment.this.getActivity());
		dialog.setTitle("请选择日期与时间");
		// 找到dialog的布局文件
		LayoutInflater inflater = (LayoutInflater) PhotosFragment.this.getActivity().getSystemService(PhotosFragment.this.getActivity().LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.time_layout, null);

		// 年
		final WheelView wv_year = (WheelView) view.findViewById(R.id.year);
		wv_year.setAdapter(new NumericWheelAdapter(START_YEAR, END_YEAR));// 设置"年"的显示数据
		wv_year.setCyclic(true);// 可循环滚动
		wv_year.setLabel("年");// 添加文字
		wv_year.setCurrentItem(year - START_YEAR);// 初始化时显示的数据

		// 月
		final WheelView wv_month = (WheelView) view.findViewById(R.id.month);
		wv_month.setAdapter(new NumericWheelAdapter(1, 12));
		wv_month.setCyclic(true);
		wv_month.setLabel("月");
		wv_month.setCurrentItem(month);

		// 日
		final WheelView wv_day = (WheelView) view.findViewById(R.id.day);
		wv_day.setCyclic(true);
		// 判断大小月及是否闰年,用来确定"日"的数据
		if (list_big.contains(String.valueOf(month + 1))) {
			wv_day.setAdapter(new NumericWheelAdapter(1, 31));
		} else if (list_little.contains(String.valueOf(month + 1))) {
			wv_day.setAdapter(new NumericWheelAdapter(1, 30));
		} else {
			// 闰年
			if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0)
				wv_day.setAdapter(new NumericWheelAdapter(1, 29));
			else
				wv_day.setAdapter(new NumericWheelAdapter(1, 28));
		}
		wv_day.setLabel("日");
		wv_day.setCurrentItem(day - 1);

		// 时
		final WheelView wv_hours = (WheelView) view.findViewById(R.id.hour);
		wv_hours.setAdapter(new NumericWheelAdapter(0, 23));
		wv_hours.setCyclic(true);
		wv_hours.setCurrentItem(hour);

		// 分
		final WheelView wv_mins = (WheelView) view.findViewById(R.id.mins);
		wv_mins.setAdapter(new NumericWheelAdapter(0, 59, "%02d"));
		wv_mins.setCyclic(true);
		wv_mins.setCurrentItem(minute);

		// 添加"年"监听
		OnWheelChangedListener wheelListener_year = new OnWheelChangedListener() {
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				int year_num = newValue + START_YEAR;
				// 判断大小月及是否闰年,用来确定"日"的数据
				if (list_big.contains(String
						.valueOf(wv_month.getCurrentItem() + 1))) {
					wv_day.setAdapter(new NumericWheelAdapter(1, 31));
				} else if (list_little.contains(String.valueOf(wv_month
						.getCurrentItem() + 1))) {
					wv_day.setAdapter(new NumericWheelAdapter(1, 30));
				} else {
					if ((year_num % 4 == 0 && year_num % 100 != 0)
							|| year_num % 400 == 0)
						wv_day.setAdapter(new NumericWheelAdapter(1, 29));
					else
						wv_day.setAdapter(new NumericWheelAdapter(1, 28));
				}
			}
		};
		// 添加"月"监听
		OnWheelChangedListener wheelListener_month = new OnWheelChangedListener() {
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				int month_num = newValue + 1;
				// 判断大小月及是否闰年,用来确定"日"的数据
				if (list_big.contains(String.valueOf(month_num))) {
					wv_day.setAdapter(new NumericWheelAdapter(1, 31));
				} else if (list_little.contains(String.valueOf(month_num))) {
					wv_day.setAdapter(new NumericWheelAdapter(1, 30));
				} else {
					if (((wv_year.getCurrentItem() + START_YEAR) % 4 == 0 && (wv_year
							.getCurrentItem() + START_YEAR) % 100 != 0)
							|| (wv_year.getCurrentItem() + START_YEAR) % 400 == 0)
						wv_day.setAdapter(new NumericWheelAdapter(1, 29));
					else
						wv_day.setAdapter(new NumericWheelAdapter(1, 28));
				}
			}
		};
		wv_year.addChangingListener(wheelListener_year);
		wv_month.addChangingListener(wheelListener_month);

		// 根据屏幕密度来指定选择器字体的大小
		int textSize = 0;
		int screenWidth  = PhotosFragment.this.getActivity().getWindowManager().getDefaultDisplay().getWidth(); 
		Log.v("PhotosFragment", "ScreenWidith = " + screenWidth);
		
		if(screenWidth <= 540) {
			textSize = 19;
		} else if(screenWidth <= 800) {
			textSize = 35;
		}

		wv_day.TEXT_SIZE = textSize;
		wv_hours.TEXT_SIZE = textSize;
		wv_mins.TEXT_SIZE = textSize;
		wv_month.TEXT_SIZE = textSize;
		wv_year.TEXT_SIZE = textSize;

		Button btn_sure = (Button) view.findViewById(R.id.btn_datetime_sure);
		// 确定
		btn_sure.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				// 如果是个数,则显示为"02"的样式
				String parten = "00";
				DecimalFormat decimal = new DecimalFormat(parten);
				// 设置日期的显示
				// tv_time.setText((wv_year.getCurrentItem() + START_YEAR) + "-"
				// + decimal.format((wv_month.getCurrentItem() + 1)) + "-"
				// + decimal.format((wv_day.getCurrentItem() + 1)) + " "
				// + decimal.format(wv_hours.getCurrentItem()) + ":"
				// + decimal.format(wv_mins.getCurrentItem()));
				int year = wv_year.getCurrentItem() + START_YEAR;
				int month = wv_month.getCurrentItem() + 1;
				int day = wv_day.getCurrentItem() + 1;
				int hours = wv_hours.getCurrentItem();
				int mins = wv_mins.getCurrentItem();
				date_selector.setText(year + "年" + month + "月" + day + "日 " + hours + ":" + mins);
				cur_year = year;
				cur_month = month;
				cur_day = day;
				cur_hour = hours;
				cur_minute = mins;
				dialog.dismiss();
			}
		});
		// 设置dialog的布局,并显示
		dialog.setContentView(view);
		dialog.show();
		//dialog.getWindow().setLayout(680, 600); 
		/*WindowManager.LayoutParams windowParams = dialog.getWindow().getAttributes();
        windowParams.width = 680; // 设置宽度
        windowParams.height = 600; // 设置高度
        dialog.getWindow().setAttributes(windowParams);*/
		
		/*WindowManager windowManager = this.getActivity().getWindowManager();
		Display display = windowManager.getDefaultDisplay();
		WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
		lp.width = (int)(display.getWidth()); //设置宽度
		dialog.getWindow().setAttributes(lp);*/
	}
	
	private List<String> QueryCourse_nameFromIn_CourseAndIn_Class() {
		List<String> list = new ArrayList<String>();
		SQLiteDatabase sld=LoadUtil.createOrOpenDatabase();//得到连接数据库的连接
		String sql = "select C.Class_Name, C.Class_id from in_Class IC, Classes C where C.Class_id = IC.Class_id and IC.Roll = 1 and IC.User_id = " + User_id;
		Log.v("PhotoFragment", sql);
		try {
			Cursor cur=sld.rawQuery(sql, new String[]{});//得到结果集	
			while(cur.moveToNext()) {
				String Class_name = cur.getString(0);
				if_exist_receiver_class = true;
				Integer Class_id = Integer.parseInt(cur.getString(1));
				Log.v("PhotosFragment", "Query Success From Courses Course_name = " + Class_name + " Class_id = " + Class_id);
				list.add(Class_name);
				receiver_list_id.add(Class_id);
			}
			cur.close();
		} catch(Exception e)
		{
			e.printStackTrace();		
			Log.v("PhotosFragment", "Query Wrong From In_CourseAndIn_Class");
		}
		
		String sql_2 = "select C.Course_Name, C.Course_id from Courses C, in_Course IC where IC.Course_id = C.Course_id and (IC.Roll = 1 or IC.Roll = 2) and IC.User_id = " + User_id;
		Cursor cur_2=sld.rawQuery(sql_2, new String[]{});//得到结果集
		try {
			
			while(cur_2.moveToNext()) {
				String Course_name = cur_2.getString(0);
				Integer Course_id = Integer.parseInt(cur_2.getString(1));
				Log.v("PhotosFragment", "Query Success From In_Course Course_Name = " + Course_name + " Course_id = " + Course_id);
				list.add(Course_name);
				receiver_list_id.add(Course_id);
			}
		} catch(Exception e)
		{
			e.printStackTrace();		
			Log.d("PhotosFragment", "Query Wrong From In_CourseAndIn_Class");
		}
		cur_2.close();
		sld.close();
		return list;
	}
	
	private int QuerycountfromInformation() {
		SQLiteDatabase sld=LoadUtil.createOrOpenDatabase();//得到连接数据库的连接
		String sql = "select * from Information";
		int result = 0;
		try {	
			Cursor cur = sld.rawQuery(sql, new String[]{});
			while(cur.moveToNext()) {
				result++;
			}
			Log.d("PhotosFragment", "Query count Success From Information, count = " + result);
		} catch(Exception e)
		{
			e.printStackTrace();		
			Log.d("PhotosFragment", "Query count Wrong From Information");
		}
		return result;
	}
	
	private boolean insertToInformationForClass(int Information_id, String Editer, int Class_id, String theme, int year, int month, int day, int hour, int minute, String Date, String add_Date, String text) {
		SQLiteDatabase sld=LoadUtil.createOrOpenDatabase();//得到连接数据库的连接
		String sql = "insert into Information(Information_id, Editer, Class_id, Title, Year, Month, Day, Hour, Minute, Date, add_Date, Text) values(" + Information_id +
				      ", '" + Editer + "', "+ Class_id + ", '" + theme + "', " + year + ", " + month + ", " + day + ", " + hour + ", " + minute + ", '" + Date + "', '" + add_Date + "', '"+ text +"')";
		 try {
		      sld.execSQL(sql);
			  sld.close();//关闭连接
			  Log.d("PhotosFragment", "Insert Success To Information");
			  return true;
			} catch(Exception e)
			{
				e.printStackTrace();		
				Log.d("PhotosFragment", "Insert Wrong To Information");
				return false;
			}
	}
	
	private boolean insertToInformationForCourse(int Information_id, String Editer, int Course_id, String theme, int year, int month, int day, int hour, int minute, String Date, String add_Date, String text) {
		SQLiteDatabase sld=LoadUtil.createOrOpenDatabase();//得到连接数据库的连接
		String sql = "insert into Information(Information_id, Editer, Course_id, Title, Year, Month, Day, Hour, Minute, Date, add_Date, Text) values(" + Information_id +
				      ", '" + Editer +"', " + Course_id + ", '" + theme + "', " + year + ", " + month + ", " + day + ", " + hour + ", " + minute + ", '" + Date + "', '" + add_Date + "', '"+ text +"')";
		 try {
		      sld.execSQL(sql);
			  sld.close();//关闭连接
			  Log.d("PhotosFragment", "Insert Success To Information");
			  return true;
			} catch(Exception e)
			{
				e.printStackTrace();		
				Log.d("PhotosFragment", "Insert Wrong To Information");
				return false;
			}
	}
	
	private String QueryUser_NameFromUsers(int User_id) {
		String User_Name = "";
		SQLiteDatabase sld=LoadUtil.createOrOpenDatabase();//得到连接数据库的连接
		String sql = "select User_Name From Users where User_id = " + User_id;
		 try {
				Cursor cur=sld.rawQuery(sql, new String[]{});
				while(cur.moveToNext())//如果存在下一条
				{
				User_Name = cur.getString(0);
				Log.v("FindPeopleFragment", "Query Success From Users User_Name = " + User_Name);
				}
		 } catch(Exception e)
			{
				e.printStackTrace();		
				Log.d("FindPeopleFragment", "Query Wrong From Users");
			}
		sld.close();
	    return User_Name;
	}
}