package bpr10.git.transtech;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;


public class CreateAlerts extends Fragment {

	Spinner customerSpinner,stateSpinner,atmSpinner,catugorySpinner,prioritySpinner,responcecodeSpinner;
	EditText summerText,descriptionText;
	Button submit;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.create_alert, container, false);
		customerSpinner=(Spinner) rootView.findViewById(R.id.customer_spinner);
		stateSpinner=(Spinner) rootView.findViewById(R.id.state_spinner);
		atmSpinner=(Spinner) rootView.findViewById(R.id.atm_spinner);
		catugorySpinner=(Spinner) rootView.findViewById(R.id.category_spinner);
		prioritySpinner=(Spinner) rootView.findViewById(R.id.priority_spinner);
		responcecodeSpinner=(Spinner) rootView.findViewById(R.id.reasonscode_pinner);
		summerText=(EditText) rootView.findViewById(R.id.summery_text);
		descriptionText=(EditText) rootView.findViewById(R.id.description_text);
		submit=(Button) rootView.findViewById(R.id.alert_submit);
		
		return rootView;
	}

}
