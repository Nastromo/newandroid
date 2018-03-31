package com.face_location.facelocation.model.PostEvent;

/**
 * Created by admin on 20.12.17.
 */

public class ScheduleTimetable {
    String title;
    ScheduleTime time;

    public ScheduleTimetable(String title, ScheduleTime scheduleTime) {
        this.title = title;
        this.time = scheduleTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ScheduleTime getScheduleTime() {
        return time;
    }

    public void setScheduleTime(ScheduleTime scheduleTime) {
        this.time = scheduleTime;
    }

    @Override
    public String toString() {
        return "\nScheduleTimetable{\n" +
                "title=" + title  + ",\n" +
                "time=" + time + "}";
    }
}
