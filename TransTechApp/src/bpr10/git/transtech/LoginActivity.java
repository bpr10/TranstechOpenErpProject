package bpr10.git.transtech;

import java.io.IOException;

import openerp.OEVersionException;
import openerp.OpenERP;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import bpr10.git.transtech.AsyncTaskCallback.AsyncTaskCallbackInterface;

public class LoginActivity extends ActionBarActivity {
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
		getSupportActionBar().hide();
		signIn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Login();
			}
		});
		password.setOnKeyListener(onSoftKeyboardDonePress);
		
	}
	void Login()
	{
		userName = username.getText().toString();
		userPassword = password.getText().toString();

		if (userName.isEmpty()) {
			Toast.makeText(getApplicationContext(),
					"usename can't be empty", Toast.LENGTH_LONG).show();
		} else if (userPassword.isEmpty()) {
			Toast.makeText(getApplicationContext(),
					"password can't be empty", Toast.LENGTH_LONG)
					.show();
		} else {

			new AsyncTaskCallback(LoginActivity.this,
					new AsyncTaskCallbackInterface() {

						@Override
						public String backGroundCallback() {
							try {

								// Connecting to openERP

								mOpenERP = ApplicationClass
										.getInstance().getOpenERPCon();
								JSONObject response = mOpenERP
										.authenticate(userName,
												userPassword,
												"Transtech");
								String loginres = response.toString();

								Log.d("Got Login Response ", loginres);

								// Storing UID in SharedPrefrences

								uId = response.getString("uid");
								if (!uId.equals("false")) {
									PreferencesHelper pref = new PreferencesHelper(
											getApplicationContext());
									pref.SavePreferences(
											PreferencesHelper.Uid, uId);
									Log.d(tag,
											pref.GetPreferences(PreferencesHelper.Uid));
								}
								return uId;
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
							if (result != null) {
								Log.d(tag, result);

								if (!uId.equals("false")) {
									ApplicationClass.surveyor_Id = Integer
											.parseInt(uId);
									Intent i = new Intent(
											LoginActivity.this,
											MainActivity.class);
									startActivity(i);
									Toast.makeText(
											getApplicationContext(),
											"success", Toast.LENGTH_LONG)
											.show();
								} else {
									Toast.makeText(
											getApplicationContext(),
											"enter valid credentials",
											Toast.LENGTH_LONG).show();
								}
							}
						}
					}).execute();
    }
	}
	
	View.OnKeyListener onSoftKeyboardDonePress=new View.OnKeyListener()
    {
        public boolean onKey(View v, int keyCode, KeyEvent event)
        {
            if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)
            {
            	Login();
            return false;
        }
			return false;
    };
};
}
