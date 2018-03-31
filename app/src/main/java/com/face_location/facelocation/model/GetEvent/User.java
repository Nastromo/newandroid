package com.face_location.facelocation.model.GetEvent;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User implements Parcelable {

    private String eventID;

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("avatar")
    @Expose
    private String avatar;
    @SerializedName("avatar_mob")
    @Expose
    private String avatarMob;
    @SerializedName("role")
    @Expose
    private String role;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("hide")
    @Expose
    private Hide hide;

    public User(String username, String email, String avatarMob) {
        this.username = username;
        this.email = email;
        this.avatarMob = avatarMob;
    }

    public User(String username, String email, String avatarMob, String eventID) {
        this.username = username;
        this.email = email;
        this.avatarMob = avatarMob;
        this.eventID = eventID;
    }

    public User(String username, String email, String avatarMob, String eventID, String userID) {
        this.username = username;
        this.email = email;
        this.avatarMob = avatarMob;
        this.id = userID;
        this.eventID = eventID;
    }

    public User(String username, String email, String avatarMob, String eventID, String userID, int status) {
        this.username = username;
        this.email = email;
        this.avatarMob = avatarMob;
        this.id = userID;
        this.eventID = eventID;
        this.status = status;
    }

    public User(Parcel in) {
        username = in.readString();
        email = in.readString();
        avatarMob = in.readString();
        eventID = in.readString();
        id = in.readString();
        status = in.readInt();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public String getEventID() {
        return eventID;
    }

    public void setEventID(String eventID) {
        this.eventID = eventID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getAvatarMob() {
        return avatarMob;
    }

    public void setAvatarMob(String avatarMob) {
        this.avatarMob = avatarMob;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Hide getHide() {
        return hide;
    }

    public void setHide(Hide hide) {
        this.hide = hide;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(username);
        parcel.writeString(email);
        parcel.writeString(avatarMob);
        parcel.writeString(eventID);
        parcel.writeString(id);
        parcel.writeInt(status);
    }
}

