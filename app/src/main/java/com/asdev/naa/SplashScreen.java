package com.asdev.naa;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                overSplash();

            }
        },2000);

    }

    private void overSplash() {
        if(isFirstRun())
        {
            Intent firstIntent = new Intent(SplashScreen.this, FirstRun.class);
            startActivity(firstIntent);
            finish();
        }
        else if(new helper.check().isValidUser())
        {
            Intent homeIntent = new Intent(SplashScreen.this, MainActivity.class);
            startActivity(homeIntent);
            finish();
        }
        else
        {
            Intent authIntent = new Intent(SplashScreen.this, SignIn.class);
            startActivity(authIntent);
            finish();
        }
    }

    private boolean isFirstRun()     // Checks if the app is run for the first time
    {
        SharedPreferences firstTimePref = this.getSharedPreferences("first_time", MODE_PRIVATE);
        boolean firstTime = firstTimePref.getBoolean("firstTime", true);   // Checking if firstTime exists and extracting its value
        if(firstTime)
        {
            SharedPreferences.Editor editor = firstTimePref.edit();
            editor.putBoolean("firstTime",false);          // Changing firstTime to false and storing is shared preference so that next time it returns false

            editor.apply();
        }
        return firstTime;   // returning first run from shared Preferences
    }
}
