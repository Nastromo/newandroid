package com.face_location.facelocation.model.PostChat;

/**
 * Created by admin on 19.01.18.
 */

public class ChatBody {
    String title, event;
    String[] user;
    int type;

    public ChatBody(String title, String event, String[] user, int type) {
        this.title = title;
        this.event = event;
        this.user = user;
        this.type = type;
    }
}
