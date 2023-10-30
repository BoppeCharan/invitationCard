package com.swa.invitationcard.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.swa.invitationcard.Entites.Card;
import com.swa.invitationcard.Home.WeddingEditCard;
import com.swa.invitationcard.R;

import java.util.List;

public class WeddingCardAdapter extends RecyclerView.Adapter<WeddingCardAdapter.viewHolder>{

    List<Card> weddingCards;
    Context context;

    public WeddingCardAdapter(List<Card> list){
        this.weddingCards = list;
    }

    @NonNull
    @Override
    public WeddingCardAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.wedding_adapter_card, parent, false);
        return new WeddingCardAdapter.viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WeddingCardAdapter.viewHolder holder, int position) {


        Card object = weddingCards.get(position);
        ConstraintLayout imageView = holder.cardLayout.findViewById(R.id.cardBackground);
        imageView.setBackgroundResource(object.getImageUrl());

        holder.editCard.setOnClickListener( v -> {
            Intent i = new Intent(context, WeddingEditCard.class);
            i.putExtra("background", object.getImageUrl());
            context.startActivity(i);
        });
    }

    @Override
    public int getItemCount() {
        return this.weddingCards.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder{
        Button editCard, download;
        ConstraintLayout cardLayout;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            editCard = itemView.findViewById(R.id.editCard);
            download = itemView.findViewById(R.id.downloadCard);
            cardLayout = itemView.findViewById(R.id.cardView);

        }
    }

}
