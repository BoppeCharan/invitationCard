package com.swa.invitationcard.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.swa.invitationcard.Entites.ContactsInfo;
import com.swa.invitationcard.Events.CreateEvent;
import com.swa.invitationcard.Helper.CardSelector;
import com.swa.invitationcard.R;
import com.swa.invitationcard.Reminder.BirthdayReminderForm;

import java.util.List;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.viewHolder>{

    List<ContactsInfo> contacts;
    Context context;
    CardSelector cardSelector;
    boolean eventIntent;

    public ContactsAdapter(List<ContactsInfo> list, boolean bool){
        cardSelector = CardSelector.getInstance();
        this.eventIntent = bool;
        this.contacts = list;
    }

    @NonNull
    @Override
    public ContactsAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.contact_card, parent, false);
        return new ContactsAdapter.viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactsAdapter.viewHolder holder, int position) {

        ContactsInfo contact = contacts.get(position);

        holder.contactIcon.setText(extractAndCapitalizeFirstLetters(contact.getDisplayName()));
        holder.contactName.setText(capitalizeFirstLetter(contact.getDisplayName()));
        holder.contactPhoneNumber.setText(contact.getPhoneNumber());

        if(cardSelector.getContactsInfo() != null){
            contact.setBirthday(cardSelector.getContactsInfo().getBirthday());
        }

        holder.contact.setOnClickListener( v -> {
            if(!eventIntent) {
                cardSelector.setContactsInfo(contact);
                Intent i = new Intent(context, BirthdayReminderForm.class);
                context.startActivity(i);
            }
            else{
                cardSelector.setContactsInfo(contact);
                Intent i = new Intent(context, CreateEvent.class);
                context.startActivity(i);
            }
        });


    }

    public String extractAndCapitalizeFirstLetters(String input) {
        if (input == null || input.isEmpty()) {
            return "";
        }

        StringBuilder result = new StringBuilder();
        int count = 0;
        String[] words = input.split(" ");

        for (String word : words) {
            if (!word.isEmpty()) {
                result.append(String.valueOf(word.charAt(0)).toUpperCase());
                count++;
                if (count >= 2) {
                    break;
                }
            }
        }

        return result.toString();
    }


    public String capitalizeFirstLetter(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        return input.substring(0, 1).toUpperCase() + input.substring(1);
    }



    @Override
    public int getItemCount() {
        return this.contacts.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder{

        TextView contactIcon, contactName, contactPhoneNumber;
        LinearLayout contact;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            contactIcon = itemView.findViewById(R.id.contactIcon);
            contactName = itemView.findViewById(R.id.contactName);
            contactPhoneNumber = itemView.findViewById(R.id.contactPhoneNumber);
            contact = itemView.findViewById(R.id.contact);

        }
    }

}
