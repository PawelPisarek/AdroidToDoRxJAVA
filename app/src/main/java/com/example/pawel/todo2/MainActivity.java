
package com.example.pawel.todo2;

import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.example.pawel.todo2.adapter.MessagesList;
import com.example.pawel.todo2.dao.Message;
import com.example.pawel.todo2.dao.MessageNewDao;
import com.example.pawel.todo2.model.Task;
import com.example.pawel.todo2.model.TaskNew;
import com.example.pawel.todo2.service.ServiceFactory;
import com.example.pawel.todo2.service.TaskService;

import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    public static final String NAME_STRING = "name";
    private ArrayList<String> taskList = new ArrayList<>();
    private PublishSubject<String> onLocationUpdated = PublishSubject.create();
    private String loginEmail;
    private String receiver;
    private String sender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        updateUI();
        String stringExtra = !getIntent().getStringExtra(NAME_STRING).trim().isEmpty() ? getIntent().getStringExtra(NAME_STRING) : "nie wprowadziłeś loginu,";
        loginEmail = stringExtra;
        receiver = loginEmail.split(",")[0];
        sender = loginEmail.split(",")[1];

        setTitle("K<-" + stringExtra);


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
        getNew();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Log.d("rekurencja","nadal działa");
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


                                MessageNewDao messageNewDao = new MessageNewDao(sender, String.valueOf(taskEditText.getText()), receiver);
                                postData(messageNewDao);
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

    private void getNew() {
        TaskService service = ServiceFactory.createRetrofitService(TaskService.class, TaskService.SERVICE_ENDPOINT);
        service.getNewMessages(loginEmail)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<com.example.pawel.todo2.model.MessageNew>() {
                    @Override
                    public final void onCompleted() {
                        // do nothing

                    }

                    @Override
                    public final void onError(Throwable e) {
                        Log.e("TaskDemo", "" + e.getMessage());
                        getNew();
                    }

                    @Override
                    public void onNext(com.example.pawel.todo2.model.MessageNew t) {

                        Log.d("czy pobać nowe komunika", t.getEmail());
                        if (Objects.equals(loginEmail, t.getEmail())) {

                            onLocationUpdated.onNext("asdsad");
                        }
                        getNew();

                    }
                });
    }


    private void postData(MessageNewDao task) {
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

//                        onLocationUpdated.onNext("asdsad");

                    }
                });
    }

    private void updateUI() {

        Log.d("co tu", String.valueOf(taskList));

        TaskService service = ServiceFactory.createRetrofitService(TaskService.class, TaskService.SERVICE_ENDPOINT);
        service.getMessages()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Message>>() {
                    @Override
                    public final void onCompleted() {
                        // do nothing
                    }

                    @Override
                    public final void onError(Throwable e) {
                        Log.e("TaskDemo", e.getMessage());
                    }

                    @Override
                    public void onNext(List<Message> tasks) {

                        List<Message> tasks2 = new ArrayList<Message>();
                        for (Message task : tasks) {
                            if (task.equalEmail(task.getReceiver(), receiver) && task.equalEmail(task.getSender(), sender) ||
                                    task.equalEmail(task.getReceiver(), sender) && task.equalEmail(task.getSender(), receiver)) {
                                tasks2.add(task);
                            }
                        }
                        updateUI2(tasks2);
                    }
                });
    }


    public void updateUI2(List<Message> collection) {

        ListView messagesList = (ListView) findViewById(R.id.list_todo);

        MessagesList adapter_listy = new MessagesList(this, collection, sender);

        messagesList.setAdapter(adapter_listy);

    }

}
