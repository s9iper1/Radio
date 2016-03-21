package com.byteshaft.shout;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class Player extends Fragment implements View.OnClickListener  {

    private View mBaseView;
    public Button mPlaybackButton;
    public CustomMediaPlayer sMediaPlayer;
    private static Player sInstance;
    private boolean mFreshRun = true;

    private void setInstance(Player activity) {
        sInstance = activity;
    }

    static Player getInstance() {
        return sInstance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBaseView = inflater.inflate(R.layout.player, container, false);
        setInstance(this);
        mPlaybackButton = (Button) mBaseView.findViewById(R.id.button_toggle_playback);
        getActivity().startService(new Intent(getActivity().getApplicationContext(),
                StreamService.class));
        mPlaybackButton.setOnClickListener(this);
        return mBaseView;
    }

    @Override
    public void onClick(View v) {
        sMediaPlayer = CustomMediaPlayer.getInstance();
        switch (v.getId()) {
            case R.id.button_toggle_playback:
                togglePlayPause();
                if (!AppGlobals.isNotificationVisible()) {
                    Intent notificationIntent = new Intent(getActivity().getApplicationContext(),
                            NotificationService.class);
                    notificationIntent.setAction(Constants.ACTION.STARTFOREGROUND_ACTION);
                    getActivity().startService(notificationIntent);
                }
        }
    }

    private StreamService getService() {
        return StreamService.getInstance();
    }

    public void togglePlayPause() {
        if (!sMediaPlayer.isPlaying()) {
            if (mFreshRun) {
                mFreshRun = false;
            }
            getService().startStream();
        } else {
            sMediaPlayer.pause();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        sMediaPlayer = CustomMediaPlayer.getInstance();
        if (sMediaPlayer.isPlaying()) {
            mPlaybackButton.setBackgroundResource(R.drawable.apollo_holo_dark_pause);
        } else {
            mPlaybackButton.setBackgroundResource(R.drawable.apollo_holo_dark_play);
        }
    }

}
