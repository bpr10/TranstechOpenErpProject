package bpr10.git.transtech;

import java.io.IOException;

import openerp.OEDomain;
import openerp.OEVersionException;
import openerp.OpenERP;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.R.string;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import bpr10.git.transtech.AsyncTaskCallback.AsyncTaskCallbackInterface;

import com.openerp.orm.OEFieldsHelper;

public class TasksFragment extends Fragment implements LocationListener {

	private ListView taskList;
	private TextView distancelabel, customer, atm, date;
	private OpenERP mOpenERP;
	private String tag;
	private TaskAdapter mTaskAdapter;
	private JSONObject searchResposne;
	private LocationManager locationManager;
	private String provider;
	private Location location, atmLocation;
	private JSONArray tasksArray;
	public static Double currentLat,currentLang;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.tasks, container, false);
		taskList = (ListView) rootView.findViewById(R.id.task_list);
		locationManager = (LocationManager) getActivity().getSystemService(
				Context.LOCATION_SERVICE);
		setHasOptionsMenu(true);
		new AsyncTaskCallback(getActivity(), new AsyncTaskCallbackInterface()
		{

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
							new JSONArray().put("waitnig_approve").put("done")
									.put("cancel"));
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
					tasksArray = searchResposne.getJSONArray("records");
					for (int i = 0; i < tasksArray.length(); i++) {
						String atmDetails[] = tasksArray.getJSONObject(i)
								.getJSONArray("atm").getString(1).split(",");
Log.d("atm details",tasksArray.getJSONObject(i)
		.getJSONArray("atm")+"");
            int n=atmDetails.length;
						if (atmDetails.length > 2) {
							String latVal = atmDetails[n-2];
							String langVal = atmDetails[n-1];
							double lat = Double.parseDouble(latVal);
							double lon = Double.parseDouble(langVal);
							atmLocation = new Location("atmLocation");
							atmLocation.setLatitude(lat);
							atmLocation.setLongitude(lon);
						}
						if (location != null) {
							double distance = Math.round((atmLocation
									.distanceTo(location) / 1000) * 100.0) / 100.0;
							tasksArray.getJSONObject(i).put("distance",
									distance);
						}

					}
					Log.d("response", tasksArray.toString());

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

	class TaskAdapter extends ArrayAdapter<JSONObject> {
		private JSONArray taskData;
		private DateUtility dateUtility;
		private String tag = getClass().getSimpleName();
		private TaskAdapter thisInstance;

		public TaskAdapter(JSONArray taskData) {
			super(getActivity(), R.layout.listview_item_row);
			this.taskData = taskData;
			thisInstance = this;
			dateUtility = new DateUtility();

		}

		public void refresh(JSONArray taskArray) {
			this.clear();
			for (int i = 0; i < taskArray.length(); i++) {
				try {
					this.add(taskArray.getJSONObject(i));
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			new Handler(Looper.getMainLooper()).post(new Runnable() {
				@Override
				public void run() {
					Log.d(tag, "UI thread");
					thisInstance.notifyDataSetChanged();
				}
			});

		}

		@Override
		public int getCount() {
			return taskData.length();
		}

		@Override
		public JSONObject getItem(int position) {
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
			if (convertView == null) {
				convertView = LayoutInflater.from(getActivity()).inflate(
						R.layout.tast_list, null);
				distancelabel = (TextView) convertView
						.findViewById(R.id.task_id);
				customer = (TextView) convertView.findViewById(R.id.customer);
				atm = (TextView) convertView.findViewById(R.id.atm);
				date = (TextView) convertView.findViewById(R.id.taskdate);
				try {
					customer.setText(taskData.getJSONObject(position)
							.getJSONArray("customer").getString(1)
							+ "");
					String[] atmarr = taskData.getJSONObject(position)
							.getJSONArray("atm").getString(1).split(",");
					if(atmarr.length>1)
					{
					String atm1 = atmarr[0];
					String atm2 = atmarr[1];

					atm.setText(atm1 + atm2);
					}
					date.setText(dateUtility.getFriendlyDateString(dateUtility
							.makeDate(taskData.getJSONObject(position)
									.getString("visit_time"))));
					distancelabel.setText(((Double) taskData.getJSONObject(
							position).get("distance")).intValue()
							+ "");

				} catch (JSONException e) {
					e.printStackTrace();
				}

			}

			return convertView;
		}
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

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.task_menu, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int itemId = item.getItemId();

		if (itemId == R.id.action_sort) {
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			LayoutInflater inflater = getActivity().getLayoutInflater();
			final View alertView = inflater.inflate(R.layout.sort_alert, null);
			builder.setView(alertView)
					.setPositiveButton(R.string.sort_dialog_confirm_label,
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int id) {
									RadioGroup sortOptions = (RadioGroup) alertView
											.findViewById(R.id.sort_options);
									switch (sortOptions
											.getCheckedRadioButtonId()) {
									case R.id.sort_by_distance:
										Log.d(tag, "Sorting by Distance");
										sortTasks("distance");
										break;
									case R.id.sort_by_visit_date:
										Log.d(tag, "Sorting by Visit Date");
										sortTasks("visit_time");
										break;
									default:
										break;
									}
								}
							})
					.setNegativeButton(R.string.cancel,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {

								}
							});
			AlertDialog d = builder.create();
			d.show();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	void sortTasks(String key) {
		Log.d(tag + " Sort by ", key);
		if (mTaskAdapter != null) {
			mTaskAdapter.clear();
			mTaskAdapter = new TaskAdapter(new JSONUtilities().sortJSONArray(
					tasksArray, key, (Double) 2.3));
			taskList.invalidate();
			taskList.setAdapter(mTaskAdapter);
		}

	}

}
