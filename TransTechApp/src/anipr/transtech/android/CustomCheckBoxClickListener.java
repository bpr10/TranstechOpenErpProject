package anipr.transtech.android;

import org.json.JSONException;

import android.util.Log;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class CustomCheckBoxClickListener implements OnCheckedChangeListener {

	String key;

	public CustomCheckBoxClickListener(String key) {
		this.key = key;
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

		try {
			TaskForm.taskPayload.put(key, isChecked);
			Log.d("taskPayload", TaskForm.taskPayload.toString());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
