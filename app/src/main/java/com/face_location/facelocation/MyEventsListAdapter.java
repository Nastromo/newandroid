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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 4/4/2017.
 */

public class MyEventsListAdapter extends ArrayAdapter<Event> {

    private static final String TAG = "MyEventsListAdapter";
    private Context mContext;
    private int mResource;


    private static class ViewHolder {
        TextView name, about, status, userQuantity, userQuantitySecond, userQuantityThird, userQuantityFourth;
        ImageView avatar, avatarSecond, avatarThird, avatarFourth;
    }


    public MyEventsListAdapter(Context context, int resource, ArrayList<Event> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.mResource = resource;
    }


    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        String name = getItem(position).getName();
        if (name.length() > 29){
            name = name.substring(0, Math.min(name.length(), 29)) + "...";
        }

        String about = getItem(position).getAbout();
        if (about.length() > 100){
            about = about.substring(0, Math.min(about.length(), 100)) + "...";
        }

        String status = getItem(position).getStatus();
        int userQuantity = getItem(position).getUserQuantity();
        Log.i(TAG, "КОЛИЧЕСТВО ЮЗЕРОВ: " + userQuantity);
        List<String> avatars = getItem(position).getAvatars();


        try{

            ViewHolder holder;

            if(convertView == null){
                LayoutInflater inflater = LayoutInflater.from(mContext);
                convertView = inflater.inflate(mResource, parent, false);

                holder= new ViewHolder();
                holder.name = (TextView) convertView.findViewById(R.id.eventTitle);
                holder.about = (TextView) convertView.findViewById(R.id.aboutEvent);
                holder.status = (TextView) convertView.findViewById(R.id.eventStatus);
                holder.userQuantity = (TextView) convertView.findViewById(R.id.usersQuantity);
                holder.userQuantitySecond = (TextView) convertView.findViewById(R.id.usersQuantitySecond);
                holder.userQuantityThird = (TextView) convertView.findViewById(R.id.usersQuantityThird);
                holder.userQuantityFourth = (TextView) convertView.findViewById(R.id.usersQuantityFourth);

                holder.avatar = (ImageView) convertView.findViewById(R.id.userAvatar);
                holder.avatarSecond = (ImageView) convertView.findViewById(R.id.userAvatarSecond);
                holder.avatarThird = (ImageView) convertView.findViewById(R.id.userAvatarThird);
                holder.avatarFourth = (ImageView) convertView.findViewById(R.id.userAvatarFourth);
                convertView.setTag(holder);

            }
            else{
                holder = (ViewHolder) convertView.getTag();
            }

            holder.name.setText(name);
            holder.about.setText(about);
            holder.status.setText(status);

            if (avatars != null && avatars.size() > 1){
                switch (avatars.size()){
                    case 2:
                        holder.userQuantity.setText("");
                        holder.userQuantitySecond.setText("+" + String.valueOf(userQuantity));

                        holder.avatarThird.setVisibility(View.INVISIBLE);
                        holder.avatarFourth.setVisibility(View.INVISIBLE);
                        Glide
                                .with(mContext)
                                .load(avatars.get(0))
                                .thumbnail(0.1f) //shows mini image which weight 0.1 from real image while real image is downloading
                                .apply(RequestOptions
                                        .circleCropTransform())
                                //      .placeholder(R.drawable.oval)) //shows drawable while real/mini image is downloading
                                .into(holder.avatar);
                        Glide
                                .with(mContext)
                                .load(avatars.get(1))
                                .thumbnail(0.1f) //shows mini image which weight 0.1 from real image while real image is downloading
                                .apply(RequestOptions
                                        .circleCropTransform())
                                //      .placeholder(R.drawable.oval)) //shows drawable while real/mini image is downloading
                                .into(holder.avatarSecond);
                        break;

                    case 3:
                        holder.userQuantity.setText("");
                        holder.userQuantityThird.setText("+" + String.valueOf(userQuantity));

                        holder.avatarFourth.setVisibility(View.INVISIBLE);
                        Glide
                                .with(mContext)
                                .load(avatars.get(0))
                                .thumbnail(0.1f) //shows mini image which weight 0.1 from real image while real image is downloading
                                .apply(RequestOptions
                                        .circleCropTransform())
                                //      .placeholder(R.drawable.oval)) //shows drawable while real/mini image is downloading
                                .into(holder.avatar);
                        Glide
                                .with(mContext)
                                .load(avatars.get(1))
                                .thumbnail(0.1f) //shows mini image which weight 0.1 from real image while real image is downloading
                                .apply(RequestOptions
                                        .circleCropTransform())
                                //      .placeholder(R.drawable.oval)) //shows drawable while real/mini image is downloading
                                .into(holder.avatarSecond);
                        Glide
                                .with(mContext)
                                .load(avatars.get(2))
                                .thumbnail(0.1f) //shows mini image which weight 0.1 from real image while real image is downloading
                                .apply(RequestOptions
                                        .circleCropTransform())
                                //      .placeholder(R.drawable.oval)) //shows drawable while real/mini image is downloading
                                .into(holder.avatarThird);
                        break;

                    case 4:
                        holder.userQuantity.setText("");
                        holder.userQuantityFourth.setText("+" + String.valueOf(userQuantity));

                        Glide
                                .with(mContext)
                                .load(avatars.get(0))
                                .thumbnail(0.1f) //shows mini image which weight 0.1 from real image while real image is downloading
                                .apply(RequestOptions
                                        .circleCropTransform())
                                //      .placeholder(R.drawable.oval)) //shows drawable while real/mini image is downloading
                                .into(holder.avatar);
                        Glide
                                .with(mContext)
                                .load(avatars.get(1))
                                .thumbnail(0.1f) //shows mini image which weight 0.1 from real image while real image is downloading
                                .apply(RequestOptions
                                        .circleCropTransform())
                                //      .placeholder(R.drawable.oval)) //shows drawable while real/mini image is downloading
                                .into(holder.avatarSecond);
                        Glide
                                .with(mContext)
                                .load(avatars.get(2))
                                .thumbnail(0.1f) //shows mini image which weight 0.1 from real image while real image is downloading
                                .apply(RequestOptions
                                        .circleCropTransform())
                                //      .placeholder(R.drawable.oval)) //shows drawable while real/mini image is downloading
                                .into(holder.avatarThird);
                        Glide
                                .with(mContext)
                                .load(avatars.get(3))
                                .thumbnail(0.1f) //shows mini image which weight 0.1 from real image while real image is downloading
                                .apply(RequestOptions
                                        .circleCropTransform())
                                //      .placeholder(R.drawable.oval)) //shows drawable while real/mini image is downloading
                                .into(holder.avatarFourth);
                        break;
                }

            } else {
                holder.userQuantity.setText("+" + String.valueOf(userQuantity));
                holder.avatarSecond.setVisibility(View.INVISIBLE);
                holder.avatarThird.setVisibility(View.INVISIBLE);
                holder.avatarFourth.setVisibility(View.INVISIBLE);
                holder.userQuantityFourth.setText("");
                holder.userQuantityThird.setText("");
                holder.userQuantitySecond.setText("");
                Glide
                        .with(mContext)
                        .load(avatars.get(0))
                        .thumbnail(0.1f) //shows mini image which weight 0.1 from real image while real image is downloading
                        .apply(RequestOptions
                                .circleCropTransform())
                        //      .placeholder(R.drawable.oval)) //shows drawable while real/mini image is downloading
                        .into(holder.avatar);
            }

            return convertView;

        }catch (IllegalArgumentException e){
            Log.e(TAG, "getView: IllegalArgumentException: " + e.getMessage() );
            return convertView;
        }

    }
}
