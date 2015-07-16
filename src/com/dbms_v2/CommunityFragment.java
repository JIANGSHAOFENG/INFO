package com.dbms_v2;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;



public class CommunityFragment extends Fragment {
	
	com.RefreshableView.RefreshableView refreshableView;
	final String CSDNURL = "http://ss.sysu.edu.cn/informationsystem/ArticleList.aspx?id=30";
	Handler handler;
	ListView list = null;
	List<Map<String, Object>> school_news_list = new ArrayList<Map<String, Object>>();
	private final int UPDATE_UI = 1;
	private Handler mHandler = new MainHandler();
	
	public CommunityFragment(){}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.fragment_community, container, false);
        list = (ListView) rootView.findViewById(R.id.school_news);
        refreshableView = (com.RefreshableView.RefreshableView) rootView.findViewById(R.id.refreshable_view);
        school_news_list = QueryNewsFromSchoolNews();
        SimpleAdapter simpleAdapter = new SimpleAdapter(CommunityFragment.this.getActivity(), school_news_list, R.layout.news_item,
                new String[] {"Title", "Editer", "Date"},
                new int[] {R.id.news_title, R.id.news_editer, R.id.news_date});
        list.setAdapter(simpleAdapter);
        list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Map<String, Object> map = school_news_list.get(arg2);
				String url = (String)("http://ss.sysu.edu.cn/informationsystem/"+map.get("URL"));
				Intent intent = new Intent(Intent.ACTION_VIEW);
				intent .setData(Uri.parse(url));
				startActivity(intent);
			}
        	
        });
        refreshableView.setOnRefreshListener(new com.RefreshableView.RefreshableView.PullToRefreshListener() {
			@Override
			public void onRefresh() {
				/*handler = getHandler();
				ThreadStart();*/
				school_news_list = getCsdnNetDate();
				inserttoSchool_News(school_news_list);
				refreshableView.finishRefreshing();
				mHandler.sendEmptyMessageDelayed(UPDATE_UI, 0);//子线程不能更新UI，需要通知主线程
				/*SimpleAdapter simpleAdapter = new SimpleAdapter(CommunityFragment.this.getActivity(), school_news_list, R.layout.news_item,
		                new String[] {"Title", "Editer", "Date"},
		                new int[] {R.id.news_title, R.id.news_editer, R.id.news_date});
		        list.setAdapter(simpleAdapter);
		        list.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
							long arg3) {
						// TODO Auto-generated method stub
						Map<String, Object> map = school_news_list.get(arg2);
						String url = (String)("http://ss.sysu.edu.cn/informationsystem/"+map.get("URL"));
						Intent intent = new Intent(Intent.ACTION_VIEW);
						intent .setData(Uri.parse(url));
						startActivity(intent);
					}
		        	
		        });*/
			}
		}, 0);
        return rootView;
    }
        private class MainHandler extends Handler {
            @Override
                public void handleMessage(Message msg) {
                switch (msg.what) {
                    case UPDATE_UI: {
                    	SimpleAdapter simpleAdapter = new SimpleAdapter(CommunityFragment.this.getActivity(), school_news_list, R.layout.news_item,
        		                new String[] {"Title", "Editer", "Date"},
        		                new int[] {R.id.news_title, R.id.news_editer, R.id.news_date});
        		        list.setAdapter(simpleAdapter);
        		        list.setOnItemClickListener(new OnItemClickListener() {

        					@Override
        					public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
        							long arg3) {
        						// TODO Auto-generated method stub
        						Map<String, Object> map = school_news_list.get(arg2);
        						String url = (String)("http://ss.sysu.edu.cn/informationsystem/"+map.get("URL"));
        						Intent intent = new Intent(Intent.ACTION_VIEW);
        						intent .setData(Uri.parse(url));
        						startActivity(intent);
        					}
        		        	
        		        });
                        break;
                    }
                    default:
                        break;
                }
            }
          }
	
	private List<Map<String, Object>> QueryNewsFromSchoolNews() {
		List<Map<String, Object>> school_news_list = new ArrayList<Map<String, Object>>();
		SQLiteDatabase sld=LoadUtil.createOrOpenDatabase();//得到连接数据库的连接
		String sql = "select URL, Title, Editer, Date from School_News";
		try {
			Cursor cur=sld.rawQuery(sql, new String[]{});//得到结果集
			while(cur.moveToNext()) {
				Map<String, Object> school_new_list_item = new HashMap<String, Object>();
				String URL = cur.getString(0);
				String Title = cur.getString(1);
				String Editer = cur.getString(2);
				String Date = cur.getString(3);
				
				school_new_list_item.put("URL", URL);
				school_new_list_item.put("Title", Title);
				school_new_list_item.put("Editer", Editer);
				school_new_list_item.put("Date", Date);
				
				Log.v("CommunityFragment", "Query Success From School_News URL = " + URL + " Title = " + Title + " Editer = " + Editer + " Date = " + Date);
				school_news_list.add(school_new_list_item);
			}
		} catch(Exception e)
		{
			e.printStackTrace();		
			Log.d("CommunityFragment", "Query Wrong From School_News");
		}
		sld.close();
		return school_news_list;
	}
	
	private void ThreadStart() {
		new Thread() {
			public void run() {
				Message msg = new Message();
				try {
					school_news_list = getCsdnNetDate();
					inserttoSchool_News(school_news_list);
					msg.what = school_news_list.size();
				} catch (Exception e) {
					e.printStackTrace();
					msg.what = -1;
				}
				handler.sendMessage(msg);
			}
		}.start();
	}
	
	private List<Map<String, Object>> getCsdnNetDate() {
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		String csdnString = http_get(CSDNURL);
		//<a id="ctl00_ContentPlaceContent_gvInformation_ct.*?_hyTitle" title=".*?" href=\"(.*?)\" target="_blank">(.*?)</a>
        //href="Article.aspx?id=7457" target="_blank">转发：教务处关于2014年端午节放假期间教学安排的通知</a>
		//<li><a title="(.*?)" href="(.*?)" target="_blank" onclick="LogClickCount\(this,363\);">\1</a></li>
		//title="(.*?)" href="(.*?)".*?,363\)
		Pattern p = Pattern.compile("title=\"(.*?)\" href=\"(.*?)\"");
		Matcher m = p.matcher(csdnString);
		while (m.find()) {
			MatchResult mr=m.toMatchResult();
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("Title", mr.group(1));
			map.put("URL", mr.group(2));
			String CSDNURL_GET = "http://ss.sysu.edu.cn/informationsystem/"+map.get("URL");
			String csdnString_get = http_get(CSDNURL_GET);
			Pattern p_get = Pattern.compile("<span id=\"ctl00_ContentPlaceContent_lblSource\">(.*?)</span>");
			Pattern p_get_2 = Pattern.compile("<span id=\"ctl00_ContentPlaceContent_lblDate\">(.*?)</span>");
			Matcher m_get = p_get.matcher(csdnString_get);
			Matcher m_get_2 = p_get_2.matcher(csdnString_get);
			if(m_get.find() && m_get_2.find()) {
				MatchResult mr_get=m_get.toMatchResult();
				MatchResult mr_get_2 = m_get_2.toMatchResult();
				map.put("Editer", mr_get.group(1));
				map.put("Date", mr_get_2.group(1));
				Log.v("CommunityFragment", "Success + Editer = " + mr_get.group(1) + " Date = " + mr_get_2.group(1));
			} else {
				Log.v("CommunityFragment", "Failed to find");
			}
			result.add(map);
		}
		return result;
	}
	
	private String http_get(String url) {
		final int RETRY_TIME = 3;
		HttpClient httpClient = null;
		HttpGet httpGet = null;

		String responseBody = "";
		int time = 0;
		do {
			try {
				httpClient = getHttpClient();
				httpGet = new HttpGet(url);
				HttpResponse response = httpClient.execute(httpGet);
				if (response.getStatusLine().getStatusCode() == 200) {
					//用utf-8编码转化为字符串
					byte[] bResult = EntityUtils.toByteArray(response.getEntity());
					if (bResult != null) {
						responseBody = new String(bResult,"utf-8");
					}
				}
				break;
			} catch (IOException e) {
				time++;
				if (time < RETRY_TIME) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {
					}
					continue;
				}
				e.printStackTrace();
			} finally {
				httpClient = null;
			}
		} while (time < RETRY_TIME);

		return responseBody;
	}
	
	private  HttpClient getHttpClient() {
		HttpParams httpParams = new BasicHttpParams();
		//设定连接超时和读取超时时间
		HttpConnectionParams.setConnectionTimeout(httpParams, 6000);
		HttpConnectionParams.setSoTimeout(httpParams, 30000);
		return new DefaultHttpClient(httpParams);
	}
	
	private Handler getHandler() {
    	return new Handler(){
			public void handleMessage(Message msg) {
				if (msg.what < 0) {
					Toast.makeText(CommunityFragment.this.getActivity(), "数据获取失败", Toast.LENGTH_SHORT).show();
				}else {
					SimpleAdapter simpleAdapter = new SimpleAdapter(CommunityFragment.this.getActivity(), school_news_list, R.layout.news_item,
			                new String[] {"Title", "Editer", "Date"},
			                new int[] {R.id.news_title, R.id.news_editer, R.id.news_date});
			        list.setAdapter(simpleAdapter);
			        list.setOnItemClickListener(new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
								long arg3) {
							// TODO Auto-generated method stub
							Map<String, Object> map = school_news_list.get(arg2);
							String url = (String)("http://ss.sysu.edu.cn/informationsystem/"+map.get("URL"));
							Intent intent = new Intent(Intent.ACTION_VIEW);
							intent .setData(Uri.parse(url));
							startActivity(intent);
						}
			        	
			        });
				}
			}
        };
	}
	
	void inserttoSchool_News(List<Map<String, Object>> list) {
		SQLiteDatabase sld=LoadUtil.createOrOpenDatabase();//得到连接数据库的连接
		for(Map<String, Object> tmp:list) {
        String URL = (String) tmp.get("URL");
        String Title = (String) tmp.get("Title");
        String Editer = (String) tmp.get("Editer");
        String Date = (String) tmp.get("Date");
		String sql = "insert into School_News(URL, Title, Editer, Date) values ('" + URL + "', '" + Title + "', '" + Editer + "', '" + Date + "')";
		Log.v("CommunityFragment", sql);
		try {
		      sld.execSQL(sql);
		      Log.v("CommunityFragment", "insert success to School_News");
		} catch(Exception e) {
			   e.printStackTrace();
			   Log.v("CommunityFragment", "insert failed");
	}
	}
		sld.close();
	}
}
