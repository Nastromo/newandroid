package com.face_location.facelocation;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.face_location.facelocation.model.DataBase.DataBaseHelper;
import com.face_location.facelocation.model.FacelocationAPI;
import com.face_location.facelocation.model.GetEvent.Announcement;
import com.face_location.facelocation.model.GetEvent.EventResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by admin on 30.11.17.
 */

public class AttentionFragment extends Fragment {

    ListView listView;
    String url, token;
    String[] applicationData;
    private static final String TAG = "AttentionFragment";
    ArrayList<Attention> attentions;
    String eventID;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.attention_fragment, container, false);
        url = getString(R.string.base_url);

        DataBaseHelper applicationDB = DataBaseHelper.getInstance(getContext());
        applicationData = applicationDB.retrieveFirstLoginValues();
        token = applicationData[5];

        listView = (ListView) rootView.findViewById(R.id.attentionListView);

        Bundle bundle = getArguments();
        String eventID = bundle.getString("eventID");

        attentions = new ArrayList<>();
        getEvent(eventID);

        return rootView;
    }

    public void getEvent(String eventID) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        FacelocationAPI api = retrofit.create(FacelocationAPI.class);

        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("Content-Type", "application/json");
        headers.put("X-Auth", token);

        Call<EventResponse> call = api.getMyEvent(headers, eventID);
        call.enqueue(new Callback<EventResponse>() {
            @Override
            public void onResponse(Call<EventResponse> call, Response<EventResponse> response) {
                Log.i(TAG, "ОТВЕТ СЕРВЕРА: " + response.toString());

                List<Announcement> announcements = response.body().getAnnouncements();
                if (announcements.isEmpty()){

                }else {
                    List<String > annTitles = new ArrayList<>();
                    Announcement announcement;
                    String annTitle = "Назва не вказана";
                    String annText;
                    String userName;
                    String annTime;
                    String userAvatar;
                    for (int i = 0; i < announcements.size(); i++) {
                        announcement = announcements.get(i);
                        annText = announcement.getText();
                        userName = announcement.getUser().getUsername();
                        annTime = announcement.getCreatedAt();
                        userAvatar = announcement.getUser().getAvatarMob();

                        String annTimeShort = annTime.substring(11,16);

                        Log.i(TAG, "ДАННЫЕ ОБЪЯВЫ: " + annTitle + "\n"
                                + annText + "\n"
                                + userName + "\n"
                                + annTimeShort + "\n"
                                + userAvatar + "\n");

                        attentions.add(new Attention(annTitle, annText, userName, annTimeShort, userAvatar));
                    }

                    AttentionAdapter attentionAdapter = new AttentionAdapter(getContext(), R.layout.attention_card, attentions);
                    listView.setAdapter(attentionAdapter);
                }
            }

            @Override
            public void onFailure(Call<EventResponse> call, Throwable t) {
                Log.i(TAG, "onFailure: " + t.toString());

            }
        });
    }
}
