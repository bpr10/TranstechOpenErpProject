package bpr10.git.transtech;

import java.io.IOException;

import openerp.OEDomain;
import openerp.OEVersionException;
import openerp.OpenERP;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import bpr10.git.transtech.AsyncTaskCallback.AsyncTaskCallbackInterface;

import com.openerp.orm.OEFieldsHelper;

public class CreateAlerts extends Fragment {

	Spinner customerSpinner, stateSpinner, atmSpinner, categorySpinner,
			assignToSpinner, prioritySpinner, reasoncodeSpinner;
	EditText summeryText, descriptionText;
	Button submit;
	ArrayAdapter<String> banks;
	ArrayAdapter<String> states;
	ArrayAdapter<String> atmDetails;
	ArrayAdapter<String> category;
	ArrayAdapter<String> priority;
	ArrayAdapter<String> reasonCode;
	ArrayAdapter<String> assignTo;
	JSONObject alertPayload;
	JSONObject customerResponce, stateResponce, atmResponce, reasonResponce,
			assignToResponce;
	OpenERP mOpenERP;
	String stateId, summeryValue, descriptionValue;
	String[] categoryArray = new String[] { "complaint", "issue" };
	String[] priorityArray = new String[] { "low", "medium", "high", "critical" };

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.create_alert, container,
				false);
		 customerSpinner = (Spinner) rootView
		 .findViewById(R.id.customer_spinner);
		 stateSpinner = (Spinner) rootView.findViewById(R.id.state_spinner);
		 atmSpinner = (Spinner) rootView.findViewById(R.id.atm_spinner);
		 categorySpinner = (Spinner) rootView
		 .findViewById(R.id.category_spinner);
		 prioritySpinner = (Spinner) rootView
		 .findViewById(R.id.priority_spinner);
		 reasoncodeSpinner = (Spinner) rootView
		 .findViewById(R.id.reasonscode_pinner);
		 summeryText = (EditText) rootView.findViewById(R.id.summery_text);
		 descriptionText = (EditText) rootView
		 .findViewById(R.id.description_text);
		 assignToSpinner=(Spinner) rootView
		 .findViewById(R.id.assigto_spinner);
		 submit = (Button) rootView.findViewById(R.id.alert_submit);
		 banks = new ArrayAdapter<String>(getActivity(),
		 android.R.layout.simple_dropdown_item_1line);
		 getCustomer();
		 alertPayload = new JSONObject();
		 customerSpinner.setOnItemSelectedListener(new
		 OnItemSelectedListener() {
		
		 @Override
		 public void onItemSelected(AdapterView<?> parent, View view,
		 int position, long id) {
		 // TODO Auto-generated method stub
		 try {
		 alertPayload.put("customer",Integer.parseInt(customerResponce.getJSONArray("records")
		 .getJSONObject(position).getString("id")));
		 } catch (JSONException e) {
		 // TODO Auto-generated catch block
		 e.printStackTrace();
		 }
		
		 }
		
		 @Override
		 public void onNothingSelected(AdapterView<?> parent) {
		 // TODO Auto-generated method stub
		
		 }
		 });
		 states = new ArrayAdapter<String>(getActivity(),
		 android.R.layout.simple_dropdown_item_1line);
		 assignTo = new ArrayAdapter<String>(getActivity(),
		 android.R.layout.simple_dropdown_item_1line);
		 atmDetails=new ArrayAdapter<String>(getActivity(),
		 android.R.layout.simple_dropdown_item_1line);
		 atmSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
		
		 @Override
		 public void onItemSelected(AdapterView<?> parent, View view,
		 int position, long id) {
		 // TODO Auto-generated method stub
		 try {
		 alertPayload.put("atm_id",Integer.parseInt(atmResponce.getJSONArray("records")
		 .getJSONObject(position).getString("id")));
		 } catch (JSONException e) {
		 // TODO Auto-generated catch block
		 e.printStackTrace();
		 }
		
		 }
		
		 @Override
		 public void onNothingSelected(AdapterView<?> parent) {
		 // TODO Auto-generated method stub
		
		 }
		 });
		 reasonCode=new ArrayAdapter<String>(getActivity(),
		 android.R.layout.simple_dropdown_item_1line);
		
		 reasoncodeSpinner.setOnItemSelectedListener(new
		 OnItemSelectedListener() {
		
		 @Override
		 public void onItemSelected(AdapterView<?> parent, View view,
		 int position, long id) {
		 // TODO Auto-generated method stub
		
		 try {
		 alertPayload.put("reason_id",Integer.parseInt(reasonResponce.getJSONArray("records")
		 .getJSONObject(position).getString("id")));
		 } catch (JSONException e) {
		 // TODO Auto-generated catch block
		 e.printStackTrace();
		 }
		
		 }
		
		 @Override
		 public void onNothingSelected(AdapterView<?> parent) {
		 // TODO Auto-generated method stub
		
		 }
		 });
		 assignToSpinner.setOnItemSelectedListener(new
		 OnItemSelectedListener() {
		
		 @Override
		 public void onItemSelected(AdapterView<?> parent, View view,
		 int position, long id) {
		 // TODO Auto-generated method stub
		
		 try {
		 alertPayload.put("assign_to",Integer.parseInt(assignToResponce.getJSONArray("records")
		 .getJSONObject(position).getString("id")));
		 } catch (JSONException e) {
		 // TODO Auto-generated catch block
		 e.printStackTrace();
		 }
		
		 }
		
		 @Override
		 public void onNothingSelected(AdapterView<?> parent) {
		 // TODO Auto-generated method stub
		
		 }
		 });
		 stateSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
		
		 @Override
		 public void onItemSelected(AdapterView<?> parent, View view,
		 int position, long id) {
		 stateId=parent.getItemAtPosition(position).toString();
		 try {
		 alertPayload.put("state_id",Integer.parseInt(stateResponce.getJSONArray("records")
		 .getJSONObject(position).getString("id")));
		 } catch (JSONException e) {
		 // TODO Auto-generated catch block
		 e.printStackTrace();
		 }
		 getAtmDetails();
		 }
		
		 @Override
		 public void onNothingSelected(AdapterView<?> parent) {
		 // TODO Auto-generated method stub
		
		 }
		 });
		 category=new
		 ArrayAdapter<String>(getActivity(),android.R.layout.simple_dropdown_item_1line,categoryArray);
		 category.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
		 categorySpinner.setAdapter(category);
		 categorySpinner.setOnItemSelectedListener(new
		 OnItemSelectedListener() {
		
		 @Override
		 public void onItemSelected(AdapterView<?> parent, View view,
		 int position, long id) {
		 // TODO Auto-generated method stub
		
		 try {
		 alertPayload.put("category",categoryArray[position]);
		 } catch (JSONException e) {
		 // TODO Auto-generated catch block
		 e.printStackTrace();
		 }
		
		 }
		
		 @Override
		 public void onNothingSelected(AdapterView<?> parent) {
		 // TODO Auto-generated method stub
		
		 }
		 });
		 priority=new
		 ArrayAdapter<String>(getActivity(),android.R.layout.simple_dropdown_item_1line,priorityArray);
		 priority.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
		 prioritySpinner.setAdapter(priority);
		 prioritySpinner.setOnItemSelectedListener(new
		 OnItemSelectedListener() {
		
		 @Override
		 public void onItemSelected(AdapterView<?> parent, View view,
		 int position, long id) {
		 // TODO Auto-generated method stub
		 try {
		 alertPayload.put("priority",priorityArray[position]);
		 } catch (JSONException e) {
		 // TODO Auto-generated catch block
		 e.printStackTrace();
		 }
		 }
		
		 @Override
		 public void onNothingSelected(AdapterView<?> parent) {
		 // TODO Auto-generated method stub
		
		 }
		 });
		
		
		 submit.setOnClickListener(new OnClickListener() {
		
		 @Override
		 public void onClick(View v) {
		 summeryValue=summeryText.getText().toString();
		
		 descriptionValue=descriptionText.getText().toString();
		 try {
		 System.out.println(summeryValue);
		 System.out.println(descriptionValue);
		 alertPayload.put("summary",summeryValue);
		 alertPayload.put("description",descriptionValue);
		 PreferencesHelper pref = new PreferencesHelper(
		 CreateAlerts.this.getActivity());
		 alertPayload.put("user", Integer.parseInt(pref
		 .GetPreferences(PreferencesHelper.Uid)));
		 alertPayload.put("status", "assigned");
		
		 } catch (JSONException e) {
		 // TODO Auto-generated catch block
		 e.printStackTrace();
		 }
		 new AsyncTaskCallback(getActivity(),
		 new AsyncTaskCallbackInterface() {
		 @Override
		 public String backGroundCallback() {
		 OpenERP mOpenERP;
		 try {
		
		 mOpenERP = ApplicationClass.getInstance()
		 .getOpenERPCon();
		 ;
		 JSONObject response = mOpenERP
		 .createNew("internal.alerts",
		 alertPayload);
		 return response.toString();
		 }catch(Exception e)
		 {
		 e.printStackTrace();
		 }
		 return null;
		 }
		
		 @Override
		 public void foregroundCallback(String result) {
		 AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
		 getActivity());
		 alertDialogBuilder.setTitle("Success");
		 alertDialogBuilder.setCancelable(false);
		 alertDialogBuilder
		 .setMessage("alert created")
		 .setCancelable(false)
		 .setPositiveButton(
		 "Okay",
		 new DialogInterface.OnClickListener() {
		 public void onClick(
		 DialogInterface dialog,
		 int id) {
		
		 Intent i = new Intent(
		 getActivity(),
		 MainActivity.class);
		 i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		 i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		
		 startActivity(i);
		 getActivity().finish();
		 }
		 });
		
		 AlertDialog alertDialog = alertDialogBuilder
		 .create();
		 alertDialog.show();
		 }
		
		 }).execute();
		
		
		
		 }
		 });
		
		return rootView;
	}

	void getCustomer() {
		new AsyncTaskCallback(getActivity(), new AsyncTaskCallbackInterface() {

			@Override
			public String backGroundCallback() {
				try {

					// Connecting to openERP

					OEDomain domain = new OEDomain();
					OEFieldsHelper fields = new OEFieldsHelper(new String[] {
							"name", "id" });
					OEFieldsHelper statefields = new OEFieldsHelper(
							new String[] { "name", "state_id" });
					OEFieldsHelper reasonfilelds = new OEFieldsHelper(
							new String[] { "reason_id", "name" });
					OEFieldsHelper assigntofields = new OEFieldsHelper(
							new String[] { "user_id", "name" });
					mOpenERP = ApplicationClass.getInstance().getOpenERPCon();
					customerResponce = mOpenERP.search_read("customer.info",
							fields.get(), domain.get());

					OEDomain statedomain = new OEDomain();
					statedomain.add("country_id", "=", "AE");

					stateResponce = mOpenERP.search_read("res.country.state",
							statefields.get(), statedomain.get());
					reasonResponce = mOpenERP.search_read("reason.code",
							reasonfilelds.get(), domain.get());

					OEDomain domain1 = new OEDomain();
					domain1.add("role", "=", "Surveyor");

					assignToResponce = mOpenERP.search_read("res.users",
							assigntofields.get(), domain1.get());

					return customerResponce.toString();
				} catch (ClientProtocolException e) {
					e.printStackTrace();
					return null;
				} catch (JSONException e) {
					Log.d("session expired", "JSON ERROR");
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
					banks.clear();
					for (int i = 0; i < customerResponce
							.getJSONArray("records").length(); i++) {
						banks.add(customerResponce.getJSONArray("records")
								.getJSONObject(i).getString("name"));
					}

				} catch (JSONException e) {
					e.printStackTrace();
				} catch (NullPointerException e) {
					getCustomer();
				}

				customerSpinner.setAdapter(banks);
				try {
					states.clear();
					for (int i = 0; i < stateResponce.getJSONArray("records")
							.length(); i++)
						states.add(stateResponce.getJSONArray("records")
								.getJSONObject(i).getString("name"));

				} catch (JSONException e) {
					e.printStackTrace();
				} catch (NullPointerException e) {
					getCustomer();
				}

				stateSpinner.setAdapter(states);
				try {
					reasonCode.clear();
					for (int i = 0; i < reasonResponce.getJSONArray("records")
							.length(); i++)
						reasonCode.add(reasonResponce.getJSONArray("records")
								.getJSONObject(i).getString("name"));

				} catch (JSONException e) {
					e.printStackTrace();
				} catch (NullPointerException e) {
					getCustomer();
				}

				reasoncodeSpinner.setAdapter(reasonCode);
				try {
					assignTo.clear();
					for (int i = 0; i < assignToResponce
							.getJSONArray("records").length(); i++)
						assignTo.add(assignToResponce.getJSONArray("records")
								.getJSONObject(i).getString("name"));

				} catch (JSONException e) {
					e.printStackTrace();
				} catch (NullPointerException e) {
					getCustomer();
				}

				assignToSpinner.setAdapter(assignTo);

			}
		}).execute();

	}

	void getAtmDetails() {
		new AsyncTaskCallback(getActivity(), new AsyncTaskCallbackInterface() {

			@Override
			public String backGroundCallback() throws JSONException {
				try {

					// Connecting to openERP

					OEDomain domain = new OEDomain();
					domain.add("state_id", "=", stateId);
					OEFieldsHelper fields = new OEFieldsHelper(new String[] {
							"name", "id" });

					mOpenERP = ApplicationClass.getInstance().getOpenERPCon();
					atmResponce = mOpenERP.search_read("atm.info",
							fields.get(), domain.get());

					return atmResponce.toString();
				} catch (ClientProtocolException e) {
					e.printStackTrace();
					return null;
				}  catch (IOException e) {
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
					atmDetails.clear();
					for (int i = 0; i < atmResponce.getJSONArray("records")
							.length(); i++)
						atmDetails.add(atmResponce.getJSONArray("records")
								.getJSONObject(i).getString("name"));

				} catch (JSONException e) {
					e.printStackTrace();
				} catch (NullPointerException e) {
					getAtmDetails();
				}

				atmSpinner.setAdapter(atmDetails);
			}
		}).execute();
	}

}