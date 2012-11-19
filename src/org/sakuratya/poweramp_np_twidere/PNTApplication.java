package org.sakuratya.poweramp_np_twidere;

import android.app.Application;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.preference.PreferenceManager;

public class PNTApplication extends Application implements OnSharedPreferenceChangeListener {
	
	private final static String TAG = "PNTApplication";
	
	/**
	 * Setting preference.
	 */
	SharedPreferences mPreference;
	
	@Override
	public void onCreate() {
		super.onCreate();
		PreferenceManager.setDefaultValues(this, R.xml.settings, false);
		 mPreference = PreferenceManager.getDefaultSharedPreferences(this);
		 mPreference.registerOnSharedPreferenceChangeListener(this);
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		// TODO Auto-generated method stub
		
	}
	
}
