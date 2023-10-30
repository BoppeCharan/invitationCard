package com.swa.invitationcard.Events;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.swa.invitationcard.Adapters.BuyListAdapter;
import com.swa.invitationcard.Entites.Event;
import com.swa.invitationcard.Entites.TODO;
import com.swa.invitationcard.databinding.BuyListBinding;

import java.util.ArrayList;
import java.util.List;

public class TodoActivity extends AppCompatActivity {

    BuyListBinding mBinding;
    List<TODO> taskList;
    BuyListAdapter adapter;
    Event event;
    FirebaseAuth mAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mBinding = BuyListBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        event = (Event) getIntent().getSerializableExtra("event");
        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        taskList = new ArrayList<>();

        if(event.getToBuyList() != null){
            taskList = event.getToBuyList();
        }

        adapter = new BuyListAdapter(taskList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(TodoActivity.this, LinearLayoutManager.VERTICAL , false);
        mBinding.taskListView.setLayoutManager(layoutManager);
        mBinding.taskListView.setAdapter(adapter);
        mBinding.backBtn.setOnClickListener( v -> {
            onBackPressed();
        });
    }

    public void addTask(View view) {
        String newTask = mBinding.editTextTask.getText().toString().trim();

        if (!newTask.isEmpty()) {
            TODO task = new TODO(newTask, false);
            taskList.add(task);
            event.setToBuyList(taskList);
            adapter.notifyDataSetChanged();
            mBinding.editTextTask.setText("");
        } else {
            Toast.makeText(this, "Please enter a task.", Toast.LENGTH_SHORT).show();
        }
    }

    public void toggleTaskStatus(View view) {
        int position = (int) view.getTag();
        TODO task = taskList.get(position);
        task.setStatus(!task.isStatus());
        event.setToBuyList(taskList);
        adapter.notifyDataSetChanged();
    }


    private void updateEvent(Event event) {
        String uid = mAuth.getUid();
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
            Toast.makeText(TodoActivity.this, "Failed to Update Event", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        updateEvent(event);
    }
}
