package com.face_location.facelocation.model.PostEvent;

import java.util.List;

/**
 * Created by admin on 19.12.17.
 */

public class EventBody {
    String title, text, start, end;
    boolean isPrivate;
    int seats, frequency, type;
    List<Schedules> schedules;
    List<Locations> locations;
    boolean published;

    public EventBody(String title, String text, String start, String end, boolean isPrivate, int seats, int frequency, int type, List<Schedules> schedules, List<Locations> locations, boolean published) {
        this.title = title;
        this.text = text;
        this.start = start;
        this.end = end;
        this.isPrivate = isPrivate;
        this.seats = seats;
        this.frequency = frequency;
        this.type = type;
        this.schedules = schedules;
        this.locations = locations;
        this.published = published;
    }

    public EventBody(String title, String text, String start, String end, boolean isPrivate, int seats, int frequency, int type, List<Schedules> schedules, List<Locations> locations) {
        this.title = title;
        this.text = text;
        this.start = start;
        this.end = end;
        this.isPrivate = isPrivate;
        this.seats = seats;
        this.frequency = frequency;
        this.type = type;
        this.schedules = schedules;
        this.locations = locations;
    }

    public EventBody(String title, String text, String start, String end, boolean isPrivate, int seats, int frequency, int type, List<Locations> locations, boolean published) {
        this.title = title;
        this.text = text;
        this.start = start;
        this.end = end;
        this.isPrivate = isPrivate;
        this.seats = seats;
        this.frequency = frequency;
        this.type = type;
        this.locations = locations;
        this.published = published;
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

    public boolean isPrivate() {
        return isPrivate;
    }

    public void setPrivate(boolean aPrivate) {
        isPrivate = aPrivate;
    }

    public int getSeats() {
        return seats;
    }

    public void setSeats(int seats) {
        this.seats = seats;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public List<Schedules> getSchedules() {
        return schedules;
    }

    public void setSchedules(List<Schedules> schedules) {
        this.schedules = schedules;
    }

    public List<Locations> getLocations() {
        return locations;
    }

    public void setLocations(List<Locations> locations) {
        this.locations = locations;
    }

    public boolean isPublished() {
        return published;
    }

    public void setPublished(boolean published) {
        this.published = published;
    }
}
