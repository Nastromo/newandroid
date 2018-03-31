package com.face_location.facelocation;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.face_location.facelocation.model.DataBase.DataBaseHelper;
import com.face_location.facelocation.model.FacelocationAPI;
import com.face_location.facelocation.model.Issue.IssueBody;

import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SupportActivity extends AppCompatActivity implements View.OnClickListener{

    EditText newLocationTitle, problemTextEdit, myEmail;
    TextView buttonBackView;
    Button sendIssue;
    String url, title, token, text;
    private static final String TAG = "SupportActivity";

    DataBaseHelper applicationDB;
    String[] userArrayData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support);

        buttonBackView = (TextView) findViewById(R.id.buttonBackView);
        buttonBackView.setOnClickListener(this);

        newLocationTitle = (EditText) findViewById(R.id.newLocationTitle);
        newLocationTitle.setOnClickListener(this);

        problemTextEdit = (EditText) findViewById(R.id.problemTextEdit);
        problemTextEdit.setOnClickListener(this);

        sendIssue = (Button) findViewById(R.id.sendIssue);
        sendIssue.setOnClickListener(this);

        myEmail = (EditText) findViewById(R.id.myEmail);

        url = getString(R.string.base_url);

        applicationDB = DataBaseHelper.getInstance(this);
        userArrayData = applicationDB.retrieveFirstLoginValues();

        if (userArrayData != null) {
            token = userArrayData[5];
        }

        myEmail.setText(userArrayData[2]);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.buttonBackView:
                onBackPressed();
                break;

            case R.id.sendIssue:
                sendIssue();
                break;
        }
    }

    private void sendIssue(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        title = newLocationTitle.getText().toString();
        text = problemTextEdit.getText().toString();

        FacelocationAPI api = retrofit.create(FacelocationAPI.class);
        IssueBody issue = new IssueBody(title, text);


        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("Content-Type", "application/json");
        headers.put("X-Auth", token);


        Call<ResponseBody> call = api.sendIssue(headers, issue);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                Log.i(TAG, "ОТВЕТ СЕРВЕРА: " + response.toString());
                Toast.makeText(SupportActivity.this, "Запит надіслано", Toast.LENGTH_SHORT).show();
                Intent mainActivity = new Intent(SupportActivity.this, MainActivity.class);
                startActivity(mainActivity);

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.i(TAG, "ОШИБКА: " + t.toString());
            }
        });
    }
}


