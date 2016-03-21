package com.byteshaft.shout;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener , View.OnClickListener {

    public Button mPlaybackButton;
    public CustomMediaPlayer sMediaPlayer;
    private static MainActivity sInstance;
    private boolean mFreshRun = true;

    private void setInstance(MainActivity activity) {
        sInstance = activity;
    }

    static MainActivity getInstance() {
        return sInstance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        setInstance(this);
        mPlaybackButton = (Button) findViewById(R.id.button_toggle_playback);
        startService(new Intent(getApplicationContext(), StreamService.class));
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (sMediaPlayer.isPlaying()) {
                showConfirmationDialog();
            } else {
                onBackPressed();
            }
        }
    }

    @Override
    public void onClick(View v) {
        sMediaPlayer = CustomMediaPlayer.getInstance();
        switch (v.getId()) {
            case R.id.button_toggle_playback:
                togglePlayPause();
                if (!AppGlobals.isNotificationVisible()) {
                    Intent notificationIntent = new Intent(getApplicationContext(), NotificationService.class);
                    notificationIntent.setAction(Constants.ACTION.STARTFOREGROUND_ACTION);
                    startService(notificationIntent);
                }
        }
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
    protected void onResume() {
        super.onResume();
        sMediaPlayer = CustomMediaPlayer.getInstance();
        if (sMediaPlayer.isPlaying()) {
            mPlaybackButton.setBackgroundResource(R.drawable.apollo_holo_dark_pause);
        } else {
            mPlaybackButton.setBackgroundResource(R.drawable.apollo_holo_dark_play);
        }
    }

    private StreamService getService() {
        return StreamService.getInstance();
    }

    private void showConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Stop playback ?");
        builder.setMessage("Should music keep playing in the background ?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                stopService(new Intent(getApplicationContext(), StreamService.class));
                finish();
            }
        });
        builder.create();
        builder.show();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
