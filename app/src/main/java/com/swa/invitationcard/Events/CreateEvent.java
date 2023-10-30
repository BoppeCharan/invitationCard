package com.swa.invitationcard.Events;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.RadioButton;
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
import com.swa.invitationcard.Entites.Event;
import com.swa.invitationcard.Helper.CardSelector;
import com.swa.invitationcard.Helper.ValidationIndicators;
import com.swa.invitationcard.Reminder.BirthdayReminderForm;
import com.swa.invitationcard.Reminder.BirthdayReminders;
import com.swa.invitationcard.databinding.CreateEventBinding;

import java.util.Calendar;
import java.util.Random;

public class CreateEvent extends AppCompatActivity {

    CreateEventBinding mBinding;
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
        mBinding = CreateEventBinding.inflate(getLayoutInflater());
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
                mBinding.eventDate.setText(selectedContact.getBirthday());
            }
        }

        mBinding.eventDate.setOnClickListener( v -> handleDateButton());

        mBinding.createEvent.setOnClickListener( v -> {
            if(CheckAllFields()){
                String name = mBinding.nameRes.getText().toString();
                String eventDate = mBinding.eventDate.getText().toString();
                int selectedRadioButtonId =  mBinding.radioGroup.getCheckedRadioButtonId();
                RadioButton selectedRadioButton = findViewById(selectedRadioButtonId);
                String eventType = selectedRadioButton.getText().toString();
                selectedContact.setDisplayName(name);

                Event event = new Event(selectedContact, eventDate, eventType);
                addEvent(event);
            }
        });
    }

    private void handleDateButton(){
        Calendar calendar = Calendar.getInstance();
        int Year = calendar.get(Calendar.YEAR);
        int Month = calendar.get(Calendar.MONTH);
        int Date = calendar.get(Calendar.DATE);

        DatePickerDialog datePickerDialog = new DatePickerDialog(CreateEvent.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int date) {

                Calendar calendar1 = Calendar.getInstance();
                calendar1.set(Calendar.YEAR , year);
                calendar1.set(Calendar.MONTH, month);
                calendar1.set(Calendar.DATE , date);

                CharSequence dateCharSequence = DateFormat.format("E, MMM dd yyyy" , calendar1);
                mBinding.eventDate.setText(dateCharSequence.toString());
            }
        } , Year , Month , Date);
        datePickerDialog.getDatePicker();
        datePickerDialog.show();

    }

    private void addEvent(Event event) {
        String uid = mAuth.getUid();
        databaseReference = firebaseDatabase.getReference("Events");
        try{
            String id = selectedContact.getContactId() + " -" + generateRandomNumberWithMinDigits();
            event.setId(id);
            databaseReference.child(uid).child(id).setValue(event).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    cardSelector.setContactsInfo(null);
                    Toast.makeText(CreateEvent.this, "Created Event Successfully", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(CreateEvent.this, EventsActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(CreateEvent.this, "Failed to Create Event", Toast.LENGTH_SHORT).show();
                }
            });
        }
        catch (Exception e){
            Log.d("FireBase : ", e.getMessage());
            Toast.makeText(CreateEvent.this, "Failed to Create Event", Toast.LENGTH_SHORT).show();
        }

    }

    private boolean CheckAllFields() {
        boolean error = true;
        if (mBinding.nameRes.length() == 0) {
            ValidationIndicators.showErrorIndicatorAfterBtnPressed(mBinding.nameRes, "Required", mBinding.nameIL);
            error = false;
        }

        if(mBinding.eventDate.getText().length() == 0){
            Toast.makeText(CreateEvent.this, "Date Should be Provided!", Toast.LENGTH_SHORT).show();
            error = false;
        }

        return error;
    }

    public String generateRandomNumberWithMinDigits() {
        Random random = new Random();
        long min = 1000; // Minimum 4-digit number
        long max = 999999999; // Maximum 9-digit number
        long randomNumber = min + (long) (random.nextDouble() * (max - min + 1));
        return Long.toString(randomNumber);
    }
}
