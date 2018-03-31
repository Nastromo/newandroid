package com.face_location.facelocation.model.PostEvent;

import java.util.List;

/**
 * Created by admin on 19.12.17.
 */

public class Schedules {
    int day;
    List<ScheduleTimetable> timetable;

    public Schedules(int day, List<ScheduleTimetable> timetable) {
        this.day = day;
        this.timetable = timetable;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public List<ScheduleTimetable> getTimetable() {
        return timetable;
    }

    public void setTimetable(List<ScheduleTimetable> timetable) {
        this.timetable = timetable;
    }

    @Override
    public String toString() {
        return "Schedules{" + "\n" +
                "day=" + day + ",\n" +
                "timetable=" + timetable + "}";
    }
}
