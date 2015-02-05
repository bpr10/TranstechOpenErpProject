package bpr10.git.transtech;

import java.io.IOException;
import java.text.ParseException;

import openerp.OEDomain;
import openerp.OEVersionException;
import openerp.OpenERP;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.json.JSONObject;

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

public class TaskDetails extends BaseActivity implements LocationListener {
	TextView customer, ATMDtails, locationText, distance, dueDate;
	Button surveyNow;
	LocationManager locationManager;
	String provider;
	OpenERP mOpenERP;
	private String tag = getClass().getSimpleName();
	JSONObject taskObj = new JSONObject();
	JSONObject atmResposne;
	public static Location location;
	String atm1,atm2;

	private DateUtility dateUtility;
	public static final String taskIDKey = "taskId";
	public static String taskDetais = "taskDetails";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.task_details);
		customer = (TextView) findViewById(R.id.customer);
		ATMDtails = (TextView) findViewById(R.id.details_atm);
		locationText = (TextView) findViewById(R.id.location);
		distance = (TextView) findViewById(R.id.distance);
		dueDate = (TextView) findViewById(R.id.due_date);
		surveyNow = (Button) findViewById(R.id.survoeynow_but);
		
		try {
			taskObj = new JSONObject(getIntent().getStringExtra("taskDetais"));
			Log.d(tag, taskObj.toString());
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		new AsyncTaskCallback(TaskDetails.this,
				new AsyncTaskCallbackInterface() {

					@Override
					public String backGroundCallback() {
						try {

							// Connecting to openERP

							OEDomain domain = new OEDomain();
							domain.add("id", "=", taskObj.getJSONArray("atm")
									.get(0));
							Log.d("domainvalues", domain.getArray().toString());
							OEFieldsHelper fields = new OEFieldsHelper(
									new String[] { "longitude", "latitude",
											"customer", "name", "country",
											"date" });
							Log.d("fields", fields.get().toString());
							mOpenERP = ApplicationClass.getInstance()
									.getOpenERPCon();
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
							String country = taskObj.getJSONArray("country")
									.getString(1);
							String[] atmarr=taskObj
									.getJSONArray("atm").getString(1).split(",");
							 atm1=atmarr[0];
							 atm2=atmarr[1];
							
							
							String customerText = taskObj.getJSONArray(
									"customer").getString(1);

							if (!lat.equals("false")) {
								double latitude = Double.parseDouble(lat);
								l.setLatitude(latitude);
							}
							if (!lang.equals("false")) {
								double longitude = Double.parseDouble(lang);
								l.setLongitude(longitude);
							}
							if (location != null) {
								double distanceVal = Math.round((location
										.distanceTo(l) / 1000) * 100.0) / 100.0;
								distance.setText(distanceVal + "");
							} else {
								distance.setVisibility(View.GONE);
							}
							dateUtility = new DateUtility();
							customer.setText(customerText);
							ATMDtails.setText(atm1+atm2);
							locationText.setText(country);
							try {
								dueDate.setText(dateUtility.getFriendlyDateString(dateUtility
										.convertSerevrDatetoLocalDate(taskObj
												.getString("visit_time"))));
							} catch (ParseException e) {
								e.printStackTrace();
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}

					}
				}).execute();
		Criteria criteria = new Criteria();
		provider = locationManager.getBestProvider(criteria, false);
		if (provider != null && !provider.equals("")) {
			location = locationManager.getLastKnownLocation(provider);
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
		surveyNow.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(TaskDetails.this, TaskForm.class);
				try {
					i.putExtra(taskDetais, taskObj.toString());
				} catch (Exception e) {
					e.printStackTrace();
				}
				startActivity(i);

			}
		});
	}

	@Override
	public void onLocationChanged(Location location) {
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {

	}

	@Override
	public void onProviderEnabled(String provider) {

	}

	@Override
	public void onProviderDisabled(String provider) {

	}

}
