package bpr10.git.transtech;

import java.io.File;
import java.io.IOException;

import openerp.OEVersionException;
import openerp.OpenERP;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import bpr10.git.transtech.AsyncTaskCallback.AsyncTaskCallbackInterface;

public class AfterImages extends Fragment {
	private ImageView afterImage1, afterImage2, afterImage3;
	private Button camera1, camera2, camera3;
	private String tag = getClass().getSimpleName();
	private TaskForm mtaskForm;
	private Context mContext;
	static final int REQUEST_IMAGE_CAPTURE = 11;
	private Button submitButton;
	private Spinner remarkCategorySpinner;
	private String currentImage = "default";

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.afterservice, container,
				false);
		mtaskForm = new TaskForm();
		mContext = getActivity().getApplicationContext();
		afterImage1 = (ImageView) rootView.findViewById(R.id.after_service1);
		afterImage2 = (ImageView) rootView.findViewById(R.id.after_service2);
		afterImage3 = (ImageView) rootView.findViewById(R.id.after_service3);
		camera1 = (Button) rootView
				.findViewById(R.id.after_service_camera_button1);
		camera2 = (Button) rootView
				.findViewById(R.id.after_service_camera_button2);
		camera3 = (Button) rootView
				.findViewById(R.id.after_service_camera_button3);
		submitButton = (Button) rootView.findViewById(R.id.submit_button);
		remarkCategorySpinner = (Spinner) rootView
				.findViewById(R.id.remark_category);
		populateCatrgories();
		remarkCategorySpinner
				.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> parent,
							View view, int position, long id) {
						try {
							TaskForm.taskPayload.put(
									"remarks_survey",
									TaskForm.remarksResponse
											.getJSONArray("records")
											.getJSONObject(position)
											.getString("description"));
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}

					@Override
					public void onNothingSelected(AdapterView<?> parent) {

					}
				});
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

	@Override
	public void onResume() {

		mtaskForm.checkforImage(getActivity(), camera1, "after_img_front",
				afterImage1);
		mtaskForm.checkforImage(getActivity(), camera2, "after_img_back",
				afterImage2);
		mtaskForm.checkforImage(getActivity(), camera3, "after_img_side",
				afterImage3);
		camera1.setOnClickListener(new CameraClickListener("after_img_front"));
		camera2.setOnClickListener(new CameraClickListener("after_img_back"));
		camera3.setOnClickListener(new CameraClickListener("after_img_side"));
		submitButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				new AsyncTaskCallback(getActivity(),
						new AsyncTaskCallbackInterface() {
							@Override
							public String backGroundCallback() {
								OpenERP mOpenERP;
								try {
									mOpenERP = ApplicationClass.getInstance()
											.getOpenERPCon();
									JSONObject response = mOpenERP
											.createNew("survey.info",
													TaskForm.taskPayload);
									try {
										if (response.get("result") != null) {
											boolean updateResponse = mOpenERP
													.updateValues(
															"atm.surverys.management",
															new JSONObject()
																	.put("status",
																			"waitnig_approve"),
															TaskForm.taskId);
											Log.d(tag, "updateResponse "
													+ updateResponse);
											return response.toString();
										}
										Log.d(tag, response.toString());

									} catch (Exception e) {

									}

								} catch (ClientProtocolException e) {
									e.printStackTrace();
								} catch (JSONException e) {
									e.printStackTrace();
								} catch (IOException e) {
									e.printStackTrace();
								} catch (OEVersionException e) {
									e.printStackTrace();
								}
								return null;
							}

							@Override
							public void foregroundCallback(String result) {
								Log.d(tag, result);
								AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
										getActivity());
								alertDialogBuilder.setTitle("Success");
								alertDialogBuilder.setCancelable(false);
								alertDialogBuilder
										.setMessage("Survey Request Recorded")
										.setCancelable(false)
										.setPositiveButton(
												"Okay",
												new DialogInterface.OnClickListener() {
													public void onClick(
															DialogInterface dialog,
															int id) {

														Intent i = new Intent(
																getActivity(),
																MainActivity.class);
														i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
														i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

														startActivity(i);
														getActivity().finish();
													}
												});

								AlertDialog alertDialog = alertDialogBuilder
										.create();
								alertDialog.show();
							}

						}).execute();

			}
		});
		super.onResume();
	}

	protected void dispatchTakePictureIntent(int fileName) {
		// Ensuring that there's a camera activity to handle the intent
		PackageManager packageManager = getActivity().getPackageManager();
		if (packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA) == false) {
			Log.e(tag, "This device does not have a camera.");
			Toast.makeText(getActivity(),
					"This device does not have a camera.", Toast.LENGTH_LONG)
					.show();
			return;
		}
		Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

		// Creating the File where the photo should go
		File photoFile = null;
		try {
			photoFile = mtaskForm.createImageFile(fileName);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		// Continue only if the File was successfully created
		if (photoFile != null) {
			takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
					Uri.fromFile(photoFile));
			int requestCode = Integer.parseInt(String
					.valueOf(REQUEST_IMAGE_CAPTURE) + String.valueOf(fileName));
			Log.d(tag, "requestCode " + requestCode);
			startActivityForResult(takePictureIntent, requestCode);
		}

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.d(tag, "onActivityResult requestCode: " + requestCode
				+ " resultCode : " + resultCode);
		int req = Integer.parseInt(String.valueOf(requestCode).substring(0, 2));
		Log.d(tag, req + "");
		if (req == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
			// SAVE photo TO Local directory
			try {
				String imageName = String.valueOf(requestCode).substring(2);
				mtaskForm.saveImageTOoLocalStorage(mContext, imageName,
						Uri.parse(TaskForm.mCurrentPhotoPath));
				mtaskForm.updateImagePath(imageName);

			} catch (Exception e) {
				Log.d(tag, "Please Try Again");
				e.printStackTrace();
			}
		} else {
			currentImage = "default";
		}
	}

	class CameraClickListener implements OnClickListener {
		String key;

		public CameraClickListener(String key) {
			this.key = key;
		}

		@Override
		public void onClick(View v) {
			currentImage = key;
			dispatchTakePictureIntent(v.getId() % TaskForm.ID_DIVIDER);
		}

	}

	void clearTaskPayload() {
		if (!currentImage.equals("default")) {
			if (TaskForm.taskPayload.has(currentImage))
				TaskForm.taskPayload.remove(currentImage);

		}
	}

}
