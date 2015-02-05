package bpr10.git.transtech;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class AsyncTaskCallback extends AsyncTask<Void, Integer, String> {
	public AsyncTaskCallbackInterface mAsyncTaskCallbackInterface;
	private String tag = getClass().getSimpleName();
	private Context context;
	ProgressDialog pd;

	public AsyncTaskCallback(Context context,
			AsyncTaskCallbackInterface mAsyncTaskCallbackInterface) {
		this.context = context;
		this.mAsyncTaskCallbackInterface = mAsyncTaskCallbackInterface;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		pd = new ProgressDialog(context);
		pd.setMessage("Please Wait..");
		pd.setCancelable(true);
		pd.setMessage("Please Wait");
		pd.show();
	}

	@Override
	protected String doInBackground(Void... params) {
		try {

		} catch (Exception e) {
			if (pd.isShowing()) {
				pd.hide();
			}
			e.printStackTrace();
		}
		return mAsyncTaskCallbackInterface.backGroundCallback();
	}

	@Override
	protected void onPostExecute(String result) {
		if (pd.isShowing()) {
			pd.dismiss();
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
