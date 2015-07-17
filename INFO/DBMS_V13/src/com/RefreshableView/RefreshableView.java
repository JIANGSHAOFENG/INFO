package com.RefreshableView;

import com.dbms_v2.R;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.View.OnTouchListener;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * 鍙繘琛屼笅鎷夊埛鏂扮殑鑷畾涔夋帶浠躲�
 * 
 * @author guolin
 * 
 */
public class RefreshableView extends LinearLayout implements OnTouchListener {

	/**
	 * 涓嬫媺鐘舵�
	 */
	public static final int STATUS_PULL_TO_REFRESH = 0;

	/**
	 * 閲婃斁绔嬪嵆鍒锋柊鐘舵�
	 */
	public static final int STATUS_RELEASE_TO_REFRESH = 1;

	/**
	 * 姝ｅ湪鍒锋柊鐘舵�
	 */
	public static final int STATUS_REFRESHING = 2;

	/**
	 * 鍒锋柊瀹屾垚鎴栨湭鍒锋柊鐘舵�
	 */
	public static final int STATUS_REFRESH_FINISHED = 3;

	/**
	 * 涓嬫媺澶撮儴鍥炴粴鐨勯�搴�
	 */
	public static final int SCROLL_SPEED = -20;

	/**
	 * 涓�垎閽熺殑姣鍊硷紝鐢ㄤ簬鍒ゆ柇涓婃鐨勬洿鏂版椂闂�
	 */
	public static final long ONE_MINUTE = 60 * 1000;

	/**
	 * 涓�皬鏃剁殑姣鍊硷紝鐢ㄤ簬鍒ゆ柇涓婃鐨勬洿鏂版椂闂�
	 */
	public static final long ONE_HOUR = 60 * ONE_MINUTE;

	/**
	 * 涓�ぉ鐨勬绉掑�锛岀敤浜庡垽鏂笂娆＄殑鏇存柊鏃堕棿
	 */
	public static final long ONE_DAY = 24 * ONE_HOUR;

	/**
	 * 涓�湀鐨勬绉掑�锛岀敤浜庡垽鏂笂娆＄殑鏇存柊鏃堕棿
	 */
	public static final long ONE_MONTH = 30 * ONE_DAY;

	/**
	 * 涓�勾鐨勬绉掑�锛岀敤浜庡垽鏂笂娆＄殑鏇存柊鏃堕棿
	 */
	public static final long ONE_YEAR = 12 * ONE_MONTH;

	/**
	 * 涓婃鏇存柊鏃堕棿鐨勫瓧绗︿覆甯搁噺锛岀敤浜庝綔涓篠haredPreferences鐨勯敭鍊�
	 */
	private static final String UPDATED_AT = "updated_at";

	/**
	 * 涓嬫媺鍒锋柊鐨勫洖璋冩帴鍙�
	 */
	private PullToRefreshListener mListener;

	/**
	 * 鐢ㄤ簬瀛樺偍涓婃鏇存柊鏃堕棿
	 */
	private SharedPreferences preferences;

	/**
	 * 涓嬫媺澶寸殑View
	 */
	private View header;

	/**
	 * 闇�鍘讳笅鎷夊埛鏂扮殑ListView
	 */
	private ListView listView;

	/**
	 * 鍒锋柊鏃舵樉绀虹殑杩涘害鏉�
	 */
	private ProgressBar progressBar;

	/**
	 * 鎸囩ず涓嬫媺鍜岄噴鏀剧殑绠ご
	 */
	private ImageView arrow;

	/**
	 * 鎸囩ず涓嬫媺鍜岄噴鏀剧殑鏂囧瓧鎻忚堪
	 */
	private TextView description;

	/**
	 * 涓婃鏇存柊鏃堕棿鐨勬枃瀛楁弿杩�
	 */
	private TextView updateAt;

	/**
	 * 涓嬫媺澶寸殑甯冨眬鍙傛暟
	 */
	private MarginLayoutParams headerLayoutParams;

	/**
	 * 涓婃鏇存柊鏃堕棿鐨勬绉掑�
	 */
	private long lastUpdateTime;

	/**
	 * 涓轰簡闃叉涓嶅悓鐣岄潰鐨勪笅鎷夊埛鏂板湪涓婃鏇存柊鏃堕棿涓婁簰鐩告湁鍐茬獊锛屼娇鐢╥d鏉ュ仛鍖哄垎
	 */
	private int mId = -1;

	/**
	 * 涓嬫媺澶寸殑楂樺害
	 */
	private int hideHeaderHeight;

	/**
	 * 褰撳墠澶勭悊浠�箞鐘舵�锛屽彲閫夊�鏈塖TATUS_PULL_TO_REFRESH, STATUS_RELEASE_TO_REFRESH,
	 * STATUS_REFRESHING 鍜�STATUS_REFRESH_FINISHED
	 */
	private int currentStatus = STATUS_REFRESH_FINISHED;;

	/**
	 * 璁板綍涓婁竴娆＄殑鐘舵�鏄粈涔堬紝閬垮厤杩涜閲嶅鎿嶄綔
	 */
	private int lastStatus = currentStatus;

	/**
	 * 鎵嬫寚鎸変笅鏃剁殑灞忓箷绾靛潗鏍�
	 */
	private float yDown;

	/**
	 * 鍦ㄨ鍒ゅ畾涓烘粴鍔ㄤ箣鍓嶇敤鎴锋墜鎸囧彲浠ョЩ鍔ㄧ殑鏈�ぇ鍊笺�
	 */
	private int touchSlop;

	/**
	 * 鏄惁宸插姞杞借繃涓�layout锛岃繖閲宱nLayout涓殑鍒濆鍖栧彧闇�姞杞戒竴娆�
	 */
	private boolean loadOnce;

	/**
	 * 褰撳墠鏄惁鍙互涓嬫媺锛屽彧鏈塋istView婊氬姩鍒板ご鐨勬椂鍊欐墠鍏佽涓嬫媺
	 */
	private boolean ableToPull;

	/**
	 * 涓嬫媺鍒锋柊鎺т欢鐨勬瀯閫犲嚱鏁帮紝浼氬湪杩愯鏃跺姩鎬佹坊鍔犱竴涓笅鎷夊ご鐨勫竷灞��
	 * 
	 * @param context
	 * @param attrs
	 */
	public RefreshableView(Context context, AttributeSet attrs) {
		super(context, attrs);
		preferences = PreferenceManager.getDefaultSharedPreferences(context);
		header = LayoutInflater.from(context).inflate(R.layout.pull_to_refresh, null, true);
		progressBar = (ProgressBar) header.findViewById(R.id.progress_bar);
		arrow = (ImageView) header.findViewById(R.id.arrow);
		description = (TextView) header.findViewById(R.id.description);
		updateAt = (TextView) header.findViewById(R.id.updated_at);
		touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
		refreshUpdatedAtValue();
		setOrientation(VERTICAL);
		addView(header, 0);
	}

	/**
	 * 杩涜涓�簺鍏抽敭鎬х殑鍒濆鍖栨搷浣滐紝姣斿锛氬皢涓嬫媺澶村悜涓婂亸绉昏繘琛岄殣钘忥紝缁橪istView娉ㄥ唽touch浜嬩欢銆�
	 */
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
		if (changed && !loadOnce) {
			hideHeaderHeight = -header.getHeight();
			headerLayoutParams = (MarginLayoutParams) header.getLayoutParams();
			headerLayoutParams.topMargin = hideHeaderHeight;
			listView = (ListView) getChildAt(1);
			listView.setOnTouchListener(this);
			loadOnce = true;
		}
	}

	/**
	 * 褰揕istView琚Е鎽告椂璋冪敤锛屽叾涓鐞嗕簡鍚勭涓嬫媺鍒锋柊鐨勫叿浣撻�杈戙�
	 */
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		setIsAbleToPull(event);
		if (ableToPull) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				yDown = event.getRawY();
				break;
			case MotionEvent.ACTION_MOVE:
				float yMove = event.getRawY();
				int distance = (int) (yMove - yDown);
				// 濡傛灉鎵嬫寚鏄笅婊戠姸鎬侊紝骞朵笖涓嬫媺澶存槸瀹屽叏闅愯棌鐨勶紝灏卞睆钄戒笅鎷変簨浠�
				if (distance <= 0 && headerLayoutParams.topMargin <= hideHeaderHeight) {
					return false;
				}
				if (distance < touchSlop) {
					return false;
				}
				if (currentStatus != STATUS_REFRESHING) {
					if (headerLayoutParams.topMargin > 0) {
						currentStatus = STATUS_RELEASE_TO_REFRESH;
					} else {
						currentStatus = STATUS_PULL_TO_REFRESH;
					}
					// 閫氳繃鍋忕Щ涓嬫媺澶寸殑topMargin鍊硷紝鏉ュ疄鐜颁笅鎷夋晥鏋�
					headerLayoutParams.topMargin = (distance / 2) + hideHeaderHeight;
					header.setLayoutParams(headerLayoutParams);
				}
				break;
			case MotionEvent.ACTION_UP:
			default:
				if (currentStatus == STATUS_RELEASE_TO_REFRESH) {
					// 鏉炬墜鏃跺鏋滄槸閲婃斁绔嬪嵆鍒锋柊鐘舵�锛屽氨鍘昏皟鐢ㄦ鍦ㄥ埛鏂扮殑浠诲姟
					new RefreshingTask().execute();
				} else if (currentStatus == STATUS_PULL_TO_REFRESH) {
					// 鏉炬墜鏃跺鏋滄槸涓嬫媺鐘舵�锛屽氨鍘昏皟鐢ㄩ殣钘忎笅鎷夊ご鐨勪换鍔�
					new HideHeaderTask().execute();
				}
				break;
			}
			// 鏃跺埢璁板緱鏇存柊涓嬫媺澶翠腑鐨勪俊鎭�
			if (currentStatus == STATUS_PULL_TO_REFRESH
					|| currentStatus == STATUS_RELEASE_TO_REFRESH) {
				updateHeaderView();
				// 褰撳墠姝ｅ浜庝笅鎷夋垨閲婃斁鐘舵�锛岃璁㎜istView澶卞幓鐒︾偣锛屽惁鍒欒鐐瑰嚮鐨勯偅涓�」浼氫竴鐩村浜庨�涓姸鎬�
				listView.setPressed(false);
				listView.setFocusable(false);
				listView.setFocusableInTouchMode(false);
				lastStatus = currentStatus;
				// 褰撳墠姝ｅ浜庝笅鎷夋垨閲婃斁鐘舵�锛岄�杩囪繑鍥瀟rue灞忚斀鎺塋istView鐨勬粴鍔ㄤ簨浠�
				return true;
			}
		}
		return false;
	}

	/**
	 * 缁欎笅鎷夊埛鏂版帶浠舵敞鍐屼竴涓洃鍚櫒銆�
	 * 
	 * @param listener
	 *            鐩戝惉鍣ㄧ殑瀹炵幇銆�
	 * @param id
	 *            涓轰簡闃叉涓嶅悓鐣岄潰鐨勪笅鎷夊埛鏂板湪涓婃鏇存柊鏃堕棿涓婁簰鐩告湁鍐茬獊锛�璇蜂笉鍚岀晫闈㈠湪娉ㄥ唽涓嬫媺鍒锋柊鐩戝惉鍣ㄦ椂涓�畾瑕佷紶鍏ヤ笉鍚岀殑id銆�
	 */
	public void setOnRefreshListener(PullToRefreshListener listener, int id) {
		mListener = listener;
		mId = id;
	}

	/**
	 * 褰撴墍鏈夌殑鍒锋柊閫昏緫瀹屾垚鍚庯紝璁板綍璋冪敤涓�笅锛屽惁鍒欎綘鐨凩istView灏嗕竴鐩村浜庢鍦ㄥ埛鏂扮姸鎬併�
	 */
	public void finishRefreshing() {
		currentStatus = STATUS_REFRESH_FINISHED;
		preferences.edit().putLong(UPDATED_AT + mId, System.currentTimeMillis()).commit();
		new HideHeaderTask().execute();
	}

	/**
	 * 鏍规嵁褰撳墠ListView鐨勬粴鍔ㄧ姸鎬佹潵璁惧畾 {@link #ableToPull}
	 * 鐨勫�锛屾瘡娆￠兘闇�鍦╫nTouch涓涓�釜鎵ц锛岃繖鏍峰彲浠ュ垽鏂嚭褰撳墠搴旇鏄粴鍔↙istView锛岃繕鏄簲璇ヨ繘琛屼笅鎷夈�
	 * 
	 * @param event
	 */
	private void setIsAbleToPull(MotionEvent event) {
		View firstChild = listView.getChildAt(0);
		if (firstChild != null) {
			int firstVisiblePos = listView.getFirstVisiblePosition();
			if (firstVisiblePos == 0 && firstChild.getTop() == 0) {
				if (!ableToPull) {
					yDown = event.getRawY();
				}
				// 濡傛灉棣栦釜鍏冪礌鐨勪笂杈圭紭锛岃窛绂荤埗甯冨眬鍊间负0锛屽氨璇存槑ListView婊氬姩鍒颁簡鏈�《閮紝姝ゆ椂搴旇鍏佽涓嬫媺鍒锋柊
				ableToPull = true;
			} else {
				if (headerLayoutParams.topMargin != hideHeaderHeight) {
					headerLayoutParams.topMargin = hideHeaderHeight;
					header.setLayoutParams(headerLayoutParams);
				}
				ableToPull = false;
			}
		} else {
			// 濡傛灉ListView涓病鏈夊厓绱狅紝涔熷簲璇ュ厑璁镐笅鎷夊埛鏂�
			ableToPull = true;
		}
	}

	/**
	 * 鏇存柊涓嬫媺澶翠腑鐨勪俊鎭�
	 */
	private void updateHeaderView() {
		if (lastStatus != currentStatus) {
			if (currentStatus == STATUS_PULL_TO_REFRESH) {
				description.setText(getResources().getString(R.string.pull_to_refresh));
				arrow.setVisibility(View.VISIBLE);
				progressBar.setVisibility(View.GONE);
				rotateArrow();
			} else if (currentStatus == STATUS_RELEASE_TO_REFRESH) {
				description.setText(getResources().getString(R.string.release_to_refresh));
				arrow.setVisibility(View.VISIBLE);
				progressBar.setVisibility(View.GONE);
				rotateArrow();
			} else if (currentStatus == STATUS_REFRESHING) {
				description.setText(getResources().getString(R.string.refreshing));
				progressBar.setVisibility(View.VISIBLE);
				arrow.clearAnimation();
				arrow.setVisibility(View.GONE);
			}
			refreshUpdatedAtValue();
		}
	}

	/**
	 * 鏍规嵁褰撳墠鐨勭姸鎬佹潵鏃嬭浆绠ご銆�
	 */
	private void rotateArrow() {
		float pivotX = arrow.getWidth() / 2f;
		float pivotY = arrow.getHeight() / 2f;
		float fromDegrees = 0f;
		float toDegrees = 0f;
		if (currentStatus == STATUS_PULL_TO_REFRESH) {
			fromDegrees = 180f;
			toDegrees = 360f;
		} else if (currentStatus == STATUS_RELEASE_TO_REFRESH) {
			fromDegrees = 0f;
			toDegrees = 180f;
		}
		RotateAnimation animation = new RotateAnimation(fromDegrees, toDegrees, pivotX, pivotY);
		animation.setDuration(100);
		animation.setFillAfter(true);
		arrow.startAnimation(animation);
	}

	/**
	 * 鍒锋柊涓嬫媺澶翠腑涓婃鏇存柊鏃堕棿鐨勬枃瀛楁弿杩般�
	 */
	private void refreshUpdatedAtValue() {
		lastUpdateTime = preferences.getLong(UPDATED_AT + mId, -1);
		long currentTime = System.currentTimeMillis();
		long timePassed = currentTime - lastUpdateTime;
		long timeIntoFormat;
		String updateAtValue;
		if (lastUpdateTime == -1) {
			updateAtValue = getResources().getString(R.string.not_updated_yet);
		} else if (timePassed < 0) {
			updateAtValue = getResources().getString(R.string.time_error);
		} else if (timePassed < ONE_MINUTE) {
			updateAtValue = getResources().getString(R.string.updated_just_now);
		} else if (timePassed < ONE_HOUR) {
			timeIntoFormat = timePassed / ONE_MINUTE;
			String value = timeIntoFormat + "分钟";
			updateAtValue = String.format(getResources().getString(R.string.updated_at), value);
		} else if (timePassed < ONE_DAY) {
			timeIntoFormat = timePassed / ONE_HOUR;
			String value = timeIntoFormat + "小时";
			updateAtValue = String.format(getResources().getString(R.string.updated_at), value);
		} else if (timePassed < ONE_MONTH) {
			timeIntoFormat = timePassed / ONE_DAY;
			String value = timeIntoFormat + "天";
			updateAtValue = String.format(getResources().getString(R.string.updated_at), value);
		} else if (timePassed < ONE_YEAR) {
			timeIntoFormat = timePassed / ONE_MONTH;
			String value = timeIntoFormat + "个月";
			updateAtValue = String.format(getResources().getString(R.string.updated_at), value);
		} else {
			timeIntoFormat = timePassed / ONE_YEAR;
			String value = timeIntoFormat + "年";
			updateAtValue = String.format(getResources().getString(R.string.updated_at), value);
		}
		updateAt.setText(updateAtValue);
	}

	/**
	 * 姝ｅ湪鍒锋柊鐨勪换鍔★紝鍦ㄦ浠诲姟涓細鍘诲洖璋冩敞鍐岃繘鏉ョ殑涓嬫媺鍒锋柊鐩戝惉鍣ㄣ�
	 * 
	 * @author guolin
	 */
	class RefreshingTask extends AsyncTask<Void, Integer, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			int topMargin = headerLayoutParams.topMargin;
			while (true) {
				topMargin = topMargin + SCROLL_SPEED;
				if (topMargin <= 0) {
					topMargin = 0;
					break;
				}
				publishProgress(topMargin);
				sleep(10);
			}
			currentStatus = STATUS_REFRESHING;
			publishProgress(0);
			if (mListener != null) {
				mListener.onRefresh();
			}
			return null;
		}

		@Override
		protected void onProgressUpdate(Integer... topMargin) {
			updateHeaderView();
			headerLayoutParams.topMargin = topMargin[0];
			header.setLayoutParams(headerLayoutParams);
		}

	}

	/**
	 * 闅愯棌涓嬫媺澶寸殑浠诲姟锛屽綋鏈繘琛屼笅鎷夊埛鏂版垨涓嬫媺鍒锋柊瀹屾垚鍚庯紝姝や换鍔″皢浼氫娇涓嬫媺澶撮噸鏂伴殣钘忋�
	 * 
	 * @author guolin
	 */
	class HideHeaderTask extends AsyncTask<Void, Integer, Integer> {

		@Override
		protected Integer doInBackground(Void... params) {
			int topMargin = headerLayoutParams.topMargin;
			while (true) {
				topMargin = topMargin + SCROLL_SPEED;
				if (topMargin <= hideHeaderHeight) {
					topMargin = hideHeaderHeight;
					break;
				}
				publishProgress(topMargin);
				sleep(10);
			}
			return topMargin;
		}

		@Override
		protected void onProgressUpdate(Integer... topMargin) {
			headerLayoutParams.topMargin = topMargin[0];
			header.setLayoutParams(headerLayoutParams);
		}

		@Override
		protected void onPostExecute(Integer topMargin) {
			headerLayoutParams.topMargin = topMargin;
			header.setLayoutParams(headerLayoutParams);
			currentStatus = STATUS_REFRESH_FINISHED;
		}
	}

	/**
	 * 浣垮綋鍓嶇嚎绋嬬潯鐪犳寚瀹氱殑姣鏁般�
	 * 
	 * @param time
	 *            鎸囧畾褰撳墠绾跨▼鐫＄湢澶氫箙锛屼互姣涓哄崟浣�
	 */
	private void sleep(int time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 涓嬫媺鍒锋柊鐨勭洃鍚櫒锛屼娇鐢ㄤ笅鎷夊埛鏂扮殑鍦版柟搴旇娉ㄥ唽姝ょ洃鍚櫒鏉ヨ幏鍙栧埛鏂板洖璋冦�
	 * 
	 * @author guolin
	 */
	public interface PullToRefreshListener {

		/**
		 * 鍒锋柊鏃朵細鍘诲洖璋冩鏂规硶锛屽湪鏂规硶鍐呯紪鍐欏叿浣撶殑鍒锋柊閫昏緫銆傛敞鎰忔鏂规硶鏄湪瀛愮嚎绋嬩腑璋冪敤鐨勶紝 浣犲彲浠ヤ笉蹇呭彟寮�嚎绋嬫潵杩涜鑰楁椂鎿嶄綔銆�
		 */
		void onRefresh();

	}

}
