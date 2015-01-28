package bpr10.git.transtech;

import java.io.IOException;

import openerp.OEVersionException;
import openerp.OpenERP;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class LoginActivity extends Activity {
	EditText username, password;
	Button signIn;
	String userName, userPassword;
	Integer id;
	OpenERP mOpenERP;
	String uId;
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

				userName = username.getText().toString();
				userPassword = password.getText().toString();
				
				if (userName.isEmpty()) {
					Toast.makeText(getApplicationContext(),
							"usename can't be empty", 1000).show();
				} else if (userPassword.isEmpty()) {
					Toast.makeText(getApplicationContext(),
							"password can't be empty", 1000).show();
				} else {
					
					
					new AsyncTask<String, Void, String>() {

						private String tag = "AsyncTask Class";

						@SuppressWarnings("static-access")
						@Override
						protected String doInBackground(String... params) {
												
							try {
								mOpenERP = new OpenERP("http://162.243.21.15:8069");
								JSONObject response = mOpenERP.authenticate(userName, userPassword,
										"Test");
								String loginres=response.toString();
								
								Log.d("responce",loginres);
								 uId=response.getString("uid");
								
								
							} catch (ClientProtocolException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (OEVersionException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							
							
							return null;

						}

						protected void onPostExecute(String result) {
							
							if(uId!=null)
							{
								Intent i=new Intent(LoginActivity.this,MainActivity.class);
								startActivity(i);
								Toast.makeText(getApplicationContext(), "sucess",1000).show();
							}else
							{
								Toast.makeText(getApplicationContext(), "enter valid credentials",1000).show();
							}

						};
					}.execute();
				}
			}
		});
	}
	
	}
	



