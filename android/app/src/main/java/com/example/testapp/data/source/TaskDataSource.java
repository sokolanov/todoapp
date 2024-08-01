package com.example.testapp.data.source;

import com.example.testapp.domain.model.Task;

import java.util.List;

public interface TaskDataSource {

    interface LoadTasksCallback {
        void onTasksLoaded(List<Task> loadedTasks);
        void onDataNotAvailable();
    }

    interface CreateTaskCallback {
        void onCreated(Task createdTask);
        void onFailed();
    }

    interface EditTaskCallback {
        void onChanged(Task changedTask);
        void onFailed();
    }

    interface DeleteTaskCallback {
        void onDeleted();
        void onFailed();
    }

    void getTasks(LoadTasksCallback callback);
    void createTask(Task task, CreateTaskCallback callback);
    void editTask(Task task, EditTaskCallback callback);
    void deleteTask(Task task, DeleteTaskCallback callback);
}
