package com.example.testapp.presentation.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.testapp.data.repository.TaskRepository;
import com.example.testapp.data.source.TaskDataSource;
import com.example.testapp.domain.model.Task;

import java.util.ArrayList;
import java.util.List;

public class TaskViewModel extends ViewModel {
    private final MutableLiveData<List<Task>> tasks;
    private final TaskRepository taskRepository;

    public TaskViewModel(TaskRepository taskRepository) {
        super();
        List<Task> taskList = new ArrayList<>();
        tasks = new MutableLiveData<>(taskList);
        this.taskRepository = taskRepository;
    }

    public LiveData<List<Task>> getTasks() {
        return tasks;
    }

    public void fetchTasks() {
        taskRepository.getTasks(new TaskDataSource.LoadTasksCallback() {
            @Override
            public void onTasksLoaded(List<Task> loadedTasks) {
                tasks.postValue(loadedTasks);
            }

            @Override
            public void onDataNotAvailable() {
                tasks.postValue(new ArrayList<>());
            }
        });
    }

    public void createTask(Task task) {
        List<Task> taskList = tasks.getValue();
        taskRepository.createTask(task, new TaskDataSource.CreateTaskCallback() {
            @Override
            public void onCreated(Task createdTask) {
                if (taskList == null) throw new AssertionError();
                taskList.add(createdTask);
                tasks.postValue(taskList);
            }

            @Override
            public void onFailed() {}
        });

    }

    public void editTask(Task task) {
        taskRepository.editTask(task, new TaskDataSource.EditTaskCallback() {
            @Override
            public void onChanged(Task changedTask) {}

            @Override
            public void onFailed() {}
        });
    }

    public void deleteTask(Task task) {
        List<Task> taskList = tasks.getValue();
        taskRepository.deleteTask(task, new TaskDataSource.DeleteTaskCallback() {
            @Override
            public void onDeleted() {
                taskList.remove(task);
                tasks.postValue(taskList);
            }

            @Override
            public void onFailed() {}
        });
    }
}
