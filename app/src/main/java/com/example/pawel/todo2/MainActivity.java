
package com.example.pawel.todo2;

import android.content.DialogInterface;
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

import com.example.pawel.todo2.model.Task;
import com.example.pawel.todo2.model.TaskNew;
import com.example.pawel.todo2.service.ServiceFactory;
import com.example.pawel.todo2.service.TaskService;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private ListView mTaskListView;
    private ArrayAdapter<String> mAdapter;
    private  ArrayList<String> taskList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTaskListView = (ListView) findViewById(R.id.list_todo);
        updateUI();
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
                                postData(new TaskNew(String.valueOf(taskEditText.getText())));
                                //updateUI();
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
                        updateUI();

                    }
                });
    }
    private void deleteTask(String id) {
        Log.d("usuun",id);
        TaskService service = ServiceFactory.createRetrofitService(TaskService.class, TaskService.SERVICE_ENDPOINT);
        service.deleteTask(id)
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

                    }
                });
    }
    private void updateUI() {

        Log.d("co tu", String.valueOf(taskList));

        TaskService service = ServiceFactory.createRetrofitService(TaskService.class, TaskService.SERVICE_ENDPOINT);
            service.getUser()
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<List<Task>>() {
                        @Override
                        public final void onCompleted() {
                            // do nothing
                        }

                        @Override
                        public final void onError(Throwable e) {
                            Log.e("TaskDemo", e.getMessage());
                        }

                        @Override
                        public void onNext(List<Task> tasks) {
                            List<String> collection = new ArrayList<String>();
                            for(Task t:tasks){
                                Log.d("co tu 2", t.getTitle());
                                collection.add(t.getTitle());
                            }
                            updateUI2(collection);

                        }
                    });


    }

    public void updateUI2(List<String> collection){
        if (mAdapter == null) {
            mAdapter = new ArrayAdapter<>(this,
                    R.layout.item_todo,
                    R.id.task_title,
                    collection);
            mTaskListView.setAdapter(mAdapter);
        } else {
            mAdapter.clear();
            mAdapter.addAll(collection);
            mAdapter.notifyDataSetChanged();
        }
    }
    public void deleteTask(View view) {
        View parent = (View) view.getParent();
        TextView taskTextView = (TextView) parent.findViewById(R.id.task_title);
        String task = String.valueOf(taskTextView.getId()); ///nie właściwe id
        deleteTask(task);
        updateUI();
    }

}
