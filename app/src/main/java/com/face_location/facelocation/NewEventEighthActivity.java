package com.face_location.facelocation;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.face_location.facelocation.model.PostEvent.ScheduleTime;
import com.face_location.facelocation.model.PostEvent.ScheduleTimetable;
import com.face_location.facelocation.model.PostEvent.Schedules;

import java.util.ArrayList;
import java.util.List;

public class NewEventEighthActivity extends AppCompatActivity implements View.OnClickListener{

    TimePicker timePickerStart, timePickerEnd;
    TextView buttonBackView;
    ImageView eventReady;
    Button addDay, addParagraf;
    TextView dayCount, eventStartName;
    private static final String TAG = "NewEventEighthActivity";
    int day;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event_eighth);

        timePickerStart = (TimePicker) findViewById(R.id.timePickerStart);
        timePickerStart.setIs24HourView(true);
        timePickerStart.setCurrentHour(9);
        timePickerStart.setCurrentMinute(0);

        timePickerEnd = (TimePicker) findViewById(R.id.timePickerEnd);
        timePickerEnd.setIs24HourView(true);
        timePickerEnd.setCurrentHour(10);
        timePickerEnd.setCurrentMinute(0);

        buttonBackView = (TextView) findViewById(R.id.buttonBackView);
        buttonBackView.setOnClickListener(this);

        eventReady = (ImageView) findViewById(R.id.eventReady);
        eventReady.setOnClickListener(this);

        addDay = (Button) findViewById(R.id.addDay);
        addDay.setOnClickListener(this);

        addParagraf = (Button) findViewById(R.id.addParagraf);
        addParagraf.setOnClickListener(this);

        dayCount = (TextView) findViewById(R.id.dayCount);
        dayCount.setText(String.valueOf(DayCounter.dayCount));

        eventStartName = (TextView) findViewById(R.id.eventStartName);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.buttonBackView:
                Intent newEventSixthActivity = new Intent(this, NewEventSixthActivity.class);
                startActivity(newEventSixthActivity);
                break;

            case R.id.eventReady:
                day = Integer.parseInt(dayCount.getText().toString());

//                createTimetable();
//                createSchedules(day, TimetableStorage.timetables.get(day - 1));

                Intent newEventSixth = new Intent(this, NewEventSixthActivity.class);
                startActivity(newEventSixth);
                break;

            case R.id.addParagraf:

//                createTimetable();

                Intent newEventEighthActivity = new Intent(this, NewEventEighthActivity.class);
                startActivity(newEventEighthActivity);
                break;

            case R.id.addDay:
                String currentCountString = dayCount.getText().toString();
                int currentCountInteger = Integer.parseInt(currentCountString);
                if (DayCounter.dayCount == currentCountInteger){
                    DayCounter.addDay();
                } else {
                    DayCounter.dayCount = currentCountInteger;
                    DayCounter.addDay();
                }

//                day = Integer.parseInt(dayCount.getText().toString());
//                createTimetable();
//                createSchedules(day, TimetableStorage.timetables.get(day - 1));

                Intent тewEventNinthActivity = new Intent(this, NewEventNinthActivity.class);
                startActivity(тewEventNinthActivity);
                break;
        }
    }

    public void createTimetable(){
        String title = eventStartName.getText().toString();

        int hourStart = timePickerStart.getCurrentHour();
        int minuteStart = timePickerStart.getCurrentMinute();

        int hourEnd = timePickerEnd.getCurrentHour();
        int minuteEnd = timePickerEnd.getCurrentMinute();

        String[] times = formatTime(hourStart, minuteStart, hourEnd, minuteEnd);
        Log.i(TAG, "ФИНАЛЬНОЕ ВРЕМЯ СТАРТА: " + times[0]);
        Log.i(TAG, "ФИНАЛЬНОЕ ВРЕМЯ ОКОНЧАНИЯ: " + times[1]);

        ScheduleTime time = new ScheduleTime(times[0], times[1]);
        ScheduleTimetable timetable = new ScheduleTimetable(title, time);
        TimetableStorage.timetablesList.add(timetable);

        TimetableStorage.timetables.add(TimetableStorage.timetablesList);
    }

    public void createSchedules(int day, List<ScheduleTimetable> timetables){
        Schedules schedules = new Schedules(day, timetables);
        SchedulesStorage.schedules.add(schedules);
    }

    public String[] formatTime(int hourStart, int minuteStart, int hourEnd, int minuteEnd){
        String hourStartStr = String.valueOf(hourStart);
        String minuteStartStr = String.valueOf(minuteStart);
        String hourEndStr = String.valueOf(hourEnd);
        String minuteEndStr = String.valueOf(minuteEnd);

        if (hourStart <= 9){
            hourStartStr = "0" + String.valueOf(hourStart);
        }

        if (minuteStart <= 9){
            minuteStartStr = "0" + String.valueOf(minuteStart);
        }

        if (hourEnd <= 9){
            hourEndStr = "0" + String.valueOf(hourEnd);
        }

        if (minuteEnd <= 9){
            minuteEndStr = "0" + String.valueOf(minuteEnd);
        }

        SharedPreferences sharedPref = getSharedPreferences(NewEventFirstActivity.FILE_EVENT_DETAILS, Context.MODE_PRIVATE);
        String eventStartDate = sharedPref.getString(NewEventFifthActivity.EVENT_START_DATE, "No key" + NewEventFifthActivity.EVENT_START_DATE);
        String eventEndDate = sharedPref.getString(NewEventFifthActivity.EVENT_END_DATE, "No key" + NewEventFifthActivity.EVENT_END_DATE);

        String startTimeShort = eventStartDate.substring(0, 11);
        String startTimeFinal = startTimeShort + hourStartStr + ":" + minuteStartStr + ":00.000Z";
        Log.i(TAG, "ВРЕМЯ СТАРТА: " + startTimeFinal);

        String endTimeShort = eventEndDate.substring(0, 11);
        String endTimeFinal = endTimeShort + hourEndStr + ":" + minuteEndStr + ":00.000Z";
        Log.i(TAG, "ВРЕМЯ ОКОНЧАНИЯ: " + endTimeFinal);

        String[] dates = {startTimeFinal, endTimeFinal};
        return dates;
    }
}
