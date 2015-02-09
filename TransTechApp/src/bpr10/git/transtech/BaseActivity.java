package bpr10.git.transtech;

import android.content.ComponentName;
import android.content.Intent;
import android.support.v4.content.IntentCompat;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class BaseActivity extends ActionBarActivity {

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.base, menu);
		return true;
	}

	@Override
	protected void onStart() {
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		super.onStart();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int itemId = item.getItemId();
		switch (itemId) {
		case android.R.id.home:
			onBackPressed();
			break;
		case R.id.action_aboutus:
			Intent i = new Intent(this, AboutUsActivity.class);
			startActivity(i);
			break;
		case R.id.action_logout:
			Intent logoutIntent = new Intent(this, LoginActivity.class);
			ComponentName cn = logoutIntent.getComponent();
			Intent intent = IntentCompat.makeRestartActivityTask(cn);
			startActivity(intent);
			Toast.makeText(getApplicationContext(), "Logged Out",
					Toast.LENGTH_LONG).show();
			break;
		default:
			break;
		}

		return super.onOptionsItemSelected(item);
	}
}
