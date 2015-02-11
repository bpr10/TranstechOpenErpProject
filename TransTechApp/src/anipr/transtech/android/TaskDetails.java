package anipr.transtech.android;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class TaskDetails extends BaseActivity {
	TextView customer, ATMDtails, locationText, distance, dueDate, textViewDis;
	Button surveyNow;
	private String tag = getClass().getSimpleName();
	JSONObject taskObj = new JSONObject();
	String atm1, atm2;
	String customerText, country, distanceVal;
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
		textViewDis = (TextView) findViewById(R.id.textView_distance);
		dateUtility = new DateUtility();
		distance.setVisibility(View.VISIBLE);
		try {
			taskObj = new JSONObject(getIntent().getStringExtra("taskDetais"));
			Log.d(tag, taskObj.toString());
		} catch (JSONException e1) {
			e1.printStackTrace();
		}

		try {
			customerText = taskObj.getJSONArray("customer").getString(1);
			String[] atmarr = taskObj.getJSONArray("atm").getString(1)
					.split("%%");
			if (atmarr.length > 0) {
				atm1 = atmarr[0];
				

				ATMDtails.setText(atm1);
			}
			country = taskObj.getJSONArray("country").getString(1);

			dueDate.setText(dateUtility.getFriendlyDateString(dateUtility
					.makeDate(taskObj.getString("visit_time"))));
			distanceVal = taskObj.get("distance").toString();
			if (distanceVal != null) {
				distance.setText(distanceVal);
			} else {
				textViewDis.setVisibility(View.GONE);
				distance.setVisibility(View.GONE);
			}
		} catch (JSONException e1) {
			textViewDis.setVisibility(View.GONE);
			distance.setVisibility(View.GONE);
			e1.printStackTrace();
		}
		customer.setText(customerText);

		locationText.setText(country);

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

}
