package com.swa.invitationcard.Events;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.swa.invitationcard.Adapters.EventsAdapter;
import com.swa.invitationcard.Adapters.GuestsAdapter;
import com.swa.invitationcard.Entites.ContactsInfo;
import com.swa.invitationcard.Entites.Event;
import com.swa.invitationcard.databinding.GuestsActivityBinding;

import java.util.ArrayList;
import java.util.List;

public class GuestsActivity extends AppCompatActivity {

    GuestsActivityBinding mBinding;
    List<ContactsInfo> guests;
    GuestsAdapter adapter;
    Event event;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mBinding = GuestsActivityBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        mBinding.backBtn.setOnClickListener( v -> onBackPressed() );

        guests = new ArrayList<>();
        event = (Event) getIntent().getSerializableExtra("event");
        guests = event.getGuestList();

        adapter = new GuestsAdapter(guests);

        updateView(adapter);



        mBinding.selectedContacts.setOnClickListener( v -> {
            Intent i = new Intent(GuestsActivity.this, SelectGuests.class);
            event = (Event) getIntent().getSerializableExtra("event");
            i.putExtra("event", event);
            startActivity(i);
        });



    }

    private void updateView(GuestsAdapter adapter) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(GuestsActivity.this, LinearLayoutManager.VERTICAL , false);
        mBinding.guestView.setLayoutManager(layoutManager);
        mBinding.guestView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}
