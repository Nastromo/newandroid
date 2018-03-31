package com.face_location.facelocation;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private List<SimilarEvent> similarEventList;
    private static Context mContext;
    static String similarEventID;
    private static final String TAG = "RecyclerViewAdapter";

    public RecyclerViewAdapter(Context context, List<SimilarEvent> events) {
        similarEventList = events;
        mContext = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        public TextView eventTitle;
        public TextView eventDate;
        public TextView passType;
        public TextView userQuantity, usersQuantitySecond, usersQuantityThird;

        public ImageView mainPic;
        public ImageView userAvatar;
        public ImageView userAvatarSecond;
        public ImageView userAvatarThird;


        public ViewHolder(final View itemView){
            super(itemView);

            eventTitle = (TextView) itemView.findViewById(R.id.eventTitle);
            eventDate = (TextView) itemView.findViewById(R.id.eventDate);
            passType = (TextView) itemView.findViewById(R.id.passType);
            userQuantity = (TextView) itemView.findViewById(R.id.usersQuantity);
            usersQuantitySecond = (TextView) itemView.findViewById(R.id.usersQuantitySecond);
            usersQuantityThird = (TextView) itemView.findViewById(R.id.usersQuantityThird);

            mainPic = (ImageView) itemView.findViewById(R.id.eventPhoto);
            userAvatar = (ImageView) itemView.findViewById(R.id.userAvatar);
            userAvatarSecond = (ImageView) itemView.findViewById(R.id.userAvatarSecond);
            userAvatarThird = (ImageView) itemView.findViewById(R.id.userAvatarThird);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    Intent similarEventActivity = new Intent(mContext, EventActivity.class);
                    similarEventActivity.putExtra("id", similarEventID);
                    mContext.startActivity(similarEventActivity);
                }
            });
        }
    }



    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View contactView = inflater.inflate(R.layout.similar_event_card, parent, false);
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerViewAdapter.ViewHolder viewHolder, int position) {
        SimilarEvent event = similarEventList.get(position);

        TextView eventTitle = viewHolder.eventTitle;
        eventTitle.setText(event.getEventTitle());

        similarEventID = event.getID();

        TextView eventDate = viewHolder.eventDate;
        eventDate.setText(event.getEventDate());

        TextView passType = viewHolder.passType;

        int eventPassTypeInt = 57;
        try{
            eventPassTypeInt = event.getPassType();
        }catch (Exception e){
            Log.i(TAG, "ВЫЫЛЕТЕЛО ИСКЛЮЧЕНИЕ: " + e.toString());
        }

        String eventPassType = "Подія";
        switch (eventPassTypeInt){
            case 0:
                eventPassType = "концерт";
                break;
            case 1:
                eventPassType = "бізнес";
                break;
            case 2:
                eventPassType = "розваги";
                break;
            case 3:
                eventPassType = "акції";
                break;
            case 4:
                eventPassType = "спорт";
                break;
            case 5:
                eventPassType = "зустріч";
                break;
            case 6:
                eventPassType = "кафе";
                break;
            case 7:
                eventPassType = "семінар";
                break;
            case 8:
                eventPassType = "конференція";
                break;
            case 9:
                eventPassType = "тренінг";
                break;
        }


        passType.setText(eventPassType);

        ImageView mainPic = viewHolder.mainPic;
        Glide
                .with(mContext)
                .load(event.getMainPicURL())
                .into(mainPic);

        TextView userQuantity = viewHolder.userQuantity;
        TextView userQuantitySecond = viewHolder.usersQuantitySecond;
        TextView userQuantityThird = viewHolder.usersQuantityThird;

        ImageView userAvatar = viewHolder.userAvatar;
        ImageView userAvatarSecond = viewHolder.userAvatarSecond;
        ImageView userAvatarThird = viewHolder.userAvatarThird;

        List<String> subsAvatars = event.getSubsAvatars();

        if (subsAvatars != null && subsAvatars.size() > 1){
            switch (subsAvatars.size()){
                case 2:
                    userQuantity.setText("");
                    userQuantitySecond.setText("+" + String.valueOf(event.getUserQuantity()));
                    userQuantityThird.setText("");

                    userAvatarThird.setVisibility(View.INVISIBLE);

                    Glide
                            .with(mContext)
                            .load(subsAvatars.get(0))
                            .thumbnail(0.1f) //shows mini image which weight 0.1 from real image while real image is downloading
                            .apply(RequestOptions
                                    .circleCropTransform())
                            //      .placeholder(R.drawable.oval)) //shows drawable while real/mini image is downloading
                            .into(userAvatar);
                    Glide
                            .with(mContext)
                            .load(subsAvatars.get(1))
                            .thumbnail(0.1f) //shows mini image which weight 0.1 from real image while real image is downloading
                            .apply(RequestOptions
                                    .circleCropTransform())
                            //      .placeholder(R.drawable.oval)) //shows drawable while real/mini image is downloading
                            .into(userAvatarSecond);
                    break;

                case 3:
                    userQuantity.setText("");
                    userQuantitySecond.setText("");
                    userQuantityThird.setText("+" + String.valueOf(event.getUserQuantity()));

                    Glide
                            .with(mContext)
                            .load(subsAvatars.get(0))
                            .thumbnail(0.1f) //shows mini image which weight 0.1 from real image while real image is downloading
                            .apply(RequestOptions
                                    .circleCropTransform())
                            //      .placeholder(R.drawable.oval)) //shows drawable while real/mini image is downloading
                            .into(userAvatar);
                    Glide
                            .with(mContext)
                            .load(subsAvatars.get(1))
                            .thumbnail(0.1f) //shows mini image which weight 0.1 from real image while real image is downloading
                            .apply(RequestOptions
                                    .circleCropTransform())
                            //      .placeholder(R.drawable.oval)) //shows drawable while real/mini image is downloading
                            .into(userAvatarSecond);
                    Glide
                            .with(mContext)
                            .load(subsAvatars.get(2))
                            .thumbnail(0.1f) //shows mini image which weight 0.1 from real image while real image is downloading
                            .apply(RequestOptions
                                    .circleCropTransform())
                            //      .placeholder(R.drawable.oval)) //shows drawable while real/mini image is downloading
                            .into(userAvatarThird);
                    break;
            }

        } else {
            userQuantity.setText("+" + String.valueOf(event.getUserQuantity()));
            userQuantitySecond.setText("");
            userQuantityThird.setText("");

            userAvatarSecond.setVisibility(View.INVISIBLE);
            userAvatarThird.setVisibility(View.INVISIBLE);

            Glide
                    .with(mContext)
                    .load(subsAvatars.get(0))
                    .thumbnail(0.1f) //shows mini image which weight 0.1 from real image while real image is downloading
                    .apply(RequestOptions
                            .circleCropTransform())
                    //      .placeholder(R.drawable.oval)) //shows drawable while real/mini image is downloading
                    .into(userAvatar);
        }
    }

    @Override
    public int getItemCount() {
        return similarEventList.size();
    }
}