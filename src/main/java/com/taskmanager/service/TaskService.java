package com.taskmanager.service;

import com.taskmanager.dto.TaskRequestDTO;
import com.taskmanager.dto.TaskResponseDTO;
import com.taskmanager.exception.ResourceNotFoundException;
import com.taskmanager.model.Task;
import com.taskmanager.model.User;
import com.taskmanager.repository.TaskRepository;
import com.taskmanager.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    // 1. Create a task
    public TaskResponseDTO createTask(Long userId, TaskRequestDTO dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        Task task = new Task();
        task.setTitle(dto.title());
        task.setStatus(dto.status() != null ? dto.status() : Task.Status.PENDING);
        task.setPriority(dto.priority() != null ? dto.priority() : Task.Priority.MEDIUM);
        task.setUser(user);

        Task savedTask = taskRepository.save(task);
        return mapToDTO(savedTask);
    }

    // 2. Get all tasks for a specific user
    public List<TaskResponseDTO> getTasksByUserId(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found with id: " + userId);
        }
        return taskRepository.findByUserId(userId)
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    // get task by task id
// Get task by Task ID
    public TaskResponseDTO getTaskById(Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + taskId));

        return mapToDTO(task);
    }

    // 3. Update task status (returns DTO)
    public TaskResponseDTO updateTaskStatus(Long taskId, Task.Status status) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + taskId));

        task.setStatus(status);
        Task updatedTask = taskRepository.save(task);
        return mapToDTO(updatedTask);
    }

    // 4. Delete a task
    public void deleteTask(Long taskId) {
        if (!taskRepository.existsById(taskId)) {
            throw new ResourceNotFoundException("Task not found with id: " + taskId);
        }
        taskRepository.deleteById(taskId);
    }

    // Helper mapper method
    private TaskResponseDTO mapToDTO(Task task) {
        return new TaskResponseDTO(
                task.getId(),
                task.getTitle(),
                task.getStatus(),
                task.getPriority(),
                task.getUser() != null ? task.getUser().getId() : null
        );
    }
}