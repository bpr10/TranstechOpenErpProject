package bpr10.git.transtech;

import android.content.Intent;
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
	public boolean onOptionsItemSelected(MenuItem item) {
		int itemId = item.getItemId();
		if (itemId == R.id.action_logout) {
			// logout action
			Toast.makeText(getApplicationContext(), "logout sucessfully",
					Toast.LENGTH_LONG).show();
			Intent i = new Intent(this, LoginActivity.class);
			startActivity(i);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}