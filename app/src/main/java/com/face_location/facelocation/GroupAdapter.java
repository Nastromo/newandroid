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

public class GroupAdapter extends ArrayAdapter<Group> {

    private static final String TAG = "LocalsAdapter";
    private Context mContext;
    private int mResource;
    private ArrayList<Group> groupChatUsers;


    private static class ViewHolder {
        TextView groupName, membersRaw, membersQuantity;
    }


    public GroupAdapter(Context context, int resource, ArrayList<Group> groupChatUsers) {
        super(context, resource, groupChatUsers);
        mContext = context;
        mResource = resource;
        this.groupChatUsers = groupChatUsers;
    }


    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        String groupName = getItem(position).getGroupName();
        if (groupName.length() > 25){
            groupName = groupName.substring(0, Math.min(groupName.length(), 25)).trim() + "...";
        }

        String membersRaw = getItem(position).getGroupMembers();
        if (membersRaw.length() > 20){
            membersRaw = membersRaw.substring(0, Math.min(membersRaw.length(), 20)).trim() + "...";
        }

        int membersQuantity = getItem(position).getMembersQuantity();



        try{

            ViewHolder holder;

            if(convertView == null){
                LayoutInflater inflater = LayoutInflater.from(mContext);
                convertView = inflater.inflate(mResource, parent, false);

                holder= new ViewHolder();
                holder.groupName = (TextView) convertView.findViewById(R.id.groupName);
                holder.membersRaw = (TextView) convertView.findViewById(R.id.membersRaw);
                holder.membersQuantity = (TextView) convertView.findViewById(R.id.quantity);

                convertView.setTag(holder);

            }
            else{
                holder = (ViewHolder) convertView.getTag();
            }

            holder.groupName.setText(groupName);
            holder.membersRaw.setText(membersRaw);
            holder.membersQuantity.setText(String.valueOf(membersQuantity));

            return convertView;

        }catch (IllegalArgumentException e){
            Log.e(TAG, "getView: IllegalArgumentException: " + e.getMessage() );
            return convertView;
        }

    }

}
