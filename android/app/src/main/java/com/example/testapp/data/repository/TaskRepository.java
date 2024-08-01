package com.example.testapp.data.repository;

import com.example.testapp.data.source.TaskDataSource;
import com.example.testapp.domain.model.Task;

public interface TaskRepository {
    void getTasks(TaskDataSource.LoadTasksCallback callback);
    void createTask(Task task, TaskDataSource.CreateTaskCallback callback);
    void editTask(Task task, TaskDataSource.EditTaskCallback callback);
    void deleteTask(Task task, TaskDataSource.DeleteTaskCallback callback);
}
