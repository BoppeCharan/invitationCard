package com.swa.invitationcard.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.swa.invitationcard.Entites.Event;
import com.swa.invitationcard.Events.EventsActivity;
import com.swa.invitationcard.Events.GuestsActivity;
import com.swa.invitationcard.Events.SelectGuests;
import com.swa.invitationcard.Events.TodoActivity;
import com.swa.invitationcard.Helper.CardSelector;
import com.swa.invitationcard.R;

import java.util.List;

public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.viewHolder> {

    List<Event> eventList;
    Context context;
    CardSelector cardSelector;

    public EventsAdapter(List<Event> list) {
        this.eventList = list;
        cardSelector = CardSelector.getInstance();
    }

    @NonNull
    @Override
    public EventsAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.event_card, parent, false);
        return new EventsAdapter.viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventsAdapter.viewHolder holder, int position) {

        Event event = eventList.get(position);

        holder.letterCircle.setText(extractAndCapitalizeFirstLetters(event.getHost().getDisplayName()));
        holder.contactName.setText(capitalizeFirstLetter(event.getHost().getDisplayName()));
        holder.date.setText(event.getDate());
        holder.eventType.setText(event.getEventType());
        holder.menu.setOnClickListener( v -> showPopupMenu(v, event));
    }

    private void showPopupMenu(View view, Event event) {
        PopupMenu popupMenu = new PopupMenu(context, view);
        popupMenu.inflate(R.menu.menu_card_options);

        popupMenu.setOnMenuItemClickListener(item -> {
            Intent i;
            switch (item.getItemId()) {
                case R.id.action_create_guest_list:
                    i = new Intent(context, GuestsActivity.class);
                    i.putExtra("event", event);
                    context.startActivity(i);
                    // Handle "Create Guest List" action
                    return true;
                case R.id.action_things_to_buy:
                    i = new Intent(context, TodoActivity.class);
                    i.putExtra("event", event);
                    context.startActivity(i);
                    return true;
                case R.id.action_rsvp_list:
                    // Handle "RSVP List" action
                    return true;
                case R.id.action_share_with_cohost:
                    // Handle "Share with Cohost" action
                    return true;
                default:
                    return false;
            }
        });

        popupMenu.show();
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
        return this.eventList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {

        TextView letterCircle, contactName, date, eventType;
        ImageView menu;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            letterCircle = itemView.findViewById(R.id.letterCircle);
            contactName = itemView.findViewById(R.id.nameText);
            date = itemView.findViewById(R.id.dateText);
            eventType = itemView.findViewById(R.id.eventTypeText);
            menu = itemView.findViewById(R.id.eventMenu);

        }
    }
}