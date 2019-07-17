package com.asdev.naa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private FirebaseDatabase db;
    private DatabaseReference userdb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

                checkRegisteredUser();

    }

    private void checkRegisteredUser() {

        db = FirebaseDatabase.getInstance();
        userdb = db.getReference("Student").child("User");
        userdb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!dataSnapshot.hasChild(FirebaseAuth.getInstance().getUid()))
                    openRegisterActivity();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MainActivity.this, "Ruh! Unable to connect to database", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openRegisterActivity() {
            Intent mainActivityIntent = new Intent(MainActivity.this, Register.class);
            startActivity(mainActivityIntent);
            finish();
    }
}
