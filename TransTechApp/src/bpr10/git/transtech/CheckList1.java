package bpr10.git.transtech;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class CheckList1 extends Fragment{
	CheckBox checkbox1,checkbox2,checkbox3,checkbox4,checkbox5,checkbox6,checkbox7,checkbox8,checkbox9;
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.check_list1, container, false);
		checkbox1=(CheckBox) rootView.findViewById(R.id.no_comments);
		checkbox1.setOnCheckedChangeListener(new CustomCheckBoxClickListener("key"));
		return rootView;
	}
	
}
