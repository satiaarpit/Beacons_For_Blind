package com.example.airport;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;

import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class MyApplication extends Application  {

    private BeaconManager beaconManager;
    MainActivity activity;
    public boolean regionExited=true;
    @Override
    public void onCreate() {
        super.onCreate();
        beaconManager = new BeaconManager(getApplicationContext());
        beaconManager.setMonitoringListener(new BeaconManager.MonitoringListener() {
            @Override
            public void onEnteredRegion(Region region, List<Beacon> list) {
                Log.e("region entered","");
                //if(regionExited) {
                    //regionExited=false;
                   // activity.enteredBeaconRange();
                //}
            }
            @Override
            public void onExitedRegion(Region region) {
                Log.e("region exited","");
                regionExited=true;
               // activity.regionExited();
            }
        });
        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                beaconManager.startMonitoring(new Region("monitored region",
                        UUID.fromString("0050ab5f-1b40-4a80-a78e-0c0d0ad255ab"), 1000, 10));
            }
        });
    }

    public void saveInstance(MainActivity activity) {
        this.activity=activity;
    }

}
