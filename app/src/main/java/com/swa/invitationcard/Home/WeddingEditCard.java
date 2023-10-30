package com.swa.invitationcard.Home;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.swa.invitationcard.Entites.Birthday;
import com.swa.invitationcard.Entites.Wedding;
import com.swa.invitationcard.Helper.CardSelector;
import com.swa.invitationcard.Helper.ValidationIndicators;
import com.swa.invitationcard.R;
import com.swa.invitationcard.databinding.WeddingEditCardBinding;

public class WeddingEditCard extends AppCompatActivity {

    WeddingEditCardBinding mBinding;
    CardSelector cardSelector;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mBinding = WeddingEditCardBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        Bundle extras = getIntent().getExtras();
        int background = (int) extras.get("background");

        if(savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.edit_frame, new ExamplePreferenceFragment())
                    .commit();
        }
        cardSelector = CardSelector.getInstance();

        if(cardSelector.getWeddingDetails() != null){
            Wedding wedding = cardSelector.getWeddingDetails();
            mBinding.groomNameRes.setText(wedding.getGroom());
            mBinding.brideNameRes.setText(wedding.getBride());
            mBinding.dateRes.setText(wedding.getDate());
            mBinding.timeRes.setText(wedding.getTime());
            mBinding.venueRes.setText(wedding.getVenue());
            mBinding.rsvpRes.setText(wedding.getRsvp());
            mBinding.emailRes.setText(wedding.getEmail());
            mBinding.phoneRes.setText(wedding.getPhone());
        }

        mBinding.submit.setOnClickListener( v -> {
//            if(CheckAllFields()){
                String groomName = mBinding.groomNameRes.getText().toString();
                String brideName = mBinding.brideNameRes.getText().toString();
                String date = mBinding.dateRes.getText().toString();
                String time = mBinding.timeRes.getText().toString();
                String venue = mBinding.venueRes.getText().toString();
                String email = mBinding.emailRes.getText().toString();
                String phone = mBinding.phoneRes.getText().toString();

                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
                int color = prefs.getInt("text_color", R.color.text_color);

                String rsvp = mBinding.rsvpRes.getText().toString().trim().length() > 2 ? mBinding.rsvpRes.getText().toString() : null;

                CardSelector selector = CardSelector.getInstance();

                Wedding wedding = new Wedding(groomName, brideName, date, time, venue, rsvp, email, phone, background, color);
                selector.setWeddingDetails(wedding);

                Intent i = new Intent(WeddingEditCard.this, WeddingDetails.class);
                i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);

//                Toast.makeText(this, wedding.toString(), Toast.LENGTH_SHORT).show();
//            }
        });


        mBinding.backBtn.setOnClickListener( v -> {
            onBackPressed();
        });




    }



    private boolean CheckAllFields() {
        boolean error = true;
        if (mBinding.groomNameRes.length() == 0) {
            ValidationIndicators.showErrorIndicatorAfterBtnPressed(mBinding.groomNameRes, "Required", mBinding.groomNameIL);
            error = false;
        }
        if (mBinding.brideNameRes.length() == 0) {
            ValidationIndicators.showErrorIndicatorAfterBtnPressed(mBinding.brideNameRes, "Required", mBinding.brideNameIL);
            error = false;
        }
        if(mBinding.phoneRes.length() == 0 || mBinding.phoneRes.length() <10){
            ValidationIndicators.showErrorIndicatorAfterBtnPressed(mBinding.phoneRes, "Required", mBinding.phoneIL);
            error = false;
        }
        if (mBinding.dateRes.length() == 0) {
            ValidationIndicators.showErrorIndicatorAfterBtnPressed(mBinding.dateRes, "Required", mBinding.dateIL);
            error = false;
        }
        if (mBinding.timeRes.length() == 0) {
            ValidationIndicators.showErrorIndicatorAfterBtnPressed(mBinding.timeRes, "Required", mBinding.timeIL);
            error = false;
        }
        if (mBinding.venueRes.length() == 0) {
            ValidationIndicators.showErrorIndicatorAfterBtnPressed(mBinding.venueRes, "Required", mBinding.venueIL);
            error = false;
        }
        if (mBinding.emailRes.length() == 0) {
            ValidationIndicators.showErrorIndicatorAfterBtnPressed(mBinding.emailRes, "Required", mBinding.emailIL);
            error = false;
        }



        return error;
    }

    public static class ExamplePreferenceFragment extends PreferenceFragment {

        @Override public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.main);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = super.onCreateView(inflater, container, savedInstanceState);
            view.setBackgroundColor(getResources().getColor(R.color.shade_color));
            return view;
        }

    }
}
