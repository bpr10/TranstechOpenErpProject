package bpr10.git.transtech;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity {

	// declare properties
	private String[] mNavigationDrawerItemTitles;
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private SharedPreferences sharedPreferences;
	private ActionBarDrawerToggle mDrawerToggle;

	// nav drawer title
	private CharSequence mDrawerTitle;

	// used to store app title
	private CharSequence mTitle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// for proper titles
		mTitle = mDrawerTitle = getTitle();

		// initialize properties
		mNavigationDrawerItemTitles = getResources().getStringArray(
				R.array.navigation_drawer_items_array);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.left_drawer);

		// list the drawer items
		ObjectDrawerItem[] drawerItem = new ObjectDrawerItem[3];

		drawerItem[0] = new ObjectDrawerItem(R.drawable.ic_task, "Tasks");
		drawerItem[1] = new ObjectDrawerItem(R.drawable.ic_alerts,
				"Alert");
		drawerItem[2] = new ObjectDrawerItem(R.drawable.ic_history,
				"History");
		
		// Pass the folderData to our ListView adapter
		DrawerItemCustomAdapter adapter = new DrawerItemCustomAdapter(this,
				R.layout.listview_item_row, drawerItem);

		// Set the adapter for the list view
		mDrawerList.setAdapter(adapter);

		// set the item click listener
		mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

		// for app icon control for nav drawer
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerToggle = new ActionBarDrawerToggle(this, /* host Activity */
		mDrawerLayout, /* DrawerLayout object */
		R.string.drawer_open, /* "open drawer" description */
		R.string.drawer_close /* "close drawer" description */
		) {

			/** Called when a drawer has settled in a completely closed state. */
			public void onDrawerClosed(View view) {
				super.onDrawerClosed(view);
				getSupportActionBar().setTitle(mTitle);
			}

			/** Called when a drawer has settled in a completely open state. */
			public void onDrawerOpened(View drawerView) {
				super.onDrawerOpened(drawerView);
				getSupportActionBar().setTitle(mDrawerTitle);
			}
		};

		// Set the drawer toggle as the DrawerListener
		mDrawerLayout.setDrawerListener(mDrawerToggle);

		// enable ActionBar app icon to behave as action to toggle nav drawer
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);

		if (savedInstanceState == null) {
			// on first time display view for first nav item
			selectItem(0);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		// The action bar home/up action should open or close the drawer.
		// ActionBarDrawerToggle will take care of this.
		
		int itemId=item.getItemId();
		if(itemId==R.id.action_logout)
		{

		    Toast.makeText(getApplicationContext(), "logout sucessfully", Toast.LENGTH_LONG).show();
		    Intent i= new Intent(MainActivity.this,LoginActivity.class);
		    startActivity(i);
			return true;
		}

		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}

		return super.onOptionsItemSelected(item);
	}
	
	// to change up caret
	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	// navigation drawer click listener
	private class DrawerItemClickListener implements
			ListView.OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			selectItem(position);
		}

	}

	private void selectItem(int position) {

		// update the main content by replacing fragments

		Fragment fragment = null;

		switch (position) {
		case 0:
			fragment = new TasksFragment();
			break;
		case 1:
			fragment = new CreateAlerts();
			break;
		case 2:
			fragment = new TaskHistory();
			break;
		
		default:
			break;
		}

		if (fragment != null) {
			FragmentManager fragmentManager = getSupportFragmentManager();
			fragmentManager.beginTransaction()
					.replace(R.id.content_frame, fragment).commit();

			// update selected item and title, then close the drawer
			mDrawerList.setItemChecked(position, true);
			mDrawerList.setSelection(position);
			setTitle(mNavigationDrawerItemTitles[position]);
			mDrawerLayout.closeDrawer(mDrawerList);

		} else {
			// error in creating fragment
			Log.e("MainActivity", "Error in creating fragment");
		}
	}

	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		getSupportActionBar().setTitle(mTitle);
	}
}
