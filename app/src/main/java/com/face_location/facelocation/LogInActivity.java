package com.face_location.facelocation;

import android.app.ProgressDialog;
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

public class LogInActivity extends AppCompatActivity implements View.OnClickListener{

    public static final String TAG = "LogInActivity";

    Button cancelLoginButton, successLoginButton;
    EditText userEmail, userPass;
    TextView registrationTextView;
    public static ProgressDialog pDialog;
    String emailUser, passUser, userToken;
    TextView enterPassTitle, enterEmailTextView;
    private static String url;

    DataBaseHelper applicationDB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        applicationDB = DataBaseHelper.getInstance(this);

        cancelLoginButton = (Button) findViewById(R.id.cancelLoginButton);
        cancelLoginButton.setOnClickListener(this);

        successLoginButton = (Button) findViewById(R.id.successLoginButton);
        successLoginButton.setOnClickListener(this);

        userEmail = (EditText) findViewById(R.id.userEmail);
        userPass = (EditText) findViewById(R.id.userPass);

        registrationTextView = (TextView) findViewById(R.id.registrationTextView);
        registrationTextView.setOnClickListener(this);

        enterEmailTextView = (TextView) findViewById(R.id.enterEmailTextView);
        enterPassTitle = (TextView) findViewById(R.id.enterPassTitle);

        url = getResources().getString(R.string.base_url);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.cancelLoginButton:
                onBackPressed();
                break;

            case R.id.successLoginButton:
                //TODO delete this if don't get paid for progressbar
//                pDialog = new ProgressDialog(this);
//                pDialog.setMessage("Завантажую...");
//                pDialog.setTitle("Вхід");
//                pDialog.show();

                emailUser = userEmail.getText().toString().trim();
                passUser = userPass.getText().toString();

                if (TextUtils.isEmpty(emailUser)){
                    showEmailRequires();
                }else {
                    if (TextUtils.isEmpty(passUser)){
                        showPassRequires();
                    }else {
                        userLogin();
                    }
                }
                break;

            case R.id.registrationTextView:
                Intent registrationTextView = new Intent (this, RegistrationFirstActivity.class);
                startActivity(registrationTextView);
        }
    }

    public void showEmailRequires(){
        enterEmailTextView.setTextColor(getResources().getColor(R.color.colorAccent));
    }

    public void showPassRequires(){
        enterPassTitle.setTextColor(getResources().getColor(R.color.colorAccent));
    }

    private void userLogin(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        FacelocationAPI api = retrofit.create(FacelocationAPI.class);

        RegistrationBody reg = new RegistrationBody(emailUser, passUser);
        HashMap<String, String> headerMap = new HashMap<String, String>();
        headerMap.put("Content-Type", "application/json");

        Call<RegistrationResponse> call = api.loginUser(headerMap, reg);
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

                    Log.i(TAG, "onResponse: " + userAvatarURL);

                    sendDeviseToken();

                    if (applicationDB.isUser(userID)){
                        boolean result = applicationDB.updatesAfterLogin(userID, userEmail, userAvatarURL, userToken);
                        if (result == true){
                            Log.i(TAG, "Запись в БД: УСПЕШНО");

                        } else {
                            Log.i(TAG, "Запись в БД: НЕ ЗАПИСАНО :(");
                        }
                    }else {
                        boolean result = applicationDB.addFirstLogginData(userID,true, userEmail, userRole, userStatus, userToken, userAvatarURL);

                        if (result == true){
                            Log.i(TAG, "Запись в БД: УСПЕШНО");

                        } else {
                            Log.i(TAG, "Запись в БД: НЕ ЗАПИСАНО :(");
                        }
                    }

                    Intent mainActivity = new Intent(LogInActivity.this, MainActivity.class);
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
        Log.i(TAG, "ДЕВАЙС ТОКЕН НА СТРАНИЦЕ ЛОГИНА: " + deviceToken);

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
