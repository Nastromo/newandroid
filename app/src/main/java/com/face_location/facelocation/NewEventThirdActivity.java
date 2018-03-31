package com.face_location.facelocation;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.face_location.facelocation.model.DataBase.DataBaseHelper;

import java.io.File;
import java.io.IOException;

public class NewEventThirdActivity extends AppCompatActivity implements View.OnClickListener{

    Spinner spinnerEventPublicity;
    TextView buttonBackView, forwardButtonTextView;
    public static final int PICK_IMAGE = 1;
    public static final String TAG = "newEvent";
    public static final String EVENT_PUBLICITY = "private";
    public static final String COVER_REALPATH = "cover_realpath";
    static String imgFileName;
    static String imgFilePath;
    static byte[] imgBytes;
    Bitmap eventImageBitmap;
    Button buttonChosePhoto;
    Uri returnUri;
    String realPAth, url, token;
    File image;
    DataBaseHelper applicationDB;
    String[] userArrayData;
    Intent getIntent, chooserIntent;
    boolean isForEdit;
    TextView titleForNewLocationAddingTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event_third);

        url = getString(R.string.base_url);

        buttonBackView = (TextView) findViewById(R.id.buttonBackView);
        buttonBackView.setOnClickListener(this);

        forwardButtonTextView = (TextView) findViewById(R.id.forwardButtonTextView);
        forwardButtonTextView.setOnClickListener(this);

        buttonChosePhoto = (Button) findViewById(R.id.buttonChosePhoto);
        buttonChosePhoto.setOnClickListener(this);

        applicationDB = DataBaseHelper.getInstance(this);
        userArrayData = applicationDB.retrieveFirstLoginValues();

        if (userArrayData != null) {
            token = userArrayData[5];
        }

        spinnerEventPublicity = (Spinner) findViewById(R.id.spinnerEventPublicity);
        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_expandable_list_item_1,
                getResources().getStringArray(R.array.event_publicity));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerEventPublicity.setAdapter(myAdapter);

        isForEdit = getIntent().getBooleanExtra("from_my_event_activity", false);
        if (isForEdit){
            titleForNewLocationAddingTextView = (TextView) findViewById(R.id.titleForNewLocationAddingTextView);
            titleForNewLocationAddingTextView.setText(R.string.edit_event);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.buttonBackView:
                onBackPressed();
                break;

            case R.id.forwardButtonTextView:
                String publicityEvent = spinnerEventPublicity.getSelectedItem().toString();
                boolean isPublic = false;
                Log.i(TAG, "Тип публичности: " + publicityEvent);
                if (publicityEvent.equals("Відкритий")){
                    isPublic = false;
                } else {
                    isPublic = true;
                }
                Log.i(TAG, "Тип публичности логический: " + isPublic);

                //Save Event publicity to shared preferences file
                SharedPreferences sharedPref = getSharedPreferences(NewEventFirstActivity.FILE_EVENT_DETAILS, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putBoolean(EVENT_PUBLICITY, isPublic);
                editor.commit();

                Intent newEventFourthActivity = new Intent(this, NewEventFourthActivity.class);
                if (isForEdit){
                    newEventFourthActivity.putExtra("from_my_event_activity", true);
                }
                startActivity(newEventFourthActivity);
                break;

            case R.id.buttonChosePhoto:
                ActivityCompat.requestPermissions(NewEventThirdActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null){
            returnUri = data.getData();
            Cursor returnCursor =
                    getContentResolver().query(returnUri, null, null, null, null);

            int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
            returnCursor.moveToFirst();

            imgFileName = returnCursor.getString(nameIndex);

            String upToNCharacters = imgFileName.substring(0, Math.min(imgFileName.length(), 20));
            buttonChosePhoto.setText(upToNCharacters + "...");

            //Get Bitmap from gallery's chosen photo
            try {
                eventImageBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), returnUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            saveCoverRealPath();
            returnCursor.close();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission granted and now can proceed
                    getIntent = new Intent(Intent.ACTION_GET_CONTENT);
                    getIntent.setType("image/*");
                    chooserIntent = Intent.createChooser(getIntent, "Select Image");
                    startActivityForResult(chooserIntent, PICK_IMAGE);

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(NewEventThirdActivity.this, "Доступ не надано", Toast.LENGTH_SHORT).show();
                }
                return;
            }
            // add other cases for more permissions
        }
    }

    private void saveCoverRealPath(){
        if (returnUri != null){
            realPAth = getRealPathFromURI(NewEventThirdActivity.this, returnUri);
        }

        //Save Event publicity to shared preferences file
        SharedPreferences sharedPref = getSharedPreferences(NewEventFirstActivity.FILE_EVENT_DETAILS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(COVER_REALPATH, realPAth);
        editor.commit();

    }

    public static String getRealPathFromURI(Context context, Uri uri){
        String filePath = "";
        String wholeID = DocumentsContract.getDocumentId(uri);

        // Split at colon, use second item in the array
        String id = wholeID.split(":")[1];

        String[] column = { MediaStore.Images.Media.DATA };

        // where id is equal to
        String sel = MediaStore.Images.Media._ID + "=?";

        Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                column, sel, new String[]{ id }, null);

        int columnIndex = cursor.getColumnIndex(column[0]);

        if (cursor.moveToFirst()) {
            filePath = cursor.getString(columnIndex);
        }
        cursor.close();
        return filePath;
    }
}
