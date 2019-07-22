package com.asdev.naa;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class SplashScreen extends AppCompatActivity {

    private FirebaseDatabase db;
    private DatabaseReference userdb;


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
            checkRegisteredUser();
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
    private void checkRegisteredUser() {

        db = FirebaseDatabase.getInstance();
        userdb = db.getReference("Student").child("User");
        userdb.child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid())).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!dataSnapshot.hasChild("name"))
                {
                    Intent mainActivityIntent = new Intent(SplashScreen.this, Register.class);
                    startActivity(mainActivityIntent);
                    finish();
                }
                else
                {
                    Intent homeIntent = new Intent(SplashScreen.this, MainActivity.class);
                    startActivity(homeIntent);
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(SplashScreen.this, "Ruh! Unable to connect to database", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
