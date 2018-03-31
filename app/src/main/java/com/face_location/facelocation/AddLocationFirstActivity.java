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

public class AddLocationFirstActivity extends AppCompatActivity implements View.OnClickListener {

    TextView backButtonTextView, forwardButtonTextView, titleForNewLocationAddingTextView;
    public static final String LOCATION_TITLE = "title";
    public static final String FILE_LOCATION_DETAILS = "Location details";
    EditText newLocationTitle;
    String locationID;
    public static final String TAG = "AddLocationFirst";
    boolean isForEdit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_location_first);

        locationID = getIntent().getStringExtra("id");
        Log.i(TAG, "ID ВЫБРАННОЙ ЛОКАЦИИ: " + locationID);

        backButtonTextView = (TextView) findViewById(R.id.buttonBackView);
        backButtonTextView.setOnClickListener(this);

        forwardButtonTextView = (TextView) findViewById(R.id.forwardButtonTextView);
        forwardButtonTextView.setOnClickListener(this);

        newLocationTitle = (EditText) findViewById(R.id.newLocationTitle);

        isForEdit = getIntent().getBooleanExtra("from_my_location_activity", false);
        Log.i(TAG, "(1 ШАГ) ПРИШЛИ С ЭКРАНА МОИ ЛОКАЦИИ?: " + isForEdit);
        if (isForEdit){
            titleForNewLocationAddingTextView = (TextView) findViewById(R.id.titleForNewLocationAddingTextView);
            titleForNewLocationAddingTextView.setText(R.string.edit_location);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.buttonBackView:
                onBackPressed();
                break;

            case R.id.forwardButtonTextView:
                String locationTitle = newLocationTitle.getText().toString();

                SharedPreferences sharedPref = getSharedPreferences(FILE_LOCATION_DETAILS, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString(LOCATION_TITLE, locationTitle);
                editor.commit();

                Intent secondStep = new Intent(this, AddLocationSecondActivity.class);
                secondStep.putExtra("id", locationID);
                if (isForEdit){
                    secondStep.putExtra("from_my_location_activity", true);
                }
                startActivity(secondStep);
                break;
        }
    }
}
