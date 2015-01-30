package bpr10.git.transtech;

import java.io.FileNotFoundException;
import java.io.IOException;

import com.squareup.picasso.Picasso;

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

public class BeforeImages extends Fragment {

	protected ImageView firstImage, secondImage, thirdImage;
	protected Button camera1, camera2, camera3;
	protected Bundle bundle;
	protected CameraManager cameraManager;
	static final int REQUEST_IMAGE_CAPTURE = 111;
	private String tag = getClass().getSimpleName();
	private String mCurrentPhotoPath;
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

		camera1 = (Button) rootView.findViewById(R.id.camara1);
		camera2 = (Button) rootView.findViewById(R.id.camara2);
		camera3 = (Button) rootView.findViewById(R.id.camara3);
		try {
			try {
				Picasso.with(getActivity().getApplicationContext()).load(
						mtaskForm.getImageFromLocalStorage(mContext,
								camera1.getId() / TaskForm.ID_DIVIDER));
			} catch (FileNotFoundException e) {
				Log.d(tag, "1st image not taken ");
			}
			try {
				Picasso.with(getActivity().getApplicationContext()).load(
						mtaskForm.getImageFromLocalStorage(mContext,
								camera2.getId() / TaskForm.ID_DIVIDER));
			} catch (FileNotFoundException e) {
				Log.d(tag, "2nd image not taken ");
			}
			try {
				Picasso.with(getActivity().getApplicationContext()).load(
						mtaskForm.getImageFromLocalStorage(mContext,
								camera3.getId() / TaskForm.ID_DIVIDER));
			} catch (FileNotFoundException e) {
				Log.d(tag, "3rd image not taken ");
			}

		} catch (NullPointerException e) {
			Log.e(tag, e.toString() + "");
		}
		camera1.setOnClickListener(cameraClickListener);
		return rootView;
	}

	OnClickListener cameraClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			dispatchTakePictureIntent(v.getId() / TaskForm.ID_DIVIDER);
		}
	};

	boolean ifImageExists(int id) {
		return false;
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
		startActivityForResult(
				takePictureIntent,
				Integer.parseInt(String.valueOf(REQUEST_IMAGE_CAPTURE)
						+ String.valueOf(filename)));

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.d(tag, "onActivityResult requestCode: " + requestCode
				+ " resultCode : " + resultCode);
		Log.d(tag, "String.valueOf(requestCode).subSequence(0, 3) "
				+ String.valueOf(requestCode).subSequence(0, 3));
		int req = requestCode / 100;
		Log.d(tag, "req " + req);
		if (req == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
			// SAVE photo TO Local directory

			Bundle extras = data.getExtras();
			Log.d(tag, extras.toString());
			Bitmap imageBitmap = (Bitmap) extras.get("data");
			firstImage.setImageBitmap(imageBitmap);
			try {
				mtaskForm.saveImageTOoLocalStorage(mContext, requestCode,
						imageBitmap);
			} catch (IOException e) {
				Log.d(tag, "Please Try Again");
				e.printStackTrace();
			}

		}
	}

	public String getCurrentPhotoPath() {
		return mCurrentPhotoPath;
	}

	public void setCurrentPhotoPath(String mCurrentPhotoPath) {
		this.mCurrentPhotoPath = mCurrentPhotoPath;
	}

	public Uri getCapturedImageURI() {
		return mCapturedImageURI;
	}

	public void setCapturedImageURI(Uri mCapturedImageURI) {
		this.mCapturedImageURI = mCapturedImageURI;
	}

}
