package com.swa.invitationcard.Home;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.swa.invitationcard.Adapters.BirthdayCardAdapter;
import com.swa.invitationcard.Adapters.WeddingCardAdapter;
import com.swa.invitationcard.Entites.Card;
import com.swa.invitationcard.Entites.User;
import com.swa.invitationcard.Helper.CardSelector;
import com.swa.invitationcard.Helper.UserDataService;
import com.swa.invitationcard.R;
import com.swa.invitationcard.databinding.HomeActivityBinding;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    HomeActivityBinding mBinding;
    UserDataService userDataService;
    CardSelector cardSelector;
    User user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mBinding = HomeActivityBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        cardSelector = CardSelector.getInstance();
        userDataService = UserDataService.getInstance();

        TabLayout.Tab birthdayTab = mBinding.tabsHome.newTab().setText("Birthday");
        TabLayout.Tab weddingTab = mBinding.tabsHome.newTab().setText("Wedding");

        mBinding.tabsHome.addTab(birthdayTab);
        mBinding.tabsHome.addTab(weddingTab);

        user = userDataService.getUser();





        mBinding.profileIconHome.setOnClickListener( v -> {
            Intent i = new Intent(HomeActivity.this, UserProfile.class);
            startActivity(i);

        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(HomeActivity.this, LinearLayoutManager.VERTICAL , false);

        mBinding.cardsList.setLayoutManager(layoutManager);

        ArrayList<Card> weddingCards = new ArrayList<>();
        weddingCards.add(new Card(R.drawable.wed_temp_4));
        weddingCards.add(new Card(R.drawable.wed_temp_5));
        weddingCards.add(new Card(R.drawable.wed_temp_6));
//        weddingCards.add(new Card("sdf"));
//        weddingCards.add(new Card("sdf"));
//        weddingCards.add(new Card("sdf"));
//        weddingCards.add(new Card("sdf"));
//        weddingCards.add(new Card("sdf"));

        WeddingCardAdapter adapter = new WeddingCardAdapter(weddingCards);


        ArrayList<Card> birthdayCards = new ArrayList<>();
        birthdayCards.add(new Card(R.drawable.birth_temp_1));
        birthdayCards.add(new Card(R.drawable.birth_temp_2));
        birthdayCards.add(new Card(R.drawable.birth_temp_3));

        BirthdayCardAdapter birthdayCardAdapter = new BirthdayCardAdapter(birthdayCards);
        mBinding.cardsList.setAdapter(birthdayCardAdapter);



        mBinding.tabsHome.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                switch (position) {
                    case 0: // Wedding tab
                        mBinding.cardsList.setAdapter(birthdayCardAdapter);
                        break;
                    case 1: // Birthday tab
                        mBinding.cardsList.setAdapter(adapter);
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();


        user = userDataService.getUser();
        if(user!= null && user.getProfilePic() != null){
            Bitmap bitmap = cardSelector.getImageBitmap(user.getProfilePic());
            mBinding.profileIconHome.setImageBitmap(bitmap);
        }

    }
}
