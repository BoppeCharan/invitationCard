package com.swa.invitationcard.Login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.swa.invitationcard.Entites.User;
import com.swa.invitationcard.Helper.UserDataService;
import com.swa.invitationcard.Helper.ValidationIndicators;
import com.swa.invitationcard.Home.HomeActivity;
import com.swa.invitationcard.MainActivity;
import com.swa.invitationcard.databinding.RegistrationActivityBinding;

public class UserRegistration extends AppCompatActivity {

    RegistrationActivityBinding mBinding;
    FirebaseAuth mAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    UserDataService userDataService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mBinding = RegistrationActivityBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        userDataService = UserDataService.getInstance();


        mBinding.registrationContinue.setOnClickListener( v -> {
            if(CheckAllFields()){
                String name = mBinding.nameRes.getText().toString();
                String number = mBinding.phoneNumberRes.getText().toString().trim();
                String email = mAuth.getCurrentUser().getEmail().toString();

                User user = new User(name, number, email, null);
                userDataService.setUser(user);
                databaseReference = firebaseDatabase.getReference("Users");
                try{
                    String uid = mAuth.getUid();
                    databaseReference.child(uid).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {

                                Intent i = new Intent(UserRegistration.this, HomeActivity.class);
                                startActivity(i);
                                finish();
                                finishAffinity();

                            } else {
                                Toast.makeText(UserRegistration.this, "Registration Failed Try again!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("Registration", "onFailure: "+e.getMessage());
                            Toast.makeText(UserRegistration.this, "Registration Failed Try again!", Toast.LENGTH_SHORT).show();
                        }
                    });


                } catch (Exception e){
                    Log.d("RegistrationFireBase : ", e.getMessage());
                    Toast.makeText(this,"Firebase : " + e.getMessage(),Toast.LENGTH_SHORT).show();
                }


            }
        });







    }

    private boolean CheckAllFields() {
        boolean error = true;
        if (mBinding.nameRes.length() == 0) {
            ValidationIndicators.showErrorIndicatorAfterBtnPressed(mBinding.nameRes, "Required", mBinding.nameIL);
            error = false;
        }

        if(mBinding.phoneNumberRes.length() == 0){
            ValidationIndicators.showErrorIndicatorAfterBtnPressed(mBinding.phoneNumberRes, "Required", mBinding.phoneIL);
            error = false;
        }

        return error;
    }
}
