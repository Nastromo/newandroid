package com.face_location.facelocation;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.face_location.facelocation.model.DataBase.DataBaseHelper;
import com.face_location.facelocation.model.Events.MyEventResponse;
import com.face_location.facelocation.model.Events.Subscriber;
import com.face_location.facelocation.model.FacelocationAPI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by admin on 29.11.17.
 */

public class MyEventsFragment extends Fragment {

    ListView listView;
    private static final String TAG = "MyEventsFragment";
    String url, token, name, about, statusString, eventStartTimem, eventID;
    int status;
    String[] applicationData;
    ArrayList<Event> events;
    View rootView;
    List<String> subscribersAvatars;
    Button createNewBtn;
    List<String> eventsID;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.my_events_fragment, container, false);

        url = getString(R.string.base_url);

        DataBaseHelper applicationDB = DataBaseHelper.getInstance(getContext());
        applicationData = applicationDB.retrieveFirstLoginValues();
        token = applicationData[5];

        createNewBtn = (Button) rootView.findViewById(R.id.createNewBtn);
        createNewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent searchLocationActivity = new Intent(getContext(), SearchLocationActivity.class);
                startActivity(searchLocationActivity);
            }
        });

        getMyEvents();

        return rootView;
    }

    public void getMyEvents() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        FacelocationAPI api = retrofit.create(FacelocationAPI.class);

        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("Content-Type", "application/json");
        headers.put("X-Auth", token);

        String userID = applicationData[0];
        Log.i(TAG, "USER ID: " + userID);

        Call<List<MyEventResponse>> call = api.getMyEvents(headers, userID);
        call.enqueue(new Callback<List<MyEventResponse>>() {
            @Override
            public void onResponse(Call<List<MyEventResponse>> call, Response<List<MyEventResponse>> response) {

                Log.i(TAG, "ОТВЕТ СЕРВЕРА: " + response.toString());

                events = new ArrayList<>();
                eventsID = new ArrayList<>();
                List<MyEventResponse> myEvents = response.body();
                for (MyEventResponse event: myEvents){
                    name = event.getTitle();
                    about = event.getText();
                    eventsID.add(event.getId());
                    Log.i(TAG, "ID ИВЕНТА: " + event.getId());
                    List<Subscriber> subscribers = event.getSubscribers();

                    subscribersAvatars = new ArrayList<>();
                    for (Subscriber sub: subscribers){
                        String subscriberAvatar = sub.getUser().getAvatar_mob();
                        Log.i(TAG, "URL АВАТАРКИ САБСКРАЙБЕРА: " + subscriberAvatar);
                        subscribersAvatars.add(subscriberAvatar);
                    }

                    status = event.getStatus();

                    switch (status){
                        case 0:
                            statusString = getString(R.string.on_air);
                            break;

                        case 1:
                            statusString = getString(R.string.off_air);
                            break;

                        case 2:
                            statusString = getString(R.string.will_be);
                    }
                    events.add(new Event(name, about, statusString, subscribers.size(), subscribersAvatars));
                }

                Log.i(TAG, "РАЗМЕР EVENTS: " + events.size());

                listView = (ListView) rootView.findViewById(R.id.listView);
                MyEventsListAdapter myEventsListAdapter = new MyEventsListAdapter(getContext(), R.layout.my_event_card, events);
                listView.setAdapter(myEventsListAdapter);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Intent eventActivity = new Intent(getContext(), MyEventActivity.class);
                        eventActivity.putExtra("id", eventsID.get(i));
                        startActivity(eventActivity);
                    }
                });
            }

            @Override
            public void onFailure(Call<List<MyEventResponse>> call, Throwable t) {
                Log.i(TAG, "onFailure: " + t.toString());
            }
        });
    }
}
