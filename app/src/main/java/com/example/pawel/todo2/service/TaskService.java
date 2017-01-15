package com.example.pawel.todo2.service;



import com.example.pawel.todo2.model.MessageNew;
import com.example.pawel.todo2.model.Task;
import com.example.pawel.todo2.model.TaskNew;

import java.util.List;

import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.Query;
import rx.Observable;

public interface TaskService {
    String SERVICE_ENDPOINT = "http://10.0.2.2:2000";

    @GET("/task")
    Observable<List<Task>> getMessages();

    @GET("/task-new")  //task-new?email=1@wp.pl212
    Observable<MessageNew> getNewMessages(@Query("email") String address);

    @DELETE("/task/{id}")
    Observable<Task> deleteTask(@Path("id") String id);
    @POST("/task")
    Observable<Task> postUser(@Body TaskNew body);
}
