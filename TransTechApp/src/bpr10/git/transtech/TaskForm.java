package bpr10.git.transtech;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import openerp.OEVersionException;
import openerp.OpenERP;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.ActionBarActivity;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;
import bpr10.git.transtech.AsyncTaskCallback.AsyncTaskCallbackInterface;

import com.openerp.orm.OEFieldsHelper;

public class TaskForm extends ActionBarActivity {
	ViewPager mViewPager;
	ImageView firstDot, secondDot, thirdDot, forthDot;
	private FragmentPageAdapter mFragmentPageAdapter;
	Bundle bundle;
	private String tag = getClass().getSimpleName();
	static int taskId;
	public static JSONObject taskPayload;
	private JSONObject taskObj;
	public static final int ID_DIVIDER = 1000;
	public static JSONObject remarksResponse;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.servicingfrom);
		deleteTaskDirectory();
		firstDot = (ImageView) findViewById(R.id.first_dot);
		secondDot = (ImageView) findViewById(R.id.second_dot);
		thirdDot = (ImageView) findViewById(R.id.third_dot);
		forthDot = (ImageView) findViewById(R.id.forth_dot);
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
				switch (arg0) {
				case 0:
					firstDot.setImageResource(R.drawable.dot_active);
					secondDot.setImageResource(R.drawable.dot_inactive);
					thirdDot.setImageResource(R.drawable.dot_inactive);
					forthDot.setImageResource(R.drawable.dot_inactive);
					break;
				case 1:
					firstDot.setImageResource(R.drawable.dot_inactive);
					secondDot.setImageResource(R.drawable.dot_active);
					thirdDot.setImageResource(R.drawable.dot_inactive);
					forthDot.setImageResource(R.drawable.dot_inactive);
					break;

				case 2:
					firstDot.setImageResource(R.drawable.dot_inactive);
					secondDot.setImageResource(R.drawable.dot_inactive);
					thirdDot.setImageResource(R.drawable.dot_active);
					forthDot.setImageResource(R.drawable.dot_inactive);
					break;
				case 3:
					firstDot.setImageResource(R.drawable.dot_inactive);
					secondDot.setImageResource(R.drawable.dot_inactive);
					thirdDot.setImageResource(R.drawable.dot_inactive);
					forthDot.setImageResource(R.drawable.dot_active);
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

		@Override
		public int getCount() {
			return 4;
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		Log.d(tag, "onActivityResult requestCode: " + requestCode
				+ " resultCode : " + resultCode);
		super.onActivityResult(requestCode, resultCode, intent);
	}

	public void saveImageTOoLocalStorage(Context context, String imageID,
			Bitmap image) throws IOException {
		String imageDirName = "images_" + taskId;
		String imageFileName = "IMG_" + imageID;
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
		if (image.compress(Bitmap.CompressFormat.JPEG, 70, fos)) {
			Log.e(tag, "Stored in " + imageDirName + " bitmap image : "
					+ imageID);
			Log.i(tag, "Image Uri : " + Uri.fromFile(imageFile));
		} else {
			imageFile.delete();
			Log.i(tag, "Could not create image file. Compress format error");
		}
		fos.close();

	}

	public File getImageFromLocalStorage(Context context, int ImageId)
			throws FileNotFoundException {
		ContextWrapper cw = new ContextWrapper(context);
		File imageFile = new File(cw.getApplicationInfo().dataDir + "/app_"
				+ "images_" + taskId, "IMG_" + ImageId + ".jpeg");
		if (imageFile.exists()) {
			Log.i(tag, "file found " + Uri.fromFile(imageFile));
			return imageFile;
		} else {
			Log.i(tag, "file does not exist " + Uri.fromFile(imageFile));
			throw new FileNotFoundException();
		}

	}

	void deleteTaskDirectory() {
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

	void deleteRecursive(File dir) {
		if (dir.isDirectory()) {
			String[] children = dir.list();
			for (int i = 0; i < children.length; i++) {
				new File(dir, children[i]).delete();
				Log.d("deleting file", children[i]);
			}
		}
		dir.delete();
	}

	String encodeImage(File imageFile) {
		Bitmap bm = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.JPEG, 60, stream);
		byte[] byteArray = stream.toByteArray();
		String encodedImage = Base64.encodeToString(byteArray, Base64.DEFAULT);
		Log.d(tag, "file :" + imageFile.getName() + " encodedImage :"
				+ encodedImage);
		return encodedImage;
	}

	JSONObject getTaskObject() {
		Log.d(tag, taskObj.toString());
		return taskObj;
	}

	void getRemarkCatrgories() {
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
}
