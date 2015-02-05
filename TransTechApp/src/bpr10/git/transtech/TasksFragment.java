package bpr10.git.transtech;

import java.io.IOException;
import java.text.ParseException;

import openerp.OEDomain;
import openerp.OEVersionException;
import openerp.OpenERP;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import bpr10.git.transtech.AsyncTaskCallback.AsyncTaskCallbackInterface;

import com.openerp.orm.OEFieldsHelper;

public class TasksFragment extends Fragment implements LocationListener {

	ListView taskList;
	TextView taskId, customer, atm, date;
	OpenERP mOpenERP;
	String atmlat,atmlong;
	private String tag;
	private TaskAdapter mTaskAdapter;
	JSONObject searchResposne;
	LocationManager locationManager;
	String provider;
	Location location,atmLocation;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.tasks, container, false);
		taskList = (ListView) rootView.findViewById(R.id.task_list);
		locationManager=(LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
		new AsyncTaskCallback(getActivity(), new AsyncTaskCallbackInterface() {

			@Override
			public String backGroundCallback() {
				try {

					// Connecting to openERP

					OEDomain domain = new OEDomain();
					PreferencesHelper pref = new PreferencesHelper(
							TasksFragment.this.getActivity());
					Log.d(tag, pref.GetPreferences(PreferencesHelper.Uid));
					domain.add("surveyor", "=", Integer.parseInt(pref
							.GetPreferences(PreferencesHelper.Uid)));
					domain.add("status", "not in",
							new JSONArray().put("waitnig_approve").put("done"));
					// domain.add("status", "=", "pending");
					// domain.add("status", "=", "progress");
					// domain.add("status", "=", "assigned");

					OEFieldsHelper fields = new OEFieldsHelper(new String[] {
							"name", "customer", "atm", "country", "task_month",
							"visit_time" });
					Log.d(tag, "domain " + domain);
					Log.d(tag, "domain JOSNOBJ" + domain.get());
					Log.d(tag, "fields " + fields);
					mOpenERP = ApplicationClass.getInstance().getOpenERPCon();
				    searchResposne = mOpenERP.search_read(
							"atm.surverys.management", fields.get(),
							domain.get());
				    JSONArray tasksArray = searchResposne.getJSONArray("records");
				    for (int i = 0 ; i<tasksArray.length();i++)
				    {
				    	String atmDetails []= tasksArray.getJSONObject(i).getJSONArray("atm").getString(1).split(",");
				    	
				    	if(atmDetails.length>2)
				    	{
				    		String latVal=atmDetails[2];
					    	String langVal=atmDetails[3];
				    	double lat = Double.parseDouble(latVal);
				    	double lon = Double.parseDouble(langVal);
				    	atmLocation = new Location("atmLocation");
				    	atmLocation.setLatitude(lat);
				    	atmLocation.setLongitude(lon);
				    	}
				    	if(location!=null)
				    	{
				    	double distance = Math.round((atmLocation
								.distanceTo(location) / 1000) * 100.0) / 100.0;
				    	tasksArray.getJSONObject(i).put("distance", distance);
				    	}
				    	
				    }
					return tasksArray.toString();
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
				try {

					JSONArray tasksArray = new JSONArray(result);
					Log.d("distance", tasksArray.toString());
					mTaskAdapter = new TaskAdapter(tasksArray);
					
					taskList.setAdapter(mTaskAdapter);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
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
				Toast.makeText(getActivity(), "Location can't be retrieved",
						Toast.LENGTH_SHORT).show();

		} else {
			Toast.makeText(getActivity(), "No Provider Found",
					Toast.LENGTH_SHORT).show();
		}
		taskList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Log.d(tag, mTaskAdapter.getItem(position).toString());
				Intent i = new Intent(getActivity(), TaskDetails.class);
				i.putExtra("taskDetais", mTaskAdapter.getItem(position)
						.toString());
				startActivity(i);
			}
		});

		return rootView;
	}

	class TaskAdapter extends BaseAdapter {
		private JSONArray taskData;
		private DateUtility dateUtility;

		public TaskAdapter(JSONArray taskData) {
			this.taskData = taskData;
			dateUtility = new DateUtility();
		}

		@Override
		public int getCount() {
			return taskData.length();
		}

		@Override
		public Object getItem(int position) {
			try {
				return taskData.getJSONObject(position);
			} catch (JSONException e) {

				e.printStackTrace();
				return null;
			}
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			if (convertView == null) {
				convertView = LayoutInflater.from(getActivity()).inflate(
						R.layout.tast_list, null);
				taskId = (TextView) convertView.findViewById(R.id.task_id);
				customer = (TextView) convertView.findViewById(R.id.customer);
				atm = (TextView) convertView.findViewById(R.id.atm);
				date = (TextView) convertView.findViewById(R.id.taskdate);
				
				try {
					taskId.setText(taskData.getJSONObject(position)
							.get("distance") + "");
					customer.setText(taskData.getJSONObject(position)
							.getJSONArray("customer").getString(1)
							+ "");
					String[] atmarr=taskData.getJSONObject(position)
							.getJSONArray("atm").getString(1).split(",");
					String atm1=atmarr[0];
					String atm2=atmarr[1];
					
					atm.setText(atm1+atm2);
					try {
						date.setText(dateUtility
								.getFriendlyDateString(dateUtility
										.convertSerevrDatetoLocalDate(taskData
												.getJSONObject(position)
												.getString("visit_time"))));
					} catch (ParseException e) {
						e.printStackTrace();
					}
					
				} catch (JSONException e) {
					e.printStackTrace();
				}

			}

			return convertView;
		}
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
