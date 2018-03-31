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

/**
 * Created by admin on 30.11.17.
 */

public class AttentionAdapter extends ArrayAdapter<Attention> {

    private static final String TAG = "LocalsAdapter";
    private Context mContext;
    private int mResource;


    private static class ViewHolder {
        TextView userName, postTime, attentionTitle, bodyText;
        ImageView userAvatar;
    }


    public AttentionAdapter(Context context, int resource, ArrayList<Attention> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }


    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        String userName = getItem(position).getUserName();
        if (userName.length() > 25){
            userName = userName.substring(0, Math.min(userName.length(), 25)).trim() + "...";
        }

        String postTime = getItem(position).getTime();

        String attentionTitle = getItem(position).getTitle();
        if (attentionTitle.length() > 25){
            attentionTitle = attentionTitle.substring(0, Math.min(attentionTitle.length(), 25)).trim() + "...";
        }

        String bodyText = getItem(position).getBody();

        String userAvatar = getItem(position).getAvatar();
        try{

            ViewHolder holder;

            if(convertView == null){
                LayoutInflater inflater = LayoutInflater.from(mContext);
                convertView = inflater.inflate(mResource, parent, false);

                holder= new ViewHolder();
                holder.userName = (TextView) convertView.findViewById(R.id.userName);
                holder.postTime = (TextView) convertView.findViewById(R.id.someText);
                holder.attentionTitle = (TextView) convertView.findViewById(R.id.attentionTitle);
                holder.bodyText = (TextView) convertView.findViewById(R.id.bodyText);
                holder.userAvatar = (ImageView) convertView.findViewById(R.id.imageView2);

                convertView.setTag(holder);

            }
            else{
                holder = (ViewHolder) convertView.getTag();
            }

            holder.userName.setText(userName);
            holder.postTime.setText(postTime);
            holder.attentionTitle.setText(attentionTitle);
            holder.bodyText.setText(bodyText);

            Glide
                    .with(mContext)
                    .load(userAvatar)
                    .thumbnail(0.1f) //shows mini image which weight 0.1 from real image while real image is downloading
                    .apply(RequestOptions
                            .circleCropTransform())
                    //      .placeholder(R.drawable.oval)) //shows drawable while real/mini image is downloading
                    .into(holder.userAvatar);


            return convertView;

        }catch (IllegalArgumentException e){
            Log.e(TAG, "getView: IllegalArgumentException: " + e.getMessage() );
            return convertView;
        }

    }
}
