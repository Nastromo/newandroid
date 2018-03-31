package com.face_location.facelocation;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class NewEventSecondActivity extends AppCompatActivity implements View.OnClickListener{

    TextView btnBackView, forwardButtonView, titleForNewLocationAddingTextView;
    EditText somethingAbout;
    boolean isForEdit;

    //TODO проверить чтобы все ключи в файле FILE_EVENT_DETAILS и FILE_LOCATION_DETAILS были уникальными
    public static final String EVENT_ABOUT = "about_event";
    public static final String TAG = "newEvent";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event_second);

        btnBackView = (TextView) findViewById(R.id.btnBackView);
        btnBackView.setOnClickListener(this);

        forwardButtonView = (TextView) findViewById(R.id.forwardButtonView);
        forwardButtonView.setOnClickListener(this);

        somethingAbout = (EditText) findViewById(R.id.somethingAbout);

        isForEdit = getIntent().getBooleanExtra("from_my_event_activity", false);
        if (isForEdit){
            titleForNewLocationAddingTextView = (TextView) findViewById(R.id.titleForNewLocationAddingTextView);
            titleForNewLocationAddingTextView.setText(R.string.edit_event);
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.btnBackView:
                onBackPressed();
                break;

            case R.id.forwardButtonView:
                String aboutEvent = somethingAbout.getText().toString().trim();
                Log.i(TAG, "Про событие: " + aboutEvent);

                //Save Event about to shared preferences file
                SharedPreferences sharedPref = getSharedPreferences(NewEventFirstActivity.FILE_EVENT_DETAILS, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString(EVENT_ABOUT, aboutEvent);
                editor.commit();

                Intent newEventThirdActivity = new Intent (this, NewEventThirdActivity.class);
                if (isForEdit){
                    newEventThirdActivity.putExtra("from_my_event_activity", true);
                }
                startActivity(newEventThirdActivity);
                break;
        }
    }
}
