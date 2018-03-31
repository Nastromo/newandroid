package com.face_location.facelocation;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.face_location.facelocation.model.DataBase.DataBaseHelper;
import com.face_location.facelocation.model.FacelocationAPI;
import com.face_location.facelocation.model.MyProfile.ProfileBody;
import com.face_location.facelocation.model.MyProfile.ProfileResponse;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MyProfileActivity extends AppCompatActivity implements View.OnClickListener{

    public static final String TAG = "MyProfileActivity";
    public static final int PICK_IMAGE = 1;
    Bitmap avatarImageBitmap;
    Intent getIntent, chooserIntent;

    TextView buttonBackView, addPhotoText;
    EditText editEmail, editName, editSoname, editNumber, editJob;
    ImageView avatar;
    Button saveButton;

    String userEmail, userName, userSoname, userNumber, userJob, userAvatar, url, imgFileName, token;
    Uri returnUri;

    String realPAth;
    File image;

    DataBaseHelper applicationDB;
    String[] userArrayData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        url = getString(R.string.base_url);

        buttonBackView = (TextView) findViewById(R.id.buttonBackView);
        buttonBackView.setOnClickListener(this);

        addPhotoText = (TextView) findViewById(R.id.addPhotoText);
        addPhotoText.setOnClickListener(this);

        editEmail = (EditText) findViewById(R.id.editEmail);
        editName = (EditText) findViewById(R.id.editName);
        editSoname = (EditText) findViewById(R.id.editSoname);
        editNumber = (EditText) findViewById(R.id.editNumber);
        editJob = (EditText) findViewById(R.id.editJob);

        avatar = (ImageView) findViewById(R.id.avatar);
        avatar.setOnClickListener(this);

        applicationDB = DataBaseHelper.getInstance(this);
        userArrayData = applicationDB.retrieveFirstLoginValues();

        if (userArrayData != null) {
            token = userArrayData[5];
        }

        getUserProfile();

        saveButton = (Button) findViewById(R.id.saveButton);
        saveButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.buttonBackView:
                onBackPressed();
                break;

            case R.id.addPhotoText:
                getIntent = new Intent(Intent.ACTION_GET_CONTENT);
                getIntent.setType("image/*");
                chooserIntent = Intent.createChooser(getIntent, "Select Image");
                startActivityForResult(chooserIntent, PICK_IMAGE);
                break;

            case R.id.avatar:
                ActivityCompat.requestPermissions(MyProfileActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
                break;

            case R.id.saveButton:
                userEmail = editEmail.getText().toString();
                userName = editName.getText().toString();
                userSoname = editSoname.getText().toString();
                userNumber = editNumber.getText().toString();
                userJob = editJob.getText().toString();

                uploadAvatarOnServer();
                updateMyProfile();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null){
            returnUri = data.getData();

            Cursor returnCursor =
                    getContentResolver().query(returnUri, null, null, null, null);
            returnCursor.moveToFirst();

            int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
            imgFileName = returnCursor.getString(nameIndex);
            Log.i(TAG, "ИМЯ ФАЙЛА: \n" + imgFileName);

            //Get Bitmap from gallery's chosen photo
            try {
                avatarImageBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), returnUri);
                avatar.setBackground(null);
                avatar.setImageBitmap(avatarImageBitmap);
                avatar.setVisibility(View.VISIBLE);
            } catch (IOException e) {
                e.printStackTrace();
            }
            returnCursor.close();
        }
    }

    private void uploadAvatarOnServer(){
        if (returnUri != null){
            realPAth = getRealPathFromURI(MyProfileActivity.this, returnUri);
        }

        if (realPAth != null) {
            image = new File(realPAth);

            RequestBody filePart = RequestBody.create(MediaType.parse("image/*"), image);
            Log.i(TAG, "uploadAvatarOnServer: ИМЯ ФАЙЛА - " + image.getName());
            MultipartBody.Part file = MultipartBody.Part.createFormData("file", image.getName(), filePart);

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            FacelocationAPI api = retrofit.create(FacelocationAPI.class);
            Log.i(TAG, "ТОКЕН: \n" + token);

            HashMap<String, String> header = new HashMap<String, String>();
            header.put("X-Auth", token);

            Call<ResponseBody> call = api.uploadAvatar(header, file);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    Log.i(TAG, "ОТВЕТ СЕРВЕРА НА ЗАГРУЗКУ ФАЙЛА: " + response.toString());
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.i(TAG, "onFailure: " + t.toString());

                }
            });
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
                    Toast.makeText(MyProfileActivity.this, "Доступ не надано", Toast.LENGTH_SHORT).show();
                }
                return;
            }
            // add other cases for more permissions
        }
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

    public void updateMyProfile(){
        Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create())
            .build();


    FacelocationAPI api = retrofit.create(FacelocationAPI.class);
    ProfileBody myProfile = new ProfileBody(userEmail, userName, userSoname, userNumber, userJob);


    HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("Content-Type", "application/json");
        headers.put("X-Auth", token);


    Call<ProfileResponse> call = api.updateMyProfile(headers, myProfile);
        call.enqueue(new Callback<ProfileResponse>() {
        @Override
        public void onResponse(Call<ProfileResponse> call, Response<ProfileResponse> response) {
            Log.i(TAG, "onResponse: " + response.toString());

            if (response.code() == 200) {
                String userIDResponse = response.body().getId();
                String userEmailResponse = response.body().getEmail();
                String userNameResponse = response.body().getUsername();
                String userSonameResponse = response.body().getLastname();
                String userPhoneResponse = response.body().getPhone();
                String userAvatarResponse = response.body().getAvatarMob();
                String userJobResponse = response.body().getJob();

                Log.i(TAG, "onResponse: \n" +
                        userIDResponse + "\n" +
                        userEmailResponse + "\n" +
                        userNameResponse + "\n" +
                        userSonameResponse + "\n" +
                        userPhoneResponse + "\n" +
                        userAvatarResponse + "\n" +
                        userJobResponse);

                boolean result = applicationDB.updateMyProfileData(
                        userIDResponse,
                        userEmailResponse,
                        userNameResponse,
                        userSonameResponse,
                        userPhoneResponse,
                        userAvatarResponse,
                        userJobResponse);

                if (result == true){
                    Log.i(TAG, "Запись в БД: УСПЕШНО");

                } else {
                    Log.i(TAG, "Запись в БД: НЕ ЗАПИСАНО :(");
                }

            } else {
                Log.i(TAG, "onResponse: \n" + response.code());
            }
        }


        @Override
        public void onFailure(Call<ProfileResponse> call, Throwable t) {
                Log.i(TAG, "onFailure: " + t.toString());
            }
        });
    }

    public void getUserProfile(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        FacelocationAPI api = retrofit.create(FacelocationAPI.class);

        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("Content-Type", "application/json");
        headers.put("X-Auth", token);

        Call<ProfileResponse> call = api.getMyProfile(headers);
        call.enqueue(new Callback<ProfileResponse>() {
            @Override
            public void onResponse(Call<ProfileResponse> call, Response<ProfileResponse> response) {
                Log.i(TAG, "onResponse: " + response.toString());

                if (response.code() == 200) {
                    String userIDResponse = response.body().getId();
                    String userEmailResponse = response.body().getEmail();
                    String userNameResponse = response.body().getUsername();
                    String userSonameResponse = response.body().getLastname();
                    String userPhoneResponse = response.body().getPhone();
                    String userAvatarResponse = response.body().getAvatarMob();
                    String userJobResponse = response.body().getJob();

                    Log.i(TAG, "onResponse: \n" +
                            userIDResponse + "\n" +
                            userEmailResponse + "\n" +
                            userNameResponse + "\n" +
                            userSonameResponse + "\n" +
                            userPhoneResponse + "\n" +
                            userAvatarResponse + "\n" +
                            userJobResponse);

                    boolean result = applicationDB.updateMyProfileData(
                            userIDResponse,
                            userEmailResponse,
                            userNameResponse,
                            userSonameResponse,
                            userPhoneResponse,
                            userAvatarResponse,
                            userJobResponse);

                    if (result == true) {
                        Log.i(TAG, "Запись в БД: УСПЕШНО");

                        userArrayData = applicationDB.retrieveFirstLoginValues();

                        if (userArrayData != null) {

                            userAvatar = userArrayData[10];
                            if (userAvatar != null){
                                if (userAvatar.equals(getString(R.string.def_avatar)) || userAvatar.equals(getString(R.string.def_avatar_second))) {
                                    //go further
                                } else {
                                    avatar.setBackground(null);
                                    Glide
                                            .with(MyProfileActivity.this)
                                            .load(userAvatar)
                                            .thumbnail(0.1f) //shows mini image which weight 0.1 from real image while real image is downloading
                                            .apply(RequestOptions
                                                    .circleCropTransform())
                                            //      .placeholder(R.drawable.oval)) //shows drawable while real/mini image is downloading
                                            .into(avatar);
                                }
                            }

                            userEmail = userArrayData[2];
                            if (userEmail != null){
                                editEmail.setText(userEmail);
                            }

                            userName = userArrayData[6];
                            if (userName != null){
                                editName.setText(userName);
                            }

                            userSoname = userArrayData[7];
                            if (userSoname != null){
                                editSoname.setText(userSoname);
                            }

                            userNumber = userArrayData[8];
                            if (userNumber != null){
                                editNumber.setText(userNumber);
                            }

                            userJob = userArrayData[9];
                            if (userJob != null){
                                editJob.setText(userJob);
                            }
                        }

                    } else {
                            Log.i(TAG, "Запись в БД: НЕ ЗАПИСАНО :(");
                        }

                    } else {
                        Log.i(TAG, "onResponse: \n" + response.code());
                }
            }


            @Override
            public void onFailure(Call<ProfileResponse> call, Throwable t) {
                Log.i(TAG, "onFailure: " + t.toString());
            }
        });
    }
}
