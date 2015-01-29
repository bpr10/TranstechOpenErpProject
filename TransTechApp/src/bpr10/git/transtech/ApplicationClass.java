package bpr10.git.transtech;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;

import openerp.OEVersionException;
import openerp.OpenERP;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

public class ApplicationClass extends Application {
	
	private static ApplicationClass applicationInstance;
	private OpenERP mOpenERP;
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
			mOpenERP = new OpenERP("http://162.243.21.15:8069");
		}

		return mOpenERP;
	}
	
	 

}
