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
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import bpr10.git.transtech.AsyncTaskCallback.AsyncTaskCallbackInterface;

import com.openerp.orm.OEFieldsHelper;


public class TaskHistory extends Fragment {

	private ListView taskList;
	private List<TaskList> taskData;
	private TextView taskId, customer, atm, date;
	private OpenERP mOpenERP;
	private String tag;
	private TaskAdapter adapter;
	private ProgressDialog pDialog;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.history, container, false);
		taskList = (ListView) rootView.findViewById(R.id.history_list);
		taskData = new ArrayList<TaskList>();
		pDialog = new ProgressDialog(TaskHistory.this.getActivity());

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
							TaskHistory.this.getActivity());
					Log.d(tag, pref.GetPreferences(PreferencesHelper.Uid));
					domain.add("surveyor", "=", Integer.parseInt(pref
							.GetPreferences(PreferencesHelper.Uid)));
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
					adapter = new TaskAdapter(results);
					taskList.setAdapter(adapter);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).execute();

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
						R.layout.history_list_item, null);
				taskId = (TextView) convertView.findViewById(R.id.history_task_id);
				customer = (TextView) convertView.findViewById(R.id.history_customer);
				atm = (TextView) convertView.findViewById(R.id.history_atm);
				date = (TextView) convertView.findViewById(R.id.history_taskdate);
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
						date.setText(dateUtility.getFriendlyDateString(dateUtility
								.convertSerevrDatetoLocalDate(taskData
										.getJSONObject(position)
										.getString("visit_time"))));
					} catch (ParseException e) {
						e.printStackTrace();
					}
					 Bundle bundle = new Bundle();
				        bundle.putInt("TaskId", taskData.getJSONObject(position)
								.getInt("id"));
				        bundle.putString("atm", taskData.getJSONObject(position)
								.getJSONArray("atm").getString(1)
								+ "");
				      
					// taskData.getJSONObject(position).getJSONArray("visit_time").getString(1)+""
				} catch (JSONException e) {
					e.printStackTrace();
				}

			}
			return convertView;
		}

	}

	class TaskList {
		String taskId, customer, atm, date;

		TaskList(String taskId, String customer, String atm, String date) {
			this.taskId = taskId;
			this.customer = customer;
			this.atm = atm;
			this.date = date;
		}
	}
}
