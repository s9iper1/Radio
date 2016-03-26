package com.byteshaft.shout;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ProgressBar;

public class Player extends Fragment implements View.OnClickListener {

    private View mBaseView;
    public Button mPlaybackButton;
    private static Player sInstance;
    private boolean mFreshRun = true;
    public ProgressBar mProgressBar;
    private ObjectAnimator animation;

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
        mProgressBar = (ProgressBar) mBaseView.findViewById(R.id.progress_bar);
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.LOLLIPOP) {
            mProgressBar.setProgressDrawable(getResources().getDrawable
                    (R.drawable.progress_bar_less_than_lollipop));
        } else {
            mProgressBar.setProgressDrawable(getResources().getDrawable
                    (R.drawable.progress_bar_for_greater_than_lollipop));

        }
        mPlaybackButton = (Button) mBaseView.findViewById(R.id.button_toggle_playback);
        mPlaybackButton.setOnClickListener(this);
        Intent intent = new Intent(getActivity().getApplicationContext(), StreamService.class);
        intent.putExtra(AppGlobals.READY_STREAM, true);
        if (getService() == null) {
            getActivity().startService(intent);
        }
        Intent notificationIntent = new Intent(getActivity().getApplicationContext(),
                NotificationService.class);
        notificationIntent.setAction(Constants.ACTION.STARTFOREGROUND_ACTION);
        Log.i("NOtification", String.valueOf(NotificationService.getsInstance() == null));
        if (NotificationService.getsInstance() != null) {
            NotificationService.getsInstance().onDestroy();
            NotificationService.sInstance = null;
        }
        if (NotificationService.getsInstance() == null) {
            getActivity().startService(notificationIntent);
        }
        return mBaseView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void updateProgressBar() {
        mProgressBar.setVisibility(View.VISIBLE);
        animation = ObjectAnimator.ofInt(mProgressBar, "progress", 0, 500); // see this max value coming back here, we animale towards that value
        animation.setDuration(5000); //in milliseconds
        animation.setRepeatCount(Animation.INFINITE);
        animation.setInterpolator(new DecelerateInterpolator());
        animation.start();
    }

    public void stopProgressBar() {
        mProgressBar.clearAnimation();
        mProgressBar.animate().cancel();
        mProgressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_toggle_playback:
                if (animation != null && animation.isRunning()) {
                    stopProgressBar();
                }
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
        if (!AppGlobals.getSongStatus()) {
            if (mFreshRun) {
                mFreshRun = false;
            }
            if (getService() == null) {
                Intent intent = new Intent(AppGlobals.getContext(), StreamService.class);
                intent.putExtra(AppGlobals.READY_STREAM, false);
                if (getActivity() == null) {
                    NotificationService.getsInstance().stopForeground(true);
                    NotificationService.getsInstance().stopSelf();
                } else {
                    getActivity().startService(intent);
                }
            }
        } else {
            if (getService() != null) {
                getService().stopStream();
                AppGlobals.setSongPlaying(false);
                Helpers.updateMainViewButton();
                NotificationService.getsInstance().showNotification();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (AppGlobals.getSongStatus()) {
            mPlaybackButton.setBackgroundResource(R.drawable.apollo_holo_dark_pause);
        } else {
            mPlaybackButton.setBackgroundResource(R.drawable.apollo_holo_dark_play);
        }
    }
}
