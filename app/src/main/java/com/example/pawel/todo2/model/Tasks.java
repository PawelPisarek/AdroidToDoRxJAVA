package com.example.pawel.todo2.model;

import java.util.ArrayList;

/**
 * Created by pawel on 2016-12-24.
 */

public class Tasks {
   private ArrayList<String> taskList;

    public ArrayList<String> getTaskList() {
        return taskList;
    }

    public Tasks(ArrayList<String> taskList) {
        this.taskList = taskList;
    }

    public void  pushD(String data ){
        taskList.add(data);
    }

}
