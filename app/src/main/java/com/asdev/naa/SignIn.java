package com.asdev.naa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import helper.countryCode;

public class SignIn extends AppCompatActivity {

    private String phoneNumber, verificationID,code;

    private boolean OTPverified = false;

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks phoneCallBacks;

    private TextInputEditText phoneText;
    private AppCompatButton getOTPbtn;
    private TextInputEditText otpTextInput;
    private AppCompatButton verifyOTPbtn, changeNumberBtn;
    private TextView timer;

    private ConstraintLayout autoOTPlayout, enterOTPlayout;

    private Dialog OTPdialog;

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    private FirebaseDatabase db;
    private DatabaseReference userdb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        phoneText = findViewById(R.id.phone_text);
        getOTPbtn = findViewById(R.id.otp_button);

        phoneText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                set_country_code(phoneText);
            }
        });

        getOTPbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                phoneNumber = Objects.requireNonNull(phoneText.getText()).toString();
                if(new helper.check().isValidPhone(phoneNumber))
                    verifyPhone();
            }
        });

        phoneCallBacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                OTPdialog.cancel();
                authUser(phoneAuthCredential);
                OTPverified = true;
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Toast.makeText(SignIn.this, "Ruh! Unable to verify your number", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                verificationID=s;
                verifyOTPDialog();

            }
        };

    }

    private void authUser(PhoneAuthCredential phoneAuthCredential) {

        mAuth.signInWithCredential(phoneAuthCredential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                           checkRegisteredUser();
                        } else {
                            // Sign in failed, display a message and update the UI
                            Toast.makeText(SignIn.this, "Oops! Something went wrong.", Toast.LENGTH_SHORT).show();

                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                Toast.makeText(SignIn.this, "Ruh! Unable to connect ot database", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });

    }

    private void checkRegisteredUser() {

        db = FirebaseDatabase.getInstance();
        userdb = db.getReference("Student").child("User");
        userdb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(Objects.requireNonNull(FirebaseAuth.getInstance().getUid())))
                {
                    userdb.child(FirebaseAuth.getInstance().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.hasChild("name"))
                                openMainActivity();
                            else
                                openRegisterActivity();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toast.makeText(SignIn.this, "Ruh! Unable to connect to database", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else
                    userdb.child(FirebaseAuth.getInstance().getUid()).child("phone").setValue(phoneNumber).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                                openRegisterActivity();
                            else
                                Toast.makeText(SignIn.this, "Ruh! Unable to connect to database", Toast.LENGTH_SHORT).show();
                        }
                    });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(SignIn.this, "Ruh! Unable to connect to database", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openRegisterActivity() {
        Intent mainActivityIntent = new Intent(SignIn.this, Register.class);
        startActivity(mainActivityIntent);
        finish();
    }

    private void openMainActivity() {

        Intent mainActivityIntent = new Intent(SignIn.this, MainActivity.class);
        startActivity(mainActivityIntent);
        finish();
    }

    private void verifyOTPDialog() {

        OTPdialog = new Dialog(this);
        OTPdialog.setContentView(R.layout.activity_otp_verification);
        OTPdialog.setTitle("Verify OTP");
        enterOTPlayout = OTPdialog.findViewById(R.id.enter_otp_view);
        autoOTPlayout = OTPdialog.findViewById(R.id.auto_verify_view);
        otpTextInput = OTPdialog.findViewById(R.id.otp_text_input);
        verifyOTPbtn = OTPdialog.findViewById(R.id.verify_otp_btn);
        changeNumberBtn = OTPdialog.findViewById(R.id.change_number_btn);
        timer = OTPdialog.findViewById(R.id.otp_timer);
        autoOTPlayout.setVisibility(View.VISIBLE);
        enterOTPlayout.setVisibility(View.GONE);

        verifyOTPbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                code = otpTextInput.getText().toString();
                PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(verificationID,code);
                authUser(phoneAuthCredential);
            }
        });


        changeNumberBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OTPdialog.cancel();
            }
        });

        OTPdialog.setCancelable(false);
        OTPdialog.show();

        new CountDownTimer(60000,1000)
        {
            @Override
            public void onTick(long millisUntilFinished) {
                timer.setText(Integer.toString((int) (millisUntilFinished/1000)));
            }

            @Override
            public void onFinish() {
                if(!OTPverified)
                {
                    Toast.makeText(SignIn.this, "It took longer than usual", Toast.LENGTH_LONG).show();
                    autoOTPlayout.setVisibility(View.GONE);
                    enterOTPlayout.setVisibility(View.VISIBLE);
                }

            }
        }.start();
    }




    private void verifyPhone()

    {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,
                60,
                TimeUnit.SECONDS,
                this,
                phoneCallBacks);

    }


    //Sets country code to the phone text
    private void set_country_code(TextInputEditText phoneText) {
        if (!new helper.check().countryCodeExisting(phoneText)) {


            TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            String countryCodeValue = tm.getNetworkCountryIso();

            if (phoneText.getText().length() <= 0)
                phoneText.setText("+" +(new countryCode().getDialCode(countryCodeValue)) + " ");
            else if (phoneText.getText().charAt(0) == '+')
                phoneText.setText("+" +(new countryCode().getDialCode(countryCodeValue)) + " " + phoneText.getText().toString().substring(1));
            else
                phoneText.setText("+" +(new countryCode().getDialCode(countryCodeValue)) + " " + phoneText.getText());

            int existingValueLength = phoneText.getText().length();
            phoneText.setSelection(existingValueLength);

        }
    }
}
