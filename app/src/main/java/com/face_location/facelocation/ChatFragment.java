package com.face_location.facelocation;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.face_location.facelocation.model.DataBase.DataBaseHelper;
import com.face_location.facelocation.model.FacelocationAPI;
import com.face_location.facelocation.model.GetMainChat.MainChatResponse;
import com.face_location.facelocation.model.GetMainChat.Message;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
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

public class ChatFragment extends Fragment {

    private static final String TAG = "ChatFragment";

    View rootView;
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager manager;
    EditText messageEditText;
    Button sendBtn;
    String url, token, eventID, myID, chatID, myAvatar;
    String[] applicationData;

    com.github.nkzawa.socketio.client.Socket socket;

    List<String> chatMessages = new ArrayList<>();
    List<String> senders = new ArrayList<>();
    List<String> avatars = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.chat_fragment, container, false);

        url = getString(R.string.base_url);
        DataBaseHelper applicationDB = DataBaseHelper.getInstance(getContext());
        applicationData = applicationDB.retrieveFirstLoginValues();
        token = applicationData[5];
        myID = applicationData[0];
        myAvatar = applicationData[10];

        Bundle bundle = getArguments();
        eventID = bundle.getString("eventID");
        Log.i(TAG, "Какой ивент проверяем: " + eventID);

        chatMessages.clear();
        senders.clear();
        avatars.clear();

        try{
            socket = IO.socket("https://face-location.com:443?token=" + token);
        } catch (URISyntaxException e){
            Log.i(TAG, "ОШИБКА СОКЕТА: ");
        }

        getChatData();
        
        messageEditText = (EditText) rootView.findViewById(R.id.messageEditText);
        sendBtn = (Button) rootView.findViewById(R.id.sendBtn);
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "ОТПРАВИТЬ СООБЩЕНИЕ: ");
                try {
                    sendMessage();
                } catch (JSONException e) {
                    Log.i(TAG, "onClick: " + e.toString());;
                }
            }
        });

        socket.on("new-message-mob", handling);




        return rootView;
    }

    private Emitter.Listener handling = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            Log.i(TAG, "ХЕНДЛЕР: " + args[0].toString());

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    String senderID;
                    String avatar;
                    String message;
                    try {
                        senderID = data.getJSONObject("message").getString("user");
                        avatar = data.getJSONObject("message").getString("avatar_mob");
                        message = data.getJSONObject("message").getString("text");

                    } catch (JSONException e) {
                        Log.i(TAG, "run: " + e.toString());
                        return;
                    }

                    // add the message to view
                    addMessage(message, senderID, avatar);
                }
            });
        }
    };


    private void sendMessage() throws JSONException {
        String messageText = messageEditText.getText().toString().trim();
        messageEditText.setText("");

        JSONObject messageData = new JSONObject();
        messageData.put("chat", chatID);

        JSONObject message = new JSONObject();
        message.put("user", myID);
        message.put("text", messageText);

        messageData.put("message", message);
        messageData.put("user", myID);

        Log.i(TAG, "sendMessage JSON: " + messageData);
        socket.emit("save-message", messageData);
        addMessage(messageText, myID, myAvatar);
    }

    private void addMessage(String message, String senderID, String avatar) {
        chatMessages.add(message);
        senders.add(senderID);
        avatars.add(avatar);

        adapter.notifyItemInserted(chatMessages.size() - 1);
        recyclerView.smoothScrollToPosition(chatMessages.size() - 1);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        socket.disconnect();
        socket.off("new-message-mob", handling);
    }

    private void getChatData(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        FacelocationAPI api = retrofit.create(FacelocationAPI.class);

        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("Content-Type", "application/json");
        headers.put("X-Auth", token);

        Call <List<MainChatResponse>> call = api.getChat(headers, eventID);
        call.enqueue(new Callback<List<MainChatResponse>>() {
            @Override
            public void onResponse(Call<List<MainChatResponse>> call, Response<List<MainChatResponse>> response) {
                Log.i(TAG, "ОТВЕТ СЕРВЕРА - ПОЛУЧИТЬ ЧАТ: " + response.toString());

                List<MainChatResponse> chatResponse = response.body();
                for (int i = 0; i < chatResponse.size(); i++) {
                    MainChatResponse mainChat = chatResponse.get(i);

                    if (mainChat.getType() == 0){
                        chatID = mainChat.getId();

                        socket.connect();
                        JSONObject chatRoom = new JSONObject();
                        try {
                            chatRoom.put("chat", chatID);
                            chatRoom.put("user", myID);
                        } catch (JSONException e) {
                            Log.i(TAG, "ОШИБКА СОЗДАНИЯ JSON: " + e.toString());
                        }
                        socket.emit("join", chatRoom);

                        List<Message> messages = mainChat.getMessages();

                        String userID;
                        String userMessage;
                        String userAvatar;

                        for (int j = 0; j < messages.size(); j++) {
                            Message message = messages.get(j);

                            userID = message.getUser().getId();
                            senders.add(userID);

                            userMessage = message.getText();
                            chatMessages.add(userMessage);

                            userAvatar = message.getUser().getAvatarMob();
                            avatars.add(userAvatar);
                        }

                        recyclerView = (RecyclerView) rootView.findViewById(R.id.mainChat);
                        adapter = new ChatAdapter(chatMessages, senders, avatars, myID);
                        recyclerView.setAdapter(adapter);

                        manager = new LinearLayoutManager(getContext());
                        recyclerView.setLayoutManager(manager);
                        recyclerView.scrollToPosition(chatMessages.size() - 1);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<MainChatResponse>> call, Throwable t) {
                Log.i(TAG, "ОШИБКА НА ПОЛУЧЕНИЕ ЧАТА: " + t.toString());

            }
        });
    }
}
