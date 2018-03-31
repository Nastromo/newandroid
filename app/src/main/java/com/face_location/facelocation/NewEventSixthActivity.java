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

import com.face_location.facelocation.model.DataBase.DataBaseHelper;
import com.face_location.facelocation.model.FacelocationAPI;
import com.face_location.facelocation.model.PostEvent.EventBody;
import com.face_location.facelocation.model.PostEvent.EventResponse;
import com.face_location.facelocation.model.PostEvent.Locations;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NewEventSixthActivity extends AppCompatActivity implements View.OnClickListener{

    Button buttonChoseSchedule;
    String title, about, startDate, endDate, url, locationID, token, realPAth, eventID;
    int type, frequency, places;
    boolean isPublic;
    TextView buttonBackView, forwardButtonTextView;
    ImageView createEvent;
    Intent mainActivity;
    private static final String TAG = "NewEventSixthActivity";
    DataBaseHelper applicationDB;
    EventBody eventBody;
    File image;
    private static final String NEW_EVENT_ID = "new_event_id";
    boolean published = true;
    boolean isForEdit;
    TextView titleForNewLocationAddingTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event_sixth);

        applicationDB = DataBaseHelper.getInstance(this);

        buttonChoseSchedule = (Button) findViewById(R.id.buttonChoseSchedule);
        buttonChoseSchedule.setOnClickListener(this);

        buttonBackView = (TextView) findViewById(R.id.buttonBackView);
        buttonBackView.setOnClickListener(this);

        forwardButtonTextView = (TextView) findViewById(R.id.forwardButtonTextView);
        forwardButtonTextView.setOnClickListener(this);

        createEvent = (ImageView) findViewById(R.id.createEvent);
        createEvent.setOnClickListener(this);

        url = getString(R.string.base_url);

        isForEdit = getIntent().getBooleanExtra("from_my_event_activity", false);
        if (isForEdit){
            titleForNewLocationAddingTextView = (TextView) findViewById(R.id.titleForNewLocationAddingTextView);
            titleForNewLocationAddingTextView.setText(R.string.edit_event);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.buttonChoseSchedule:
                Intent newEventSeventhActivity = new Intent(this, NewEventSeventhActivity.class);
                startActivity(newEventSeventhActivity);
                break;

            case R.id.buttonBackView:
                onBackPressed();
                break;

            case R.id.createEvent:
                createEvent();
                mainActivity = new Intent(this, MainActivity.class);
                startActivity(mainActivity);
                break;

            case R.id.forwardButtonTextView:
                createEvent();
                mainActivity = new Intent(this, MainActivity.class);
                startActivity(mainActivity);
                break;
        }
    }


    private void createEvent(){

        SharedPreferences sharedPref = getSharedPreferences(NewEventFirstActivity.FILE_EVENT_DETAILS, Context.MODE_PRIVATE);

        title = sharedPref.getString(NewEventFirstActivity.EVENT_TITLE, "No key like " + NewEventFirstActivity.EVENT_TITLE);
        type = sharedPref.getInt(NewEventFirstActivity.EVENT_TYPE, -1);
        about = sharedPref.getString(NewEventSecondActivity.EVENT_ABOUT, "No key like " + NewEventSecondActivity.EVENT_ABOUT);
        isPublic = sharedPref.getBoolean(NewEventThirdActivity.EVENT_PUBLICITY, true);
        places = sharedPref.getInt(NewEventFourthActivity.EVENT_PLACES, -1);
        frequency = sharedPref.getInt(NewEventFourthActivity.EVENT_PERIOD, -1);
        startDate = sharedPref.getString(NewEventFifthActivity.EVENT_START_DATE, "No key like " + NewEventFifthActivity.EVENT_START_DATE);
        endDate = sharedPref.getString(NewEventFifthActivity.EVENT_END_DATE, "No key like " + NewEventFifthActivity.EVENT_END_DATE);
        locationID = sharedPref.getString(NewEventFirstActivity.EVENT_LOCATION_ID, "No key like " + NewEventFirstActivity.EVENT_LOCATION_ID);
        realPAth = sharedPref.getString(NewEventThirdActivity.COVER_REALPATH, "No key like " + NewEventThirdActivity.COVER_REALPATH);

        Log.i(TAG, "РЕЗУЛЬТАТ: " + title + "\n" +
                type + "\n" +
                about + "\n" +
                isPublic + "\n" +
                places + "\n" +
                frequency + "\n" +
                startDate + "\n" +
                endDate + "\n" +
                published + "\n" +
                locationID);

        Locations loc = new Locations(locationID);
        List<Locations> locationsIDs = new ArrayList<>();
        locationsIDs.add(loc);
        eventBody = new EventBody(title, about, startDate, endDate, isPublic, places, frequency, type, locationsIDs, published);


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        String[] userInfo = applicationDB.retrieveFirstLoginValues();
        token = userInfo[5];

        FacelocationAPI api = retrofit.create(FacelocationAPI.class);

        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("Content-Type", "application/json");
        headers.put("X-Auth", token);

        Call<EventResponse> call = api.createEvent(headers, eventBody);
        call.enqueue(new Callback<EventResponse>() {
            @Override
            public void onResponse(Call<EventResponse> call, Response<EventResponse> response) {

                Log.i(TAG, "ОТВЕТ СЕРВЕРА: " + response.toString());

                if (response.code() == 200){
                    eventID = response.body().getId();
                    Log.i(TAG, "ID НОВОСОЗДАННОГО ИВЕНТА: " + eventID);

                    String eventTitle = response.body().getTitle();
                    Log.i(TAG, "ЗАГОЛОВОК ИВЕНТА: " + eventTitle);

                    List<String> locations = response.body().getLocations();
                    String locationID = locations.get(0);
                    Log.i(TAG, "ID ПЕРВОЙ ЛОКАЦИИ: " + locationID);

                    int type = response.body().getType();
                    Log.i(TAG, "НОМЕР ТИПА ИВЕНТА: " + type);

                    boolean isPrivate = response.body().getIsPrivate();
                    Log.i(TAG, "СТАТУС ПРИВАТНОСТИ: " + isPrivate);

                    boolean published = response.body().getPublished();
                    Log.i(TAG, "СТАТУС ОПУБЛИКОВАННОСТИ ИВЕНТА: " + published);

                    uploadEventCoverOnServer(eventID);

//                    if (SchedulesStorage.schedules.size() > 0){
//                        for (int i = 0; i <SchedulesStorage.schedules.size() ; i++) {
//
//                            Schedules ttb =  SchedulesStorage.schedules.get(i);
//
//                            Log.i(TAG, "ЧТО СОХРАНИЛ В СТОРЕДЖЕ: " + i + "\n" + ttb.toString());
//                        }
//                    }

                }
            }

            @Override
            public void onFailure(Call<EventResponse> call, Throwable t) {
                Log.i(TAG, "onFailure: " + t.toString());
            }
        });
    }


    private void uploadEventCoverOnServer(String eventID){

            image = new File(realPAth);

            RequestBody filePart = RequestBody.create(MediaType.parse("image/*"), image);
            Log.i(TAG, "ИМЯ ЗАГРУЖАЕМОГО ФАЙЛА - " + image.getName());
            MultipartBody.Part file = MultipartBody.Part.createFormData("file", image.getName(), filePart);

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            FacelocationAPI api = retrofit.create(FacelocationAPI.class);
            Log.i(TAG, "ТОКЕН: \n" + token);

            HashMap<String, String> header = new HashMap<String, String>();
            header.put("X-Auth", token);

            Call<ResponseBody> call = api.uploadEventCover(header, eventID, file);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    Log.i(TAG, "ОТВЕТ СЕРВЕРА НА ЗАГРУЗКУ ФАЙЛА: " + response.toString());
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.i(TAG, "onFailure: " + t.toString());

                }
            });
    }
}
