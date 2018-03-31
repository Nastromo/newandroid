package com.facelocation.testretrofit;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    String TAG = "Reg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = (Button) findViewById(R.id.button);
        final EditText login = (EditText) findViewById(R.id.login);
        final EditText password = (EditText) findViewById(R.id.password);

        //Facelocation API
        /*button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("https://face-location.com/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                FaceAPI api = retrofit.create(FaceAPI.class);
                RegistrationBody reg = new RegistrationBody("somenewemail111@gmail.com", "password12");

                Call<ResponseBody> call = api.registerUser(reg);
                Log.i(TAG, "Works until here");

                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        Toast.makeText(MainActivity.this, "Все прошло хорошо",Toast.LENGTH_SHORT).show();
                        Log.i(TAG, "THIS METHOD DOESN'T CALL! WHY?" + response.toString());

                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.i(TAG, "ERROR" + t.toString());
                    }
                });
            }
        });*/

      //Reddit API
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("https://www.reddit.com/api/login/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                FaceAPI api = retrofit.create(FaceAPI.class);

                HashMap<String, String> header = new HashMap<>();
                header.put("Content-Type", "application/json");

                Call<ResponseBody> call = api.registerUser(header,
                        login.getText().toString().trim(),
                        login.getText().toString().trim(),
                        password.getText().toString(),
                        "json");

                Log.i(TAG, "Works here");

                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        Toast.makeText(MainActivity.this, "Все прошло хорошо",Toast.LENGTH_SHORT).show();
                        Log.i(TAG, "THIS METHOD CALL!  -->  " + response.toString());

                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.i(TAG, "ERROR" + t.toString());
                    }
                });
            }
        });
    }
}
