package bpr10.git.transtech;

import org.json.JSONArray;
import org.json.JSONException;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import bpr10.git.transtech.AsyncTaskCallback.AsyncTaskCallbackInterface;

public class CreateAlerts extends Fragment {

	Spinner customerSpinner, stateSpinner, atmSpinner, catugorySpinner,
			prioritySpinner, responcecodeSpinner;
	EditText summerText, descriptionText;
	Button submit;
	ArrayAdapter<String> banks;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.create_alert, container,
				false);
		customerSpinner = (Spinner) rootView
				.findViewById(R.id.customer_spinner);
		stateSpinner = (Spinner) rootView.findViewById(R.id.state_spinner);
		atmSpinner = (Spinner) rootView.findViewById(R.id.atm_spinner);
		catugorySpinner = (Spinner) rootView
				.findViewById(R.id.category_spinner);
		prioritySpinner = (Spinner) rootView
				.findViewById(R.id.priority_spinner);
		responcecodeSpinner = (Spinner) rootView
				.findViewById(R.id.reasonscode_pinner);
		summerText = (EditText) rootView.findViewById(R.id.summery_text);
		descriptionText = (EditText) rootView
				.findViewById(R.id.description_text);
		submit = (Button) rootView.findViewById(R.id.alert_submit);
		banks = new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_dropdown_item_1line);

		customerSpinner.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

			}
		});

		return rootView;
	}

	class SpinnerAdapter extends BaseAdapter {
		JSONArray dataArray;

		public SpinnerAdapter(JSONArray dataArray) {
			this.dataArray = dataArray;
		}

		@Override
		public int getCount() {
			return dataArray.length();
		}

		@Override
		public Object getItem(int position) {
			try {
				return dataArray.get(position);
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

			return null;
		}

	}

	void getBanks() {
		new AsyncTaskCallback(new AsyncTaskCallbackInterface() {

			@Override
			public String backGroundCallback() {
				return null;
			}

			@Override
			public void foregroundCallback(String result) {

			}

		});
	}

}