package com.swa.invitationcard.Home;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.swa.invitationcard.Entites.Birthday;
import com.swa.invitationcard.Entites.Wedding;
import com.swa.invitationcard.Helper.CardSelector;
import com.swa.invitationcard.Helper.ValidationIndicators;
import com.swa.invitationcard.R;
import com.swa.invitationcard.databinding.BirthdayEditCardBinding;

import java.io.ByteArrayOutputStream;

public class BirthdayEditCard extends AppCompatActivity {

    BirthdayEditCardBinding mBinding;
    private static final int REQUEST_IMAGE_PICK = 1;
    String encodedImage = null;
    CardSelector cardSelector;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mBinding = BirthdayEditCardBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        Bundle extras = getIntent().getExtras();
        cardSelector = CardSelector.getInstance();
        int background = (int) extras.get("background");

        if(savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.edit_frame, new WeddingEditCard.ExamplePreferenceFragment())
                    .commit();
        }

        if(cardSelector.getBirthdayCard() != null){
            Birthday birthday = cardSelector.getBirthdayCard();
            encodedImage = birthday.getImage();
            mBinding.profileIcon.setImageBitmap(cardSelector.getImageBitmap(birthday.getImage()));
            mBinding.nameRes.setText(birthday.getName());
            mBinding.dateRes.setText(birthday.getDate());
            mBinding.timeRes.setText(birthday.getTime());
            mBinding.venueRes.setText(birthday.getVenue());
            mBinding.rsvpRes.setText(birthday.getRsvp());
            mBinding.emailRes.setText(birthday.getEmail());
            mBinding.phoneRes.setText(birthday.getPhone());
        }

        mBinding.submit.setOnClickListener( v -> {
//            if(CheckAllFields()){
            String name = mBinding.nameRes.getText().toString();
            String date = mBinding.dateRes.getText().toString();
            String time = mBinding.timeRes.getText().toString();
            String venue = mBinding.venueRes.getText().toString();
            String email = mBinding.emailRes.getText().toString();
            String phone = mBinding.phoneRes.getText().toString();

            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
            int color = prefs.getInt("text_color", R.color.text_color);

            String rsvp = mBinding.rsvpRes.getText().toString().trim().length() > 2 ? mBinding.rsvpRes.getText().toString() : null;

            CardSelector selector = CardSelector.getInstance();

            Birthday birthday = new Birthday(name, date, time, venue, rsvp, email, phone, background, color, encodedImage);
            selector.setBirthdayCard(birthday);

            Intent i = new Intent(BirthdayEditCard.this, BirthdayDetails.class);
            i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);

//                Toast.makeText(this, wedding.toString(), Toast.LENGTH_SHORT).show();
//            }
        });


        mBinding.backBtn.setOnClickListener( v -> {
            onBackPressed();
        });


        mBinding.profileIcon.setOnClickListener(V -> {
            openGalleryForImageSelection();
        });



    }


    private void openGalleryForImageSelection() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_IMAGE_PICK);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_PICK && resultCode == RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] b = baos.toByteArray();
                encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
                mBinding.profileIcon.setImageURI(selectedImageUri);
            }
            catch (Exception e){

            }
        }
    }



    private boolean CheckAllFields() {
        boolean error = true;
        if (mBinding.nameRes.length() == 0) {
            ValidationIndicators.showErrorIndicatorAfterBtnPressed(mBinding.nameRes, "Required", mBinding.nameIL);
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