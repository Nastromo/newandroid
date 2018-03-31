package com.face_location.facelocation;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class RegistrationFirstActivity extends AppCompatActivity implements View.OnClickListener{

    Button backLoginButton, nextLoginButton;
    EditText userEmailForLogining;
    static String userEmail;
    TextView enterEmailTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_first);

        backLoginButton = (Button) findViewById(R.id.backLoginButton);
        backLoginButton.setOnClickListener(this);

        nextLoginButton = (Button) findViewById(R.id.nextLoginButton);
        nextLoginButton.setOnClickListener(this);

        userEmailForLogining = (EditText) findViewById(R.id.userEmailForLogining);
        userEmailForLogining.setOnClickListener(this);

        enterEmailTextView = (TextView) findViewById(R.id.enterEmailTextView);

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.backLoginButton:
                onBackPressed();
                break;
            case R.id.nextLoginButton:
                getUserEmail();

                if(TextUtils.isEmpty(userEmail)) {
                    showEmailRequires();
                    break;
                }
                Intent registrationSecondActivity = new Intent(this, RegistrationSecondActivity.class);
                startActivity(registrationSecondActivity);
        }
    }

    public String getUserEmail(){
       return userEmail = userEmailForLogining.getText().toString().trim();
    }

    public void showEmailRequires(){
        enterEmailTextView.setTextColor(getResources().getColor(R.color.colorAccent));
    }
}
