package com.example.application.demowhatapp.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.application.demowhatapp.R;
import com.example.application.demowhatapp.models.ChatMessage;

import java.util.ArrayList;

public class ChatArrayAdapter extends ArrayAdapter<ChatMessage> {

    private TextView chatText;
    private LinearLayout singleMessageContainer;

    ArrayList<ChatMessage> arrayMap;

    public ChatArrayAdapter(Context context, int textViewResourceId, ArrayList<ChatMessage> objects) {
        super(context, textViewResourceId, objects);
        arrayMap = objects;
    }
//    http://abhiandroid.com/ui/custom-arrayadapter-tutorial-example.html
//    https://javapapers.com/android/android-chat-bubble/
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        Log.i("---", "getView = " + row);
        Log.i("---", "position = " + position);
        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.custom_list_item_1, parent, false);
        }
        singleMessageContainer = (LinearLayout) row.findViewById(R.id.singleMessageContainer);
        chatText = (TextView) row.findViewById(R.id.singleMessage);
//        if( arrayMap.size() > 0 ) {
//            for(ChatMessage chat : arrayMap) {
            Log.i("arrayMap" , arrayMap.toString());
            chatText.setText(arrayMap.get(position).getMessage().toString());
            chatText.setBackgroundResource(arrayMap.get(position).isLeft() ? R.drawable.bubble_b : R.drawable.bubble_a);
            singleMessageContainer.setGravity(arrayMap.get(position).isLeft() ? Gravity.LEFT : Gravity.RIGHT);
//            }
//            chatText.setText(message);
//            chatText.setBackgroundResource(left ? R.drawable.bubble_b : R.drawable.bubble_a);
//            singleMessageContainer.setGravity(left ? Gravity.LEFT : Gravity.RIGHT);

//        }

        return row;
    }

    public Bitmap decodeToBitmap(byte[] decodedByte) {
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }
}
