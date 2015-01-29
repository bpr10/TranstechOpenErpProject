package bpr10.git.transtech;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

public class BeforeImages extends Fragment {
	
	ImageView beforeService1,beforeService2,beforeService3;
	Button cemara1,cemara2,cemara3;
	Bundle bundle;
	
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.beforeservice, container, false);
		beforeService1=(ImageView) rootView.findViewById(R.id.before_service1);
		beforeService2=(ImageView) rootView.findViewById(R.id.before_service2);
		beforeService3=(ImageView) rootView.findViewById(R.id.before_service3);
		cemara1=(Button) rootView.findViewById(R.id.camara1);
		cemara2=(Button) rootView.findViewById(R.id.camara2);
		cemara3=(Button) rootView.findViewById(R.id.camara3);
		
		
		
		
		return rootView;
	}

}
