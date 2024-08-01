package com.example.testapp.data.repository;

import com.example.testapp.data.source.TaskDataSource;
import com.example.testapp.domain.model.Task;

public class TaskRepositoryImpl implements TaskRepository {

    private final TaskDataSource taskDataSource;

    public TaskRepositoryImpl(TaskDataSource taskDataSource) {
        this.taskDataSource = taskDataSource;
    }

    @Override
    public void getTasks(TaskDataSource.LoadTasksCallback callback) {
        taskDataSource.getTasks(callback);
    }

    @Override
    public void createTask(Task task, TaskDataSource.CreateTaskCallback callback) {
        taskDataSource.createTask(task, callback);
    }

    @Override
    public void editTask(Task task, TaskDataSource.EditTaskCallback callback) {
        taskDataSource.editTask(task, callback);
    }

    @Override
    public void deleteTask(Task task, TaskDataSource.DeleteTaskCallback callback) {
        taskDataSource.deleteTask(task, callback);
    }
}