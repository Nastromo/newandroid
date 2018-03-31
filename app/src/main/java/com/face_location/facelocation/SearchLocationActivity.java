package com.face_location.facelocation;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.face_location.facelocation.model.FacelocationAPI;
import com.face_location.facelocation.model.Location.LocationGetResponse;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SearchLocationActivity extends AppCompatActivity implements View.OnClickListener{

    TextView buttonBackView, locationTitleSearch, locationAbout, enterTitleLocationTip, titleForNewLocationAddingTextView;
    EditText newLocationTitle;
    Button buttonSearch;
    public static final String TAG = "SearchLocationActivity";
    String url, searchedLocationTitle;
    Intent newEventFirstActivity;
    String locationID;
    boolean isForEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_location);

        url = getResources().getString(R.string.base_url);

        buttonBackView = (TextView) findViewById(R.id.buttonBackView);
        buttonBackView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        locationTitleSearch = (TextView) findViewById(R.id.locationTitleSearch);

        locationAbout = (TextView) findViewById(R.id.locationAbout);

        newLocationTitle = (EditText) findViewById(R.id.newLocationTitle);

        enterTitleLocationTip = (TextView) findViewById(R.id.enterTitleLocationTip);

        buttonSearch = (Button) findViewById(R.id.buttonSearch);
        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchLocation();
            }
        });

        isForEdit = getIntent().getBooleanExtra("from_my_event_activity", false);
        if (isForEdit){
            titleForNewLocationAddingTextView = (TextView) findViewById(R.id.titleForNewLocationAddingTextView);
            titleForNewLocationAddingTextView.setText(R.string.edit_event);
        }
    }

    public void searchLocation(){
        locationTitleSearch.setText("");
        locationAbout.setText("");

        searchedLocationTitle = newLocationTitle.getText().toString();
        Log.i(TAG, "searchLocation: " + "ВВЕДЕНЫЙ ТЕКСТ " + searchedLocationTitle);

        if(TextUtils.isEmpty(searchedLocationTitle)){
            enterTitleLocationTip.setTextColor(getResources().getColor(R.color.colorAccent));
        } else {
            Log.i(TAG, "ПРОШЛО ОТРИЦАТЕЛЬНОЕ УСЛОВИЕ");
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            FacelocationAPI api = retrofit.create(FacelocationAPI.class);

            HashMap<String, String> headerMap = new HashMap<String, String>();
            headerMap.put("Content-Type", "application/json");

            Call<List<LocationGetResponse>> call = api.searchLocation(headerMap);
            call.enqueue(new Callback<List<LocationGetResponse>>() {
                @Override
                public void onResponse(Call<List<LocationGetResponse>> call, Response<List<LocationGetResponse>> response) {
                    Log.i(TAG, "onResponse: " + response.code());

                    if (response.code() == 200) {
                        List <LocationGetResponse> locations = response.body();

                        for (LocationGetResponse location: locations){
                            if (location.getTitle().equals(searchedLocationTitle)){

                                locationID = location.getId();

                                locationTitleSearch.setText(location.getTitle());
                                locationTitleSearch.setOnClickListener(SearchLocationActivity.this);

                                locationAbout.setText(location.getText());
                                locationAbout.setOnClickListener(SearchLocationActivity.this);
                                break;

                            } else {
                                locationTitleSearch.setText(getString(R.string.no_such_location));
                                locationAbout.setText("");
                            }
                        }

                    } else {
                        Log.i(TAG, "onResponse: \n" + response.code());
                    }
                }

                @Override
                public void onFailure(Call<List<LocationGetResponse>> call, Throwable t) {
                    Log.i(TAG, "onFailure: " + t.toString());
                }
            });
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.locationTitleSearch:
                newEventFirstActivity = new Intent(this, NewEventFirstActivity.class);
                newEventFirstActivity.putExtra("locationID", locationID);
                if (isForEdit){
                    newEventFirstActivity.putExtra("from_my_event_activity", true);
                }
                startActivity(newEventFirstActivity);

            case R.id.locationAbout:
                newEventFirstActivity = new Intent(this, NewEventFirstActivity.class);
                newEventFirstActivity.putExtra("locationID", locationID);
                if (isForEdit){
                    newEventFirstActivity.putExtra("from_my_event_activity", true);
                }
                startActivity(newEventFirstActivity);
        }
    }
}
