package bpr10.git.transtech;

import java.io.File;
import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class BeforeImages extends Fragment {

	protected ImageView firstImage, secondImage, thirdImage;
	protected Button camera1, camera2, camera3;
	protected Bundle bundle;
	protected CameraManager cameraManager;
	static final int REQUEST_IMAGE_CAPTURE = 11;
	private String tag = getClass().getSimpleName();
	private TaskForm mtaskForm;
	private Context mContext;
	private String currentImage;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.beforeservice, container,
				false);
		Log.d(tag, "Oncreate");
		mContext = getActivity();
		mtaskForm = new TaskForm();
		firstImage = (ImageView) rootView.findViewById(R.id.before_service1);
		secondImage = (ImageView) rootView.findViewById(R.id.before_service2);
		thirdImage = (ImageView) rootView.findViewById(R.id.before_service3);

		camera1 = (Button) rootView
				.findViewById(R.id.before_service_camera_button1);
		camera2 = (Button) rootView
				.findViewById(R.id.before_service_camera_button2);
		camera3 = (Button) rootView
				.findViewById(R.id.before_service_camera_button3);
		Log.d(tag,
				"ids : camera1" + camera1.getId() + "camera2" + camera2.getId()
						+ "camera3" + camera3.getId());
		return rootView;
	}

	public void onResume() {

		mtaskForm.checkforImage(getActivity(), camera1, "bfr_img_front",
				firstImage);
		mtaskForm.checkforImage(getActivity(), camera2, "bfr_img_back",
				secondImage);
		mtaskForm.checkforImage(getActivity(), camera3, "bfr_img_side",
				thirdImage);

		camera1.setOnClickListener(new CameraClickListener("bfr_img_front"));
		camera2.setOnClickListener(new CameraClickListener("bfr_img_back"));
		camera3.setOnClickListener(new CameraClickListener("bfr_img_side"));
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
			takePictureIntent.putExtra(MediaStore.EXTRA_SCREEN_ORIENTATION, ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
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
				Log.e(tag, "Please Try Again");
				e.printStackTrace();
			}
		} else {
			currentImage = "default";
		}
	}

	void clearTaskPayload() {
		if (!currentImage.equals("default")) {
			if (TaskForm.taskPayload.has(currentImage))
				TaskForm.taskPayload.remove(currentImage);

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

}
