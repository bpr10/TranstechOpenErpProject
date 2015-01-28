package bpr10.git.transtech;



import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ListView;

public class TasksFragment extends Fragment {

	ListView taskList;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.tasks, container, false);
		taskList=(ListView) rootView.findViewById(R.id.task_list);
//		taskList.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				Fragment fragment=new TaskDetails();
//				FragmentManager fragmentmanager=getActivity().getSupportFragmentManager();
//				FragmentTransaction fragmentransation=fragmentmanager.beginTransaction();
//				fragmentransation.replace(R.id.content_frame, fragment);
//				fragmentransation.addToBackStack(null);
//				fragmentransation.commit();
//				
//				
//			}
//		});
//				

		return rootView;
	}

}
