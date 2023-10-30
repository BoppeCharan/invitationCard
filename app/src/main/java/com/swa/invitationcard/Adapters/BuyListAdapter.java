package com.swa.invitationcard.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.swa.invitationcard.Entites.TODO;
import com.swa.invitationcard.Events.TodoActivity;
import com.swa.invitationcard.Helper.CardSelector;
import com.swa.invitationcard.R;

import java.util.List;

public class BuyListAdapter extends RecyclerView.Adapter<BuyListAdapter.viewHolder>{

    List<TODO> todoList;
    Context context;
    public BuyListAdapter(List<TODO> list){
        this.todoList = list;
    }

    @NonNull
    @Override
    public BuyListAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.task_item_card, parent, false);
        return new BuyListAdapter.viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BuyListAdapter.viewHolder holder, int position) {
        TODO task = todoList.get(position);

        holder.taskName.setText(task.getTask());
        holder.taskStatus.setChecked(task.isStatus());
        holder.taskStatus.setTag(position);
        holder.taskStatus.setOnClickListener((v) -> ((TodoActivity) context).toggleTaskStatus(v));

    }

    @Override
    public int getItemCount() {
        return this.todoList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder{
        TextView taskName;
        CheckBox taskStatus;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            taskName = itemView.findViewById(R.id.taskName);
            taskStatus = itemView.findViewById(R.id.taskStatus);

        }
    }

}
