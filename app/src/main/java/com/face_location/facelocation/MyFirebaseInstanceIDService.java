package com.face_location.facelocation;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    public static final String DEVICE_TOKEN = "device_token";
    private static final String LAST_TOKEN = "LAST_TOKEN";

    @Override
    public void onTokenRefresh() {

        String lastToken = FirebaseInstanceId.getInstance().getToken();
        Log.i(LAST_TOKEN, "ТОКЕН ДЕВАЙСА: " + lastToken);

        SharedPreferences sharedPref = getSharedPreferences(NewEventFirstActivity.FILE_EVENT_DETAILS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(DEVICE_TOKEN, lastToken);
        editor.commit();

    }
}
