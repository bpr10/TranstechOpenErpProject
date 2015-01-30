package bpr10.git.transtech;

import java.io.IOException;

import openerp.OEDomain;
import openerp.OEVersionException;
import openerp.OpenERP;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import bpr10.git.transtech.AsyncTaskCallback.AsyncTaskCallbackInterface;

import com.openerp.orm.OEFieldsHelper;

public class TaskDetails extends Activity implements LocationListener {
	TextView customer, ATMDtails, location, distance, dueDate;
	Button survoeyNow;
	LocationManager locationManager;
	String provider;
	OpenERP mOpenERP;
	private ProgressDialog pDialog;
	private String tag = getClass().getSimpleName();
	JSONObject taskObj = new JSONObject();
	JSONObject atmResposne;
	public static final String taskIDKey = "taskId";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.task_details);
		customer = (TextView) findViewById(R.id.customer);
		ATMDtails = (TextView) findViewById(R.id.details_atm);
		location = (TextView) findViewById(R.id.location);
		distance = (TextView) findViewById(R.id.distance);
		dueDate = (TextView) findViewById(R.id.due_date);
		survoeyNow = (Button) findViewById(R.id.survoeynow_but);
		try {
			taskObj = new JSONObject(getIntent().getStringExtra("taskDetais"));
			Log.d(tag, taskObj.toString());
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		new AsyncTaskCallback(new AsyncTaskCallbackInterface() {

			@Override
			public String backGroundCallback() {
				try {

					// Connecting to openERP

					OEDomain domain = new OEDomain();
					domain.add("id", "=", taskObj.getJSONArray("atm").get(0));
					Log.d("domainvalues", domain.getArray().toString());
					OEFieldsHelper fields = new OEFieldsHelper(new String[] {
							"longitude", "latitude", "customer", "name",
							"country", "date" });
					Log.d("fields", fields.get().toString());
					mOpenERP = ApplicationClass.getInstance().getOpenERPCon();
					atmResposne = mOpenERP.search_read("atm.info",
							fields.get(), domain.get());

					Log.d("Atm details", atmResposne.toString());
					return atmResposne.toString();
				} catch (ClientProtocolException e) {
					e.printStackTrace();
					return null;
				} catch (JSONException e) {
					e.printStackTrace();
					return null;
				} catch (IOException e) {
					e.printStackTrace();
					return null;
				} catch (OEVersionException e) {
					e.printStackTrace();
					return null;
				}

			}

			@Override
			public void foregroundCallback(String result) {

				Location l = new Location("atmlocation");
				try {
					String lat = atmResposne.getJSONArray("records")
							.getJSONObject(0).getString("latitude");
					String lang = atmResposne.getJSONArray("records")
							.getJSONObject(0).getString("longitude");
					String country = atmResposne.getJSONArray("records")
							.getJSONObject(0).getJSONArray("country")
							.getString(1);
					String atmDetails = atmResposne.getJSONArray("records")
							.getJSONObject(0).getString("name");
					String customerText = atmResposne.getJSONArray("records")
							.getJSONObject(0).getJSONArray("customer")
							.getString(1);

					if (!lat.equals("false")) {
						double latitude = Double.parseDouble(lat);
						l.setLatitude(latitude);
					}
					if (!lang.equals("false")) {
						double longitude = Double.parseDouble(lang);
						l.setLongitude(longitude);
					}

					Location location = locationManager
							.getLastKnownLocation(provider);
					double distanceVal = Math.round((location.distanceTo(l) / 1000) * 100.0) / 100.0;
					distance.setText(distance + "");
					System.out.println(distanceVal);
					customer.setText(customerText);
					ATMDtails.setText(atmDetails);

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}).execute();
		Criteria criteria = new Criteria();
		provider = locationManager.getBestProvider(criteria, false);
		if (provider != null && !provider.equals("")) {
			Location location = locationManager.getLastKnownLocation(provider);
			locationManager.requestLocationUpdates(provider, 20000, 1, this);
			if (location != null)
				onLocationChanged(location);
			else
				Toast.makeText(getBaseContext(), "Location can't be retrieved",
						Toast.LENGTH_SHORT).show();

		} else {
			Toast.makeText(getBaseContext(), "No Provider Found",
					Toast.LENGTH_SHORT).show();
		}
		survoeyNow.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(TaskDetails.this, TaskForm.class);
				try {
					i.putExtra(taskIDKey, taskObj.getInt("id"));
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				startActivity(i);

			}
		});
	}

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub

	}

}
