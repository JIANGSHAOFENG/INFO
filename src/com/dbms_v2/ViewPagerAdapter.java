package com.dbms_v2;

import java.util.List;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

public class ViewPagerAdapter extends PagerAdapter{
    List<View> pag=null;
    ViewPagerAdapter(List<View> pag){
    	this.pag=pag;
    }
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return pag.size();
	}

	@Override
	public Object instantiateItem(View container, int position) {
		// TODO Auto-generated method stub
		((ViewPager) container).addView(pag.get(position), position);
		return pag.get(position);

	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		// TODO Auto-generated method stub
		return (Object)arg0==arg1;
	}
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		// TODO Auto-generated method stub
		((ViewPager) container).removeView((View)object);
		System.out.println("delete......");
        //super.destroyItem(container, position, object);
	}

	

}
