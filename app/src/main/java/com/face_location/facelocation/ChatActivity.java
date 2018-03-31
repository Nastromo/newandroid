package com.face_location.facelocation;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.face_location.facelocation.model.DataBase.DataBaseHelper;
import com.face_location.facelocation.model.FacelocationAPI;
import com.face_location.facelocation.model.GetMainChat.MainChatResponse;
import com.face_location.facelocation.model.GetMainChat.Message;
import com.face_location.facelocation.model.PostChat.ChatBody;
import com.face_location.facelocation.model.PostChat.ChatResponse;
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

public class ChatActivity extends AppCompatActivity {

    ImageView avatar;
    Button sendBtn;
    TextView backTextView, eventName, usersQuantity;
    EditText messageEditText;
    DataBaseHelper applicationDB;
    String[] userArrayData;
    String url, token, eventID, myID, chatID, userAvatar, chatName;
    private static final String TAG = "ChatActivity";

    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager manager;

    List<String> chatMessages = new ArrayList<>();
    List<String> senders = new ArrayList<>();
    List<String> avatars = new ArrayList<>();
    List<String> usersIDS = new ArrayList<>();

    com.github.nkzawa.socketio.client.Socket socket;
    int type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        url = getString(R.string.base_url);
        applicationDB = DataBaseHelper.getInstance(this);
        userArrayData = applicationDB.retrieveFirstLoginValues();
        token = userArrayData[5];
        myID = userArrayData[0];
        userAvatar = userArrayData[10];
        chatID = getIntent().getStringExtra("chatID");
        eventID = getIntent().getStringExtra("eventID");
        usersIDS = getIntent().getStringArrayListExtra("usersIDS");

        chatMessages.clear();
        senders.clear();
        avatars.clear();

        backTextView = (TextView) findViewById(R.id.backTextView);
        backTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        avatar = (ImageView) findViewById(R.id.avatar);
        avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myProfile = new Intent (ChatActivity.this, MyProfileActivity.class);
                startActivity(myProfile);
            }
        });



        if (userAvatar != null){
            avatar.setBackground(null);
            Glide
                    .with(ChatActivity.this)
                    .load(userAvatar)
                    .thumbnail(0.1f) //shows mini image which weight 0.1 from real image while real image is downloading
                    .apply(RequestOptions
                            .circleCropTransform())
                    //      .placeholder(R.drawable.oval)) //shows drawable while real/mini image is downloading
                    .into(avatar);
        }


        eventName = (TextView) findViewById(R.id.eventName);
        chatName = getIntent().getStringExtra("chat_name");
        eventName.setText(chatName);

        usersQuantity = (TextView) findViewById(R.id.usersQuantity);
        usersQuantity.setText(String.valueOf(getIntent().getIntExtra("quantity", -1)));

        messageEditText = (EditText) findViewById(R.id.messageEditText);

        try{
            socket = IO.socket("https://face-location.com:443?token=" + token);
        } catch (URISyntaxException e){
            Log.i(TAG, "ОШИБКА СОКЕТА: ");
        }

        boolean isNewChat = getIntent().getBooleanExtra("isNewChat", true);
        boolean isOneOnOne = getIntent().getBooleanExtra("one_on_one", false);

        if (isNewChat){
            Log.i(TAG, "isNewChat: " + isNewChat);
            if (isOneOnOne){
                Log.i(TAG, "isOneOnOne: " + isOneOnOne);
                type = 1;
                createChat();
                usersQuantity.setText(String.valueOf(2));
            }else {
                type = 2;
                createChat();
                Log.i(TAG, "СРАБОТАЛО СОЗДАТЬ ГРУППОВОЙ ЧАТ: " + type);
            }
        }else {
            Log.i(TAG, "ID которое прошло в элсе: " + chatID);
            getChat();
        }


        sendBtn = (Button) findViewById(R.id.sendBtn);
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
    }

    private Emitter.Listener handling = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            Log.i(TAG, "ХЕНДЛЕР: " + args[0].toString());

            runOnUiThread(new Runnable() {
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
        addMessage(messageText, myID, userAvatar);
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

    private void createChat(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        FacelocationAPI api = retrofit.create(FacelocationAPI.class);

        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("Content-Type", "application/json");
        headers.put("X-Auth", token);

        Log.i(TAG, "АЙДИ ИВЕНТА В КОТОРОМ СОЗДАЕМ ЧАТ: " + eventID);
        Log.i(TAG, "Список юзеров в чате: " + usersIDS.size());
        Log.i(TAG, "ТАЙП ЦІФРА: " + type);

        String[] usersIDarray = new String[usersIDS.size()];

        for (int i = 0; i < usersIDS.size(); i++) {
            usersIDarray[i] = usersIDS.get(i);
        }

        ChatBody chatBody = new ChatBody(chatName, eventID, usersIDarray, type);

        Call<ChatResponse> call = api.createChat(headers, chatBody);
        call.enqueue(new Callback<ChatResponse>() {
            @Override
            public void onResponse(Call<ChatResponse> call, Response<ChatResponse> response) {
                Log.i(TAG, "ОТВЕТ СЕРВЕРА - СОЗДАТЬ ЧАТ: " + type + " " + response.toString());

                chatID = response.body().getId();
                Log.i(TAG, "ТАЙП В СОЗДАНОМ ЧАТЕ: " + type);
                Log.i(TAG, "ID ЧАТА КОТОРИЙ ВЕРНУЛСЯ: " + chatID);

                socket.connect();
                JSONObject chatRoom = new JSONObject();
                try {
                    chatRoom.put("chat", chatID);
                    chatRoom.put("user", myID);
                } catch (JSONException e) {
                    Log.i(TAG, "ОШИБКА СОЗДАНИЯ JSON: " + e.toString());
                }
                socket.emit("join", chatRoom);

                List<Message> messages = response.body().getMessages();

                String userID;
                String userMessage;
                String userAvatar;

                if (!messages.isEmpty()){
                    for (int j = 0; j < messages.size(); j++) {
                        Message message = messages.get(j);

                        userID = message.getUser().getId();
                        Log.i(TAG, "ID ЮЕЗАРА В ЧАТЕ: " + userID);
                        senders.add(userID);

                        userMessage = message.getText();
                        Log.i(TAG, "СООБЩЕНИЕ ЮЕЗАРА В ЧАТЕ: " + userMessage);
                        chatMessages.add(userMessage);

                        userAvatar = message.getUser().getAvatarMob();
                        Log.i(TAG, "АВАТАР ЮЕЗАРА В ЧАТЕ: " + userAvatar);
                        avatars.add(userAvatar);
                    }
                }

                recyclerView = (RecyclerView) findViewById(R.id.mainChat);
                adapter = new ChatAdapter(chatMessages, senders, avatars, myID);
                recyclerView.setAdapter(adapter);

                manager = new LinearLayoutManager(ChatActivity.this);
                recyclerView.setLayoutManager(manager);
                recyclerView.scrollToPosition(chatMessages.size() - 1);
            }

            @Override
            public void onFailure(Call<ChatResponse> call, Throwable t) {
                Log.i(TAG, "ОШИБКА НА ПОЛУЧЕНИЕ НОВОГО ЧАТА: " + t.toString());

            }
        });
    }

    private void getChat(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        FacelocationAPI api = retrofit.create(FacelocationAPI.class);

        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("Content-Type", "application/json");
        headers.put("X-Auth", token);

        Call <List<MainChatResponse>> call = api.getChatByID(headers, chatID);
        call.enqueue(new Callback<List<MainChatResponse>>() {
            @Override
            public void onResponse(Call<List<MainChatResponse>> call, Response<List<MainChatResponse>> response) {
                Log.i(TAG, "ОТВЕТ СЕРВЕРА - ПОЛУЧИТЬ ГРУППОВОЙ ЧАТ: " + response.toString());
                List<MainChatResponse> chatResponse = response.body();

                for (int i = 0; i < chatResponse.size(); i++) {
                    MainChatResponse mainChat = chatResponse.get(i);

                    if (mainChat.getId().equals(chatID)){
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
                            Log.i(TAG, "ID ЮЕЗАРА В ЧАТЕ: " + userID);
                            senders.add(userID);

                            userMessage = message.getText();
                            Log.i(TAG, "СООБЩЕНИЕ ЮЕЗАРА В ЧАТЕ: " + userMessage);
                            chatMessages.add(userMessage);

                            userAvatar = message.getUser().getAvatarMob();
                            Log.i(TAG, "АВАТАР ЮЕЗАРА В ЧАТЕ: " + userAvatar);
                            avatars.add(userAvatar);
                        }

                        recyclerView = (RecyclerView) findViewById(R.id.mainChat);
                        adapter = new ChatAdapter(chatMessages, senders, avatars, myID);
                        recyclerView.setAdapter(adapter);

                        manager = new LinearLayoutManager(ChatActivity.this);
                        recyclerView.setLayoutManager(manager);
                        recyclerView.scrollToPosition(chatMessages.size() - 1);

                    }
                }
            }

            @Override
            public void onFailure(Call<List<MainChatResponse>> call, Throwable t) {
                Log.i(TAG, "ОШИБКА НА ПОЛУЧЕНИЕ ГРУППОВОГО ЧАТА: " + t.toString());

            }
        });
    }
}
