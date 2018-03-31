package com.face_location.facelocation.model.Issue;

/**
 * Created by Artem on 30.12.2017.
 */

public class IssueBody {
    String title, text;
    int type;

    public IssueBody(String title, String text) {
        this.title = title;
        this.text = text;
        this.type = 4;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
