package com.swa.invitationcard.Entites;

public class User {
    private String name;
    private String phoneNumber;
    private String email;
    private String profilePic;

    public User() {}

    public User(String name, String phoneNumber, String email, String profilePic) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.profilePic = profilePic;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }
}
