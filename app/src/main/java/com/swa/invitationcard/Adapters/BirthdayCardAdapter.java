package com.swa.invitationcard.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.swa.invitationcard.Entites.Card;
import com.swa.invitationcard.Home.BirthdayEditCard;
import com.swa.invitationcard.Home.WeddingEditCard;
import com.swa.invitationcard.R;

import java.util.List;

public class BirthdayCardAdapter extends RecyclerView.Adapter<BirthdayCardAdapter.viewHolder>{

    List<Card> birthdayCards;
    Context context;

    public BirthdayCardAdapter(List<Card> list){
        this.birthdayCards = list;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.birthday_adapter_card, parent, false);
        return new BirthdayCardAdapter.viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        Card object = birthdayCards.get(position);
        ConstraintLayout imageView = holder.cardLayout.findViewById(R.id.cardBackground);
        imageView.setBackgroundResource(object.getImageUrl());

        holder.editCard.setOnClickListener( v -> {
            Intent i = new Intent(context, BirthdayEditCard.class);
            i.putExtra("background", object.getImageUrl());
            context.startActivity(i);
        });



    }

    @Override
    public int getItemCount() {
        return this.birthdayCards.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder{
        Button editCard, download;
        ConstraintLayout cardLayout;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            editCard = itemView.findViewById(R.id.editCard);
            download = itemView.findViewById(R.id.downloadCard);
            cardLayout = itemView.findViewById(R.id.birthdayView);

        }
    }

}
