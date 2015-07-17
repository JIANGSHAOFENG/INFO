package com.dbms_v2;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends FragmentActivity implements OnItemClickListener {

	private CharSequence mTitle;
	private CharSequence mDrawerTitle;
	private String[] mNavMenuTitles;
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private List<NavDrawerItem> mNavDrawerItems;
	private TypedArray mNavMenuIconsTypeArray;
	private NavDrawerListAdapter mAdapter;
	private ActionBarDrawerToggle mDrawerToggle;
	private int User_id;
	private int Class_id;
	
	public GestureDetector gestureDetector = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//requestWindowFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
		setContentView(R.layout.activity_main);
		Intent from_main = getIntent();
		User_id = from_main.getIntExtra("UserId", 12330000);
		Class_id = QueryClassFromin_Class(User_id);
		List<Integer> list = QueryM_Course_idFromCourses(Class_id);
		insertCourse_idToIn_Course(list);
        //getActionBar().setBackgroundDrawable(new ColorDrawable(android.R.color.transparent));
		findView();
		
		if (savedInstanceState == null) {
	        selectItem(0);
	    }
	}

	@SuppressLint("NewApi")
	private void findView() {
		
		mTitle = mDrawerTitle = getTitle();
        
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        
        mNavMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);
     	mNavMenuIconsTypeArray = getResources()
     				.obtainTypedArray(R.array.nav_drawer_icons);
     		
        mNavDrawerItems = new ArrayList<NavDrawerItem>();

		mNavDrawerItems.add(new NavDrawerItem(mNavMenuTitles[0], mNavMenuIconsTypeArray
				.getResourceId(0, -1)));
		
		mNavDrawerItems.add(new NavDrawerItem(mNavMenuTitles[1], mNavMenuIconsTypeArray
				.getResourceId(1, -1)));

		mNavDrawerItems.add(new NavDrawerItem(mNavMenuTitles[2], mNavMenuIconsTypeArray
				.getResourceId(2, -1)));
		
		mNavDrawerItems.add(new NavDrawerItem(mNavMenuTitles[3], mNavMenuIconsTypeArray
				.getResourceId(3, -1)));
		
		mNavDrawerItems.add(new NavDrawerItem(mNavMenuTitles[4], mNavMenuIconsTypeArray
				.getResourceId(4, -1)));
        
		mNavDrawerItems.add(new NavDrawerItem(mNavMenuTitles[5], mNavMenuIconsTypeArray
				.getResourceId(4, -1)));
		mNavMenuIconsTypeArray.recycle();
        
		
		mAdapter = new NavDrawerListAdapter(getApplicationContext(),
						mNavDrawerItems);
		mDrawerList.setAdapter(mAdapter);
		mDrawerList.setOnItemClickListener(this);
		
		
		// enable ActionBar app icon to behave as action to toggle nav drawer
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
		
		// ActionBarDrawerToggle ties together the the proper interactions
        // between the sliding drawer and the action bar app icon
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.drawable.ic_drawer,  /* nav drawer image to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
                ) {
            public void onDrawerClosed(View view) {
                getActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);

	}
    /*
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	/* Called whenever we call invalidateOptionsMenu() */
	/*
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        return super.onPrepareOptionsMenu(menu);
    }*/
    
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
         // The action bar home/up action should open or close the drawer.
         // ActionBarDrawerToggle will take care of this.
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle action buttons
        switch(item.getItemId()) {
        default:
            return super.onOptionsItemSelected(item);
        }
    }
	
	@Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }
    
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		
		selectItem(position);
	}
	
	/**
	 * Diplaying fragment view for selected nav drawer list item
	 * */
	private void selectItem(int position) {
		// update the main content by replacing fragments
		Fragment fragment = null;
		switch (position) {
		case 0:
			fragment = new HomeFragment();
			break;
		case 1:
			fragment = new FindPeopleFragment();
			((FindPeopleFragment) fragment).setUserIdAndUserGrade(User_id);
			break;
		case 2:
			fragment = new PhotosFragment();
			((PhotosFragment) fragment).setUserId(User_id);
			break;
		case 3:
			fragment = new CommunityFragment();
			break;
		case 4:
			fragment = new WhatsHotFragment();
			break;
		case 5:
			Intent MainActivity_Login = new Intent(MainActivity.this, Main.class);
			startActivity(MainActivity_Login);
		default:
			break;
		}

		if (fragment != null) {
			FragmentManager fragmentManager = getSupportFragmentManager();
			fragmentManager.beginTransaction()
					.replace(R.id.content_frame, fragment).commit();

			// update selected item and title, then close the drawer
			mDrawerList.setItemChecked(position, true);
			mDrawerList.setSelection(position);
			setTitle(mNavMenuTitles[position]);
			mDrawerLayout.closeDrawer(mDrawerList);
		} else {
			// error in creating fragment
			Log.e("MainActivity", "Error in creating fragment");
		}
	}
	
	@SuppressLint("NewApi")
	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		getActionBar().setTitle(mTitle);
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

	private List<Integer> QueryM_Course_idFromCourses(int Class_id) {
		List<Integer>list = new ArrayList<Integer>();
		int Course_id = 0;
		SQLiteDatabase sld=LoadUtil.createOrOpenDatabase();//得到连接数据库的连接
		String sql = "select Course_id from Courses where Course_Class_id = " + Class_id;
		try {
			Cursor cur=sld.rawQuery(sql, new String[]{});//得到结果集
			while(cur.moveToNext())//如果存在下一条
			{
				Course_id = Integer.valueOf(cur.getString(0));
				list.add(Course_id);
				Log.d("MainActivityt", "Query Success From QueryM_Course_idFromCourses" + " Course_id = " + Course_id);
			}
			cur.close();//关闭结果集
			sld.close();//关闭连接
		} catch(Exception e)
		{
			e.printStackTrace();		
			Log.d("MainActivity", "Query Wrong From QueryM_Course_idFromCourses");
		}
		return list;
	}
	
	private void insertCourse_idToIn_Course(List<Integer> list) {
		SQLiteDatabase sld=LoadUtil.createOrOpenDatabase();//得到连接数据库的连接
		for(Integer Course_id:list) {
			String sql = "insert into in_Course(User_id, Course_id, Roll, Course_status) values( " + User_id + ", " + Course_id + ", " + "0, 0)";
			try {
				sld.execSQL(sql);
			} catch(Exception e)
			{
				e.printStackTrace();		
				Log.d("MainActivity", "Insert Wrong To In_Course");
			}
		}
		
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
