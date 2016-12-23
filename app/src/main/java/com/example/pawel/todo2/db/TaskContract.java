package com.example.pawel.todo2.db;

import android.provider.BaseColumns;

/**
 * Created by pawel on 2016-12-23.
 */

public class TaskContract {
    public static final String DB_NAME= "com.aziflaj.todolist.db";
    public static final int DB_VERSION = 1;
    public class TaskEntry implements BaseColumns {
        public static final String TABLE ="task";
        public static final String COL_TASK_TITLE = "title";
    }
}
