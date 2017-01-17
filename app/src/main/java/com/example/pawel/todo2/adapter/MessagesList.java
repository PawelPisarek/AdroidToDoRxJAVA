package com.example.pawel.todo2.adapter;


import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.pawel.todo2.R;
import com.example.pawel.todo2.dao.Message;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pawel on 2017-01-17.
 */

public class MessagesList extends BaseAdapter {


    private List<Message> userList;

    private Context context;
    private String author;

    public MessagesList(Context context, List<Message> userList, String author) {
        this.context = context;
        this.userList = userList;
        this.author = author;

    }

    @Override
    public int getCount() {
        return userList.size();
    }

    @Override
    public Message getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    private class ViewHolderPattern {
        TextView messageContent;
        TextView messageAuthor;
        int color;
    }


    public View getView(int position, View convertView, ViewGroup parent) {


        ViewHolderPattern view_holder;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_todo, parent, false);

            view_holder = new ViewHolderPattern();
            view_holder.messageContent = (TextView) convertView.findViewById(R.id.task_title);
            view_holder.messageAuthor = (TextView) convertView.findViewById(R.id.task_email);
            if (author.equals(userList.get(position).getSender())) {

                view_holder.color = (Color.parseColor("#E43F3F"));
            }

            convertView.setTag(view_holder);
        } else {
            view_holder = (ViewHolderPattern) convertView.getTag();
        }

        view_holder.messageContent.setText(userList.get(position).getContent());
        view_holder.messageAuthor.setText(userList.get(position).getSender());
        if(userList.get(position).equalEmail(userList.get(position).getSender(),author)){

            convertView.setBackgroundColor(view_holder.color);
        }

        return convertView;
    }
}
