package com.swa.invitationcard.Home;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;
import com.swa.invitationcard.Entites.Wedding;
import com.swa.invitationcard.Helper.CardSelector;
import com.swa.invitationcard.R;
import com.swa.invitationcard.databinding.WeddingTemplateBinding;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

public class WeddingDetails extends AppCompatActivity implements PaymentResultListener {

    WeddingTemplateBinding mBinding;
    CardSelector selector;
    FirebaseAuth mAuth;
    String planAmount = "51";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // Prevent screen capture
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE
        );
        mBinding = WeddingTemplateBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        selector = CardSelector.getInstance();
        mAuth = FirebaseAuth.getInstance();


    }

    @Override
    protected void onStart() {
        super.onStart();


        Wedding details = selector.getWeddingDetails();

        setTextColor(details.getColor());

        mBinding.groom.setText(details.getGroom());
        mBinding.date.setText("Date : " + details.getDate());
        mBinding.time.setText("Time : " + details.getTime());
        mBinding.venue.setText("Venue : " + details.getVenue());
        mBinding.email.setText("Email : " + details.getEmail());
        mBinding.phone.setText("Phone : " + details.getPhone());
        if(details.getRsvp() != null){
            mBinding.rsvp.setText("Please RSVP by "+ details.getRsvp());
        }
        else{
            mBinding.rsvp.setVisibility(View.GONE);
        }

        mBinding.cardBackground.setBackgroundResource(details.getBackground());


        mBinding.downloadCard.setOnClickListener( v -> doPayment());




    }

    private void setTextColor(int color) {
        mBinding.text1.setTextColor(color);
        mBinding.text2.setTextColor(color);
        mBinding.text3.setTextColor(color);
        mBinding.groom.setTextColor(color);
        mBinding.and.setTextColor(color);
        mBinding.bride.setTextColor(color);
        mBinding.date.setTextColor(color);
        mBinding.time.setTextColor(color);
        mBinding.venue.setTextColor(color);
        mBinding.email.setTextColor(color);
        mBinding.phone.setTextColor(color);
        mBinding.rsvp.setTextColor(color);

    }


    private Bitmap viewToBitmap(View view) {
        int width = view.getWidth();
        int height = view.getHeight();
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }

    // Function to save a bitmap as an image file
    private void saveBitmapToStorage(Bitmap bitmap) {
        File directory = Environment.getExternalStorageDirectory(); // Get the external storage directory
        File imageFile = new File(directory, "Wedding_" + getRandomNumber() +".png");

        try {
            FileOutputStream fos = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos); // Save the bitmap as a PNG image
            Toast.makeText(this, "Downloaded..", Toast.LENGTH_SHORT);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private String getRandomNumber(){
        Random random = new Random();
        int min = 1;
        int max = 1000;

        // Generate a random number between 1 and 1000
        int uniqueNumber = random.nextInt(max - min + 1) + min;

        // Convert the number to a string
        return  String.valueOf(uniqueNumber);
    }


    private void downloadImage() {
        ConstraintLayout layout = mBinding.cardBackground;

        // Capture the layout as a bitmap
        Bitmap bitmap = viewToBitmap(layout);

        // Save the bitmap as an image file
        saveBitmapToStorage(bitmap);


    }

    public void doPayment(){

        long amount = Math.round(Float.parseFloat(planAmount) * 100);


        Checkout checkout = new Checkout();
        checkout.setKeyID("rzp_live_ThrLbsYU5cmP9K");
        checkout.preload(getApplicationContext());
        JSONObject object = new JSONObject();
        try {
            object.put("name", mAuth.getCurrentUser().getDisplayName());
            object.put("description", "Membership Plan");
            object.put("theme.color", "#171717");
            object.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
            object.put("currency", "INR");
            object.put("amount", amount);
            object.put("prefill.contact", mAuth.getCurrentUser().getPhoneNumber());
            object.put("prefill.email", mAuth.getCurrentUser().getPhoneNumber());
            checkout.open(WeddingDetails.this, object);
        }
        catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onPaymentSuccess(String s) {
        downloadImage();
        Toast.makeText(WeddingDetails.this, "Payment Success!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPaymentError(int i, String s) {
        Toast.makeText(WeddingDetails.this, "Payment Failed!", Toast.LENGTH_SHORT).show();
    }



}
