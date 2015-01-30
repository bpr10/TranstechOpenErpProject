package bpr10.git.transtech;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.widget.ImageView;

public class ServiceingForm extends FragmentActivity {
	ViewPager mViewPager;
	ImageView firstDot, secondDot, thirdDot, forthDot;
	private FragmentPageAdapter mFragmentPageAdapter;
	Bundle bundle;
	static int taskId;
	public static JSONObject taskPayload;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.servicingfrom);
		firstDot = (ImageView) findViewById(R.id.first_dot);
		secondDot = (ImageView) findViewById(R.id.second_dot);
		thirdDot = (ImageView) findViewById(R.id.third_dot);
		forthDot = (ImageView) findViewById(R.id.forth_dot);
		firstDot.setImageResource(R.drawable.dot_active);
		secondDot.setImageResource(R.drawable.dot_inactive);
		thirdDot.setImageResource(R.drawable.dot_inactive);
		forthDot.setImageResource(R.drawable.dot_inactive);
//		taskId = bundle.getInt("TaskId");
		mFragmentPageAdapter = new FragmentPageAdapter(
				getSupportFragmentManager());

		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mFragmentPageAdapter);
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				switch (arg0) {
				case 0:
					firstDot.setImageResource(R.drawable.dot_active);
					secondDot.setImageResource(R.drawable.dot_inactive);
					thirdDot.setImageResource(R.drawable.dot_inactive);
					forthDot.setImageResource(R.drawable.dot_inactive);
					break;
				case 1:
					firstDot.setImageResource(R.drawable.dot_inactive);
					secondDot.setImageResource(R.drawable.dot_active);
					thirdDot.setImageResource(R.drawable.dot_inactive);
					forthDot.setImageResource(R.drawable.dot_inactive);
					break;

				case 2:
					firstDot.setImageResource(R.drawable.dot_inactive);
					secondDot.setImageResource(R.drawable.dot_inactive);
					thirdDot.setImageResource(R.drawable.dot_active);
					forthDot.setImageResource(R.drawable.dot_inactive);
					break;
				case 3:
					firstDot.setImageResource(R.drawable.dot_inactive);
					secondDot.setImageResource(R.drawable.dot_inactive);
					thirdDot.setImageResource(R.drawable.dot_inactive);
					forthDot.setImageResource(R.drawable.dot_active);
					break;
				default:
					break;
				}
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub

			}

		});
		try {
			if (checkForTaskId(taskId).equals("0")) {
				// create task in shared prefs
				creteTask(taskId);
				taskPayload = new JSONObject();
			} else {

				taskPayload = new JSONObject(checkForTaskId(taskId));

			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	void creteTask(int taskId)
	{
		SharedPreferences obj = getSharedPreferences("SharedPrefs", Context.MODE_PRIVATE);
		Editor editor = obj.edit();
		editor.putString(String.valueOf(taskId), new JSONObject().toString());
		editor.commit();
	}

	public String checkForTaskId(int taskId) {
		SharedPreferences preferences = getSharedPreferences("SharedPrefs",
				Context.MODE_PRIVATE);
		return preferences.getString(String.valueOf(taskId), "0");
	}

	class FragmentPageAdapter extends FragmentPagerAdapter {

		public FragmentPageAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int arg0) {
			switch (arg0) {
			case 0:
				return new BeforeImages();
			case 1:
				return new CheckList1();
			case 2:
				return new CheckList2();
			case 3:
				return new AfterImages();
			default:
				break;
			}
			return null;

		}

		@Override
		public int getCount() {
			return 4;
		}

	}

}
