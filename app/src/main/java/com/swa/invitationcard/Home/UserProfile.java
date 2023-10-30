package com.swa.invitationcard.Home;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.swa.invitationcard.Entites.User;
import com.swa.invitationcard.Events.EventsActivity;
import com.swa.invitationcard.Helper.AlertPopUps;
import com.swa.invitationcard.Helper.CardSelector;
import com.swa.invitationcard.Helper.UserDataService;
import com.swa.invitationcard.Login.GetPhoneNumber;
import com.swa.invitationcard.Login.UserRegistration;
import com.swa.invitationcard.R;
import com.swa.invitationcard.Reminder.BirthdayReminders;
import com.swa.invitationcard.Reminder.SelectContact;
import com.swa.invitationcard.databinding.UserProfileBinding;

import java.io.ByteArrayOutputStream;
import java.io.File;

public class UserProfile extends AppCompatActivity {

    UserProfileBinding mBinding;
    private Uri mCropImageUri;
    FirebaseAuth mAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    UserDataService userDataService;
    User user;
    CardSelector cardSelector;

    private static final int REQUEST_IMAGE_PICK = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mBinding = UserProfileBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        userDataService = UserDataService.getInstance();
        cardSelector = CardSelector.getInstance();
        user = userDataService.getUser();

        if(user!=null && user.getProfilePic() != null){
            Bitmap bitmap = cardSelector.getImageBitmap(user.getProfilePic());
            mBinding.profileIcon.setImageBitmap(bitmap);
        }

        mBinding.backBtn.setOnClickListener(v -> {
            onBackPressed();
        });

        mBinding.subscribe.setOnClickListener(v -> {
            Intent i = new Intent(UserProfile.this, Subscription.class);
            startActivity(i);
        });

        mBinding.profileIcon.setOnClickListener(v -> {
            openGalleryForImageSelection();
        });

        mBinding.logout.setOnClickListener(v -> {
            logoutPopup(v);
        });

        mBinding.birthdayReminder.setOnClickListener( v -> {
            Intent i = new Intent(UserProfile.this, BirthdayReminders.class);
            startActivity(i);
        });

        mBinding.events.setOnClickListener( v -> {
            Intent i = new Intent(UserProfile.this, EventsActivity.class);
            startActivity(i);
        });
    }

    private void logoutPopup(View v) {
        Activity activity = scanForActivity(UserProfile.this);
        View view = AlertPopUps.showLayoutPopup(R.layout.logout_popup, UserProfile.this, activity);

        final Button cancel = view.findViewById(R.id.cancel);
        final Button logout = view.findViewById(R.id.logout);

        mBinding.profileIcon.setOnClickListener(V -> {
            openGalleryForImageSelection();
        });

        cancel.setOnClickListener(V -> {
            AlertPopUps.dismissPopup();
        });

        logout.setOnClickListener(V -> {
            mAuth.signOut();
            clearApplicationData();
            Intent i = new Intent(UserProfile.this, GetPhoneNumber.class);
            startActivity(i);
            finish();
            finishAffinity();
        });
    }

    private static Activity scanForActivity(Context cont) {
        if (cont == null)
            return null;
        else if (cont instanceof Activity)
            return (Activity) cont;
        else if (cont instanceof ContextWrapper)
            return scanForActivity(((ContextWrapper) cont).getBaseContext());
        return null;
    }

    public void clearApplicationData() {
        File cacheDirectory = getCacheDir();
        File applicationDirectory = new File(cacheDirectory.getParent());
        if (applicationDirectory.exists()) {
            String[] fileNames = applicationDirectory.list();
            for (String fileName : fileNames) {
                if (!fileName.equals("lib")) {
                    deleteFile(new File(applicationDirectory, fileName));
                }
            }
        }
    }

    public static boolean deleteFile(File file) {
        boolean deletedAll = true;
        if (file != null) {
            if (file.isDirectory()) {
                String[] children = file.list();
                for (int i = 0; i < children.length; i++) {
                    deletedAll = deleteFile(new File(file, children[i])) && deletedAll;
                }
            } else {
                deletedAll = file.delete();
            }
        }

        return deletedAll;
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
            mBinding.profileIcon.setImageURI(selectedImageUri);
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] b = baos.toByteArray();
                String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
                user.setProfilePic(encodedImage);
                updateUser(mAuth.getUid(), user);

            }
            catch (Exception e){
                Toast.makeText(UserProfile.this,"Exception : " + e.getMessage(),Toast.LENGTH_SHORT).show();
            }



        }
    }

    private void updateUser(String uid, User user) {

        databaseReference = firebaseDatabase.getReference("Users");
        try{
            databaseReference.child(uid).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Toast.makeText(UserProfile.this, "Uploaded..", Toast.LENGTH_SHORT).show();

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d("Upload", "onFailure: "+e.getMessage());
                    Toast.makeText(UserProfile.this, "Upload Failed!", Toast.LENGTH_SHORT).show();
                }
            });


        } catch (Exception e){
            Log.d("FireBase : ", e.getMessage());
            Toast.makeText(this,"Firebase : " + e.getMessage(),Toast.LENGTH_SHORT).show();
        }

    }
}
