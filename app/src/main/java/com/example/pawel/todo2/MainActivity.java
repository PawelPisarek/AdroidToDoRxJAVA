
package com.example.pawel.todo2;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.pawel.todo2.adapter.TaskAdapter;
import com.example.pawel.todo2.db.TaskContract;
import com.example.pawel.todo2.db.TaskDbHelper;
import com.example.pawel.todo2.model.Task;
import com.example.pawel.todo2.model.TaskNew;
import com.example.pawel.todo2.model.Tasks;
import com.example.pawel.todo2.service.TaskService;
import com.example.pawel.todo2.service.ServiceFactory;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private TaskDbHelper mHelper;

    private ListView mTaskListView;
    private String responseBody;
    //    private ArrayAdapter<String> mAdapter;
//    private ArrayList<String> taskList = new ArrayList<>();
    private Tasks taskList;
    private TaskAdapter taskAd;
    private PublishSubject<String> onLocationUpdated = PublishSubject.create();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        taskList = new Tasks(new ArrayList<String>());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mHelper = new TaskDbHelper(this);
        mTaskListView = (ListView) findViewById(R.id.list_todo);
        TaskService service = ServiceFactory.createRetrofitService(TaskService.class, TaskService.SERVICE_ENDPOINT);


        onLocationUpdated.subscribe(new Subscriber<String>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable throwable) {
            }

            @Override
            public void onNext(String s) {
                Log.d("co tu 10", s);
                updateUI();
            }
        });
        refres();
        taskAd = new TaskAdapter(this, taskList);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_task:
                final EditText taskEditText = new EditText(this);
                AlertDialog dialog = new AlertDialog.Builder(this)
                        .setTitle("Add a new task")
                        .setMessage("What do you want to do next?")
                        .setView(taskEditText)
                        .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String task = String.valueOf(taskEditText.getText());
                                SQLiteDatabase db = mHelper.getWritableDatabase();
                                ContentValues values = new ContentValues();
                                values.put(TaskContract.TaskEntry.COL_TASK_TITLE, task);
                                db.insertWithOnConflict(TaskContract.TaskEntry.TABLE,
                                        null,
                                        values,
                                        SQLiteDatabase.CONFLICT_REPLACE);
                                db.close();
                                postData(new TaskNew(String.valueOf(taskEditText.getText())));
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .create();
                dialog.show();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void postData(TaskNew task) {
        TaskService service = ServiceFactory.createRetrofitService(TaskService.class, TaskService.SERVICE_ENDPOINT);
        service.postUser(task)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Task>() {
                    @Override
                    public final void onCompleted() {
                        // do nothing
                    }

                    @Override
                    public final void onError(Throwable e) {
                        Log.e("TaskDemo", e.getMessage());
                    }

                    @Override
                    public void onNext(Task t) {

                        Log.d("co tu 12321321", t.getId());
                        refres();
                    }
                });

    }


    private void refres() {
        TaskService service = ServiceFactory.createRetrofitService(TaskService.class, TaskService.SERVICE_ENDPOINT);
        service.getUser()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Task>>() {
                    @Override
                    public final void onCompleted() {
                        // do nothing
                        onLocationUpdated.onNext("asdsad");
                    }

                    @Override
                    public final void onError(Throwable e) {
                        Log.e("TaskDemo", e.getMessage());
                    }

                    @Override
                    public void onNext(List<Task> tasks) {

                        for (Task t : tasks) {
                            Log.d("co tu 2", t.getTitle());
                            taskList.pushD(t.getTitle());

                        }


                    }
                });
    }

    private void updateUI() {

        taskAd.update(taskList);
    }

    public void deleteTask(View view) {
        View parent = (View) view.getParent();
        TextView taskTextView = (TextView) parent.findViewById(R.id.task_title);
        String task = String.valueOf(taskTextView.getText());
        SQLiteDatabase db = mHelper.getWritableDatabase();
        db.delete(TaskContract.TaskEntry.TABLE,
                TaskContract.TaskEntry.COL_TASK_TITLE + " = ?",
                new String[]{task});
        db.close();
        updateUI();
    }

}
