package com.face_location.facelocation;

import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 12.01.18.
 */

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder>{

    List<String> chatData = new ArrayList<>();
    List<String> sender = new ArrayList<>();
    List<String> avatars = new ArrayList<>();
    String myID;
    private static final String TAG = "ChatAdapter";

    public ChatAdapter(List<String> messageData, List<String> sender, List<String> avatars, String myID) {
        this.chatData = messageData;
        this.sender = sender;
        this.avatars = avatars;
        this.myID = myID;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.setIsRecyclable(false);

        if (sender.get(position).equals(myID)){
            holder.getUserMessage().setText(chatData.get(position));
            holder.getUserMessage().setBackground(holder.itemView.getResources().getDrawable(R.drawable.my_message_rec));
            holder.getUserMessage().setTextColor(holder.itemView.getResources().getColor(R.color.colorBlack));
            holder.getUserMessage().setTextSize(holder.itemView.getResources().getDimension(R.dimen.my_mess_size));

            final float scale = holder.itemView.getContext().getResources().getDisplayMetrics().density;
            int pixels = (int) (230 * scale + 0.5f);
            holder.getUserMessage().setMaxWidth(pixels);

            holder.userAvatar.setVisibility(View.INVISIBLE);
            holder.getMessageLayout().setGravity(Gravity.RIGHT);
        } else {
            holder.getUserMessage().setText(chatData.get(position));
            Glide
                    .with(holder.itemView)
                    .load(avatars.get(position))
                    .apply(RequestOptions.circleCropTransform())
                    .into(holder.getUserAvatar());
        }
    }

    @Override
    public int getItemCount() {
        return chatData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView userMessage;
        ImageView userAvatar;
        LinearLayout messageLayout;


        public ViewHolder(View itemView) {
            super(itemView);
            userMessage = (TextView) itemView.findViewById(R.id.userMessage);
            userAvatar = (ImageView) itemView.findViewById(R.id.userAvatar);
            messageLayout = (LinearLayout) itemView.findViewById(R.id.messageLayout);
        }

        public TextView getUserMessage() {
            return userMessage;
        }

        public void setUserMessage(TextView userMessage) {
            this.userMessage = userMessage;
        }

        public ImageView getUserAvatar() {
            return userAvatar;
        }

        public void setUserAvatar(ImageView userAvatar) {
            this.userAvatar = userAvatar;
        }

        public LinearLayout getMessageLayout() {
            return messageLayout;
        }

        public void setMessageLayout(LinearLayout messageLayout) {
            this.messageLayout = messageLayout;
        }
    }
}
