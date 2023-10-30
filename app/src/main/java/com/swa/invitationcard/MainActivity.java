package com.swa.invitationcard;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.GridView;

import com.google.firebase.auth.FirebaseAuth;
import com.swa.invitationcard.Adapters.WeddingCardAdapter;
import com.swa.invitationcard.Entites.Card;
import com.swa.invitationcard.Helper.UserDataService;
import com.swa.invitationcard.Home.HomeActivity;
import com.swa.invitationcard.Login.GetPhoneNumber;
import com.swa.invitationcard.Login.LoginManager;
import com.swa.invitationcard.databinding.ActivityMainBinding;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding mBinding;
    LoginManager loginManager;
    FirebaseAuth mAuth;
    UserDataService userDataService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        mAuth = FirebaseAuth.getInstance();
        userDataService = UserDataService.getInstance();



        if(mAuth.getCurrentUser() != null){
            userDataService.load(mAuth.getUid());
            Intent i = new Intent(MainActivity.this, HomeActivity.class);
            startActivity(i);
            finish();
            finishAffinity();
        }
        else{
            startActivity(new Intent(this, GetPhoneNumber.class));
            finish();
        }

    }
}