package com.example.application.demowhatapp.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.application.demowhatapp.R;
import com.example.application.demowhatapp.adapters.ChatArrayAdapter;
import com.example.application.demowhatapp.models.ChatMessage;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    String activeUser = "";
    ArrayList<String> messages = new ArrayList<>();
    ArrayAdapter arrayAdapter;
    EditText chatEditText;

    public void sendChat(View view) {
        if(chatEditText.getText().toString().isEmpty()) {
            Toast.makeText(ChatActivity.this, "chat can not be empty" ,Toast.LENGTH_SHORT).show();
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
                        messages.add(messageContent);
                        arrayAdapter.notifyDataSetChanged();
                        chatEditText.clearFocus();
                        Toast.makeText(ChatActivity.this, "success" ,Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ChatActivity.this, e.getMessage().substring(e.getMessage().indexOf(" ")) ,Toast.LENGTH_SHORT).show();
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
        arrayAdapter = new ArrayAdapter(ChatActivity.this, android.R.layout.simple_list_item_1, messages);

//        chatArrayAdapter = new ChatArrayAdapter(getApplicationContext(), R.layout.custom_list_item_1);
        chatListView.setAdapter(arrayAdapter);
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
                        messages.clear();
                        for(ParseObject message : objects) {
                            String messageContent = message.getString("message");

                            if( !message.getString("sender").equals(ParseUser.getCurrentUser().getUsername()) ) {
                                messageContent = "> " + messageContent;
                                Log.i("messageContent", messageContent);
                            }

                            messages.add(messageContent);
                        }

                        arrayAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }
}
