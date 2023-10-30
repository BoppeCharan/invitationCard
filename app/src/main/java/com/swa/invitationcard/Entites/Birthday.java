package com.swa.invitationcard.Entites;

public class Birthday {

    private String name;
    private String date;
    private String time;
    private String venue;
    private String rsvp;
    private String email;
    private String phone;
    private int background;
    private int color;
    private String image;

    public Birthday() {}

    public Birthday(String name, String date, String time, String venue, String rsvp, String email, String phone, int background, int color, String image) {
        this.name = name;
        this.date = date;
        this.time = time;
        this.venue = venue;
        this.rsvp = rsvp;
        this.email = email;
        this.phone = phone;
        this.background = background;
        this.color = color;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
