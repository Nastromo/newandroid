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
import com.face_location.facelocation.model.FacelocationAPI;
import com.face_location.facelocation.model.Location.LocationGetResponse;

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

public class MyLocationsFragment extends Fragment {

    ListView listView;
    ArrayList<LocationForAdapter> locations;
    View rootView;
    String locationName, url, token;
    String[] applicationData;
    private static final String TAG = "MyLocationsFragment";
    Button createNewBtn;
    public static boolean isEditable = false;
    public static String userID;
    public static List<String> locationID;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.my_locations_fragment, container, false);

        url = getString(R.string.base_url);

        DataBaseHelper applicationDB = DataBaseHelper.getInstance(getContext());
        applicationData = applicationDB.retrieveFirstLoginValues();
        token = applicationData[5];

        createNewBtn = (Button) rootView.findViewById(R.id.createNewBtn);
        createNewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isEditable = false;
                Intent addLocationFirstActivity = new Intent(getContext(), AddLocationFirstActivity.class);
                startActivity(addLocationFirstActivity);
            }
        });

        getMyLocations();

        return rootView;
    }

    public void getMyLocations() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        FacelocationAPI api = retrofit.create(FacelocationAPI.class);

        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("Content-Type", "application/json");
        headers.put("X-Auth", token);

        userID = applicationData[0];

        Call<List<LocationGetResponse>> call = api.getMyLocations(headers, userID);
        call.enqueue(new Callback<List<LocationGetResponse>>() {
            @Override
            public void onResponse(Call<List<LocationGetResponse>> call, Response<List<LocationGetResponse>> response) {

                Log.i(TAG, "ОТВЕТ СЕРВЕРА: " + response.toString());

                locations = new ArrayList<>();
                locationID = new ArrayList<>();
                List<LocationGetResponse> mylocations = response.body();
                for (LocationGetResponse location: mylocations){
                    locationName = location.getTitle();
                    locationID.add(location.getId());
                    Log.i(TAG, "НАЗВАНИЕ ЛОКАЦИИ: " + locationName);
                    Log.i(TAG, "ID ЛОКАЦИИ: " + location.getId());
                    locations.add(new LocationForAdapter(locationName));
                }
                Log.i(TAG, "РАЗМЕР СПИСКА ПЕРВАЯ ПРОВЕРКА: " + locationID.size() + "   " + locations.size());
                listView = (ListView) rootView.findViewById(R.id.locationListView);
                MyLocationsListAdapter myLocationsFragment = new MyLocationsListAdapter(getContext(), R.layout.my_location_card, locations);
                listView.setAdapter(myLocationsFragment);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        isEditable = true;
                        Log.i(TAG, "РАЗМЕР СПИСКА: " + locationID.size());
                        Intent addLocationFirstActivity = new Intent(getContext(), AddLocationFirstActivity.class);
                        addLocationFirstActivity.putExtra("id", locationID.get(i));
                        addLocationFirstActivity.putExtra("from_my_location_activity", true);
                        startActivity(addLocationFirstActivity);
                    }
                });
            }

            @Override
            public void onFailure(Call<List<LocationGetResponse>> call, Throwable t) {
                Log.i(TAG, "onFailure: " + t.toString());
            }
        });
    }
}
