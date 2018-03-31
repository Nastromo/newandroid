package com.face_location.facelocation.model.PostEvent;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Time {

    @SerializedName("start")
    @Expose
    private String start;
    @SerializedName("end")
    @Expose
    private String end;
    @SerializedName("communication")
    @Expose
    private Object communication;
    @SerializedName("days")
    @Expose
    private List<Object> days = null;
    @SerializedName("frequency")
    @Expose
    private Integer frequency;

    public Time(String start, String end) {
        this.start = start;
        this.end = end;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public Object getCommunication() {
        return communication;
    }

    public void setCommunication(Object communication) {
        this.communication = communication;
    }

    public List<Object> getDays() {
        return days;
    }

    public void setDays(List<Object> days) {
        this.days = days;
    }

    public Integer getFrequency() {
        return frequency;
    }

    public void setFrequency(Integer frequency) {
        this.frequency = frequency;
    }

}