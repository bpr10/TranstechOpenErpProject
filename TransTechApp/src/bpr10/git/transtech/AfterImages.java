package bpr10.git.transtech;

import java.io.File;
import java.io.FileNotFoundException;
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
import android.graphics.Bitmap;
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
import bpr10.git.transtech.AsyncTaskCallback.AsyncTaskCallbackInterface;

import com.squareup.picasso.Picasso;

public class AfterImages extends Fragment {
	private ImageView afterImage1, afterImage2, afterImage3;
	private Button camera1, camera2, camera3;
	private Uri mCapturedImageURI;
	private String tag = getClass().getSimpleName();
	private TaskForm mtaskForm;
	private Context mContext;
	static final int REQUEST_IMAGE_CAPTURE = 11;
	private Button submitButton;
	private Spinner remarkCategorySpinner;

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
		try {

			try {
				File image1 = mtaskForm.getImageFromLocalStorage(mContext,
						camera1.getId() % TaskForm.ID_DIVIDER);
				TaskForm.taskPayload.putOpt("after_img_front",
						mtaskForm.encodeImage(image1));
				Picasso.with(getActivity().getApplicationContext())
						.load(image1).skipMemoryCache().into(afterImage1);
			} catch (FileNotFoundException e) {
				Log.d(tag, "1st image not taken ");
			} catch (JSONException e) {
				e.printStackTrace();
			}
			try {
				File image2 = mtaskForm.getImageFromLocalStorage(mContext,
						camera2.getId() % TaskForm.ID_DIVIDER);
				TaskForm.taskPayload.putOpt("after_img_back",
						mtaskForm.encodeImage(image2));
				Picasso.with(getActivity().getApplicationContext())
						.load(image2).skipMemoryCache().into(afterImage2);
			} catch (FileNotFoundException e) {
				Log.d(tag, "2nd image not taken ");
			} catch (JSONException e) {
				e.printStackTrace();
			}
			try {
				File image3 = mtaskForm.getImageFromLocalStorage(mContext,
						camera3.getId() % TaskForm.ID_DIVIDER);
				TaskForm.taskPayload.putOpt("after_img_side",
						mtaskForm.encodeImage(image3));
				Picasso.with(getActivity().getApplicationContext())
						.load(image3).skipMemoryCache().into(afterImage3);
			} catch (FileNotFoundException e) {
				Log.d(tag, "3rd image not taken ");
			} catch (JSONException e) {
				e.printStackTrace();
			}

		} catch (NullPointerException e) {
			Log.e(tag, e.toString() + "");
		}
		camera1.setOnClickListener(cameraClickListener);
		camera2.setOnClickListener(cameraClickListener);
		camera3.setOnClickListener(cameraClickListener);
		submitButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Log.d(tag, "Button CLicked");

				new AsyncTaskCallback(getActivity(),
						new AsyncTaskCallbackInterface() {
							@Override
							public String backGroundCallback() {
								OpenERP mOpenERP;
								try {
									mOpenERP = ApplicationClass.getInstance()
											.getOpenERPCon();
									Log.d(tag, "TaskForm.taskPayload "
											+ TaskForm.taskPayload.toString());
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

	private void dispatchTakePictureIntent(int filename) {
		// Checking if there is a camera.
		Context context = getActivity();
		PackageManager packageManager = context.getPackageManager();
		if (packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA) == false) {
			Log.e(tag, "This device does not have a camera.");
			return;
		}
		Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

		takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
				getCapturedImageURI());
		startActivityForResult(
				takePictureIntent,
				Integer.parseInt(String.valueOf(REQUEST_IMAGE_CAPTURE)
						+ String.valueOf(filename)));

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.d(tag, "onActivityResult requestCode: " + requestCode
				+ " resultCode : " + resultCode);
		int req = Integer.parseInt(String.valueOf(requestCode).substring(0, 2));
		Log.d(tag, req + "");
		if (req == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
			// SAVE photo TO Local directory

			Bundle extras = data.getExtras();
			Bitmap imageBitmap = (Bitmap) extras.get("data");
			try {
				mtaskForm.saveImageTOoLocalStorage(mContext,
						String.valueOf(requestCode).substring(2), imageBitmap);
			} catch (IOException e) {
				Log.d(tag, "Please Try Again");
				e.printStackTrace();
			}

		}
		this.onResume();
	}

	public Uri getCapturedImageURI() {
		return mCapturedImageURI;
	}

	OnClickListener cameraClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			dispatchTakePictureIntent(v.getId() % TaskForm.ID_DIVIDER);
		}
	};

	public void setCapturedImageURI(Uri mCapturedImageURI) {
		this.mCapturedImageURI = mCapturedImageURI;
	}

}
