package com.example.pawel.todo2.adapter;

import android.content.Context;
import android.widget.ArrayAdapter;

import com.example.pawel.todo2.R;
import com.example.pawel.todo2.model.Tasks;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by pawel on 2016-12-24.
 */

public class TaskAdapter {

    ArrayAdapter mAdapter;

    public ArrayAdapter getmAdapter() {
        return mAdapter;
    }

    public TaskAdapter(Context context, Tasks taskList) {
        new ArrayAdapter<>(context,
                R.layout.item_todo,
                R.id.task_title,
                taskList.getTaskList());

    }

    public void update(Tasks data){
        mAdapter.clear();
        mAdapter.addAll(data.getTaskList());
        mAdapter.notifyDataSetChanged();

    }
}
