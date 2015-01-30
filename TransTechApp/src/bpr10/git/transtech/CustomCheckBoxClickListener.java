package bpr10.git.transtech;

import org.json.JSONException;

import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

public  class CustomCheckBoxClickListener implements OnCheckedChangeListener{
	
	String key ;
	public CustomCheckBoxClickListener(String key ) {
		this.key = key ;
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		try {
			ServiceingForm.taskPayload.put(key, isChecked);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}