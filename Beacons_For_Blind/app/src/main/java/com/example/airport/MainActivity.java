package com.example.airport;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;
import com.estimote.sdk.SystemRequirementsChecker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.jar.Manifest;

public class MainActivity extends Activity implements TextToSpeech.OnInitListener,GestureDetector.OnGestureListener,
        GestureDetector.OnDoubleTapListener{
    BeaconManager beaconManager;
    Region region;
    MyApplication application;
    TextToSpeech tts;
    int nextLocation;
    boolean regionExited=true;
    String text;
    int locationNumber=0;
    boolean startNow=true;
    TextView textView,steps;
    ImageView image;
    private GestureDetectorCompat mDetector;
    public boolean onTouchEvent(MotionEvent event){

        this.mDetector.onTouchEvent(event);
        // Be sure to call the superclass implementation
        return super.onTouchEvent(event);
    }

    public void showNotification()  {
        tts= new TextToSpeech(this,this);
        tts.setLanguage(Locale.US);
        tts.setPitch((float) 0.6);
        tts.setSpeechRate(1);
    }

    private void speakOut() {
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        application=(MyApplication)getApplication();
        application.saveInstance(this);
        textView=(TextView)findViewById(R.id.textView);
        steps=(TextView)findViewById(R.id.steps);
        image=(ImageView)findViewById(R.id.imageView);
        mDetector = new GestureDetectorCompat(this,this);
        mDetector.setOnDoubleTapListener(this);
        /*if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_CONTACTS)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(thisActivity,
                        new String[]{Manifest.permission.READ_CONTACTS},
                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }*/
        beaconManager=new BeaconManager(this);
        region=new Region("ranged region",
                UUID.fromString("0050ab5f-1b40-4a80-a78e-0c0d0ad255ab"), 1000, 10);
        beaconManager.setRangingListener(new BeaconManager.RangingListener() {
            @Override
            public void onBeaconsDiscovered(Region region, List<Beacon> list) {
                if(!list.isEmpty()) {
                    Beacon nearestBeacon=list.get(0);
                    stopFindingBeacons();
                    enteredBeaconRange();
                }
            }
        });
    }

    @Override
    public void onResume()  {
        super.onResume();
        SystemRequirementsChecker.checkWithDefaultDialogs(this);
        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                beaconManager.startRanging(region);
            }
        });
    }

    public void startFindingBeacon()    {
        beaconManager.startRanging(region);
    }

    public void stopFindingBeacons()    {
        beaconManager.stopRanging(region);
    }

    public void onPause()   {
        beaconManager.startRanging(region);
        super.onPause();
    }

    public void enteredBeaconRange()   {
        Log.e("entered beacon range","in range");
        //startNow=true;
        switch (locationNumber) {
            case 0:
                text="Welcome to Ralph. Our Application will help you to find and locate items " +
                        "you require. Single tap if you want to visit bread section. Double tap " +
                        "if you want to go to milk section";
                textView.setText("Welcome ");
                break;
            case 1:
                text="You are at the bread section...... For information on all the available products " +
                        "Please scroll....... Single tap if you want to visit water section....... Double tap " +
                        " if you want to go to milk section";
                textView.setText("Bread");
                steps.setText("");
                image.setImageDrawable(getDrawable(R.drawable.bread));
                break;
            case 2:
                text="You are at the milk section...... For information on all the available products" +
                        "Please scroll....... Single tap if you want to visit bread section......... Double tap " +
                        " if you want to go to juice section";
                textView.setText("Milk");
                steps.setText("");
                image.setImageDrawable(getDrawable(R.drawable.milk));
                break;
            case 3:
                text="You are at the juice section...... For information on all the available products" +
                        "Please scroll....... Single tap if you want to visit milk section......... Double tap" +
                        "if you want to go to water section";
                textView.setText("Juice");
                steps.setText("");
                image.setImageDrawable(getDrawable(R.drawable.juice));
                break;
            case 4:
                text="You are at the water section...... For information on all the available products" +
                        "Please scroll....... Single tap if you want to visit juice section......... Double tap" +
                        "if you want to exit";
                textView.setText("Water");
                steps.setText("");
                image.setImageDrawable(getDrawable(R.drawable.water));
                break;
            case 5:
                text="Thank you for shopping at Ralph..... Hope you had a great experience!";
                textView.setText("Exit");
                steps.setText("");
                image.setImageDrawable(getDrawable(R.drawable.info));
                break;
        }
        showNotification();
    }

    public void regionExited()  {
       // startNow=false;
        Log.e("Region exited ","");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            int result = tts.setLanguage(Locale.US);
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Toast.makeText(this, "This language is not supported", Toast.LENGTH_SHORT).show();
            } else {
                speakOut();
            }
        } else {
            Toast.makeText(this, "This language is not supported", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        if(startNow) {
            switch (locationNumber) {
                case 0:
                    text = "You would like to visit bread section. Kindly go right 10 steps";
                    textView.setText("Go right");
                    steps.setText("10");
                    image.setImageDrawable(getDrawable(R.drawable.right_arrow));
                    locationNumber = 1;
                    showNotification();
                    break;
                case 1:
                    text = "You would like to visit water section. Kindly go left 10 steps";
                    textView.setText("Go left");
                    steps.setText("10");
                    image.setImageDrawable(getDrawable(R.drawable.left_arrow));
                    locationNumber = 4;
                    showNotification();
                    break;
                case 2:
                    text = "You would like to visit bread section. Kindly turn around and go 10 steps";
                    textView.setText("Turn around");
                    steps.setText("10");
                    image.setImageDrawable(getDrawable(R.drawable.left_arrow));
                    locationNumber = 1;
                    showNotification();
                    break;
                case 3:
                    text = "You would like to visit milk section. Kindly go left 12 steps";
                    textView.setText("Go left");
                    steps.setText("12");
                    image.setImageDrawable(getDrawable(R.drawable.left_arrow));
                    locationNumber = 2;
                    showNotification();
                    break;
                case 4:
                    text = "You would like to visit juice section. Kindly go left 15 steps";
                    textView.setText("Go left");
                    steps.setText("15");
                    image.setImageDrawable(getDrawable(R.drawable.left_arrow));
                    locationNumber = 3;
                    showNotification();
                    break;
            }
            Log.e("In single tap ", "Double tapped");
        }
        startFindingBeacon();
        return true;
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        if (startNow) {
            switch (locationNumber) {
                case 0:
                    locationNumber=1;
                    text = "You would like to visit milk section. Kindly go straight 12 steps";
                    textView.setText("Go straight");
                    steps.setText("12");
                    image.setImageDrawable(getDrawable(R.drawable.straight_arrow));
                    break;
                case 1:
                    text = "You would like to visit milk section. Kindly turn around and go 10 steps";
                    textView.setText("Turn around");
                    steps.setText("10");
                    image.setImageDrawable(getDrawable(R.drawable.turn_around));
                    locationNumber=2;
                    break;
                case 2:
                    text = "You would like to visit juice section. Kindly go right 12 steps";
                    textView.setText("Go right");
                    steps.setText("12");
                    image.setImageDrawable(getDrawable(R.drawable.right_arrow));
                    locationNumber=3;
                    break;
                case 3:
                    text = "You would like to visit water section. Kindly go right 10 steps";
                    textView.setText("Go right");
                    steps.setText("10");
                    image.setImageDrawable(getDrawable(R.drawable.right_arrow));
                    locationNumber=4;
                    break;
                case 4:
                    text = "You would like to exit. Kindly turn around and go 12 steps";
                    textView.setText("Turn around");
                    steps.setText("12");
                    image.setImageDrawable(getDrawable(R.drawable.turn_around));
                    locationNumber=5;
                    break;
            }
            Log.e("In double tap ", "Double tapped");
            showNotification();
            startFindingBeacon();
        }
        return true;

    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return true;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
         if(startNow) {
        switch (locationNumber) {
            case 0:
                text = "Sorry wrong command. Please try again";
                break;
            case 1:
                text = " The different types of breads we have in our store are : white bread, wheat bread, gluten free " +
                        "bread and whole grain bread.";
                textView.setText("Information");
                steps.setText("");
                image.setImageDrawable(getDrawable(R.drawable.info));
                break;
            case 2:
                text = "The different types of milk we have in our store are : whole milk, reduced-fat milk " +
                        "2%, low-fat milk 1%, and fat-free milk.";
                textView.setText("Information");
                steps.setText("");
                image.setImageDrawable(getDrawable(R.drawable.info));
                break;
            case 3:
                text="The different types of juice we have in our store are Tropical punch, 100% orange juice, Berry punch and Low pulp minute maid";
                textView.setText("Information");
                steps.setText("");
                image.setImageDrawable(getDrawable(R.drawable.info));
                break;
            case 4:
                text="The different types of water we have in our store are : "+
                        "Spring water, Purified water, Mineral water and Sparkling Bottled water";
                textView.setText("Information");
                steps.setText("");
                image.setImageDrawable(getDrawable(R.drawable.info));
                break;
        }
        showNotification();
    }
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }
}
