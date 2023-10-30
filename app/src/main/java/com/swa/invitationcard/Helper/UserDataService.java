package com.swa.invitationcard.Helper;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.swa.invitationcard.Entites.User;

public class UserDataService {
    public static UserDataService instance;
    FirebaseDatabase database;
    DatabaseReference usersDatabase;
    public static FirebaseAuth mAuth;
    User currentUser;

    public static UserDataService getInstance(){
        if(instance == null){
            instance = new UserDataService();
            mAuth = FirebaseAuth.getInstance();
        }
        return instance;
    }

    public  UserDataService(){
        database = FirebaseDatabase.getInstance();
        usersDatabase = database.getReference("Users");
    }


    public User load(String uid){

        usersDatabase = database.getReference("Users").child(uid);

        usersDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    User userValue = new User();
                    User user = snapshot.getValue(User.class);
                    userValue.setProfilePic(user.getProfilePic());
                    userValue.setEmail(user.getEmail());
                    userValue.setName(user.getName());
                    userValue.setPhoneNumber(user.getPhoneNumber());
                    currentUser = userValue;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return currentUser;
    }

    public void setUser(User user){
        this.currentUser = user;
    }


    public User getUser(){
        if(currentUser == null){
            currentUser = load(mAuth.getUid());
            return currentUser;
        }

        return currentUser;
    }






}
