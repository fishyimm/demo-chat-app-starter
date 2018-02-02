package com.example.application.demowhatapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.application.demowhatapp.R;
import com.example.application.demowhatapp.adapters.ChatArrayAdapter;
import com.example.application.demowhatapp.models.ChatMessage;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity2 extends AppCompatActivity {

    private static final String TAG = "ChatActivity2";
    String activeUser = "";
    EditText chatEditText;

    ArrayList<ChatMessage> listMap = new ArrayList<ChatMessage>();
    private ChatArrayAdapter chatArrayAdapter;

    public void sendChat(View view) {
        if(chatEditText.getText().toString().isEmpty()) {
            Toast.makeText(ChatActivity2.this, "chat can not be empty" ,Toast.LENGTH_SHORT).show();
        } else {
            ParseObject message = new ParseObject("Message");

            final String messageContent = chatEditText.getText().toString();
            message.put("sender", ParseUser.getCurrentUser().getUsername());
            message.put("recipient", activeUser);
            message.put("message", messageContent);

            chatEditText.setText("");

            message.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                if(e == null) {
                    listMap.add(new ChatMessage(false, messageContent));
                    chatArrayAdapter.notifyDataSetChanged();
                    chatEditText.clearFocus();
                    Toast.makeText(ChatActivity2.this, "success" ,Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ChatActivity2.this, e.getMessage().substring(e.getMessage().indexOf(" ")) ,Toast.LENGTH_SHORT).show();
                }
                }
            });
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        chatEditText = (EditText)findViewById(R.id.chatEditText);
        Intent intent = getIntent();
        activeUser = intent.getStringExtra("username");
        setTitle("Chat with " + activeUser);

        ListView chatListView = (ListView) findViewById(R.id.chatListView);
//        https://stackoverflow.com/questions/9280965/arrayadapter-requires-the-resource-id-to-be-a-textview-xml-problems
        chatArrayAdapter = new ChatArrayAdapter(getApplicationContext(), R.layout.custom_list_item_1, listMap);
        chatListView.setAdapter(chatArrayAdapter);
//        chatListView.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
//        chatListView.setSelection(chatListView.getAdapter().getCount()-1);

        ParseQuery<ParseObject> query1 = new ParseQuery<ParseObject>("Message");
        query1.whereEqualTo("sender", ParseUser.getCurrentUser().getUsername());
        query1.whereEqualTo("recipient", activeUser);

        ParseQuery<ParseObject> query2 = new ParseQuery<ParseObject>("Message");
        query2.whereEqualTo("recipient", ParseUser.getCurrentUser().getUsername());
        query2.whereEqualTo("sender", activeUser);

        List<ParseQuery<ParseObject>> queryList = new ArrayList<ParseQuery<ParseObject>>();
        queryList.add(query1);
        queryList.add(query2);

        ParseQuery<ParseObject> query = ParseQuery.or(queryList);
        query.orderByAscending("createdAt");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e == null) {
                    if(objects.size() > 0) {
                        listMap.clear();
                        for(ParseObject message : objects) {
                            String messageContent = message.getString("message");
                            Boolean left = true;
                            if( message.getString("sender").equals(ParseUser.getCurrentUser().getUsername()) ) {
                                left = false;
                            }

                            ChatMessage chatMessage = new ChatMessage(left, messageContent);
                            listMap.add(chatMessage);
                            Log.i("messageContent", messageContent);
                        }
                        chatArrayAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }
}
