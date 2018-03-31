package com.face_location.facelocation;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.face_location.facelocation.model.DataBase.DataBaseHelper;
import com.face_location.facelocation.model.FacelocationAPI;

import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by admin on 30.11.17.
 */

public class LocalsAdapter extends ArrayAdapter<ChatUser> {

    private static final String TAG = "LocalsAdapter";
    private Context mContext;
    private int mResource;
    String[] applicationData;
    DataBaseHelper applicationDB;
    String userID, eventID, url, token;
    boolean isMyEventActivity;
    ArrayList<ChatUser> chatUsers;


    private static class ViewHolder {
        TextView userName;
        TextView someText;
        ImageView imageView2, banUser;
    }


    public LocalsAdapter(Context context, int resource, ArrayList<ChatUser> chatUsers, boolean isMyEventActivity) {
        super(context, resource, chatUsers);
        mContext = context;
        mResource = resource;
        this.isMyEventActivity = isMyEventActivity;
        this.chatUsers = chatUsers;
    }


    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        String userName = getItem(position).getName();
        String userEmail = getItem(position).getEmail();
        String userAvatar = getItem(position).getAvatar();
        eventID = getItem(position).getEventID();
        userID = chatUsers.get(position).getUserID();

        applicationDB = DataBaseHelper.getInstance(getContext());
        applicationData = applicationDB.retrieveFirstLoginValues();

        url = "https://face-location.com/";
        token = applicationData[5];


        if (userName == null){
            userName = "Ім'я не вказано";
        }

        if (userName.length() > 25){
            userName = userName.substring(0, Math.min(userName.length(), 25)).trim() + "...";
        }



        try{

            ViewHolder holder;

            if(convertView == null){
                LayoutInflater inflater = LayoutInflater.from(mContext);
                convertView = inflater.inflate(mResource, parent, false);

                holder= new ViewHolder();
                holder.userName = (TextView) convertView.findViewById(R.id.userName);
                holder.someText = (TextView) convertView.findViewById(R.id.someText);
                holder.imageView2 = (ImageView) convertView.findViewById(R.id.imageView2);
                holder.banUser = (ImageView) convertView.findViewById(R.id.banUser);

                convertView.setTag(holder);

            }
            else{
                holder = (ViewHolder) convertView.getTag();
            }

            holder.userName.setText(userName);
            holder.someText.setText(userEmail);
            Glide
                    .with(mContext)
                    .load(userAvatar)
                    .apply(RequestOptions
                            .circleCropTransform())
                    .into(holder.imageView2);

            holder.banUser.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    banUser(userID, eventID);
                }
            });

            if (isMyEventActivity){

            }else {
                holder.banUser.setVisibility(View.INVISIBLE);
            }

            if (position == 0){
                holder.banUser.setVisibility(View.INVISIBLE);
            }

            return convertView;

        }catch (IllegalArgumentException e){
            Log.e(TAG, "getView: IllegalArgumentException: " + e.getMessage() );
            return convertView;
        }

    }

    private void banUser(final String userID, final String eventID){
        Log.i(TAG, "ID ВНУТРИ МЕТОДА: " + userID);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        FacelocationAPI api = retrofit.create(FacelocationAPI.class);

        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("Content-Type", "application/json");
        headers.put("X-Auth", token);

        Call<ResponseBody> call = api.userBan(headers, eventID, userID);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.i(TAG, "ЗАБАНИТЬ ЮЗЕРА: " + response.toString());
                Log.i(TAG, "EVENTID ГДЕ ЕСТЬ БАНЫ: " + eventID);
                Log.i(TAG, "ЗАБАНЕНЫЙ ЮЗЕР: " + userID);
                Log.i(TAG, "ТОКЕН: " + token);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.i(TAG, "ЗАБАНИТЬ ЮЗЕРА ОШИБКА: " + t.toString());

            }
        });
    }
}
