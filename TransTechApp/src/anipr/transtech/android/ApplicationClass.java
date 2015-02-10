package anipr.transtech.android;

import java.io.IOException;

import openerp.OEVersionException;
import openerp.OpenERP;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;

import com.crittercism.app.Crittercism;

import android.app.Application;

public class ApplicationClass extends Application {
	
	private static ApplicationClass applicationInstance;
	private OpenERP mOpenERP;
	public static int surveyor_Id ; 
	@Override
	public void onCreate() {
		super.onCreate();
		applicationInstance = new ApplicationClass();
		Crittercism.initialize(getApplicationContext(), "54d8c0e651de5e9f042ed84c");
	}

	public static synchronized ApplicationClass getInstance() {
		if (!(applicationInstance != null)) {
			applicationInstance = new ApplicationClass();
		}
		return applicationInstance;
	}
	public OpenERP getOpenERPCon() throws ClientProtocolException, JSONException, IOException, OEVersionException {
		if (mOpenERP == null) {
			mOpenERP = new OpenERP("http://162.243.21.15:8069");
		}

		return mOpenERP;
	}
	
	 

}
