package org.sakuratya.poweramp_np_twidere;

import org.mariotaku.twidere.Twidere;

import com.maxmpz.audioplayer.player.PowerAMPiAPI;

import android.os.Bundle;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class ComposeActivity extends Activity {
	private final static String TAG = "ComposeActivity";
	
	private String mTrackTitle ;
	private String mTrackArtist;
	private String mTrackAlbum;

	private TextView mTrackTitlePreview;
	private TextView mTrackArtistPreview;
	private TextView mTrackAlbumPreview;
	private TextView mTextPendingPreview;
	private Button mAppendNPTextButton;
	
	private String mTextPending;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        mTrackTitlePreview = (TextView) findViewById(R.id.track_title);
        mTrackArtistPreview = (TextView) findViewById(R.id.track_artist);
        mTrackAlbumPreview = (TextView) findViewById(R.id.track_album);
        mTextPendingPreview = (TextView) findViewById(R.id.text_pending);
        mAppendNPTextButton = (Button) findViewById(R.id.append_np_text);
        mAppendNPTextButton.setOnClickListener(mOnClickListener);
        
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
	
	private void updateTrackInfo(Bundle track) {
		mTrackTitle = track.getString(PowerAMPiAPI.Track.TITLE);
		mTrackArtist = track.getString(PowerAMPiAPI.Track.ARTIST);
		mTrackAlbum = track.getString(PowerAMPiAPI.Track.ALBUM);
		Log.d(TAG, "mTrackTitle: "+ mTrackTitle);
		Log.d(TAG, "mTrackArtist: "+ mTrackArtist);
		Log.d(TAG, "mTrackAlbum: "+ mTrackAlbum);
		mTrackTitlePreview.setText(mTrackTitle);
		mTrackArtistPreview.setText(mTrackArtist);
		mTrackAlbumPreview.setText(mTrackAlbum);
		mTextPending = "#NowPlaying " + mTrackArtist + mTrackTitle;
		mTextPendingPreview.setText(mTextPending);
	}

	@Override
	protected void onDestroy() {
		unregisterReceiver(mTrackReceiver);
		super.onDestroy();
	}
	
	
}
