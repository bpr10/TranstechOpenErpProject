package bpr10.git.transtech;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
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
		pDialog.getProgressHelper().setBarColor(context.getResources().getColor(R.color.primary_color));
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
		return mAsyncTaskCallbackInterface.backGroundCallback();
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

		public String backGroundCallback();
	}

}
