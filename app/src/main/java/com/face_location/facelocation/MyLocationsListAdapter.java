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
 * Created by admin on 29.11.17.
 */

public class MyLocationsListAdapter extends ArrayAdapter<LocationForAdapter> {

    private static final String TAG = "MyLocationsListAdapter";
    private Context mContext;
    private int mResource;


    private static class ViewHolder {
        TextView locationName;
        TextView locationID;
    }


    public MyLocationsListAdapter(Context context, int resource, ArrayList<LocationForAdapter> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }


    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        String locationName = getItem(position).getLocationName();
        Log.i(TAG, "НАЗВАНИЕ ЛОКАЦИЙ: " + locationName.toString() + position);

        if (locationName.length() > 25){
            locationName = locationName.substring(0, Math.min(locationName.length(), 25)).trim() + "...";
        }


        try{

            ViewHolder holder;

            if(convertView == null){
                LayoutInflater inflater = LayoutInflater.from(mContext);
                convertView = inflater.inflate(mResource, parent, false);

                holder= new ViewHolder();
                holder.locationName = (TextView) convertView.findViewById(R.id.locationName);
                convertView.setTag(holder);

            }
            else{
                holder = (ViewHolder) convertView.getTag();
            }

            holder.locationName.setText(locationName);


            return convertView;

        }catch (IllegalArgumentException e){
            Log.e(TAG, "getView: IllegalArgumentException: " + e.getMessage() );
            return convertView;
        }

    }
}
