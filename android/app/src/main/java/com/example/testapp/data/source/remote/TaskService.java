package com.example.testapp.data.source.remote;

import com.example.testapp.data.dto.CreateTaskDto;
import com.example.testapp.data.dto.EditTaskDto;
import com.example.testapp.domain.model.Task;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface TaskService {
    @GET("tasks")
    Call<List<Task>> getTasks();

    @POST("tasks")
    Call<Task> createTask(@Body CreateTaskDto task);

    @PUT("tasks/{id}")
    Call<Task> editTask(@Path("id") int id, @Body EditTaskDto task);

    @DELETE("tasks/{id}")
    Call<Void> deleteTask(@Path("id") int id);
}
