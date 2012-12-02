package org.sakuratya.poweramp_np_twidere;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;

public class SettingsActivity extends PreferenceActivity implements OnSharedPreferenceChangeListener {
	
	private static final String TAG = "SettingsActivity";
	
	public static final String SETTING_NP_TEXT_FORMAT = "setting_np_text_format";
	public static final String SETTING_SHOW_PREVIEW = "setting_show_preview";
	public static final String SETTING_INSERT_ALBUMART = "setting_insert_albumart";
	public static final String SETTING_ALBUMART_FORMAT = "setting_albumart_format";

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		addPreferencesFromResource(R.xml.settings);
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		// TODO Auto-generated method stub
		
	}

}
