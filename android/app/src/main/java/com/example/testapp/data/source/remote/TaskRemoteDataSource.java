package com.example.testapp.data.source.remote;

import androidx.annotation.NonNull;

import com.example.testapp.data.dto.CreateTaskDto;
import com.example.testapp.data.dto.EditTaskDto;
import com.example.testapp.data.source.TaskDataSource;
import com.example.testapp.domain.model.Task;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TaskRemoteDataSource implements TaskDataSource {

    private final TaskService taskService;

    public TaskRemoteDataSource() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.0.36:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        taskService = retrofit.create(TaskService.class);
    }

    @Override
    public void getTasks(LoadTasksCallback callback) {
        taskService.getTasks().enqueue(new Callback<List<Task>>() {
            @Override
            public void onResponse(@NonNull Call<List<Task>> call, @NonNull Response<List<Task>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onTasksLoaded(response.body());
                } else {
                    callback.onDataNotAvailable();
                }
            }
            @Override
            public void onFailure(@NonNull Call<List<Task>> call, @NonNull Throwable t) {
                callback.onDataNotAvailable();
            }
        });
    }

    @Override
    public void createTask(Task task, CreateTaskCallback callback) {
        CreateTaskDto createTaskDto = new CreateTaskDto();
        createTaskDto.setTitle(task.getTitle());
        createTaskDto.setDescription(task.getDescription());
        createTaskDto.setCompleted(task.isCompleted());


        taskService.createTask(createTaskDto).enqueue(new Callback<Task>() {
            @Override
            public void onResponse(@NonNull Call<Task> call, @NonNull Response<Task> response) {
                if (response.isSuccessful()) {
                    callback.onCreated(response.body());
                } else {
                    callback.onFailed();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Task> call, @NonNull Throwable t) {
                callback.onFailed();
            }
        });
    }

    @Override
    public void editTask(Task task, EditTaskCallback callback) {
        EditTaskDto editTaskDto = new EditTaskDto();
        editTaskDto.setTitle(task.getTitle());
        editTaskDto.setDescription(task.getDescription());
        editTaskDto.setCompleted(task.isCompleted());


        taskService.editTask(task.getId(), editTaskDto).enqueue(new Callback<Task>() {
            @Override
            public void onResponse(@NonNull Call<Task> call, @NonNull Response<Task> response) {
                if (response.isSuccessful()) {
                    callback.onChanged(response.body());
                } else {
                    callback.onFailed();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Task> call, @NonNull Throwable t) {
                callback.onFailed();
            }
        });
    }

    @Override
    public void deleteTask(Task task, DeleteTaskCallback callback) {
        taskService.deleteTask(task.getId()).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.isSuccessful()) {
                    callback.onDeleted();
                } else {
                    callback.onFailed();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                callback.onFailed();
            }
        });
    }
}
