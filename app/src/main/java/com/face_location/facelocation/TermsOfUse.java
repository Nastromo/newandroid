package com.face_location.facelocation;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class TermsOfUse extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_of_use);

        Button tosCancel = (Button) findViewById(R.id.tosCancel);
        tosCancel.setOnClickListener(this);

        Button tosAccept = (Button) findViewById(R.id.tosAccept);
        tosAccept.setOnClickListener(this);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tosCancel:
                Intent tosCancel = new Intent();
                setResult(RESULT_OK, tosCancel);
                finish();
                break;
            case R.id.tosAccept:
                Intent tosAccept = new Intent();
                setResult(RESULT_OK, tosAccept);
                finish();
                break;
        }
    }
}
