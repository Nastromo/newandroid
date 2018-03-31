package com.face_location.facelocation;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.face_location.facelocation.model.DataBase.DataBaseHelper;
import com.face_location.facelocation.model.GetEvent.User;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by admin on 18.01.18.
 */

public class GroupChatAdapter extends RecyclerView.Adapter<GroupChatAdapter.ViewHolder> {
    ArrayList<User> localizedUsers;
    ArrayList<User> cutedLocalizedUsers;
    Context mContext;
    private static final String TAG = "GroupChatAdapter";
    public static HashMap<String, Boolean> checkedUsersID;
    String[] applicationData;
    String myID;

    private SparseBooleanArray itemStateArray= new SparseBooleanArray();

    public GroupChatAdapter(Context mContext, ArrayList<User> localizedUsers) {
        this.mContext = mContext;
        this.localizedUsers = localizedUsers;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.locals_group_card, parent, false);
        checkedUsersID = new HashMap<String, Boolean>();

        DataBaseHelper applicationDB = DataBaseHelper.getInstance(mContext);
        applicationData = applicationDB.retrieveFirstLoginValues();
        myID = applicationData[0];

        cutedLocalizedUsers = new ArrayList<>();
        for (int i = 0; i < localizedUsers.size(); i++) {
            if (localizedUsers.get(i).getId().equals(myID)){

            }else {
                cutedLocalizedUsers.add(localizedUsers.get(i));
            }
        }


        return new GroupChatAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        //https://android.jlelse.eu/android-handling-checkbox-state-in-recycler-views-71b03f237022
        holder.bind(position);
        holder.setIsRecyclable(false);

        if (cutedLocalizedUsers.get(position).getUsername() == null){
            holder.userName.setText("Ім'я не вказано");
        } else {
            holder.userName.setText(cutedLocalizedUsers.get(position).getUsername());
        }

        holder.someText.setText(cutedLocalizedUsers.get(position).getEmail());
        Glide
                .with(mContext)
                .load(cutedLocalizedUsers.get(position).getAvatarMob())
                .apply(RequestOptions.circleCropTransform())
                .into(holder.imageView2);

        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                int adapterPosition = holder.getAdapterPosition();
                if (!itemStateArray.get(adapterPosition, false)) {
                    holder.checkBox.setChecked(true);
                    itemStateArray.put(adapterPosition, true);
                }
                else  {
                    holder.checkBox.setChecked(false);
                    itemStateArray.put(adapterPosition, false);
                }

                String userID = cutedLocalizedUsers.get(position).getId();
                checkedUsersID.put(myID, true);
                checkedUsersID.put(userID, holder.checkBox.isChecked());
                Log.i(TAG, "СПИСОК АЙДИ: " + checkedUsersID.toString());

            }
        });
    }

    @Override
    public int getItemCount() {
        return localizedUsers.size()-1;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView2;
        TextView userName, someText;
        CheckBox checkBox;
        ConstraintLayout layout;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView2 = (ImageView) itemView.findViewById(R.id.imageView2);
            userName = (TextView) itemView.findViewById(R.id.userName);
            someText = (TextView) itemView.findViewById(R.id.someText);
            checkBox = (CheckBox) itemView.findViewById(R.id.checkBox);
            layout = (ConstraintLayout) itemView.findViewById(R.id.layout);
        }

        void bind(int position) {
            // use the sparse boolean array to check
            if (!itemStateArray.get(position, false)) {
                checkBox.setChecked(false);}
            else {
                checkBox.setChecked(true);
            }
        }
    }
}
