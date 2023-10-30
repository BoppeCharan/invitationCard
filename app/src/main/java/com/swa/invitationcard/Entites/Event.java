package com.swa.invitationcard.Entites;

import android.media.Image;

import java.io.Serializable;
import java.util.List;

public class Event implements Serializable {

    private ContactsInfo host;
    private ContactsInfo cohost;
    private String date;
    private String eventType;
    private List<ContactsInfo> guestList;
    private List<ContactsInfo> rsvpList;
    private List<TODO> toBuyList;
    private Image invitationCard;
    private String id;

    public Event() {
    }

    public Event(ContactsInfo host, ContactsInfo cohost, String eventType, List<ContactsInfo> guestList, List<ContactsInfo> rsvpList, List<TODO> toBuyList) {
        this.host = host;
        this.cohost = cohost;
        this.eventType = eventType;
        this.guestList = guestList;
        this.rsvpList = rsvpList;
        this.toBuyList = toBuyList;
    }

    public Event(ContactsInfo host, String date, String eventType) {
        this.host = host;
        this.date = date;
        this.eventType = eventType;
    }

    public ContactsInfo getHost() {
        return host;
    }

    public void setHost(ContactsInfo host) {
        this.host = host;
    }

    public ContactsInfo getCohost() {
        return cohost;
    }

    public void setCohost(ContactsInfo cohost) {
        this.cohost = cohost;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public List<ContactsInfo> getGuestList() {
        return guestList;
    }

    public void setGuestList(List<ContactsInfo> guestList) {
        this.guestList = guestList;
    }

    public List<ContactsInfo> getRsvpList() {
        return rsvpList;
    }

    public void setRsvpList(List<ContactsInfo> rsvpList) {
        this.rsvpList = rsvpList;
    }

    public List<TODO> getToBuyList() {
        return toBuyList;
    }

    public void setToBuyList(List<TODO> toBuyList) {
        this.toBuyList = toBuyList;
    }

    public Image getInvitationCard() {
        return invitationCard;
    }

    public void setInvitationCard(Image invitationCard) {
        this.invitationCard = invitationCard;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
