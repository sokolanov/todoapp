package com.example.testapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testapp.data.repository.TaskRepositoryImpl;
import com.example.testapp.data.source.remote.TaskRemoteDataSource;
import com.example.testapp.presentation.adapter.TaskAdapter;
import com.example.testapp.domain.model.Task;
import com.example.testapp.presentation.viewmodel.TaskViewModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView  recyclerView;
    private TaskAdapter taskAdapter;
    private TaskViewModel taskViewModel;

    private Button btnCreate;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        taskViewModel = new ViewModelProvider(this, new ViewModelProvider.Factory() {
            @NonNull
            @Override
            public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
                return (T) new TaskViewModel(new TaskRepositoryImpl(new TaskRemoteDataSource()));
            }
        }).get(TaskViewModel.class);
        taskAdapter = new TaskAdapter(this, taskViewModel);
        recyclerView.setAdapter(taskAdapter);
        taskViewModel.getTasks().observe(this, tasks -> taskAdapter.setTasks(tasks));
        taskViewModel.fetchTasks();

        btnCreate = findViewById(R.id.btnCreate);
        btnCreate.setOnClickListener(view -> {
            showAddTaskDialog();
        });
    }

    private void showAddTaskDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_edit_task, null);
        builder.setView(dialogView);

        EditText editTaskTitle = dialogView.findViewById(R.id.editTaskTitle);
        EditText editTaskDescription = dialogView.findViewById(R.id.editTaskDescription);
        Button btnSave = dialogView.findViewById(R.id.btnSave);

        AlertDialog dialog = builder.create();

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = editTaskTitle.getText().toString();
                String description = editTaskDescription.getText().toString();
                Task newTask = new Task();
                newTask.setTitle(title);
                newTask.setDescription(description);
                taskViewModel.createTask(newTask);
                dialog.dismiss();
            }
        });

        dialog.show();
    }
}
