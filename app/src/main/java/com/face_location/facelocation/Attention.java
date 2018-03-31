package com.face_location.facelocation;

/**
 * Created by admin on 30.11.17.
 */

public class Attention {

   private String title, body, userName, time, avatar;

    public Attention(String title, String body, String userName, String time, String avatar) {
        this.title = title;
        this.body = body;
        this.userName = userName;
        this.time = time;
        this.avatar = avatar;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
