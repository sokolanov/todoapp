package org.example.backend.service;

import org.example.backend.dto.CreateTaskDto;
import org.example.backend.dto.EditTaskDto;
import org.example.backend.exception.ResourceNotFoundException;
import org.example.backend.model.Task;
import org.example.backend.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public Task createTask(CreateTaskDto createTaskDto) {
        Task task = new Task();
        task.setCompleted(createTaskDto.isCompleted());
        task.setDescription(createTaskDto.getDescription());
        task.setTitle(createTaskDto.getTitle());
        return taskRepository.save(task);
    }

    public Task updateTask(Long id, EditTaskDto editTaskDto) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found for this id :: " + id));

        task.setTitle(editTaskDto.getTitle());
        task.setDescription(editTaskDto.getDescription());
        task.setCompleted(editTaskDto.isCompleted());
        return taskRepository.save(task);
    }

    public void deleteTask(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found for this id :: " + id));
        taskRepository.delete(task);
    }
}
