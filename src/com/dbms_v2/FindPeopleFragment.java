package com.dbms_v2;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;












import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.TranslateAnimation;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


public class FindPeopleFragment extends Fragment {
	
	private Button ClickMe;
	private Spinner Class;
	private Spinner Teacher;
	private ListView listview;
	private int UserId, UserGrade, UserClass;
	ViewPager pag=null;
	ImageView cursor=null;
	private List<View> pager=null;
	private int width=0;            //屏幕宽度
    private int cursor_width=0;      //cursor宽度
    private int offset=0;                //偏移量
    TextView view0=null;
    TextView view1=null;
    int currentIndex=0;
    View page1View = null;
    View page2View = null;
    List<Map<String, Object>> M_Course_list = new ArrayList<Map<String, Object>>();
    List<Map<String, Object>> NM_Course_list = new ArrayList<Map<String, Object>>();
	
	public FindPeopleFragment(){}
	
	public void setUserIdAndUserGrade(int Id) {
		UserId = Id;
		Log.d("FindPeopleFragment", "UserId = " + UserId);
	    UserClass = QueryClassFromin_Class(UserId);
	    UserGrade = UserClass / 100;
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.fragment_find_people, container, false);
        pager=new ArrayList<View>();
        page1View = inflater.inflate(R.layout.page1,null);
        page2View = inflater.inflate(R.layout.page2,null);
        cursor=(ImageView)rootView.findViewById(R.id.cursor);
		pag=(ViewPager)rootView.findViewById(R.id.ViewPager);
		InitCursorPosition();                                // 初始化cursor的位置
		InitViewpager();
		pag.setCurrentItem(0);
		pag.setOnPageChangeListener(new MyOnClickPagerChange());
		view0=(TextView)rootView.findViewById(R.id.ToPage1);
		view1=(TextView)rootView.findViewById(R.id.ToPage2);
		view0.setOnClickListener(new Myclick(0));
		view1.setOnClickListener(new Myclick(1));
		
		/*View page1View = inflater.inflate(R.layout.page1,null);
		ListView listview = (ListView) page1View.findViewById(R.id.course_list_1);
		List<Map<String, Object>> M_Course_list = QueryCourseFromIn_Course(UserId, 0);
		SimpleAdapter simpleAdapter = new SimpleAdapter(FindPeopleFragment.this.getActivity(), M_Course_list, R.layout.course_item,
                new String[] {"Course_Name", "Course_teacher", "Course_position"},
                new int[] {R.id.course_name, R.id.course_teacher, R.id.course_position});
        listview.setAdapter(simpleAdapter);*/
        //ClickMe = (Button) rootView.findViewById(R.id.test);
        //listview = (ListView) rootView.findViewById(R.id.myCourse);
        
        /*List<Map<String, Object>> list = QueryClassFromCourses(UserClass);
        SimpleAdapter simpleAdapter = new SimpleAdapter(FindPeopleFragment.this.getActivity(), list, R.layout.course_item,
        		                                        new String[] {"Course_Name", "Course_teacher", "Course_position"},
        		                                        new int[] {R.id.course_name, R.id.course_teacher, R.id.course_position});
        listview.setAdapter(simpleAdapter);
        
        ClickMe.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				//Spinner add_course_class = (Spinner) rootView.findViewById(R.id.add_course_class);
				
				View menuView = View.inflate(FindPeopleFragment.this.getActivity(), R.layout.add_course, null);
				final Spinner add_course_class = (Spinner) menuView.findViewById(R.id.add_course_class);
		        final Spinner add_course_grade = (Spinner) menuView.findViewById(R.id.add_course_grade);
		        final Spinner add_course_name = (Spinner) menuView.findViewById(R.id.add_course_name);
		        final Spinner add_course_teacher = (Spinner) menuView.findViewById(R.id.add_course_teacher);
		        final Spinner add_course_status = (Spinner) menuView.findViewById(R.id.add_course_status);
		        
		        
		        ArrayAdapter add_course_class_adapter = ArrayAdapter.createFromResource(
		        		FindPeopleFragment.this.getActivity(), R.array.classify, android.R.layout.simple_spinner_item);
		        add_course_class_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			    add_course_class.setAdapter(add_course_class_adapter);
			    
			    ArrayAdapter add_course_grade_adapter = ArrayAdapter.createFromResource(
		        		FindPeopleFragment.this.getActivity(), R.array.grades, android.R.layout.simple_spinner_item);
		        add_course_grade_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		        add_course_grade.setAdapter(add_course_grade_adapter);
		        
		        ArrayAdapter add_course_status_adapter = ArrayAdapter.createFromResource(
		        		FindPeopleFragment.this.getActivity(), R.array.status, android.R.layout.simple_spinner_item);
		        add_course_status_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		        add_course_status.setAdapter(add_course_status_adapter);
		        
		        add_course_class.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						// TODO Auto-generated method stub
						int Course_class = 0;
						int Course_grade = 0;
						int add_course_grade_id = add_course_grade.getSelectedItemPosition();
						Log.v("FindPeopleFragment", "add_course_grade_id = " + add_course_grade_id);
						switch(add_course_grade_id) {
						case 0:
							Course_grade = 13;
							break;
						case 1:
							Course_grade = 12;
							break;
						case 2:
							Course_grade = 11;
							break;
						case 3:
							Course_grade = 10;
							break;
						}
						Log.v("FindPeopleFragment", "Course_grade = " + Course_grade);
						switch(arg2) {
						case 0:
							Course_class = 0;
							break;
						case 1:
							Course_class = 1;
							break;
						}
						Log.v("FindPeopleFragment", "Course_class = " + Course_class);
						List<String> list = QueryClassFromCourses(Course_grade, Course_class);
						int size = list.size();
						if(size == 0)add_course_teacher.setAdapter(null);
						String course_name[] = (String[])list.toArray(new String[size]);
						ArrayAdapter<String> add_course_name_adapter= new ArrayAdapter<String>( 
								FindPeopleFragment.this.getActivity(), android.R.layout.simple_spinner_item, 
						        course_name); 
						add_course_name_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
						add_course_name.setAdapter(add_course_name_adapter);
						
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
						// TODO Auto-generated method stub
						
					}
		        	
		        });
		        
		        add_course_grade.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						// TODO Auto-generated method stub
						int Course_class = 0;
						int Course_grade = 0;
						switch(arg2) {
						case 0:
							Course_grade = 13;
							break;
						case 1:
							Course_grade = 12;
							break;
						case 2:
							Course_grade = 11;
							break;
						case 3:
							Course_grade = 10;
							break;
						}
						int add_course_class_id = add_course_class.getSelectedItemPosition();
						switch(add_course_class_id) {
						case 0:
							Course_class = 0;
							break;
						case 1:
							Course_class = 1;
							break;
						}
						List<String> list = QueryClassFromCourses(Course_grade, Course_class);
						int size = list.size();
						if(size == 0)add_course_teacher.setAdapter(null);
						String course_name[] = (String[])list.toArray(new String[size]);
						ArrayAdapter<String> add_course_name_adapter= new ArrayAdapter<String>( 
								FindPeopleFragment.this.getActivity(), android.R.layout.simple_spinner_item, 
						        course_name); 
						add_course_name_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
						add_course_name.setAdapter(add_course_name_adapter);
						
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
						// TODO Auto-generated method stub
						
					}
		        	
		        });
		        
		        add_course_name.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						// TODO Auto-generated method stub
						String Course_Name = add_course_name.getSelectedItem().toString();
						Log.v("FindPeopFragment", "Course_Name = " + Course_Name);
						List<String> list = null;
						try {
							list = QueryTeacherFromCourses(Course_Name);
						} catch (UnsupportedEncodingException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						int size = list.size();
						String course_teacher[] = (String[])list.toArray(new String[size]);
						ArrayAdapter<String> add_course_teacher_adapter= new ArrayAdapter<String>( 
								FindPeopleFragment.this.getActivity(), android.R.layout.simple_spinner_item, 
						        course_teacher); 
						add_course_teacher_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
						add_course_teacher.setAdapter(add_course_teacher_adapter);
						
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
						// TODO Auto-generated method stub
						
					}
		        	
		        });
		        
				// 创建AlertDialog
				//List<Map<String, Object>> list = QueryClassFromCourses(UserClass);
				final AlertDialog menuDialog = new AlertDialog.Builder(FindPeopleFragment.this.getActivity()).create();
				menuDialog.setView(menuView);
				menuDialog.setCanceledOnTouchOutside(true);
				menuDialog.show();
			}
        	
        });*/
        
        return rootView;
    }
    

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// TODO Auto-generated method stub
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.main, menu);
	}
	
	
	


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		final AlertDialog menuDialog = new AlertDialog.Builder(FindPeopleFragment.this.getActivity()).create();
		if(item.getItemId() == R.id.ADD_COURSE) {
			View menuView = View.inflate(FindPeopleFragment.this.getActivity(), R.layout.add_course, null);
			final Spinner add_course_class = (Spinner) menuView.findViewById(R.id.add_course_class);
	        final Spinner add_course_grade = (Spinner) menuView.findViewById(R.id.add_course_grade);
	        final Spinner add_course_name = (Spinner) menuView.findViewById(R.id.add_course_name);
	        final Spinner add_course_teacher = (Spinner) menuView.findViewById(R.id.add_course_teacher);
	        final Spinner add_course_status = (Spinner) menuView.findViewById(R.id.add_course_status);
	        final Button add_course_comfirm = (Button) menuView.findViewById(R.id.btn_add_course);
	        
	        ArrayAdapter add_course_class_adapter = ArrayAdapter.createFromResource(
	        		FindPeopleFragment.this.getActivity(), R.array.classify, android.R.layout.simple_spinner_item);
	        add_course_class_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		    add_course_class.setAdapter(add_course_class_adapter);
		    
		    ArrayAdapter add_course_grade_adapter = ArrayAdapter.createFromResource(
	        		FindPeopleFragment.this.getActivity(), R.array.grades, android.R.layout.simple_spinner_item);
	        add_course_grade_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	        add_course_grade.setAdapter(add_course_grade_adapter);
	        
	        ArrayAdapter add_course_status_adapter = ArrayAdapter.createFromResource(
	        		FindPeopleFragment.this.getActivity(), R.array.status, android.R.layout.simple_spinner_item);
	        add_course_status_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	        add_course_status.setAdapter(add_course_status_adapter);
	        
	        add_course_class.setOnItemSelectedListener(new OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					// TODO Auto-generated method stub
					int Course_class = 0;
					int Course_grade = 0;
					int add_course_grade_id = add_course_grade.getSelectedItemPosition();
					Log.v("FindPeopleFragment", "add_course_grade_id = " + add_course_grade_id);
					switch(add_course_grade_id) {
					case 0:
						Course_grade = 13;
						break;
					case 1:
						Course_grade = 12;
						break;
					case 2:
						Course_grade = 11;
						break;
					case 3:
						Course_grade = 10;
						break;
					}
					Log.v("FindPeopleFragment", "Course_grade = " + Course_grade);
					switch(arg2) {
					case 0:
						Course_class = 0;
						break;
					case 1:
						Course_class = 1;
						break;
					}
					Log.v("FindPeopleFragment", "Course_class = " + Course_class);
					List<String> list = QueryClassFromCourses(Course_grade, Course_class);
					int size = list.size();
					String course_name[] = (String[])list.toArray(new String[size]);
					ArrayAdapter<String> add_course_name_adapter= new ArrayAdapter<String>( 
							FindPeopleFragment.this.getActivity(), android.R.layout.simple_spinner_item, 
					        course_name); 
					add_course_name_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					add_course_name.setAdapter(add_course_name_adapter);
					if(size == 0) {	
						add_course_teacher.setAdapter(add_course_name_adapter);
					}
					
				}

				@Override
				public void onNothingSelected(AdapterView<?> arg0) {
					// TODO Auto-generated method stub
					
				}
	        	
	        });
	        
	        add_course_grade.setOnItemSelectedListener(new OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					// TODO Auto-generated method stub
					int Course_class = 0;
					int Course_grade = 0;
					switch(arg2) {
					case 0:
						Course_grade = 13;
						break;
					case 1:
						Course_grade = 12;
						break;
					case 2:
						Course_grade = 11;
						break;
					case 3:
						Course_grade = 10;
						break;
					}
					int add_course_class_id = add_course_class.getSelectedItemPosition();
					switch(add_course_class_id) {
					case 0:
						Course_class = 0;
						break;
					case 1:
						Course_class = 1;
						break;
					}
					List<String> list = QueryClassFromCourses(Course_grade, Course_class);
					int size = list.size();
					
					String course_name[] = (String[])list.toArray(new String[size]);
					ArrayAdapter<String> add_course_name_adapter= new ArrayAdapter<String>( 
							FindPeopleFragment.this.getActivity(), android.R.layout.simple_spinner_item, 
					        course_name); 
					add_course_name_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					add_course_name.setAdapter(add_course_name_adapter);
					if(size == 0) {	
						add_course_teacher.setAdapter(add_course_name_adapter);
					}
				}

				@Override
				public void onNothingSelected(AdapterView<?> arg0) {
					// TODO Auto-generated method stub
					
				}
	        	
	        });
	        
	        add_course_name.setOnItemSelectedListener(new OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					// TODO Auto-generated method stub
					String Course_Name = add_course_name.getSelectedItem().toString();
					Log.v("FindPeopFragment", "Course_Name = " + Course_Name);
					List<String> list = null;
					try {
						list = QueryTeacherFromCourses(Course_Name);
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					int size = list.size();
					String course_teacher[] = (String[])list.toArray(new String[size]);
					ArrayAdapter<String> add_course_teacher_adapter= new ArrayAdapter<String>( 
							FindPeopleFragment.this.getActivity(), android.R.layout.simple_spinner_item, 
					        course_teacher); 
					add_course_teacher_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					add_course_teacher.setAdapter(add_course_teacher_adapter);
					
				}

				@Override
				public void onNothingSelected(AdapterView<?> arg0) {
					// TODO Auto-generated method stub
					
				}
	        	
	        });
	        
	        add_course_comfirm.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					int User_id = UserId;
					int student_status = add_course_status.getSelectedItemPosition();
					int Course_status = add_course_class.getSelectedItemPosition();
					Log.v("FindPeopleFragment", "student_status = " + student_status + " Course_status = " + Course_status);
					String Course_Name = (String) add_course_name.getSelectedItem();
					String Teacher_Name = (String) add_course_teacher.getSelectedItem();
					Log.v("FindPeopleFragment", "Course_Name = " + Course_Name + " Teacher_Name = " + Teacher_Name);
					int Course_id = QueryCourseIdFromCourses(Course_Name, Teacher_Name);
					if(insertToIn_Course(User_id, Course_id, student_status, Course_status)) {
						ListView listview = (ListView) page1View.findViewById(R.id.course_list_1);
						M_Course_list = QueryCourseFromIn_Course(UserId, 0);
						SimpleAdapter simpleAdapter = new SimpleAdapter(FindPeopleFragment.this.getActivity(), M_Course_list, R.layout.course_item,
				                new String[] {"Course_Name", "Course_teacher", "Course_position"},
				                new int[] {R.id.course_name, R.id.course_teacher, R.id.course_position});
				        listview.setAdapter(simpleAdapter);
				        ListView listview_2 = (ListView) page2View.findViewById(R.id.course_list_2);
						NM_Course_list = QueryCourseFromIn_Course(UserId, 1);
						   SimpleAdapter simpleAdapter_2 = new SimpleAdapter(FindPeopleFragment.this.getActivity(), NM_Course_list, R.layout.course_item,
					                new String[] {"Course_Name", "Course_teacher", "Course_position"},
					                new int[] {R.id.course_name, R.id.course_teacher, R.id.course_position});
						   listview_2.setAdapter(simpleAdapter_2);
						menuDialog.cancel();
					} else {
						Toast.makeText(FindPeopleFragment.this.getActivity(), "插入课程失败，请重新选择！", Toast.LENGTH_LONG).show();
					};
				}
	        });
	        
			// 创建AlertDialog
			//List<Map<String, Object>> list = QueryClassFromCourses(UserClass);
			//final AlertDialog menuDialog = new AlertDialog.Builder(FindPeopleFragment.this.getActivity()).create();
			menuDialog.setView(menuView);
			menuDialog.setCanceledOnTouchOutside(true);
			menuDialog.show();
			return true;
		}
		return false;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}




	class Myclick implements View.OnClickListener{
		int mark;
        public Myclick(int dex){
        	mark=dex;
        }
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			pag.setCurrentItem(mark);
		}	
	}
	
	class MyOnClickPagerChange implements android.support.v4.view.ViewPager.OnPageChangeListener{     //PagerView切换的监听器

		@Override
		public void onPageScrollStateChanged(int arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onPageSelected(int arg0) {
			// TODO Auto-generated method stub
			TranslateAnimation animation = null;
			switch(arg0){
			case 0:
				if(currentIndex==1){
					animation=new TranslateAnimation(2*offset+cursor_width,0,0,0); 
					System.out.println("1-------------------->0");
				}
				break;
			case 1:
                if(currentIndex==0){
                	animation=new TranslateAnimation(0,2*offset+cursor_width,0,0);
                	System.out.println("0-------------------->1");
				}
				break;
			}
			currentIndex=arg0;
			animation.setFillAfter(true);
			animation.setDuration(300);
			cursor.startAnimation(animation);
		}
		
		
	}
	

	private int QueryClassFromin_Class(int User_id) {
		Integer Class_id = null;
		SQLiteDatabase sld=LoadUtil.createOrOpenDatabase();//得到连接数据库的连接
		String sql = "select Class_id from in_Class where User_id = " + User_id;
		try {
			Cursor cur=sld.rawQuery(sql, new String[]{});//得到结果集
			while(cur.moveToNext())//如果存在下一条
			{
				Class_id = Integer.valueOf(cur.getString(0));
				Log.d("FindPeopleFragment", "Query Success From Class_id" + " Class_id = " + Class_id);
			}
			cur.close();//关闭结果集
			sld.close();//关闭连接
		} catch(Exception e)
		{
			e.printStackTrace();		
			Log.d("FindPeopleFragment", "Query Wrong From Class_id");
		}
		return Class_id;
	}
	
	private List<String> QueryClassFromCourses(int Grade, int Status) {
		List<String> list = new ArrayList<String>();
		String Course_Name = "";
		SQLiteDatabase sld=LoadUtil.createOrOpenDatabase();//得到连接数据库的连接
		Log.v("FindPeopleFragment", "Status = " + Status);
		String sql = "select  distinct Course_Name from Courses where Course_Grade = " + Grade + " AND Course_status = " + Status;
		Log.v("FindPeopleFragment", sql);
		try {
			Cursor cur=sld.rawQuery(sql, new String[]{});//得到结果集
			while(cur.moveToNext())//如果存在下一条
			{
				Course_Name = cur.getString(0);
				Log.d("FindPeopleFragment", "Query Success From Course" + " Course_Name = " + Course_Name);
				list.add(Course_Name);
			}
			cur.close();//关闭结果集
			sld.close();//关闭连接
		} catch(Exception e)
		{
			e.printStackTrace();		
			Log.d("FindPeopleFragment", "Query Wrong From Course");
		}
		return list;
	}
	
	private List<String> QueryTeacherFromCourses(String Course_Name) throws UnsupportedEncodingException {
		List<String> list = new ArrayList<String>();
		String Course_Teacher = "";
		SQLiteDatabase sld=LoadUtil.createOrOpenDatabase();//得到连接数据库的连接
		String sql = "select distinct Course_teacher from Courses where Course_Name = '" + Course_Name + "'";
		Log.v("FindPeopleFragment", sql);
		try {
			Cursor cur=sld.rawQuery(sql, new String[]{});//得到结果集
			while(cur.moveToNext())//如果存在下一条
			{
				Course_Teacher = cur.getString(0);
				Log.d("FindPeopleFragment", "Query Success From Course" + " Course_Teacher = " + Course_Teacher);
				list.add(Course_Teacher);
			}
			cur.close();//关闭结果集
			sld.close();//关闭连接
		} catch(Exception e)
		{
			e.printStackTrace();		
			Log.d("FindPeopleFragment", "Query Wrong From Course");
		}
		return list;
	}
	
	private List<Map<String, Object>> QueryClassFromCourses(int UserClass) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		String Course_Name = "";
		String Course_teacher = "";
		String Course_position = "";
		SQLiteDatabase sld=LoadUtil.createOrOpenDatabase();//得到连接数据库的连接
		String sql = "select Course_Name, Course_teacher, Course_position from Courses where Course_Class_id = " + UserClass;
		try {
			Cursor cur=sld.rawQuery(sql, new String[]{});//得到结果集
			while(cur.moveToNext())//如果存在下一条
			{
				Map<String, Object> listItem = new HashMap<String, Object>();
				
				Course_Name = cur.getString(0);
				Course_teacher = cur.getString(1);
				Course_position = cur.getString(2);
				
				listItem.put("Course_Name", Course_Name);
				listItem.put("Course_teacher", Course_teacher);
				listItem.put("Course_position", Course_position);
				
				list.add(listItem);
				
				Log.d("FindPeopleFragment", "Query Success From Classes " + "Class_Name = " + Course_Name + "Class_teacher = " + Course_teacher + " Course_position = " + Course_position);
			}
			cur.close();//关闭结果集
			sld.close();//关闭连接
		} catch(Exception e)
		{
			e.printStackTrace();		
			Log.d("FindPeopleFragment", "Query Wrong From Classes");
		}
		return list;
	}
	
	private void InitViewpager(){                          //初始化Adapter
		   LayoutInflater inflater = FindPeopleFragment.this.getActivity().getLayoutInflater();
		   //pager=new ArrayList<View>();
		   //View page1View = inflater.inflate(R.layout.page1,null);
			ListView listview = (ListView) page1View.findViewById(R.id.course_list_1);
			M_Course_list = QueryCourseFromIn_Course(UserId, 0);
			SimpleAdapter simpleAdapter = new SimpleAdapter(FindPeopleFragment.this.getActivity(), M_Course_list, R.layout.course_item,
	                new String[] {"Course_Name", "Course_teacher", "Course_position"},
	                new int[] {R.id.course_name, R.id.course_teacher, R.id.course_position});
	        listview.setAdapter(simpleAdapter);
	        
	        listview.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					// TODO Auto-generated method stub
					Map<String, Object> listItem = new HashMap<String, Object>();
					listItem = M_Course_list.get(arg2);
					Integer Course_id = (Integer) listItem.get("Course_id");
					Log.v("FindPeopleFragment", "Course_id = " + Course_id);
					Intent MainActivityToInFormation = new Intent();
					MainActivityToInFormation.setClass(FindPeopleFragment.this.getActivity(), Information_activity.class);
					Bundle bundle = new Bundle();
					bundle.putString("User_id", String.valueOf(UserId));
					bundle.putString("Course_id", Course_id.toString());
					bundle.putString("Class_id", String.valueOf(UserClass));
					MainActivityToInFormation.putExtras(bundle);
					startActivity(MainActivityToInFormation);
					Log.v("FindPeopleFragment", "Course_id = " + Course_id);
				} 	
	        });
	        
	       listview.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				Map<String, Object> listItem = new HashMap<String, Object>();
				listItem = M_Course_list.get(arg2);
				final Integer Course_id = (Integer) listItem.get("Course_id");
				String Course_Name = (String) listItem.get("Course_Name");
				String Course_Teacher = (String) listItem.get("Course_teacher");
				Log.v("FindPeopleFragment", "LongClick Course_id = " + Course_id);
				AlertDialog dialog = new AlertDialog.Builder(FindPeopleFragment.this.getActivity()) 
			 	.setTitle("警告")
			 	.setMessage("课程名：" + Course_Name + "\n" + "主讲人：" + Course_Teacher + "\n" + "确定删除吗？")
			 	.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						SQLiteDatabase sld=LoadUtil.createOrOpenDatabase();//得到连接数据库的连接
						String sql = "delete from in_Course where User_id = " + UserId + " and Course_id = " + Course_id;
						try {
						      sld.execSQL(sql);
							  sld.close();//关闭连接
							  Log.v("FindPeopleFragment", sql);
							  Toast.makeText(FindPeopleFragment.this.getActivity(), "恭喜你，删除成功！", Toast.LENGTH_LONG).show();
							} catch(Exception e) {
							   e.printStackTrace();
							   Toast.makeText(FindPeopleFragment.this.getActivity(), "对不起，删除失败！", Toast.LENGTH_LONG).show();
							}
						ListView listview = (ListView) page1View.findViewById(R.id.course_list_1);
						M_Course_list = QueryCourseFromIn_Course(UserId, 0);
						SimpleAdapter simpleAdapter = new SimpleAdapter(FindPeopleFragment.this.getActivity(), M_Course_list, R.layout.course_item,
				                new String[] {"Course_Name", "Course_teacher", "Course_position"},
				                new int[] {R.id.course_name, R.id.course_teacher, R.id.course_position});
				        listview.setAdapter(simpleAdapter);
						dialog.cancel();
					}
			 	}
			 	)
			 	.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.cancel();
					}
				})
			 	.show();
				return false;
			}
	    	   
	       });	       
		   pager.add(page1View);
		   //View page2View = inflater.inflate(R.layout.page2,null);
		   ListView listview_2 = (ListView) page2View.findViewById(R.id.course_list_2);
		   NM_Course_list = QueryCourseFromIn_Course(UserId, 1);
		   SimpleAdapter simpleAdapter_2 = new SimpleAdapter(FindPeopleFragment.this.getActivity(), NM_Course_list, R.layout.course_item,
	                new String[] {"Course_Name", "Course_teacher", "Course_position"},
	                new int[] {R.id.course_name, R.id.course_teacher, R.id.course_position});
		   listview_2.setAdapter(simpleAdapter_2);
		   listview_2.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					// TODO Auto-generated method stub
					Map<String, Object> listItem = new HashMap<String, Object>();
					listItem = NM_Course_list.get(arg2);
					Integer Course_id = (Integer) listItem.get("Course_id");
					Intent MainActivityToInFormation = new Intent();
					MainActivityToInFormation.setClass(FindPeopleFragment.this.getActivity(), Information_activity.class);
					Bundle bundle = new Bundle();
					bundle.putString("User_id", String.valueOf(UserId));
					bundle.putString("Course_id", Course_id.toString());
					bundle.putString("Class_id", String.valueOf(UserClass));
					MainActivityToInFormation.putExtras(bundle);
					startActivity(MainActivityToInFormation);
					Log.v("FindPeopleFragment", "Course_id = " + Course_id);
					
				} 	
	        });
	        
	       listview_2.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				Map<String, Object> listItem = new HashMap<String, Object>();
				listItem = NM_Course_list.get(arg2);
				final Integer Course_id = (Integer) listItem.get("Course_id");
				String Course_Name = (String) listItem.get("Course_Name");
				String Course_Teacher = (String) listItem.get("Course_teacher");
				Log.v("FindPeopleFragment", "LongClick Course_id = " + Course_id);
				AlertDialog dialog = new AlertDialog.Builder(FindPeopleFragment.this.getActivity()) 
			 	.setTitle("警告")
			 	.setMessage("课程名：" + Course_Name + "\n" + "主讲人：" + Course_Teacher + "\n" + "确定删除吗？")
			 	.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						SQLiteDatabase sld=LoadUtil.createOrOpenDatabase();//得到连接数据库的连接
						String sql = "delete from in_Course where User_id = " + UserId + " and Course_id = " + Course_id;
						try {
						      sld.execSQL(sql);
							  sld.close();//关闭连接
							  Log.v("FindPeopleFragment", sql);
							  Toast.makeText(FindPeopleFragment.this.getActivity(), "恭喜你，删除成功！", Toast.LENGTH_LONG).show();
							} catch(Exception e) {
							   e.printStackTrace();
							   Toast.makeText(FindPeopleFragment.this.getActivity(), "对不起，删除失败！", Toast.LENGTH_LONG).show();
							}
				        ListView listview_2 = (ListView) page2View.findViewById(R.id.course_list_2);
						NM_Course_list = QueryCourseFromIn_Course(UserId, 1);
						   SimpleAdapter simpleAdapter_2 = new SimpleAdapter(FindPeopleFragment.this.getActivity(), NM_Course_list, R.layout.course_item,
					                new String[] {"Course_Name", "Course_teacher", "Course_position"},
					                new int[] {R.id.course_name, R.id.course_teacher, R.id.course_position});
						   listview_2.setAdapter(simpleAdapter_2);
						dialog.cancel();
					}
			 	}
			 	)
			 	.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.cancel();
					}
				})
			 	.show();
				return false;
			}
	    	   
	       });
		   
		   pager.add(page2View);
		   pag.setAdapter(new ViewPagerAdapter(pager));
	   }
	
	 private void  InitCursorPosition(){
    	 DisplayMetrics metric = new DisplayMetrics();
         FindPeopleFragment.this.getActivity().getWindowManager().getDefaultDisplay().getMetrics(metric);
         width = metric.widthPixels;  
         //System.out.println("屏  幕  width"+width+"px");
         cursor_width=BitmapFactory.decodeResource(getResources(), R.drawable.line).getWidth();
         //System.out.println("cursor  width"+cursor_width+"px");
         offset=((width/2)-cursor_width)/2;
         //System.out.println(offset);
         Matrix matrix=new Matrix();
         matrix.postTranslate(offset,0);
         cursor.setImageMatrix(matrix);
         
    }
	 
	 private List<Map<String, Object>> QueryCourseFromIn_Course(int UserId, int status) {
		 List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			String Course_Name = "";
			String Course_teacher = "";
			String Course_position = "";
			Integer Course_id;
			SQLiteDatabase sld=LoadUtil.createOrOpenDatabase();//得到连接数据库的连接
			String sql = "select C.Course_Name, C.Course_teacher, C.Course_position, C.Course_id from Courses C, in_Course IC where IC.User_id =  " + UserId + " and IC.Course_status = " + status + " and IC.Course_id = C.Course_id";
			Log.v("FindPeopleFragment", sql);
			try {
				Cursor cur=sld.rawQuery(sql, new String[]{});//得到结果集
				while(cur.moveToNext())//如果存在下一条
				{
					Map<String, Object> listItem = new HashMap<String, Object>();
					
					Course_Name = cur.getString(0);
					Course_teacher = cur.getString(1);
					Course_position = cur.getString(2);
					Course_id = Integer.parseInt(cur.getString(3));
					
					listItem.put("Course_Name", Course_Name);
					listItem.put("Course_teacher", Course_teacher);
					listItem.put("Course_position", Course_position);
					listItem.put("Course_id", Course_id);
					
					list.add(listItem);
					
					Log.d("FindPeopleFragment", "Query Success From Courses " + "Class_Name = " + Course_Name + "Class_teacher = " + Course_teacher + " Course_position = " + Course_position);
				}
				cur.close();//关闭结果集
				sld.close();//关闭连接
			} catch(Exception e)
			{
				e.printStackTrace();		
				Log.d("FindPeopleFragment", "Query Wrong From Courses");
			}
			return list;
	 }
	 private int QueryCourseIdFromCourses(String Course_Name, String Teacher_Name) {
		 SQLiteDatabase sld=LoadUtil.createOrOpenDatabase();//得到连接数据库的连接
		 String sql = "select Course_id from Courses where Course_Name = '" + Course_Name + "' and Course_teacher = '" + Teacher_Name +"'";
		 Log.v("FindPeopleFragment", sql);
		 Integer Course_id = null;
		 try {
				Cursor cur=sld.rawQuery(sql, new String[]{});//得到结果集
				while(cur.moveToNext())//如果存在下一条
				{
				Course_id = Integer.parseInt(cur.getString(0));
				Log.v("FindPeopleFragment", "Query Success From Courses " + "Course_id = " + Course_id);
				break;
				}
				cur.close();//关闭结果集
				sld.close();//关闭连接
	     } catch(Exception e)
			{
				e.printStackTrace();		
				Log.d("FindPeopleFragment", "Query Wrong From Courses");
			}
		return Course_id;
}
	 
	 private boolean insertToIn_Course(int User_id, int Course_id, int student_status, int Course_status) {
		 SQLiteDatabase sld=LoadUtil.createOrOpenDatabase();//得到连接数据库的连接
		 String sql = "insert into in_Course(User_id, Course_id, Roll, Course_status) values( " + User_id + ", " + Course_id + ", " + student_status + ", " + Course_status + ")";
		 try {
		      sld.execSQL(sql);
			  sld.close();//关闭连接
			  return true;
			} catch(Exception e)
			{
				e.printStackTrace();		
				Log.d("MainActivity", "Insert Wrong To In_Course");
				return false;
			}
	 }
}