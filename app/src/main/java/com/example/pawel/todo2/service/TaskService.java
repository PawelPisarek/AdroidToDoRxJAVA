package com.example.pawel.todo2.service;



import com.example.pawel.todo2.model.Task;
import com.example.pawel.todo2.model.TaskNew;

import java.util.List;

import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import rx.Observable;

public interface TaskService {
    String SERVICE_ENDPOINT = "http://10.0.2.2:3000";

    @GET("/task")
    Observable<List<Task>> getUser();
    @POST("/task")
    Observable<Task> postUser(@Body TaskNew body);
}
