package bpr10.git.transtech;

import java.io.FileNotFoundException;
import java.io.IOException;

import com.squareup.picasso.Picasso;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

public class AfterImages extends Fragment {
	ImageView afterImage1, afterImage2, afterImage3;
	Button camera1, camera2, camera3;
	private Uri mCapturedImageURI;
	private String tag = getClass().getSimpleName();
	private TaskForm mtaskForm;
	private Context mContext;
	static final int REQUEST_IMAGE_CAPTURE = 11;

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
		return rootView;
	}

	@Override
	public void onResume() {
		try {
			try {
				Picasso.with(getActivity().getApplicationContext())
						.load(mtaskForm.getImageFromLocalStorage(mContext,
								camera1.getId() % TaskForm.ID_DIVIDER)).skipMemoryCache()
						.into(afterImage1);
			} catch (FileNotFoundException e) {
				Log.d(tag, "1st image not taken ");
			}
			try {
				Picasso.with(getActivity().getApplicationContext())
						.load(mtaskForm.getImageFromLocalStorage(mContext,
								camera2.getId() % TaskForm.ID_DIVIDER)).skipMemoryCache()
						.into(afterImage2);
				;
			} catch (FileNotFoundException e) {
				Log.d(tag, "2nd image not taken ");
			}
			try {
				Picasso.with(getActivity().getApplicationContext())
						.load(mtaskForm.getImageFromLocalStorage(mContext,
								camera3.getId() % TaskForm.ID_DIVIDER)).skipMemoryCache()
						.into(afterImage3);
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

		Log.d(tag, "String.valueOf(requestCode).subSequence(0, 2) "
				+ String.valueOf(requestCode).subSequence(0, 2));
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
