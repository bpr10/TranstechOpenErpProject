package bpr10.git.transtech;

import android.os.AsyncTask;

public class AsyncTaskCallback extends AsyncTask<Void, Integer, String> {
	public AsyncTaskCallbackInterface mAsyncTaskCallbackInterface;
	
	public AsyncTaskCallback(
			AsyncTaskCallbackInterface mAsyncTaskCallbackInterface) {
		this.mAsyncTaskCallbackInterface = mAsyncTaskCallbackInterface;
	}

	@Override
	protected String doInBackground(Void... params) {

		return mAsyncTaskCallbackInterface.backGroundCallback();
	}

	@Override
	protected void onPostExecute(String result) {
		mAsyncTaskCallbackInterface.foregroundCallback(result);
		super.onPostExecute(result);
	}
	public interface AsyncTaskCallbackInterface {
		public void foregroundCallback(String result);
		public String backGroundCallback();
	}

}
