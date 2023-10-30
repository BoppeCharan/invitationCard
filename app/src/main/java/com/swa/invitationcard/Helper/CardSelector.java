package com.swa.invitationcard.Helper;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import com.swa.invitationcard.Entites.Birthday;
import com.swa.invitationcard.Entites.ContactsInfo;
import com.swa.invitationcard.Entites.Wedding;

public class CardSelector {

    private Wedding weddingCard;
    private Birthday birthdayCard;
    private ContactsInfo contactsInfo;
    public static CardSelector instance;

    public static CardSelector getInstance(){
        if(instance == null){
            instance = new CardSelector();
        }
        return instance;
    }

    public void setWeddingDetails(Wedding card){
        this.weddingCard = card;
    }

    public Wedding getWeddingDetails(){
        return  this.weddingCard;
    }

    public Birthday getBirthdayCard() {
        return birthdayCard;
    }

    public void setBirthdayCard(Birthday birthdayCard) {
        this.birthdayCard = birthdayCard;
    }

    public Bitmap getImageBitmap(String profileImage){
        byte[] b = Base64.decode(profileImage, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
        return bitmap;
    }

    public ContactsInfo getContactsInfo() {
        return contactsInfo;
    }

    public void setContactsInfo(ContactsInfo contactsInfo) {
        this.contactsInfo = contactsInfo;
    }
}
