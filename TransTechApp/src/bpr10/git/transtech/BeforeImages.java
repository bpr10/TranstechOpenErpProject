package bpr10.git.transtech;

import java.io.FileNotFoundException;
import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
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

import com.squareup.picasso.Picasso;

public class BeforeImages extends Fragment {

	protected ImageView firstImage, secondImage, thirdImage;
	protected Button camera1, camera2, camera3;
	protected Bundle bundle;
	protected CameraManager cameraManager;
	static final int REQUEST_IMAGE_CAPTURE = 11;
	private String tag = getClass().getSimpleName();
	private Uri mCapturedImageURI;
	private TaskForm mtaskForm;
	private Context mContext;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.beforeservice, container,
				false);
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
		try {
			try {
				Picasso.with(getActivity().getApplicationContext())
						.load(mtaskForm.getImageFromLocalStorage(mContext,
								camera1.getId() % TaskForm.ID_DIVIDER))
						.skipMemoryCache().into(firstImage);
			} catch (FileNotFoundException e) {
				Log.d(tag, "1st image not taken ");
			}
			try {
				Picasso.with(getActivity().getApplicationContext())
						.load(mtaskForm.getImageFromLocalStorage(mContext,
								camera2.getId() % TaskForm.ID_DIVIDER))
						.skipMemoryCache().into(secondImage);
				;
			} catch (FileNotFoundException e) {
				Log.d(tag, "2nd image not taken ");
			}
			try {
				Picasso.with(getActivity().getApplicationContext())
						.load(mtaskForm.getImageFromLocalStorage(mContext,
								camera3.getId() % TaskForm.ID_DIVIDER))
						.skipMemoryCache().into(thirdImage);
				;
			} catch (FileNotFoundException e) {
				Log.d(tag, "3rd image not taken ");
			}

		} catch (NullPointerException e) {
			Log.e(tag, e.toString() + "");
		}
		camera1.setOnClickListener(cameraClickListener);
		camera2.setOnClickListener(cameraClickListener);
		camera3.setOnClickListener(cameraClickListener);
		super.onResume();
	}

	OnClickListener cameraClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			dispatchTakePictureIntent(v.getId() % TaskForm.ID_DIVIDER);
		}
	};

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
		int requestCode = Integer.parseInt(String
				.valueOf(REQUEST_IMAGE_CAPTURE) + String.valueOf(filename));
		Log.d(tag, "request code" + requestCode);
		startActivityForResult(takePictureIntent, requestCode);

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.d(tag, "onActivityResult requestCode: " + requestCode
				+ " resultCode : " + resultCode);
		Log.d(tag, "String.valueOf(requestCode).subSequence(0, 2) "
				+ String.valueOf(requestCode).subSequence(0, 2));
		int req = Integer.parseInt(String.valueOf(requestCode).substring(0, 2));
		Log.d(tag, req + "");
		if (req == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
			// SAVE photo TO Local directory

			Bundle extras = data.getExtras();
			Bitmap imageBitmap = (Bitmap) extras.get("data");
			firstImage.setImageBitmap(imageBitmap);
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

	public void setCapturedImageURI(Uri mCapturedImageURI) {
		this.mCapturedImageURI = mCapturedImageURI;
	}

}
