package bpr10.git.transtech;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

public class AfterImages extends Fragment{
	ImageView afterService1,afterService2,afterService3;
	Button cemara1,cemara2,cemara3;
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.afterservice, container, false);
		
		afterService1=(ImageView) rootView.findViewById(R.id.after_service1);
		afterService2=(ImageView) rootView.findViewById(R.id.after_service2);
		afterService3=(ImageView) rootView.findViewById(R.id.after_service3);
		cemara1=(Button) rootView.findViewById(R.id.camara1);
		cemara2=(Button) rootView.findViewById(R.id.camara2);
		cemara3=(Button) rootView.findViewById(R.id.camara3);
		return rootView;
	}

}
