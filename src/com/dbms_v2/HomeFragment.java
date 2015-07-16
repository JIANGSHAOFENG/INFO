package com.dbms_v2;

import java.text.SimpleDateFormat;
import java.util.Date;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class HomeFragment extends Fragment {
	
	private GestureDetector gestureDetector = null;
	private com.calendar.CalendarAdapter calV = null;
	private GridView gridView = null;
	private TextView topText = null;
	private LinearLayout left = null;
	private LinearLayout right = null;
	private TextView today = null;
	private static int jumpMonth = 0;      //每次滑动，增加或减去一个月,默认为0（即显示当前月）
	private static int jumpYear = 0;       //滑动跨越一年，则增加或者减去一年,默认为0(即当前年)
	private int year_c = 0;
	private int month_c = 0;
	private int day_c = 0;
	private String currentDate = "";
	private Bundle bd=null;//发送参数
	private Bundle bun=null;//接收参数
	private String ruzhuTime;
	private String lidianTime;
	private String state="";
	private int lastDayOfMonth = 0;
	private int DaysOfWeek = 0;
	private int DaysOfMonth = 0;

	
	public HomeFragment() {
		Date date = new Date();
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-d");
    	currentDate = sdf.format(date);  //当期日期
    	year_c = Integer.parseInt(currentDate.split("-")[0]);
    	month_c = Integer.parseInt(currentDate.split("-")[1]);
    	day_c = Integer.parseInt(currentDate.split("-")[2]);
	}
	


	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        
        calV = new com.calendar.CalendarAdapter(this.getActivity().getApplicationContext(),getResources(),jumpMonth,jumpYear,year_c,month_c,day_c);
        gridView =(GridView)rootView.findViewById(R.id.gridview);
        //addGridView(rootView);
        gridView.setAdapter(calV);   
        topText = (TextView) rootView.findViewById(R.id.tv_month);
		addTextToTopTextView(topText);
		lastDayOfMonth = calV.GetlastDaysOfMonth();
		DaysOfWeek = calV.GetDayOfWeek();
		DaysOfMonth = calV.GetDayOfMonth();
		
		gridView.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				Log.d("HomeFragment", "OnSelected " + arg2);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
	   gridView.setOnItemClickListener(new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
		 int FirstDay = 0;
		 int Year = 0, Month = 0, Day = 0;
			// TODO Auto-generated method stub
		 if(DaysOfWeek == 0) {
		   FirstDay = 1;
		   if(FirstDay + arg2 <= DaysOfMonth) {
		   Year = Integer.parseInt(calV.getShowYear());
		   Month = Integer.parseInt(calV.getShowMonth());
		   Day = FirstDay + arg2;
		   } else {
		   if(Integer.parseInt(calV.getShowMonth())== 12) {
		   Year = Integer.parseInt(calV.getShowYear()) + 1;
		   Month = 1;
		   } else {
		   Year = Integer.parseInt(calV.getShowYear());
		   Month = Integer.parseInt(calV.getShowMonth()) + 1;
		   Day = FirstDay + arg2 - DaysOfMonth;
		   }
		   }
		 } else {
		   FirstDay = lastDayOfMonth - (DaysOfWeek - 1);
		   if(FirstDay + arg2 <= lastDayOfMonth) {
				if(Integer.parseInt(calV.getShowMonth()) == 1) {
			    Year = Integer.parseInt(calV.getShowYear()) - 1;
			    Month = 12;
				} else {
				Year = Integer.parseInt(calV.getShowYear());
				Month = Integer.parseInt(calV.getShowMonth()) - 1;
				}
				Day = FirstDay + arg2;
			} else if(FirstDay + arg2 > lastDayOfMonth && FirstDay + arg2 - lastDayOfMonth <= DaysOfMonth) {
				Year = Integer.parseInt(calV.getShowYear());
				Month = Integer.parseInt(calV.getShowMonth());
				Day = FirstDay + arg2 - lastDayOfMonth;
			} else {
				if(Integer.parseInt(calV.getShowMonth()) == 12) {
					Year = Integer.parseInt(calV.getShowYear()) + 1;
					Month = 1;
					Day = FirstDay + arg2 - lastDayOfMonth - DaysOfMonth;
				} else {
					Year = Integer.parseInt(calV.getShowYear());
					Month = Integer.parseInt(calV.getShowMonth()) + 1;
					Day = FirstDay + arg2 - lastDayOfMonth - DaysOfMonth;
				}
				
			}
		 }
		 Log.v("HomeFragment", "Year = " + Year + "Month = " + Month + "Day = " + Day);
		}
	   });
		
		left = (LinearLayout) rootView.findViewById(R.id.btn_prev_month);
		right = (LinearLayout) rootView.findViewById(R.id.btn_next_month);
		today = (TextView) rootView.findViewById(R.id.tv_month);
		
		left.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Log.d("HomeFragment", "Left On Click");
				jumpMonth--;     //下一个月
				calV = new com.calendar.CalendarAdapter(HomeFragment.this.getActivity().getApplicationContext(),getResources(),jumpMonth,jumpYear,year_c,month_c,day_c);
		        gridView.setAdapter(calV);
		        lastDayOfMonth = calV.GetlastDaysOfMonth();
		        DaysOfWeek = calV.GetDayOfWeek();
		        DaysOfMonth = calV.GetDayOfMonth();
		        addTextToTopTextView(topText);
			}
		});
		
		right.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.d("HomeFragment", "Right On Click");
				jumpMonth++;     //下一个月
				calV = new com.calendar.CalendarAdapter(HomeFragment.this.getActivity().getApplicationContext(),getResources(),jumpMonth,jumpYear,year_c,month_c,day_c);
		        gridView.setAdapter(calV);
		        lastDayOfMonth = calV.GetlastDaysOfMonth();
		        DaysOfWeek = calV.GetDayOfWeek();
		        DaysOfMonth = calV.GetDayOfMonth();
		        addTextToTopTextView(topText);
			}
		});
		
		today.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.v("HomeFragment", "today On Click");
				jumpMonth = 0;
				jumpYear = 0;
				calV = new com.calendar.CalendarAdapter(HomeFragment.this.getActivity().getApplicationContext(),getResources(),jumpMonth,jumpYear,year_c,month_c,day_c);
		        //addGridView(rootView);
		        gridView.setAdapter(calV);   
				addTextToTopTextView(topText);
			}
		});
		
        return rootView;
	}

private void addGridView(View rootView) {
		gridView =(GridView)rootView.findViewById(R.id.gridview);
	}

public void addTextToTopTextView(TextView view){
	StringBuffer textDate = new StringBuffer();
	textDate.append(calV.getShowYear()).append("年").append(
			calV.getShowMonth()).append("月").append("\t");
	view.setText(textDate);
	view.setTextColor(Color.WHITE);
	view.setTypeface(Typeface.DEFAULT_BOLD);
}
}


