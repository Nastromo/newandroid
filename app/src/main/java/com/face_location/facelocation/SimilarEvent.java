package com.face_location.facelocation;

import java.util.List;

/**
 * Created by admin on 14.12.17.
 */

public class SimilarEvent {

    private String eventTitle;
    private String eventDate;
    private int passType;
    private String ID;
    private int userQuantity;

    private String mainPicURL;
    List<String> subsAvatars;

    public SimilarEvent(String eventTitle, String eventDate, int passType, String ID, int userQuantity, String mainPicURL, List<String> subsAvatars) {
        this.eventTitle = eventTitle;
        this.eventDate = eventDate;
        this.passType = passType;
        this.ID = ID;
        this.userQuantity = userQuantity;
        this.mainPicURL = mainPicURL;
        this.subsAvatars = subsAvatars;
    }

    public String getEventTitle() {
        return eventTitle;
    }

    public void setEventTitle(String eventTitle) {
        this.eventTitle = eventTitle;
    }

    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public int getPassType() {
        return passType;
    }

    public void setPassType(int passType) {
        this.passType = passType;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public int getUserQuantity() {
        return userQuantity;
    }

    public void setUserQuantity(int userQuantity) {
        this.userQuantity = userQuantity;
    }

    public String getMainPicURL() {
        return mainPicURL;
    }

    public void setMainPicURL(String mainPicURL) {
        this.mainPicURL = mainPicURL;
    }

    public List<String> getSubsAvatars() {
        return subsAvatars;
    }

    public void setSubsAvatars(List<String> subsAvatars) {
        this.subsAvatars = subsAvatars;
    }

    @Override
    public String toString() {
        return "SimilarEvent{" +
                "eventTitle='" + eventTitle + '\'' +
                ", eventDate='" + eventDate + '\'' +
                ", passType=" + passType +
                ", ID='" + ID + '\'' +
                ", userQuantity=" + userQuantity +
                ", mainPicURL='" + mainPicURL + '\'' +
                ", subsAvatars=" + subsAvatars +
                '}';
    }
}
