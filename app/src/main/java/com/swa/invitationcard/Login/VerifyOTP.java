package com.swa.invitationcard.Login;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.goodiebag.pinview.Pinview;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.swa.invitationcard.databinding.VerifyOtpBinding;

import java.util.concurrent.TimeUnit;

public class VerifyOTP extends AppCompatActivity {

    VerifyOtpBinding mBinding;
    FirebaseAuth mAuth;
    DatabaseReference databaseReference;
    LoginManager loginManager;
    private SmsBroadcastReceiver smsReceiver;
    String otp;
    FirebaseDatabase database;
    private String verificationId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mBinding = VerifyOtpBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        String code = (String)getIntent().getStringExtra("otp");
        verificationId = (String)getIntent().getStringExtra("verificationId");
        String phoneNumber = getIntent().getStringExtra("PhoneNo");
        mBinding.pinView.setTextColor(Color.WHITE);
        mBinding.pinView.setTextSize(20);
        mBinding.pinView.setInputType(Pinview.InputType.NUMBER);

        String numberText = "OTP Sent to " + phoneNumber;

        mBinding.number.setText(numberText);

        mBinding.verifyOTPBtn.setOnClickListener(v -> {
            verifyCode(mBinding.pinView.getValue(), phoneNumber);
        });

        mBinding.resendOtp.setOnClickListener(v -> {
            sendVerificationCode(phoneNumber);
        });


    }

    private void signInWithCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            String phoneNumber = getIntent().getStringExtra("PhoneNo");
                            Toast.makeText(VerifyOTP.this, "User Verification Complete", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(VerifyOTP.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
            mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationId = s;
        }
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            final String code = phoneAuthCredential.getSmsCode();
            if (code != null) {
                mBinding.pinView.setValue(code);
                String phoneNumber = (String)getIntent().getStringExtra("PhoneNo");
                verifyCode(mBinding.pinView.getValue(), phoneNumber);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(VerifyOTP.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    };

    private void sendVerificationCode(String number) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(number)            // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallBack)           // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void verifyCode(String code, String phoneNumber) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithCredential(credential);
    }
}
