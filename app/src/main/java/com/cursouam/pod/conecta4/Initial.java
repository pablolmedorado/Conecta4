package com.cursouam.pod.conecta4;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.view.MotionEvent;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class Initial extends AppCompatActivity {

    private static final long SPLASH_SCREEN_DELAY = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set portrait orientation
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setContentView(R.layout.activity_initial);


        TimerTask task = new TimerTask() {
            @Override
            public void run() {

                // Start the main activity
                startActivity(new Intent("com.cursouam.pod.conecta4.MAINACTIVITY"));

                // Close the activity so the user won't able to go back this
                // activity pressing Back button
                finish();
            }
        };

        // Simulate a long loading process on application startup.
        Timer timer = new Timer();
        timer.schedule(task, SPLASH_SCREEN_DELAY);

        PreferenceManager.setDefaultValues(this, R.xml.settings, false);
    }

    /*
    @Override
    public boolean onTouchEvent (MotionEvent event){
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            startActivity(new Intent("com.cursouam.pod.conecta4.MAINACTIVITY"));
            finish();
        }
        return true;
    }
    */
}
