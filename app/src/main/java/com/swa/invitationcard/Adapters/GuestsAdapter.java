package com.swa.invitationcard.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.swa.invitationcard.Entites.ContactsInfo;
import com.swa.invitationcard.R;

import java.util.List;

public class GuestsAdapter extends RecyclerView.Adapter<GuestsAdapter.viewHolder>{

    List<ContactsInfo> contacts;
    Context context;
    boolean eventIntent;

    public GuestsAdapter(List<ContactsInfo> list){
        this.contacts = list;
    }

    @NonNull
    @Override
    public GuestsAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.guest_invite_card, parent, false);
        return new GuestsAdapter.viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GuestsAdapter.viewHolder holder, int position) {

        ContactsInfo contact = contacts.get(position);

        holder.contactIcon.setText(extractAndCapitalizeFirstLetters(contact.getDisplayName()));
        holder.contactName.setText(capitalizeFirstLetter(contact.getDisplayName()));
        holder.contactPhoneNumber.setText(contact.getPhoneNumber());





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