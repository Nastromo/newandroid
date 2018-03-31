package com.face_location.facelocation;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.face_location.facelocation.model.DataBase.DataBaseHelper;
import com.face_location.facelocation.model.FacelocationAPI;
import com.face_location.facelocation.model.GetEvent.User;
import com.face_location.facelocation.model.GetMainChat.MainChatResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by admin on 30.11.17.
 */

public class LocalsFragment extends Fragment{


    ListView groupList, localsList;
    ArrayList<User> parcelables;
    String eventID, url, token, myID;
    String[] applicationData;
    View rootView;
    List<String> chatID;
    ArrayList<String> usersIDS, groupChatNames;
    ArrayList<Integer> quantitys;
    List<ArrayList<String>> usersIDSarray;
    String groupChatName;
    int quantity;
    ArrayList<ChatUser> users;

    private static final String TAG = "LocalsFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.locals_fragment, container, false);

        Bundle bundle = getArguments();
        parcelables = bundle.getParcelableArrayList("data");
        eventID = bundle.getString("eventID");

        url = getString(R.string.base_url);
        DataBaseHelper applicationDB = DataBaseHelper.getInstance(getContext());
        applicationData = applicationDB.retrieveFirstLoginValues();
        token = applicationData[5];
        myID = applicationData[0];

        FloatingActionButton createGroupChatFab = (FloatingActionButton) rootView.findViewById(R.id.createGroupChatFab);
        createGroupChatFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(getContext());
                View mView = getLayoutInflater().inflate(R.layout.new_group_chat_title, null);
                final EditText groupChatTitle = (EditText) mView.findViewById(R.id.groupChatTitle);

                mBuilder.setView(mView);
                final AlertDialog dialog = mBuilder.create();

                Button cancelBtn = (Button) mView.findViewById(R.id.cancelBtn);
                cancelBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.cancel();
                    }
                });

                Button createBtn = (Button) mView.findViewById(R.id.createBtn);
                createBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!groupChatTitle.getText().toString().isEmpty()){
                            Intent groupChatUserChose = new Intent(getContext(), GroupChatUserChose.class);
                            groupChatUserChose.putExtra("chat_name", groupChatTitle.getText().toString().trim());
                            groupChatUserChose.putExtra("eventID", eventID);
                            groupChatUserChose.putParcelableArrayListExtra("data", parcelables);
                            startActivity(groupChatUserChose);
                        }else {
                            Toast.makeText(getContext(), "Вкажіть назву чату", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                dialog.show();
            }
        });

        users = new ArrayList<>();

        getGroupChatList();

        for (int i = 0; i < parcelables.size(); i++) {
            if (parcelables.get(i).getStatus() != null && parcelables.get(i).getStatus() != 1){
                users.add(new ChatUser(
                        parcelables.get(i).getUsername(),
                        parcelables.get(i).getEmail(),
                        parcelables.get(i).getAvatarMob(),
                        parcelables.get(i).getEventID(),
                        parcelables.get(i).getId()));
            }
        }

        boolean isMyEventActivity = getActivity().getIntent().getBooleanExtra("isMyEvent", true);
        localsList = (ListView) rootView.findViewById(R.id.localsListView);
        LocalsAdapter localsAdapter = new LocalsAdapter(getContext(), R.layout.locals_card, users, isMyEventActivity);
        localsList.setAdapter(localsAdapter);

        final ArrayList<User> newParcelables = new ArrayList<>();
        for (int i = 0; i < parcelables.size(); i++) {
            if (parcelables.get(i).getStatus() != 1){
                newParcelables.add(parcelables.get(i));
            }
        }


        localsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                ArrayList<String> usersIDS = new ArrayList<>();
                usersIDS.add(myID);
                usersIDS.add(users.get(i).getUserID());

                Intent chatActivity = new Intent(getContext(), ChatActivity.class);
                chatActivity.putStringArrayListExtra("usersIDS", usersIDS);
                chatActivity.putExtra("eventID", eventID);
                chatActivity.putExtra("chat_name", newParcelables.get(i).getEmail());
                chatActivity.putExtra("quantity", 3);
                chatActivity.putExtra("isNewChat", true);
                chatActivity.putExtra("one_on_one", true);
                startActivity(chatActivity);
            }
        });

        return rootView;
    }

    private void getGroupChatList(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        FacelocationAPI api = retrofit.create(FacelocationAPI.class);

        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("Content-Type", "application/json");
        headers.put("X-Auth", token);

        Call<List<MainChatResponse>> call = api.getChat(headers, eventID);
        call.enqueue(new Callback<List<MainChatResponse>>() {
            @Override
            public void onResponse(Call<List<MainChatResponse>> call, Response<List<MainChatResponse>> response) {
                Log.i(TAG, "ОТВЕТ СЕРВЕРА НА ПОЛУЧЕНИЕ СПИСКА ГРУППОВЫХ ЧАТОВ: " + response.toString());
                ArrayList<Group> groups = new ArrayList<>();
                List<MainChatResponse> chatResponses = response.body();

                chatID = new ArrayList<>();
                usersIDS = new ArrayList<>();
                usersIDSarray = new ArrayList<>();
                groupChatNames = new ArrayList<>();
                quantitys = new ArrayList<>();

                for (int i = 0; i < chatResponses.size(); i++) {
                    MainChatResponse groupResponseBody = chatResponses.get(i);

                    Log.i(TAG, "ТАЙП В ПОЛУЧЕННОМ ЧАТЕ: " + groupResponseBody.getType());


                    if (groupResponseBody.getType() == 2){
                        groupChatName = groupResponseBody.getTitle();
                        quantity = groupResponseBody.getParticipants().size();

                        Log.i(TAG, "ID ГРУППОВОГО ЧАТА: " + groupResponseBody.getId());

                        chatID.add(groupResponseBody.getId());

                        StringBuilder stringBuilder = new StringBuilder();
                        String groupChatUserId;
                        for (int j = 0; j < groupResponseBody.getParticipants().size(); j++) {
                            groupChatUserId = groupResponseBody.getParticipants().get(j).getId();
                            stringBuilder.append(groupResponseBody.getParticipants().get(j).getEmail());
                            stringBuilder.append(" ");
                            usersIDS.add(groupChatUserId);
                        }

                        usersIDSarray.add(usersIDS);
                        groupChatNames.add(groupChatName);
                        quantitys.add(quantity);

                        String groupMembers = stringBuilder.toString();
                        Log.i(TAG, "УЧАСНИКИ ЧАТА: " + groupMembers);

                        Group group = new Group(groupChatName, groupMembers, groupResponseBody.getParticipants().size());
                        groups.add(group);
                    }
                }

                groupList = (ListView) rootView.findViewById(R.id.groupsListView);
                GroupAdapter groupAdapter = new GroupAdapter(getContext(), R.layout.group_card, groups);
                groupList.setAdapter(groupAdapter);

                groupList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Intent chatActivity = new Intent(getContext(), ChatActivity.class);
                        chatActivity.putExtra("chatID", chatID.get(i));
                        chatActivity.putStringArrayListExtra("usersIDS", usersIDSarray.get(i));
                        chatActivity.putExtra("eventID", eventID);
                        chatActivity.putExtra("chat_name", groupChatNames.get(i));
                        chatActivity.putExtra("quantity", quantitys.get(i));
                        chatActivity.putExtra("isNewChat", false);
                        startActivity(chatActivity);
                    }
                });
            }

            @Override
            public void onFailure(Call<List<MainChatResponse>> call, Throwable t) {
                Log.i(TAG, "ОШИБКА ПОЛУЧЕНИЯ ГРУППОВЫХ ЧАТОВ: " + t.toString());
            }
        });
    }
}
