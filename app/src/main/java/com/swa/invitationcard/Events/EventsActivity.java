package com.swa.invitationcard.Events;

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
import com.swa.invitationcard.Adapters.EventsAdapter;
import com.swa.invitationcard.Entites.ContactsInfo;
import com.swa.invitationcard.Entites.Event;
import com.swa.invitationcard.Helper.CardSelector;
import com.swa.invitationcard.Reminder.BirthdayReminders;
import com.swa.invitationcard.Reminder.SelectContact;
import com.swa.invitationcard.databinding.EventsActivityBinding;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class EventsActivity extends AppCompatActivity {

    EventsActivityBinding mBinding;
    FirebaseAuth mAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    CardSelector cardSelector;
    List<Event> eventList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mBinding = EventsActivityBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        cardSelector = CardSelector.getInstance();
        eventList = new ArrayList<>();

        mBinding.backBtn.setOnClickListener( v -> onBackPressed());

        getEventsList(mAuth.getUid());

        mBinding.calendar.setOnClickListener( v -> {
            handleDateButton();
        });

        mBinding.createEvent.setOnClickListener( v -> {
            Intent i = new Intent(EventsActivity.this, SelectContact.class);
            i.putExtra("event", true);
            startActivity(i);
        });

    }

    private void getEventsList(String uid) {
        databaseReference = firebaseDatabase.getReference("Events").child(uid);
        try{
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        eventList.clear();
                        for(DataSnapshot item : snapshot.getChildren()){
                            Event event = item.getValue(Event.class);
                            eventList.add(event);
                        }
                        mBinding.noEvents.setVisibility(View.GONE);
                        LinearLayoutManager layoutManager = new LinearLayoutManager(EventsActivity.this, LinearLayoutManager.VERTICAL , false);
                        EventsAdapter adapter = new EventsAdapter(eventList);
                        mBinding.eventsView.setLayoutManager(layoutManager);
                        mBinding.eventsView.setAdapter(adapter);
                        mBinding.eventsView.setVisibility(View.VISIBLE);
                    }
                    else{
                        mBinding.noEvents.setVisibility(View.VISIBLE);
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

    private void handleDateButton(){
        Calendar calendar = Calendar.getInstance();
        int Year = calendar.get(Calendar.YEAR);
        int Month = calendar.get(Calendar.MONTH);
        int Date = calendar.get(Calendar.DATE);

        DatePickerDialog datePickerDialog = new DatePickerDialog(EventsActivity.this, new DatePickerDialog.OnDateSetListener() {
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
                Intent i = new Intent(EventsActivity.this, SelectContact.class);
                i.putExtra("event", true);
                startActivity(i);
            }
        } , Year , Month , Date);
        datePickerDialog.getDatePicker();
        datePickerDialog.show();

    }


}
