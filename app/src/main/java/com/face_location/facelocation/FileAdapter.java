package com.face_location.facelocation;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by admin on 30.11.17.
 */

public class FileAdapter extends ArrayAdapter<Files> {

    private static final String TAG = "LocalsAdapter";
    private Context mContext;
    private int mResource;


    private static class ViewHolder {
        TextView name;
    }


    public FileAdapter(Context context, int resource, ArrayList<Files> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }


    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        String name = getItem(position).getName();
        if (name.length() > 25){
            name = name.substring(0, Math.min(name.length(), 25)).trim() + "...";
        }



        try{

            ViewHolder holder;

            if(convertView == null){
                LayoutInflater inflater = LayoutInflater.from(mContext);
                convertView = inflater.inflate(mResource, parent, false);

                holder= new ViewHolder();
                holder.name = (TextView) convertView.findViewById(R.id.fileName);


                convertView.setTag(holder);

            }
            else{
                holder = (ViewHolder) convertView.getTag();
            }

            holder.name.setText(name);



            return convertView;

        }catch (IllegalArgumentException e){
            Log.e(TAG, "getView: IllegalArgumentException: " + e.getMessage() );
            return convertView;
        }

    }
}
