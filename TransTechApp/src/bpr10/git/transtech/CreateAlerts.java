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

import com.openerp.orm.OEFieldsHelper;

import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
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
	ArrayAdapter<String> states;
	JSONObject customerResponce, stateResponce;
	OpenERP mOpenERP;

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
		getCustomer();
		states = new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_dropdown_item_1line);
		getCustomer();
		return rootView;
	}

	

	void getCustomer() {
		new AsyncTaskCallback(new AsyncTaskCallbackInterface() {

			@Override
			public String backGroundCallback() {
				try {

					// Connecting to openERP

					OEDomain domain = new OEDomain();
					PreferencesHelper pref = new PreferencesHelper(
							CreateAlerts.this.getActivity());
					Log.d("domainvalues", domain.getArray().toString());
					OEFieldsHelper fields = new OEFieldsHelper(new String[] {
							"name", "id" });
					OEFieldsHelper statefields = new OEFieldsHelper(new String[] {
							"name", "state_id" });
					Log.d("fields", fields.get().toString());
					mOpenERP = ApplicationClass.getInstance().getOpenERPCon();
				 customerResponce = mOpenERP.search_read(
							"customer.info", fields.get(), domain.get());
				
					Log.d("customer", customerResponce.toString());
					 stateResponce = mOpenERP.search_read(
								"res.country.state", statefields.get(), domain.get());
					
						Log.d("state", stateResponce.toString());
					return customerResponce.toString();
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
					   for (int i = 0; i < customerResponce
					     .getJSONArray("records").length(); i++)
					    banks.add(customerResponce.getJSONArray("records").getJSONObject(i).getString("name"));
					      
					  } catch (JSONException e) {
					   e.printStackTrace();
					  } catch (NullPointerException e) {
					   getCustomer();
					  }

				 customerSpinner.setAdapter(banks);
				 try {
					   for (int i = 0; i < stateResponce
					     .getJSONArray("records").length(); i++)
					    states.add(stateResponce.getJSONArray("records").getJSONObject(i).getString("name"));
					      
					  } catch (JSONException e) {
					   e.printStackTrace();
					  } catch (NullPointerException e) {
					   getCustomer();
					  }

				 stateSpinner.setAdapter(states);
			
			}
		}).execute();
		
	}
//	void getState() {
//		new AsyncTaskCallback(new AsyncTaskCallbackInterface() {
//
//			@Override
//			public String backGroundCallback() {
//				try {
//
//					// Connecting to openERP
//
//					OEDomain domain = new OEDomain();
//					Log.d("domainvalues", domain.getArray().toString());
//					OEFieldsHelper fields = new OEFieldsHelper(new String[] {
//							"name", "state_id" });
//					Log.d("fields", fields.get().toString());
//					mOpenERP = ApplicationClass.getInstance().getOpenERPCon();
//				    stateResponce = mOpenERP.search_read(
//							"res.country.state", fields.get(), domain.get());
//				
//					Log.d("state", stateResponce.toString());
//					return stateResponce.toString();
//				} catch (ClientProtocolException e) {
//					e.printStackTrace();
//					return null;
//				} catch (JSONException e) {
//					e.printStackTrace();
//					return null;
//				} catch (IOException e) {
//					e.printStackTrace();
//					return null;
//				} catch (OEVersionException e) {
//					e.printStackTrace();
//					return null;
//				}
//
//			}
//
//			@Override
//			public void foregroundCallback(String result) {
//				 try {
//					   for (int i = 0; i < stateResponce
//					     .getJSONArray("records").length(); i++)
//					    states.add(stateResponce.getJSONArray("records").getJSONObject(i).getString("name"));
//					      
//					  } catch (JSONException e) {
//					   e.printStackTrace();
//					  } catch (NullPointerException e) {
//					   getCustomer();
//					  }
//
//				 stateSpinner.setAdapter(states);
//			}
//		}).execute();
//	}	
}