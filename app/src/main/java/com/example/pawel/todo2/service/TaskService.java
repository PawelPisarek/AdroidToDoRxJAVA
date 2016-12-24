package com.example.pawel.todo2.service;



import com.example.pawel.todo2.model.Task;

import java.util.List;

import retrofit.http.GET;
import rx.Observable;

public interface TaskService {
    String SERVICE_ENDPOINT = "http://10.0.2.2:3000";

    @GET("/task")
    Observable<List<Task>> getUser();
}
