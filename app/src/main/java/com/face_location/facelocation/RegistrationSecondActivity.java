package com.face_location.facelocation;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.face_location.facelocation.model.DataBase.DataBaseHelper;
import com.face_location.facelocation.model.DeviceToken.DeviceTokenBody;
import com.face_location.facelocation.model.FacelocationAPI;
import com.face_location.facelocation.model.Registration.RegistrationBody;
import com.face_location.facelocation.model.Registration.RegistrationResponse;

import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegistrationSecondActivity extends AppCompatActivity implements View.OnClickListener{

    Button backLoginButton, signupButton;
    EditText userPassForLogining;
    String userPass, userToken;
    TextView enterPassTextView;

    private static final String TAG = "Registration";
    private String url;

    private DataBaseHelper applicationDB;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_second);

        backLoginButton = (Button) findViewById(R.id.backLoginButton);
        backLoginButton.setOnClickListener(this);

        signupButton = (Button) findViewById(R.id.signupButton);
        signupButton.setOnClickListener(this);

        userPassForLogining = (EditText) findViewById(R.id.userPassForLogining);

        enterPassTextView = (TextView) findViewById(R.id.enterPassTextView);

        url = getResources().getString(R.string.base_url);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.backLoginButton:
                onBackPressed();
                break;

            case R.id.signupButton:
                getUserPass();

                if(TextUtils.isEmpty(userPass)){
                    showPassRequires();
                    break;
                } else {
                    userRegistration();
                }
                break;
        }
    }

    public void showPassRequires(){
        enterPassTextView.setTextColor(getResources().getColor(R.color.colorAccent));
    }

    private String getUserPass(){
        return userPass = userPassForLogining.getText().toString();
    }


    private void userRegistration(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        FacelocationAPI api = retrofit.create(FacelocationAPI.class);
        final RegistrationBody reg = new RegistrationBody(RegistrationFirstActivity.userEmail, userPass);


        HashMap<String, String> headerMap = new HashMap<String, String>();
        headerMap.put("Content-Type", "application/json");

        Call<RegistrationResponse> call = api.registerUser(headerMap, reg);
        call.enqueue(new Callback<RegistrationResponse>() {
            @Override
            public void onResponse(Call<RegistrationResponse> call, Response<RegistrationResponse> response) {
                Log.i(TAG, "onResponse: " + response.toString());

                if (response.code() == 200) {
                    String userID = response.body().getUser().getId();
                    String userEmail = response.body().getUser().getEmail();
                    String userRole = response.body().getUser().getRole();
                    int userStatus = response.body().getUser().getStatus();
                    userToken = response.body().getToken();
                    String userAvatarURL = response.body().getUser().getAvatarMob();

                    Log.i(TAG, "onResponse: \n" +
                            userID + "\n" +
                            userEmail + "\n" +
                            userRole + "\n" +
                            userStatus + "\n" +
                            userToken + "\n" +
                            userAvatarURL);

                    sendDeviseToken();

                    applicationDB = DataBaseHelper.getInstance(RegistrationSecondActivity.this);
                    boolean insertFirstLoginData = applicationDB.addFirstLogginData(userID,true, userEmail, userRole, userStatus, userToken, userAvatarURL);

                    if (insertFirstLoginData == true){
                        Log.i(TAG, "Запись в БД: УСПЕШНО");

                    } else {
                        Log.i(TAG, "Запись в БД: НЕ ЗАПИСАНО :(");
                    }

                    Intent mainActivity = new Intent(RegistrationSecondActivity.this, MainActivity.class);
                    startActivity(mainActivity);

                } else {
                    Log.i(TAG, "onResponse: \n" +
                    response.code());
                }
            }


            @Override
            public void onFailure(Call<RegistrationResponse> call, Throwable t) {
                Log.i(TAG, "onFailure: " + t.toString());
            }
        });
    }

    public void sendDeviseToken(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        FacelocationAPI api = retrofit.create(FacelocationAPI.class);

        SharedPreferences sharedPref = getSharedPreferences(NewEventFirstActivity.FILE_EVENT_DETAILS, Context.MODE_PRIVATE);
        String deviceToken = sharedPref.getString("device_token", "No key like device_token");
        Log.i(TAG, "ДЕВАЙС ТОКЕН НА СТРАНИЦЕ РЕГИСТРАЦИИ: " + deviceToken);

        DeviceTokenBody token = new DeviceTokenBody(deviceToken);
        HashMap<String, String> headerMap = new HashMap<String, String>();
        headerMap.put("Content-Type", "application/json");
        headerMap.put("X-Auth", userToken);

        Call<ResponseBody> call = api.sendDeviceToken(headerMap, token);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if (response.code() == 200) {
                    Log.i(TAG, "ДЭВАЙС ТОКЕН ОТПРАВЛЕН");


                } else {
                    Log.i(TAG, "onResponse: \n" +
                            response.code());
                }
            }


            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.i(TAG, "onFailure: " + t.toString());
            }
        });
    }
}
