package bpr10.git.transtech;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import openerp.OEVersionException;
import openerp.OpenERP;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import bpr10.git.transtech.AsyncTaskCallback.AsyncTaskCallbackInterface;

import com.openerp.orm.OEFieldsHelper;
import com.squareup.picasso.Picasso;

public class TaskForm extends BaseActivity {
	ViewPager mViewPager;
	ImageView firstDot, secondDot, thirdDot, forthDot;
	ImageView nextItem, previousItem;
	RelativeLayout previousItmLayout, nextItemLayout;
	protected FragmentPageAdapter mFragmentPageAdapter;
	Bundle bundle;
	private String tag = getClass().getSimpleName();
	static int taskId;
	public static JSONObject taskPayload;
	private JSONObject taskObj;
	protected static String mCurrentPhotoPath;
	public static final int ID_DIVIDER = 1000;
	public static JSONObject remarksResponse;
	protected static final int REQUEST_IMAGE_CAPTURE = 11;
	protected static Map<String, String> imageUris;
	private static Map<String, Uri> thumbnailUris;
	public static int taskFlag = 0;
	private int currentPosition;
	static int updateCount = 0;
	private NoCommentCheckListener mNoCommentCheckListener;

	@Override
	protected void onResume() {
		mNoCommentCheckListener = new NoCommentCheckListener();
		IntentFilter filter = new IntentFilter();
		filter.addAction("bpr10.git.transtech.no_comments_checked");
		LocalBroadcastManager.getInstance(getApplicationContext())
				.registerReceiver(mNoCommentCheckListener, filter);
		super.onResume();
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		mCurrentPhotoPath = savedInstanceState.getString("mCurrentPhotoPath");
		super.onRestoreInstanceState(savedInstanceState);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.servicingfrom);
		deleteTaskDirectory();
		if (savedInstanceState != null) {
			mCurrentPhotoPath = savedInstanceState
					.getString("mCurrentPhotoPath");
		}
		imageUris = new HashMap<String, String>();
		thumbnailUris = new HashMap<String, Uri>();
		imageUris.put("demo", "test");
		firstDot = (ImageView) findViewById(R.id.first_dot);
		secondDot = (ImageView) findViewById(R.id.second_dot);
		thirdDot = (ImageView) findViewById(R.id.third_dot);
		forthDot = (ImageView) findViewById(R.id.forth_dot);
		nextItem = (ImageView) findViewById(R.id.next_item);
		previousItem = (ImageView) findViewById(R.id.previous_item);
		previousItmLayout = (RelativeLayout) findViewById(R.id.previous_item_layout);
		nextItemLayout = (RelativeLayout) findViewById(R.id.next_item_layout);
		previousItmLayout.setVisibility(View.INVISIBLE);
		firstDot.setImageResource(R.drawable.dot_active);
		secondDot.setImageResource(R.drawable.dot_inactive);
		thirdDot.setImageResource(R.drawable.dot_inactive);
		forthDot.setImageResource(R.drawable.dot_inactive);

		mFragmentPageAdapter = new FragmentPageAdapter(
				getSupportFragmentManager());

		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mFragmentPageAdapter);
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				currentPosition = arg0;
				switch (arg0) {
				case 0:
					firstDot.setImageResource(R.drawable.dot_active);
					secondDot.setImageResource(R.drawable.dot_inactive);
					thirdDot.setImageResource(R.drawable.dot_inactive);
					forthDot.setImageResource(R.drawable.dot_inactive);
					nextItemLayout.setVisibility(View.VISIBLE);
					previousItmLayout.setVisibility(View.INVISIBLE);
					break;
				case 1:
					firstDot.setImageResource(R.drawable.dot_inactive);
					secondDot.setImageResource(R.drawable.dot_active);
					thirdDot.setImageResource(R.drawable.dot_inactive);
					forthDot.setImageResource(R.drawable.dot_inactive);
					nextItemLayout.setVisibility(View.VISIBLE);
					previousItmLayout.setVisibility(View.VISIBLE);
					break;

				case 2:
					firstDot.setImageResource(R.drawable.dot_inactive);
					secondDot.setImageResource(R.drawable.dot_inactive);
					thirdDot.setImageResource(R.drawable.dot_active);
					forthDot.setImageResource(R.drawable.dot_inactive);
					nextItemLayout.setVisibility(View.VISIBLE);
					previousItmLayout.setVisibility(View.VISIBLE);
					break;
				case 3:
					firstDot.setImageResource(R.drawable.dot_inactive);
					secondDot.setImageResource(R.drawable.dot_inactive);
					thirdDot.setImageResource(R.drawable.dot_inactive);
					forthDot.setImageResource(R.drawable.dot_active);
					nextItemLayout.setVisibility(View.INVISIBLE);
					previousItmLayout.setVisibility(View.VISIBLE);
					break;
				default:
					break;
				}
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}
		});

		nextItem.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1, true);

			}
		});
		previousItem.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				mViewPager.setCurrentItem(mViewPager.getCurrentItem() - 1, true);
			}

		});
		try {
			taskObj = new JSONObject(getIntent().getStringExtra(
					TaskDetails.taskDetais));
			Log.d(tag, "taskObj " + taskObj.toString());
			taskId = taskObj.getInt("id");
			Log.d(tag, " taskId " + taskId);
			if (checkForTaskId(taskId).equals("0")) {
				// create task in shared prefs
				creteTask(taskId);
				taskPayload = new JSONObject();
			} else {

				taskPayload = new JSONObject(checkForTaskId(taskId));

			}
			taskPayload.put("surv_task", taskId);
			taskPayload.put("atm_surv", taskObj.getJSONArray("atm").get(0));
			taskPayload.put("month", taskObj.getString("task_month"));
			taskPayload.put("customer_surv", taskObj.getJSONArray("customer")
					.get(0));
			taskPayload.put("surv_task", taskId);
			taskPayload.put("surveyor_surv", ApplicationClass.surveyor_Id);
			Log.d(tag, "taskPayload " + taskPayload.toString());

		} catch (JSONException e) {
			e.printStackTrace();
		}
		getRemarkCatrgories();
	}

	void creteTask(int taskId) {
		SharedPreferences obj = getSharedPreferences("SharedPrefs",
				Context.MODE_PRIVATE);
		Editor editor = obj.edit();
		editor.putString(String.valueOf(taskId), new JSONObject().toString());
		editor.commit();
	}

	public String checkForTaskId(int taskId) {
		SharedPreferences preferences = getSharedPreferences("SharedPrefs",
				Context.MODE_PRIVATE);
		return preferences.getString(String.valueOf(taskId), "0");
	}

	class FragmentPageAdapter extends FragmentPagerAdapter {

		public FragmentPageAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int arg0) {
			switch (arg0) {
			case 0:
				return new BeforeImages();
			case 1:
				return new CheckList1();
			case 2:
				return new CheckList2();
			case 3:
				return new AfterImages();
			default:
				break;
			}
			return null;

		}

		// public void refresh() {
		// mFragmentPageAdapter.startUpdate(mViewPager);
		// }

		@Override
		public int getCount() {
			return 4;
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
	}

	@SuppressLint("NewApi")
	protected void saveImageTOoLocalStorage(Context context, String imageID,
			Uri imageUri) throws IOException {
		String imageDirName = "images_" + taskId;
		String imageFileName = "IMG_" + imageID;
		final int THUMBNAIL_HEIGHT = 48;
		Bitmap imageBitmap = MediaStore.Images.Media.getBitmap(
				context.getContentResolver(), imageUri);
		Float width = Float.valueOf(imageBitmap.getWidth());
		Float height = Float.valueOf(imageBitmap.getHeight());
		Float ratio = width / height;
		Bitmap thumbnailImage = Bitmap.createScaledBitmap(imageBitmap,
				(int) (THUMBNAIL_HEIGHT * ratio), THUMBNAIL_HEIGHT, false);
		ContextWrapper cw = new ContextWrapper(context);
		File directory = cw.getDir(imageDirName, Context.MODE_PRIVATE);

		File imageFile = new File(directory, imageFileName + ".jpeg");
		if (imageFile.exists()) {
			imageFile.delete();
			if (imageFile.createNewFile()) {
				Log.d(tag, "Emptyfile created");
			}
		} else {
			Log.d(tag, "File does not exist");

		}
		FileOutputStream fos = null;

		fos = new FileOutputStream(imageFile);
		if (thumbnailImage.compress(Bitmap.CompressFormat.PNG, 100, fos)) {
			Log.i(tag, "Stored in " + imageDirName + " bitmap image : "
					+ imageID);
			Log.i(tag, "image size : " + imageBitmap.getAllocationByteCount()
					/ 1000 + " kb");

			Log.i(tag,
					"thumbnail Image size : "
							+ thumbnailImage.getAllocationByteCount() / 1000
							+ " kb");

			Log.i(tag, "stored image size : " + imageFile.length() / 1000
					+ " kb");
		} else {
			imageFile.delete();
			Log.i(tag, "Could not create image file. Compress format error");
		}
		thumbnailImage.recycle();
		thumbnailUris.put(imageID, Uri.fromFile(imageFile));
		fos.close();

	}

	protected Uri getImageFromLocalStorage(int ImageId)
			throws FileNotFoundException {
		if (thumbnailUris.containsKey(String.valueOf(ImageId)))
			return thumbnailUris.get(String.valueOf(ImageId));
		else
			throw new FileNotFoundException();

	}

	protected void deleteTaskDirectory() {
		Context cw = new ContextWrapper(getApplicationContext());
		File directory = cw.getDir("images_" + taskId, Context.MODE_PRIVATE);
		deleteRecursive(directory);
	}

	@Override
	protected void onDestroy() {
		deleteTaskDirectory();
		Log.d(tag, taskPayload.toString());
		super.onDestroy();
	}

	protected void deleteRecursive(File dir) {
		if (dir.isDirectory()) {
			String[] children = dir.list();
			for (int i = 0; i < children.length; i++) {
				new File(dir, children[i]).delete();
				Log.d("deleting file", children[i]);
			}
		}
		dir.delete();
	}

	@SuppressLint("NewApi")
	protected String encodeImage(Context context, Uri imageUri) {
		try {
			String tag = "encoding iamge";
			String encodedImage = "";
			Bitmap bitmap;

			bitmap = MediaStore.Images.Media.getBitmap(
					context.getContentResolver(), imageUri);
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream);
			byte[] byteArray = stream.toByteArray();
			encodedImage = Base64.encodeToString(byteArray, Base64.DEFAULT);
			Log.d(tag,
					"file :" + imageUri.toString()
							+ " encodedImage size in kb :"
							+ bitmap.getAllocationByteCount() / 1000);
			bitmap.recycle();
			return encodedImage;
		} catch (FileNotFoundException e) {
			Log.e(tag, "Image not found " + imageUri);
		} catch (IOException e) {
			Log.e(tag, "I/O Exception " + imageUri);
			e.printStackTrace();
		}
		return null;
	}

	protected JSONObject getTaskObject() {
		Log.d(tag, taskObj.toString());
		return taskObj;
	}

	protected void getRemarkCatrgories() {
		new AsyncTaskCallback(this, new AsyncTaskCallbackInterface() {

			@Override
			public String backGroundCallback() {
				OpenERP mOpenERP;
				try {
					mOpenERP = ApplicationClass.getInstance().getOpenERPCon();
					OEFieldsHelper fields = new OEFieldsHelper(new String[] {
							"description", "name" });

					remarksResponse = mOpenERP.search_read("remarks.category",
							fields.get());
					Log.i(tag, remarksResponse.toString());
					return remarksResponse.toString();
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

			}

		}).execute();
	}

	protected File createImageFile(int imageFileName) throws IOException {
		// Create an image file name
		File storageDir = Environment
				.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
		File image = File.createTempFile(imageFileName + "", /* prefix */
				".jpg", /* suffix */
				storageDir /* directory */
		);
		// Save a file: path for use with ACTION_VIEW intents
		mCurrentPhotoPath = "file:" + image.getAbsolutePath();
		return image;
	}

	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {

		savedInstanceState.putString("mCurrentPhotoPath", mCurrentPhotoPath);
		super.onSaveInstanceState(savedInstanceState);
	}

	protected void updateImagePath(String key) {
		imageUris.put(key, TaskForm.mCurrentPhotoPath);

	}

	protected void checkforImage(Context context, Button cameraButton,
			String key, ImageView imageView) {
		String tag = "chackForImage";
		if (TaskForm.imageUris.containsKey(String.valueOf(cameraButton.getId()
				% TaskForm.ID_DIVIDER))) {
			try {
				if (!TaskForm.taskPayload.has(key)) {
					Uri image1Uri = Uri
							.parse(TaskForm.imageUris.get(String
									.valueOf(cameraButton.getId()
											% TaskForm.ID_DIVIDER)));
					TaskForm.taskPayload.putOpt(key,
							encodeImage(context, image1Uri));

				}

				Picasso.with(context)
						.load(getImageFromLocalStorage(cameraButton.getId()
								% TaskForm.ID_DIVIDER)).skipMemoryCache()
						.into(imageView);
				// Log.d(tag,
				// "Loaded Image " + key + " size : " + imageFile.length()
				// / 1000 + "kb");

			} catch (JSONException e) {
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				Log.d(tag, key + " image not taken ");
			}
		} else {
			Log.d(tag, "image not available " + key);
		}

	}

	@Override
	public void onPause() {
		LocalBroadcastManager.getInstance(getApplicationContext())
				.unregisterReceiver(mNoCommentCheckListener);
		super.onPause();
	}

	protected static void noComments() {

		taskPayload.remove("check_list2");
		taskPayload.remove("check_list3");
		taskPayload.remove("check_list4");
		taskPayload.remove("check_list5");
		taskPayload.remove("check_list6");
		taskPayload.remove("check_list7");
		taskPayload.remove("check_list8");
		taskPayload.remove("check_list9");
		taskPayload.remove("check_list10");
		taskPayload.remove("check_list11");
		taskPayload.remove("check_list12");
		taskPayload.remove("check_list13");
		taskPayload.remove("check_list14");
		taskPayload.remove("check_list15");
		taskPayload.remove("check_list16");
		taskPayload.remove("check_list17");
		taskPayload.remove("check_list18");
		taskPayload.remove("check_list19");
		taskPayload.remove("check_list20");
		taskPayload.remove("check_list21");
		taskPayload.remove("check_list22");
		taskPayload.remove("check_list23");
		taskPayload.remove("check_list24");
		Log.d("nocoments click", taskPayload.toString());

	}

	class NoCommentCheckListener extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (updateCount == 0) {
				Log.d("No Comments Checked", "broadcastRecieverWorking");
				mFragmentPageAdapter.notifyDataSetChanged();
				mViewPager.setAdapter(mFragmentPageAdapter);
				mViewPager.setCurrentItem(currentPosition);
				updateCount++;
			} else {
				Log.d(tag, "adapter didn't update. Count " + updateCount);
			}

		}
	}
}
