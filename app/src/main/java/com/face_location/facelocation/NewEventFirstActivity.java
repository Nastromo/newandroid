package com.face_location.facelocation;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class NewEventFirstActivity extends AppCompatActivity implements View.OnClickListener{

    EditText eventName;
    Button cancelEventCreation, successEventCreation;
    Spinner spinnerEventType;
    TextView buttonBackView, addNewEventTitle;
    int type;

    private static final String TAG = "newEvent";

    public static final String FILE_EVENT_DETAILS = "New Event details";
    public static final String EVENT_TITLE = "title";
    public static final String EVENT_TYPE = "type";
    public static final String EVENT_LOCATION_ID = "location_id";
    String locationID;
    boolean isForEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event_first);

        locationID = getIntent().getStringExtra("locationID");
        Log.i(TAG, "ID ЛОКАЦИИ НА СЛЕД ЭКРАНЕ: " + locationID);

        eventName = (EditText) findViewById(R.id.eventName);

        cancelEventCreation = (Button) findViewById(R.id.cancelEventCreation);
        cancelEventCreation.setOnClickListener(this);

        successEventCreation = (Button) findViewById(R.id.successEventCreation);
        successEventCreation.setOnClickListener(this);

        buttonBackView = (TextView) findViewById(R.id.buttonBackView);
        buttonBackView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        spinnerEventType = (Spinner) findViewById(R.id.spinnerEventType);
        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_expandable_list_item_1,
                getResources().getStringArray(R.array.event_types));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerEventType.setAdapter(myAdapter);

        isForEdit = getIntent().getBooleanExtra("from_my_event_activity", false);
        if (isForEdit){
            addNewEventTitle = (TextView) findViewById(R.id.addNewEventTitle);
            addNewEventTitle.setText(R.string.edit_event);
        }

    }

    @Override
    public void onClick(View view) {
      switch(view.getId()){

          case R.id.cancelEventCreation:
              Intent mainActivity = new Intent(this, MainActivity.class);
              startActivity(mainActivity);
              break;

          case R.id.successEventCreation:
              String titleEvent = eventName.getText().toString().trim();
              String typeEvent = spinnerEventType.getSelectedItem().toString();
              type = 57;
              switch (typeEvent){
                  case "концерт":
                      type = 0;
                      break;
                  case "бізнес":
                      type = 1;
                      break;
                  case "розваги":
                      type = 2;
                      break;
                  case "акції":
                      type = 3;
                      break;
                  case "спорт":
                      type = 4;
                      break;
                  case "зустріч":
                      type = 5;
                      break;
                  case "кафе":
                      type = 6;
                      break;
                  case "семінар":
                      type = 7;
                      break;
                  case "конференція":
                      type = 8;
                      break;
                  case "тренінг":
                      type = 9;
                      break;
              }
              Log.i(TAG, "Тип события: " + String.valueOf(type));

              //Save Event name and category to shared preferences file
              SharedPreferences sharedPref = getSharedPreferences(FILE_EVENT_DETAILS, Context.MODE_PRIVATE);
              SharedPreferences.Editor editor = sharedPref.edit();
              editor.putString(EVENT_TITLE, titleEvent);
              editor.putInt(EVENT_TYPE, type);
              editor.putString(EVENT_LOCATION_ID, locationID);
              editor.commit();

              Intent secondStepEventCreation = new Intent(this, NewEventSecondActivity.class);
              if (isForEdit){
                  secondStepEventCreation.putExtra("from_my_event_activity", true);
              }
              startActivity(secondStepEventCreation);
              break;
      }
    }
}
