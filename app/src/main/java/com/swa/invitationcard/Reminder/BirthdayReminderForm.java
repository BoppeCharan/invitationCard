package com.swa.invitationcard.Reminder;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
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
import com.swa.invitationcard.Entites.ContactsInfo;
import com.swa.invitationcard.Helper.CardSelector;
import com.swa.invitationcard.Helper.ValidationIndicators;
import com.swa.invitationcard.Home.HomeActivity;
import com.swa.invitationcard.Home.UserProfile;
import com.swa.invitationcard.databinding.SetBirthdayReminderBinding;

import java.util.Calendar;

public class BirthdayReminderForm extends AppCompatActivity {

    SetBirthdayReminderBinding mBinding;
    CardSelector cardSelector;
    ContactsInfo selectedContact;
    FirebaseAuth mAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mBinding = SetBirthdayReminderBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        cardSelector = CardSelector.getInstance();
        selectedContact = cardSelector.getContactsInfo();
        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        mBinding.backBtn.setOnClickListener( v -> onBackPressed() );

        if(selectedContact != null){
            mBinding.nameRes.setText(selectedContact.getDisplayName());
            mBinding.phoneNumberRes.setText(selectedContact.getPhoneNumber());
            mBinding.phoneNumberRes.setFocusable(false);
            if(selectedContact.getBirthday() != null){
                mBinding.birthdayDate.setText(selectedContact.getBirthday());
            }
        }

        mBinding.birthdayDate.setOnClickListener( v -> {
            handleDateButton();
        });

        mBinding.setReminder.setOnClickListener(v -> {
            if(CheckAllFields()){
                String name = mBinding.nameRes.getText().toString();
                String birthday = mBinding.birthdayDate.getText().toString();
                selectedContact.setDisplayName(name);
                selectedContact.setBirthday(birthday);
                addReminder(selectedContact);
            }
        });
    }

    private void addReminder(ContactsInfo selectedContact) {
        String uid = mAuth.getUid();
        databaseReference = firebaseDatabase.getReference("Reminders");
        try{
          databaseReference.child(uid).child(selectedContact.getContactId()).setValue(selectedContact).addOnCompleteListener(new OnCompleteListener<Void>() {
              @Override
              public void onComplete(@NonNull Task<Void> task) {
                  cardSelector.setContactsInfo(null);
                  Toast.makeText(BirthdayReminderForm.this, "Added Reminder", Toast.LENGTH_SHORT).show();
                  Intent i = new Intent(BirthdayReminderForm.this, BirthdayReminders.class);
                  i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                  startActivity(i);
              }
          }).addOnFailureListener(new OnFailureListener() {
              @Override
              public void onFailure(@NonNull Exception e) {
                  Toast.makeText(BirthdayReminderForm.this, "Reminder Failed to Add!", Toast.LENGTH_SHORT).show();
              }
          });
        }
        catch (Exception e){
            Log.d("FireBase : ", e.getMessage());
            Toast.makeText(BirthdayReminderForm.this, "Reminder Failed to Add!", Toast.LENGTH_SHORT).show();
        }


    }

    private void handleDateButton(){
        Calendar calendar = Calendar.getInstance();
        int Year = calendar.get(Calendar.YEAR);
        int Month = calendar.get(Calendar.MONTH);
        int Date = calendar.get(Calendar.DATE);

        DatePickerDialog datePickerDialog = new DatePickerDialog(BirthdayReminderForm.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int date) {

                Calendar calendar1 = Calendar.getInstance();
                calendar1.set(Calendar.YEAR , year);
                calendar1.set(Calendar.MONTH, month);
                calendar1.set(Calendar.DATE , date);

                CharSequence dateCharSequence = DateFormat.format("E, MMM dd yyyy" , calendar1);
                mBinding.birthdayDate.setText(dateCharSequence.toString());
            }
        } , Year , Month , Date);
        datePickerDialog.getDatePicker().setMaxDate(Calendar.getInstance().getTime().getTime());
        datePickerDialog.show();

    }


    private boolean CheckAllFields() {
        boolean error = true;
        if (mBinding.nameRes.length() == 0) {
            ValidationIndicators.showErrorIndicatorAfterBtnPressed(mBinding.nameRes, "Required", mBinding.nameIL);
            error = false;
        }

        if(mBinding.birthdayDate.getText().length() == 0){
            Toast.makeText(BirthdayReminderForm.this, "Date Should be Provided!", Toast.LENGTH_SHORT).show();
            error = false;
        }

        return error;
    }

}
