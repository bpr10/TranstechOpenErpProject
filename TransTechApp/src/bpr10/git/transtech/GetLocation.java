package bpr10.git.transtech;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

public class GetLocation implements LocationListener {

	Activity activity;
	double lat = 0;
	double lng = 0;
	int ENABLED = 1;
	int DISABLED = 2;
	int status;
	public LocationManager locationManager;
	Location mLocation;

	public GetLocation(Activity mContext) {
		this.activity = mContext;
		String context = Context.LOCATION_SERVICE;
		locationManager = (LocationManager) activity.getApplicationContext()
				.getSystemService(context);

	}

	public Location getCurrentLocation() {
		try {
			String provider = LocationManager.GPS_PROVIDER;
			// String networkProvider = LocationManager.NETWORK_PROVIDER;
			locationManager.requestLocationUpdates(
					LocationManager.NETWORK_PROVIDER, 0, 2000, this);
			mLocation = locationManager.getLastKnownLocation(provider);
		} catch (Exception e) {
			Log.e("GetLocaion Class", "Error while fetching location");
			e.printStackTrace();
		}
		return mLocation;
	}

	@Override
	public void onLocationChanged(Location location) {
		lat = location.getLatitude();
		lng = location.getLongitude();
		mLocation = location;
	}

	@Override
	public void onProviderDisabled(String provider) {
		status = 0;
	}

	@Override
	public void onProviderEnabled(String provider) {
		status = 1;
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {

	}

}
