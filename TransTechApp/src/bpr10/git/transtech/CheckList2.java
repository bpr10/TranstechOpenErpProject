package bpr10.git.transtech;

import org.json.JSONException;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Spinner;

public class CheckList2 extends Fragment {


	
	private Spinner remarkCategorySpinner;
	private TaskForm mtaskForm;
	CheckBox keyPadDisplaced,spotLightsOff,decals,ATMTowerBranding,canopyBranding,surroundLockIsDamage,
	spotLightIsOff,mainBoardLightsAreOff,securityCameraIsOutOfFocus;
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.checklist2, container, false);
		keyPadDisplaced = (CheckBox) rootView
				.findViewById(R.id.keyp_Pad_displaced);
		spotLightsOff = (CheckBox) rootView.findViewById(R.id.spot_lights_off);
		decals = (CheckBox) rootView.findViewById(R.id.decals);
		ATMTowerBranding = (CheckBox) rootView
				.findViewById(R.id.atm_tower_branding);
		canopyBranding = (CheckBox) rootView.findViewById(R.id.canopy_branding);
		surroundLockIsDamage = (CheckBox) rootView
				.findViewById(R.id.surround_lock);
		spotLightIsOff = (CheckBox) rootView
				.findViewById(R.id.spot_light_is_off);
		mainBoardLightsAreOff = (CheckBox) rootView
				.findViewById(R.id.main_board_lights);
		securityCameraIsOutOfFocus = (CheckBox) rootView
				.findViewById(R.id.security_camera);
		keyPadDisplaced
				.setOnCheckedChangeListener(new CustomCheckBoxClickListener(
						"check_list16"));
		spotLightsOff
				.setOnCheckedChangeListener(new CustomCheckBoxClickListener(
						"check_list17"));
		decals.setOnCheckedChangeListener(new CustomCheckBoxClickListener(
				"check_list18"));
		ATMTowerBranding
				.setOnCheckedChangeListener(new CustomCheckBoxClickListener(
						"check_list19"));
		canopyBranding
				.setOnCheckedChangeListener(new CustomCheckBoxClickListener(
						"check_list20"));
		surroundLockIsDamage
				.setOnCheckedChangeListener(new CustomCheckBoxClickListener(
						"check_list21"));
		spotLightIsOff
				.setOnCheckedChangeListener(new CustomCheckBoxClickListener(
						"check_list22"));
		mainBoardLightsAreOff
				.setOnCheckedChangeListener(new CustomCheckBoxClickListener(
						"check_list23"));
		securityCameraIsOutOfFocus
				.setOnCheckedChangeListener(new CustomCheckBoxClickListener(
						"check_list24"));
		Log.d("task flag", TaskForm.taskFlag + "");
		if (TaskForm.taskFlag == 1) {
			keyPadDisplaced.setVisibility(View.INVISIBLE);
			spotLightsOff.setVisibility(View.INVISIBLE);
			decals.setVisibility(View.INVISIBLE);
			ATMTowerBranding.setVisibility(View.INVISIBLE);
			canopyBranding.setVisibility(View.INVISIBLE);
			surroundLockIsDamage.setVisibility(View.INVISIBLE);
			spotLightIsOff.setVisibility(View.INVISIBLE);
			mainBoardLightsAreOff.setVisibility(View.INVISIBLE);
			securityCameraIsOutOfFocus.setVisibility(View.INVISIBLE);
			keyPadDisplaced.setChecked(false);
			spotLightsOff.setChecked(false);
			decals.setChecked(false);
			ATMTowerBranding.setChecked(false);
			canopyBranding.setChecked(false);
			surroundLockIsDamage.setChecked(false);
			spotLightIsOff.setChecked(false);
			mainBoardLightsAreOff.setChecked(false);
			securityCameraIsOutOfFocus.setChecked(false);
		} else {
			keyPadDisplaced.setVisibility(View.VISIBLE);
			spotLightsOff.setVisibility(View.VISIBLE);
			decals.setVisibility(View.VISIBLE);
			ATMTowerBranding.setVisibility(View.VISIBLE);
			canopyBranding.setVisibility(View.VISIBLE);
			surroundLockIsDamage.setVisibility(View.VISIBLE);
			spotLightIsOff.setVisibility(View.VISIBLE);
			mainBoardLightsAreOff.setVisibility(View.VISIBLE);
			securityCameraIsOutOfFocus.setVisibility(View.VISIBLE);

		}
		remarkCategorySpinner = (Spinner) rootView
				.findViewById(R.id.remark_category);
		populateCatrgories();
		return rootView;
	}

	void populateCatrgories() {
		ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(
				getActivity(), android.R.layout.simple_spinner_item);
		try {
			for (int i = 0; i < TaskForm.remarksResponse
					.getJSONArray("records").length(); i++)
				adapter.add(TaskForm.remarksResponse.getJSONArray("records")
						.getJSONObject(i).getString("name"));
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (NullPointerException e) {
			mtaskForm.getRemarkCatrgories();
		}
		remarkCategorySpinner.setAdapter(adapter);
	}

}
