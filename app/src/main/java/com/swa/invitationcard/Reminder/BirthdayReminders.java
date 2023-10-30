package com.swa.invitationcard.Reminder;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.swa.invitationcard.Adapters.BirthdaysAdapter;
import com.swa.invitationcard.Entites.ContactsInfo;
import com.swa.invitationcard.Helper.CardSelector;
import com.swa.invitationcard.databinding.BirthdayRemindersBinding;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class BirthdayReminders extends AppCompatActivity {

    BirthdayRemindersBinding mBinding;
    FirebaseAuth mAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    List<ContactsInfo> birthdayList;
    CardSelector cardSelector;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mBinding = BirthdayRemindersBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        cardSelector = CardSelector.getInstance();
        birthdayList = new ArrayList<>();

        mBinding.addReminder.setOnClickListener( v -> {
            Intent i = new Intent(BirthdayReminders.this, SelectContact.class);
            startActivity(i);
        });

        mBinding.backBtn.setOnClickListener( v -> onBackPressed());

        getBirthdayList(mAuth.getUid());

        mBinding.calendar.setOnClickListener( v -> {
            handleDateButton();
        });

    }

    private void handleDateButton(){
        Calendar calendar = Calendar.getInstance();
        int Year = calendar.get(Calendar.YEAR);
        int Month = calendar.get(Calendar.MONTH);
        int Date = calendar.get(Calendar.DATE);

        DatePickerDialog datePickerDialog = new DatePickerDialog(BirthdayReminders.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int date) {

                Calendar calendar1 = Calendar.getInstance();
                calendar1.set(Calendar.YEAR , year);
                calendar1.set(Calendar.MONTH, month);
                calendar1.set(Calendar.DATE , date);

                CharSequence dateCharSequence = DateFormat.format("E, MMM dd yyyy" , calendar1);
                ContactsInfo contactsInfo = new ContactsInfo();
                contactsInfo.setBirthday(dateCharSequence.toString());
                cardSelector.setContactsInfo(contactsInfo);
                Intent i = new Intent(BirthdayReminders.this, SelectContact.class);
                startActivity(i);
            }
        } , Year , Month , Date);
        datePickerDialog.getDatePicker().setMaxDate(Calendar.getInstance().getTime().getTime());
        datePickerDialog.show();

    }

    private void getBirthdayList(String uid) {
        databaseReference = firebaseDatabase.getReference("Reminders").child(uid);
        try{
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        for(DataSnapshot item : snapshot.getChildren()){
                            ContactsInfo contact = item.getValue(ContactsInfo.class);
                            birthdayList.add(contact);
                        }
                        mBinding.noReminders.setVisibility(View.GONE);
                        LinearLayoutManager layoutManager = new LinearLayoutManager(BirthdayReminders.this, LinearLayoutManager.VERTICAL , false);
                        BirthdaysAdapter adapter = new BirthdaysAdapter(birthdayList);
                        mBinding.birthdayView.setLayoutManager(layoutManager);
                        mBinding.birthdayView.setAdapter(adapter);
                        mBinding.birthdayView.setVisibility(View.VISIBLE);
                    }
                    else{
                        mBinding.noReminders.setVisibility(View.VISIBLE);
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        catch (Exception e){

        }
    }
}
