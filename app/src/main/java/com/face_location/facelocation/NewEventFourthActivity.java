package com.face_location.facelocation;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class NewEventFourthActivity extends AppCompatActivity implements View.OnClickListener{

    Spinner spinnerEventPeriod;
    TextView buttonBackView, forwardButtonTextView;
    EditText placesQuantity;
    public static final String TAG = "newEvent";
    public static final String EVENT_PLACES = "seats";
    public static final String EVENT_PERIOD = "frequency";
//    ImageView imageView2;
    int frequency;
    boolean isForEdit;
    TextView titleForNewLocationAddingTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event_fourth);

        frequency = -1;

        buttonBackView = (TextView) findViewById(R.id.buttonBackView);
        buttonBackView.setOnClickListener(this);

        forwardButtonTextView = (TextView) findViewById(R.id.forwardButtonTextView);
        forwardButtonTextView.setOnClickListener(this);

        spinnerEventPeriod = (Spinner) findViewById(R.id.spinnerEventPeriod);
        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_expandable_list_item_1,
                getResources().getStringArray(R.array.event_period));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerEventPeriod.setAdapter(myAdapter);

        placesQuantity = (EditText) findViewById(R.id.placesQuantity);

        isForEdit = getIntent().getBooleanExtra("from_my_event_activity", false);
        if (isForEdit){
            titleForNewLocationAddingTextView = (TextView) findViewById(R.id.titleForNewLocationAddingTextView);
            titleForNewLocationAddingTextView.setText(R.string.edit_event);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.buttonBackView:
                onBackPressed();
                break;

            case R.id.forwardButtonTextView:
                String placesEvent = placesQuantity.getText().toString();
                Log.i(TAG, "Количество мест: " + placesEvent);
                int places = Integer.parseInt(placesEvent);

                String periodEvent = spinnerEventPeriod.getSelectedItem().toString();
                Log.i(TAG, "Периодичность: " + periodEvent);

                //TODO спросить Диму, какие значения соответствуют словам
                switch (periodEvent){
                    case "Одноразово":
                        frequency = 0;
                        Log.i(TAG, "Периодичность: " + String.valueOf(frequency));
                        break;

                    case "Щотижнево":
                        frequency = 1;
                        Log.i(TAG, "Периодичность: " + String.valueOf(frequency));
                        break;

                    case "Без кінця":
                        frequency = 2;
                        Log.i(TAG, "Периодичность: " + String.valueOf(frequency));
                        break;
                }

                //Save Event place quantity to shared preferences file
                SharedPreferences sharedPref = getSharedPreferences(NewEventFirstActivity.FILE_EVENT_DETAILS, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putInt(EVENT_PLACES, places);
                editor.putInt(EVENT_PERIOD, frequency);
                editor.commit();

                Intent newEventFifthActivity = new Intent(this, NewEventFifthActivity.class);
                if (isForEdit){
                    newEventFifthActivity.putExtra("from_my_event_activity", true);
                }
                startActivity(newEventFifthActivity);
        }
    }
}

