package com.swa.invitationcard.Home;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.swa.invitationcard.databinding.SubcriptionBinding;
import com.swa.invitationcard.databinding.UserProfileBinding;

public class Subscription extends AppCompatActivity {

    SubcriptionBinding mBinding;
    int amount;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mBinding = SubcriptionBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        amount = 99;



        mBinding.backBtn.setOnClickListener(v -> {
            onBackPressed();
        });



    }

    @Override
    protected void onStart() {
        super.onStart();

        mBinding.planSwitch.setOnClickListener( v -> {
            if(! mBinding.planSwitch.isChecked()) {
                amount = 999;
                mBinding.planValue.setText("₹" + String.valueOf(amount));
                mBinding.planSwitch.setText("Yearly Payment");
                mBinding.planName.setText("Business Plan");
            }
            else{
                amount = 99;
                mBinding.planValue.setText("₹" + String.valueOf(amount));
                mBinding.planSwitch.setText("Monthly Payment");
                mBinding.planName.setText("Basic Plan");
            }
        });



    }
}
