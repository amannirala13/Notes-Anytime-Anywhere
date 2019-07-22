package com.asdev.naa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class Register extends AppCompatActivity {

    private TextInputEditText registerName, registerPRN;
    private AppCompatButton registerBtn;
    private String name, PRN;

    private DatabaseReference userdb = FirebaseDatabase.getInstance().getReference().child("Student").child("User");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

            registerName = findViewById(R.id.register_name_text);
            registerBtn = findViewById(R.id.register_btn);
            registerPRN = findViewById(R.id.register_prn_text);

            registerBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    name = Objects.requireNonNull(registerName.getText()).toString();
                    PRN =  Objects.requireNonNull(registerPRN.getText()).toString();
                    registerUser();
                }
            });
    }

    private void registerUser() {
        if(name.length()<4)
            Toast.makeText(this, "Dude! No bluffing. Enter a valid name", Toast.LENGTH_SHORT).show();
        else if(PRN.length()!=11)
            Toast.makeText(this, "Oops! Check your PRN again", Toast.LENGTH_SHORT).show();
        else
        {
            userdb.child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid())).child("name").setValue(name).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful())
                        userdb.child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid())).child("PRN").setValue(PRN).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful())
                                    openMainActivity();
                                else
                                    Toast.makeText(Register.this, "Ruh! Unable to connect to Database", Toast.LENGTH_SHORT).show();
                            }
                        });
                    else
                        Toast.makeText(Register.this, "Ruh! Unable to connect to Database", Toast.LENGTH_SHORT).show();

                }
            });
        }
    }

    private void openMainActivity()
    {
        Intent mainActivityIntent = new Intent(Register.this, MainActivity.class);
        startActivity(mainActivityIntent);
        finish();
    }
}
