package org.sakuratya.poweramp_np_twidere;

import org.mariotaku.twidere.Twidere;

import com.maxmpz.audioplayer.player.PowerAMPiAPI;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ComposeActivity extends Activity implements OnSharedPreferenceChangeListener {
	private final static String TAG = "ComposeActivity";
	
	/**
	 * Track info.
	 */
	private String mTrackTitle ;
	private String mTrackArtist;
	private String mTrackAlbum;

	/** 
	 * Previews widget.
	 */
	private TextView mTrackTitlePreview;
	private TextView mTrackArtistPreview;
	private TextView mTrackAlbumPreview;
	private TextView mTextPendingPreview;
	private Button mAppendNPTextButton;
	
	/**
	 * Pending text which will append to tweet.
	 */
	private String mTextPending;
	
	private SharedPreferences mPreferences;
	
	/**
	 * When set to false. all previews widget will not show. This activity skip and auto append np text to tweet.
	 */
	private boolean isPreviewEnabled = true;
	/**
	 * Identify if track info has been gained.
	 */
	private boolean isTrackInfoRead = false;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPreferences  = PreferenceManager.getDefaultSharedPreferences(this);
        
        // Read the preference.
        mTextPending = mPreferences.getString(SettingsActivity.SETTING_NP_TEXT_FORMAT, "");
        isPreviewEnabled = mPreferences.getBoolean(SettingsActivity.SETTING_SHOW_PREVIEW, true);
        
        if(isPreviewEnabled) {
        	setContentView(R.layout.activity_compose);
            
            mTrackTitlePreview = (TextView) findViewById(R.id.track_title);
            mTrackArtistPreview = (TextView) findViewById(R.id.track_artist);
            mTrackAlbumPreview = (TextView) findViewById(R.id.track_album);
            mTextPendingPreview = (TextView) findViewById(R.id.text_pending);
            mAppendNPTextButton = (Button) findViewById(R.id.append_np_text);
            mAppendNPTextButton.setOnClickListener(mOnClickListener);	
        }
        
        registerReceiver(mTrackReceiver, new IntentFilter(PowerAMPiAPI.ACTION_TRACK_CHANGED));
    }
    
    private OnClickListener mOnClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			int id = v.getId();
			switch(id){
			case R.id.append_np_text:
				Twidere.appendComposeActivityText(ComposeActivity.this, mTextPending);
				break;
			}
		}
	};

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    private BroadcastReceiver mTrackReceiver = new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context context, Intent intent) {
			Bundle currentTrack = intent.getBundleExtra(PowerAMPiAPI.TRACK);
			if(currentTrack!=null) {
				updateTrackInfo(currentTrack);
			}
		}
	};
	
	/**
	 * Update track info from give Bundle
	 * @param track bundle contains trackinfo.
	 */
	private void updateTrackInfo(Bundle track) {
		mTrackTitle = track.getString(PowerAMPiAPI.Track.TITLE);
		mTrackArtist = track.getString(PowerAMPiAPI.Track.ARTIST);
		mTrackAlbum = track.getString(PowerAMPiAPI.Track.ALBUM);
		Log.d(TAG, "mTrackTitle: "+ mTrackTitle);
		Log.d(TAG, "mTrackArtist: "+ mTrackArtist);
		Log.d(TAG, "mTrackAlbum: "+ mTrackAlbum);
		isTrackInfoRead = true;
		updatePendingText();
		if(isPreviewEnabled) {
			mTrackTitlePreview.setText(mTrackTitle);
			mTrackArtistPreview.setText(mTrackArtist);
			mTrackAlbumPreview.setText(mTrackAlbum);
			mTextPendingPreview.setText(mTextPending);
		} else {
			Twidere.appendComposeActivityText(ComposeActivity.this, mTextPending);
		}
	}
	
	/**
	 * Update {@link #mTextPending} with give track info and np text format from preference.
	 */
	private void updatePendingText() {
		if(!TextUtils.isEmpty(mTextPending)) {
			mTextPending = mTextPending.replace("[title]", mTrackTitle).replace("[artist]", mTrackArtist).replace("[album]", mTrackAlbum);
		} else {
			Toast.makeText(this, R.string.error_loading_text, Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	protected void onDestroy() {
		unregisterReceiver(mTrackReceiver);
		super.onDestroy();
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		// When preference has changed, we need to update pending text with new format.
		// Usually this will not occur.
		if(SettingsActivity.SETTING_NP_TEXT_FORMAT.equals(key)) {
			mTextPending = sharedPreferences.getString(SettingsActivity.SETTING_NP_TEXT_FORMAT, "");
			if(isTrackInfoRead) {
				updatePendingText();
				if(isPreviewEnabled) {
					mTextPendingPreview.setText(mTextPending);
				}
			}
		}
	}
	
	
}
