package bpr10.git.transtech;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

public class ApplicationClass extends Application {
	public static String sharedPrefsName = "TransTechData";
	public static String Uid = "UId";
	private SharedPreferences mSharedPreferences;
	private static ApplicationClass applicationInstance;

	@Override
	public void onCreate() {
		super.onCreate();
		applicationInstance = new ApplicationClass();
		mSharedPreferences = getSharedPreferences(sharedPrefsName,
				Context.MODE_PRIVATE);
	}

	SharedPreferences getSharedPrefs() {
		return mSharedPreferences;

	}

	static ApplicationClass getInstance() {
		if (!(applicationInstance != null)) {
			applicationInstance = new ApplicationClass();
		}
		return applicationInstance;
	}
}
