package anipr.transtech.android;

import org.json.JSONException;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.IntentCompat;
import android.util.Log;
import android.widget.Toast;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class AsyncTaskCallback extends AsyncTask<Void, Integer, String> {
	public AsyncTaskCallbackInterface mAsyncTaskCallbackInterface;
	private String tag = getClass().getSimpleName();
	private Context context;
	private SweetAlertDialog pDialog;

	public AsyncTaskCallback(Context context,
			AsyncTaskCallbackInterface mAsyncTaskCallbackInterface) {
		this.context = context;
		this.mAsyncTaskCallbackInterface = mAsyncTaskCallbackInterface;
	}

	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		pDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
		pDialog.getProgressHelper().setBarColor(
				context.getResources().getColor(R.color.primary_color));
		pDialog.setTitleText("Please Wait...");
		pDialog.setCancelable(false);
		pDialog.show();
	}

	@Override
	protected String doInBackground(Void... params) {
		try {

		} catch (Exception e) {
			if (pDialog.isShowing()) {
				pDialog.dismiss();
			}
			e.printStackTrace();
		}
		try {
			return mAsyncTaskCallbackInterface.backGroundCallback();
		} catch (JSONException e) {
			Intent logoutIntent = new Intent(context, LoginActivity.class);
			ComponentName cn = logoutIntent.getComponent();
			Intent intent = IntentCompat.makeRestartActivityTask(cn);
			context.startActivity(intent);
			Toast.makeText(context, "Logged Out", Toast.LENGTH_LONG).show();
			e.printStackTrace();
			return null;
		}
	}

	@Override
	protected void onPostExecute(String result) {
		if (pDialog.isShowing()) {
			pDialog.dismiss();
		}
		if (result != null) {
			mAsyncTaskCallbackInterface.foregroundCallback(result);
		} else {
			Log.d(tag, "foregroundCallback result" + result);

		}
		super.onPostExecute(result);
	}

	public interface AsyncTaskCallbackInterface {
		public void foregroundCallback(String result);

		public String backGroundCallback() throws JSONException;
	}

}
