package anipr.transtech.android;

import java.io.IOException;

import openerp.OEVersionException;
import openerp.OpenERP;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import anipr.transtech.android.AsyncTaskCallback.AsyncTaskCallbackInterface;

public class LoginActivity extends Activity {
	private EditText username, password;
	private Button signIn;
	private String userName, userPassword;
	private OpenERP mOpenERP;
	private String uId = "";
	private String tag = getClass().getSimpleName();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		username = (EditText) findViewById(R.id.user_name);
		password = (EditText) findViewById(R.id.password);
		signIn = (Button) findViewById(R.id.sign_in);

		signIn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Login();

			}
		});
		
	}

	void Login() {
		userName = username.getText().toString();
		userPassword = password.getText().toString();

		if (userName.isEmpty()) {
			Toast.makeText(getApplicationContext(), "usename can't be empty",
					Toast.LENGTH_LONG).show();
		} else if (userPassword.isEmpty()) {
			Toast.makeText(getApplicationContext(), "password can't be empty",
					Toast.LENGTH_LONG).show();
		} else {

			new AsyncTaskCallback(LoginActivity.this,
					new AsyncTaskCallbackInterface() {

						@Override
						public String backGroundCallback() throws JSONException {
							try {

								// Connecting to openERP

								mOpenERP = ApplicationClass.getInstance()
										.getOpenERPCon();
								JSONObject response = mOpenERP.authenticate(
										userName, userPassword, "Test");
								String loginres = response.toString();

								Log.d("Got Login Response ", loginres);

								// Storing UID in SharedPrefrences

								uId = response.getString("uid");
								if (!uId.equals("false")) {
									PreferencesHelper pref = new PreferencesHelper(
											getApplicationContext());
									pref.SavePreferences(PreferencesHelper.Uid,
											uId);
									Log.d(tag,
											pref.GetPreferences(PreferencesHelper.Uid));
								}
								return uId;
							} catch (ClientProtocolException e) {
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
							if (result != null) {
								Log.d(tag, result);

								if (!uId.equals("false")) {
									ApplicationClass.surveyor_Id = Integer
											.parseInt(uId);
									Intent i = new Intent(LoginActivity.this,
											MainActivity.class);
									startActivity(i);
									finish();
								} else {
									Toast.makeText(getApplicationContext(),
											"Please try again.",
											Toast.LENGTH_LONG).show();
								}
							}
						}
					}).execute();
		}
	}

	
}
