package bpr10.git.transtech;

import java.io.IOException;

import openerp.OEVersionException;
import openerp.OpenERP;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;

import android.app.Application;

public class ApplicationClass extends Application {
	
	private static ApplicationClass applicationInstance;
	private OpenERP mOpenERP;
	public static int surveyor_Id ; 
	@Override
	public void onCreate() {
		super.onCreate();
		applicationInstance = new ApplicationClass();
		
	}

	public static synchronized ApplicationClass getInstance() {
		if (!(applicationInstance != null)) {
			applicationInstance = new ApplicationClass();
		}
		return applicationInstance;
	}
	public OpenERP getOpenERPCon() throws ClientProtocolException, JSONException, IOException, OEVersionException {
		if (mOpenERP == null) {
			mOpenERP = new OpenERP("http://192.168.1.28:8069");
		}

		return mOpenERP;
	}
	
	 

}
