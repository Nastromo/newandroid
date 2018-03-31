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

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by admin on 28.11.17.
 */

public class VisitedEventsFragment extends Fragment{

    ListView listView;
    private static final String TAG = "VisitedEventsFragment";
    String url, token, name, about, statusString, eventStartTime, eventID;
    int status;
    String[] applicationData;
    ArrayList<Event> events;
    View rootView;
    List<String> subscribersAvatars;
    Button createNewBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.visited_events_fragment, container, false);

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

        getMyVisitedEvents();

        return rootView;
    }

    public void getMyVisitedEvents() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        FacelocationAPI api = retrofit.create(FacelocationAPI.class);

        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("Content-Type", "application/json");
        headers.put("X-Auth", token);

        String userID = applicationData[0];

        Call<List<MyEventResponse>> call = api.getMyVisitedEvents(headers, userID);
        call.enqueue(new Callback<List<MyEventResponse>>() {
            @Override
            public void onResponse(Call<List<MyEventResponse>> call, Response<List<MyEventResponse>> response) {

                Log.i(TAG, "ОТВЕТ СЕРВЕРА: " + response.toString());

                events = new ArrayList<>();
                List<MyEventResponse> myEvents = response.body();
                for (MyEventResponse event: myEvents){
                    name = event.getTitle();
                    about = event.getText();
                    eventID = event.getId();
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
                            eventStartTime = event.getTime().getStart();
                            Log.i(TAG, "ВРЕМЯ ПОЛУЧИЛ: " + eventStartTime);

                            String dateTime = "2017-08-23T08:08:00Z"; //TODO всавить сюда eventStartTime, когда сервер будет возвращать правильную дату
                            DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ssZ");
                            DateTime jodatime = dtf.parseDateTime(dateTime);
                            DateTimeFormatter dtfOut = DateTimeFormat.forPattern("dd.MM.yyyy");

                            Log.i(TAG, "ПЕРЕФОРМАТИРОВАЛ: " + dtfOut.print(jodatime));
                            statusString = "Розпочнеться: " + dtfOut.print(jodatime);


                    }
                    events.add(new Event(name, about, statusString, subscribers.size(), subscribersAvatars, eventID));
                }

                Log.i(TAG, "РАЗМЕР EVENTS: " + events.size());

                listView = (ListView) rootView.findViewById(R.id.visitedListView);
                MyEventsListAdapter myEventsListAdapter = new MyEventsListAdapter(getContext(), R.layout.my_event_card, events);
                listView.setAdapter(myEventsListAdapter);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Intent eventActivity = new Intent(getContext(), EventActivity.class);
                        eventActivity.putExtra("id", events.get(i).getId());
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
