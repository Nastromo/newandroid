package com.face_location.facelocation;

/**
 * Created by admin on 30.11.17.
 */

public class ChatUser {
    String name;
    String email;
    String avatar;
    String eventID;
    String userID;

    public ChatUser(String name, String email, String avatar, String eventID, String userID) {
        this.name = name;
        this.email = email;
        this.avatar = avatar;
        this.eventID = eventID;
        this.userID = userID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getEventID() {
        return eventID;
    }

    public void setEventID(String eventID) {
        this.eventID = eventID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}

