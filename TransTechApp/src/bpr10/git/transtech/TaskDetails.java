package bpr10.git.transtech;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class TaskDetails extends BaseActivity{
	TextView customer, ATMDtails, locationText, distance, dueDate;
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
		dateUtility = new DateUtility();

		try {
			taskObj = new JSONObject(getIntent().getStringExtra("taskDetais"));
			Log.d(tag, taskObj.toString());
		} catch (JSONException e1) {
			e1.printStackTrace();
		}

		try {
			customerText = taskObj.getJSONArray("customer").getString(1);
			String[] atmarr = taskObj.getJSONArray("atm").getString(1)
					.split(",");
			atm1 = atmarr[0];
			atm2 = atmarr[1];
			country = taskObj.getJSONArray("country").getString(1);
			distanceVal = taskObj.get("distance").toString();
			dueDate.setText(dateUtility.getFriendlyDateString(dateUtility
					.makeDate(taskObj
							.getString("visit_time"))));

		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		customer.setText(customerText);
		ATMDtails.setText(atm1 + atm2);
		locationText.setText(country);
		distance.setText(distanceVal);

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
