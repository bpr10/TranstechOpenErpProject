package bpr10.git.transtech;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import openerp.OEDomain;
import openerp.OEVersionException;
import openerp.OpenERP;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import bpr10.git.transtech.AsyncTaskCallback.AsyncTaskCallbackInterface;

import com.openerp.orm.OEFieldsHelper;

public class TasksFragment extends Fragment {

	ListView taskList;
	TextView taskId, customer, atm, date;
	OpenERP mOpenERP;
	private String tag;
	private TaskAdapter mTaskAdapter;
	private ProgressDialog pDialog;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.tasks, container, false);
		taskList = (ListView) rootView.findViewById(R.id.task_list);
		pDialog = new ProgressDialog(TasksFragment.this.getActivity());

		pDialog.setCancelable(false);
		pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		pDialog.setTitle("Please Wait");
		pDialog.show();
		new AsyncTaskCallback(new AsyncTaskCallbackInterface() {

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
					domain.add("status", "=", "pending");
//					domain.add("status", "=", "assigned");
//					domain.add("status", "=", "progress");
					OEFieldsHelper fields = new OEFieldsHelper(new String[] {
							"name", "customer", "atm", "country", "task_month",
							"visit_time" });
					mOpenERP = ApplicationClass.getInstance().getOpenERPCon();
					JSONObject serachResposne = mOpenERP.search_read(
							"atm.surverys.management", fields.get(),
							domain.get());

					Log.d("ser", serachResposne.getJSONArray("records")
							.toString());
					return serachResposne.getJSONArray("records").toString();
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
					if (pDialog.isShowing()) {
						pDialog.dismiss();
					}
					JSONArray results = new JSONArray(result);
					mTaskAdapter = new TaskAdapter(results);
					taskList.setAdapter(mTaskAdapter);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).execute();

		 taskList.setOnItemClickListener(new OnItemClickListener() {
		
		 @Override
		 public void onItemClick(AdapterView<?> parent, View view,
		 int position, long id) {
			 Log.d(tag,mTaskAdapter.getItem(position).toString());
		 Intent i=new Intent(getActivity(),TaskDetails.class);
		 i.putExtra("taskDetais",
				 mTaskAdapter.getItem(position).toString());
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
				if (position % 2 == 0) {
					convertView.findViewById(
							R.id.tasklist_layout)
							.setBackgroundColor(
									getResources().getColor(R.color.task_list_backgorung_white));
				} else {
					convertView
							.findViewById(
									R.id.tasklist_layout)
									.setBackgroundColor(
											getResources().getColor(R.color.task_list_backgorung_gray));
				}

				
				
				try {
					taskId.setText(taskData.getJSONObject(position)
							.getInt("id") + "");
					customer.setText(taskData.getJSONObject(position)
							.getJSONArray("customer").getString(1)
							+ "");
					atm.setText(taskData.getJSONObject(position)
							.getJSONArray("atm").getString(1)
							+ "");
					try {
						date.setText(dateUtility
								.getFriendlyDateString(dateUtility
										.convertSerevrDatetoLocalDate(taskData
												.getJSONObject(position)
												.getString("visit_time"))));
					} catch (ParseException e) {
						e.printStackTrace();
					}
//					Bundle bundle = new Bundle();
//					bundle.putInt("TaskId", taskData.getJSONObject(position)
//							.getInt("id"));
//					bundle.putString("atm", taskData.getJSONObject(position)
//							.getJSONArray("atm").getString(1)
//							+ "");

					// taskData.getJSONObject(position).getJSONArray("visit_time").getString(1)+""
				} catch (JSONException e) {
					e.printStackTrace();
				}

			}

			return convertView;
		}
	}
}
