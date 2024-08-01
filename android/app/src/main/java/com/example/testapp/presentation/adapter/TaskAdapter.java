package com.example.testapp.presentation.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testapp.R;
import com.example.testapp.domain.model.Task;
import com.example.testapp.presentation.viewmodel.TaskViewModel;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Collections;
import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.BaseTaskViewHolder> {
    private final Context context;
    private final TaskViewModel taskViewModel;
    private List<Task> taskList;
    private int editingPosition;
    private Task editingTask;

    private static final int VIEW_TYPE_VIEW = 1;
    private static final int VIEW_TYPE_EDIT = 2;

    public TaskAdapter(Context context, TaskViewModel taskViewModel) {
        this.context = context;
        this.taskViewModel = taskViewModel;
        this.taskList = Collections.emptyList();
    }

    private void startEditTask (Task task, int position) {
        if (editingTask != task && editingTask != null ) {
            finishEditTask(editingTask, editingPosition);
        }
        editingTask = task;
        editingPosition = position;
        notifyItemChanged(position);
    }

    private void finishEditTask (Task task, int position) {
        editingTask = null;
        editingPosition = -1;
        notifyItemChanged(position);
        taskViewModel.editTask(task);
    }

    public void setTasks(List<Task> taskList) {
        this.taskList = taskList;
        notifyDataSetChanged();
    };

    @Override
    public int getItemViewType(int position) {
        Task task = taskList.get(position);
        if (task == editingTask) {
            return VIEW_TYPE_EDIT;
        }
        return VIEW_TYPE_VIEW;
    }

    @NonNull
    @Override
    public BaseTaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_VIEW) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_task, parent, false);
            return new TaskViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.edit_item_task, parent, false);
            return new EditTaskViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull BaseTaskViewHolder holder, int position) {
        Task task = taskList.get(position);

        if (holder instanceof TaskViewHolder) {
            TaskViewHolder taskViewHolder = (TaskViewHolder) holder;
            taskViewHolder.titleTextView.setText(task.getTitle());
            taskViewHolder.descriptionTextView.setText(task.getDescription());
            taskViewHolder.btnEdit.setOnClickListener(v -> {
                startEditTask(task, position);
            });
        } else if (holder instanceof EditTaskViewHolder) {
            EditTaskViewHolder editTaskViewHolder = (EditTaskViewHolder) holder;
            editTaskViewHolder.titleInput.setText(task.getTitle());
            editTaskViewHolder.descriptionInput.setText(task.getDescription());
            editTaskViewHolder.btnAcceptEdit.setOnClickListener(v -> {
                finishEditTask(task, position);
            });
            if (editTaskViewHolder.titleWatcher != null) {
                editTaskViewHolder.titleInput.removeTextChangedListener(editTaskViewHolder.titleWatcher);
            }
            if (editTaskViewHolder.descriptionWatcher != null) {
                editTaskViewHolder.descriptionInput.removeTextChangedListener(editTaskViewHolder.descriptionWatcher);
            }
            editTaskViewHolder.titleWatcher = new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

                @Override
                public void afterTextChanged(Editable editable) {
                    task.setTitle(editable.toString());
                }
            };
            editTaskViewHolder.descriptionWatcher = new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

                @Override
                public void afterTextChanged(Editable editable) {
                    task.setDescription(editable.toString());
                }
            };
            editTaskViewHolder.titleInput.addTextChangedListener(editTaskViewHolder.titleWatcher);
            editTaskViewHolder.descriptionInput.addTextChangedListener(editTaskViewHolder.descriptionWatcher);
        }

        holder.btnComplete.setText(task.isCompleted() ? "Не готово" : "Готово");

        holder.btnComplete.setOnClickListener(v -> {
            task.setCompleted(!task.isCompleted());
            taskViewModel.editTask(task);
            notifyItemChanged(position);
            Toast.makeText(context, task.isCompleted() ? "Задача выполнена" : "Задача не выполнена", Toast.LENGTH_SHORT).show();
        });

        /**
         * Обработка удаления задания.
         */
        holder.btnDelete.setOnClickListener(v -> {
            taskList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, taskList.size());
            taskViewModel.deleteTask(task);
        });
    }

    @Override
    public int getItemCount() {
        if (taskList == null) {
            return 0;
        }
        return taskList.size();
    }

    public static class BaseTaskViewHolder extends RecyclerView.ViewHolder {
        Button btnComplete;
        Button btnDelete;

        public BaseTaskViewHolder (@NonNull View itemView) {
            super(itemView);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            btnComplete = itemView.findViewById(R.id.btnComplete);
        }
    }

    public static class TaskViewHolder extends BaseTaskViewHolder {
        TextView titleTextView;
        TextView descriptionTextView;
        Button btnEdit;

        public TaskViewHolder (@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.taskTitle);
            descriptionTextView = itemView.findViewById(R.id.taskDescription);
            btnEdit = itemView.findViewById(R.id.btnEdit);
        }
    }

    public static class EditTaskViewHolder extends BaseTaskViewHolder {
        TextInputEditText titleInput;
        TextInputEditText descriptionInput;
        TextWatcher titleWatcher;
        TextWatcher descriptionWatcher;
        Button btnAcceptEdit;

        public EditTaskViewHolder (@NonNull View itemView) {
            super(itemView);
            titleInput = itemView.findViewById(R.id.taskTitleInput);
            descriptionInput = itemView.findViewById(R.id.taskDescriptionInput);
            btnAcceptEdit = itemView.findViewById(R.id.btnAcceptEdit);
        }
    }
}