package com.swa.invitationcard.Entites;

import java.io.Serializable;

public class Wedding implements Serializable {

    private String groom;
    private String bride;
    private String date;
    private String time;
    private String venue;
    private String rsvp;
    private String email;
    private String phone;
    private int background;
    private int color;



    public Wedding(){}

    public Wedding(String groom, String bride, String date, String time, String venue, String rsvp, String email, String phone, int background, int color) {
        this.groom = groom;
        this.bride = bride;
        this.date = date;
        this.time = time;
        this.venue = venue;
        this.rsvp = rsvp;
        this.email = email;
        this.phone = phone;
        this.background = background;
        this.color = color;
    }

    public String getGroom() {
        return groom;
    }

    public void setGroom(String groom) {
        this.groom = groom;
    }

    public String getBride() {
        return bride;
    }

    public void setBride(String bride) {
        this.bride = bride;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public String getRsvp() {
        return rsvp;
    }

    public void setRsvp(String rsvp) {
        this.rsvp = rsvp;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getBackground() {
        return background;
    }

    public void setBackground(int background) {
        this.background = background;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
