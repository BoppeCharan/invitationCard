package com.swa.invitationcard.Events;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.swa.invitationcard.Adapters.SelectGuestsAdapter;
import com.swa.invitationcard.Entites.ContactsInfo;
import com.swa.invitationcard.Entites.Event;
import com.swa.invitationcard.databinding.SelectGuestsBinding;

import java.util.ArrayList;
import java.util.List;

public class SelectGuests extends AppCompatActivity {

    SelectGuestsBinding mBinding;

    public static final int PERMISSIONS_REQUEST_READ_CONTACTS = 1;
    List<ContactsInfo> contactsInfoList;
    Event event;
    SelectGuestsAdapter adapter;
    FirebaseAuth mAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mBinding = SelectGuestsBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        mBinding.backBtn.setOnClickListener( v -> onBackPressed() );
        requestContactPermission();

        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();




        mBinding.searchContact.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                List<ContactsInfo> queryList = new ArrayList<>();
                String text = editable.toString();
                for(ContactsInfo contact : contactsInfoList){
                    if (contact.getDisplayName().toLowerCase().contains(text.toLowerCase())) {
                        queryList.add(contact);
                    }
                }
                LinearLayoutManager layoutManager = new LinearLayoutManager(SelectGuests.this, LinearLayoutManager.VERTICAL , false);
                event = (Event) getIntent().getSerializableExtra("event");
                adapter = new SelectGuestsAdapter(queryList, event);
                mBinding.contactsView.setLayoutManager(layoutManager);
                mBinding.contactsView.setAdapter(adapter);

            }
        });


        mBinding.selectedContacts.setOnClickListener( v -> {
            List<ContactsInfo> guestList = adapter.getGuestList();
            if(guestList.size() > 0) {
                event = (Event) getIntent().getSerializableExtra("event");
                List<ContactsInfo> guests = event.getGuestList();
                if(guests != null && guests.size() > 0) {
                    for (ContactsInfo contact2 : guestList) {
                        boolean alreadyExists = false;
                        for (ContactsInfo contact1 : guests) {
                            if (contact1.getContactId().equals(contact2.getContactId())) {
                                alreadyExists = true;
                                break;
                            }
                        }
                        if (!alreadyExists) {
                            guests.add(contact2);
                        }
                    }
                }
                else{
                    guests = guestList;
                }
                event.setGuestList(guests);
                updateEvent(event);
                Intent i = new Intent(SelectGuests.this, GuestsActivity.class);
                i.putExtra("event", event);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
            else{
                Toast.makeText(this, "Select Contacts to Proceed!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateEvent(Event event) {
        String uid = mAuth.getUid();
        assert uid != null;
        databaseReference = firebaseDatabase.getReference("Events").child(uid).child(event.getId());
        try{
            databaseReference.setValue(event).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });
        }
        catch (Exception e){
            Log.d("FireBase : ", e.getMessage());
            Toast.makeText(SelectGuests.this, "Failed to Update Event", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    public void requestContactPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        android.Manifest.permission.READ_CONTACTS)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Read contacts access needed");
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.setMessage("Please enable access to contacts.");
                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @TargetApi(Build.VERSION_CODES.M)
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            requestPermissions(
                                    new String[]
                                            {android.Manifest.permission.READ_CONTACTS}
                                    , PERMISSIONS_REQUEST_READ_CONTACTS);
                        }
                    });
                    builder.show();
                } else {
                    ActivityCompat.requestPermissions(this,
                            new String[]{android.Manifest.permission.READ_CONTACTS},
                            PERMISSIONS_REQUEST_READ_CONTACTS);
                }
            } else {
                displayContacts();
            }
        } else {
            displayContacts();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSIONS_REQUEST_READ_CONTACTS: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    displayContacts();
                } else {
                    Toast.makeText(this, "You have disabled a contacts permission", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }


    @SuppressLint("Range")
    private void displayContacts(){
        ContentResolver contentResolver = getContentResolver();
        String contactId = null;
        String displayName = null;
        contactsInfoList = new ArrayList<ContactsInfo>();
        Cursor cursor = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                int hasPhoneNumber = Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)));
                if (hasPhoneNumber > 0) {

                    ContactsInfo contactsInfo = new ContactsInfo();
                    contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                    displayName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

                    contactsInfo.setContactId(contactId);
                    contactsInfo.setDisplayName(displayName);

                    Cursor phoneCursor = getContentResolver().query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{contactId},
                            null);

                    if (phoneCursor.moveToNext()) {
                        String phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                        contactsInfo.setPhoneNumber(phoneNumber);
                    }

                    phoneCursor.close();

                    contactsInfoList.add(contactsInfo);
                }
            }
        }
        cursor.close();
        LinearLayoutManager layoutManager = new LinearLayoutManager(SelectGuests.this, LinearLayoutManager.VERTICAL , false);
        event = (Event) getIntent().getSerializableExtra("event");
        adapter = new SelectGuestsAdapter(contactsInfoList, event);
        mBinding.contactsView.setLayoutManager(layoutManager);
        mBinding.contactsView.setAdapter(adapter);
    }

}