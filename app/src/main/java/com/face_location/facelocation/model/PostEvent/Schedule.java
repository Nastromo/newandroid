package com.face_location.facelocation.model.PostEvent;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Schedule {

    @SerializedName("day")
    @Expose
    private Integer day;
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("scheduleTimetable")
    @Expose
    private List<ScheduleTimetable> scheduleTimetable = null;

    public Integer getDay() {
        return day;
    }

    public void setDay(Integer day) {
        this.day = day;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<ScheduleTimetable> getScheduleTimetable() {
        return scheduleTimetable;
    }

    public void setScheduleTimetable(List<ScheduleTimetable> scheduleTimetable) {
        this.scheduleTimetable = scheduleTimetable;
    }

}
