package com.taskmanager.service;

import com.taskmanager.dto.TaskRequestDTO;
import com.taskmanager.dto.TaskResponseDTO;
import com.taskmanager.exception.ResourceNotFoundException;
import com.taskmanager.model.Task;
import com.taskmanager.model.User;
import com.taskmanager.repository.TaskRepository;
import com.taskmanager.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    // 1. Create a task
    public TaskResponseDTO createTaskByEmail(String email, TaskRequestDTO dto) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));

        Task task = new Task();
        task.setTitle(dto.title());
        task.setStatus(dto.status() != null ? dto.status() : Task.Status.PENDING);
        task.setPriority(dto.priority() != null ? dto.priority() : Task.Priority.MEDIUM);
        task.setUser(user);

        Task savedTask = taskRepository.save(task);
        return mapToDTO(savedTask);
    }

    // 2. Get all tasks for a specific user (Admin access)
    public List<TaskResponseDTO> getTasksByUserId(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found with id: " + userId);
        }
        return taskRepository.findByUserId(userId)
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    // 3. Get all tasks for LOGGED-IN user
    public List<TaskResponseDTO> getMyTasks(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));

        return taskRepository.findByUserId(user.getId())
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    // 4. Update task status WITH ownership check
    public TaskResponseDTO updateTaskStatusSecure(Long taskId, Task.Status status, String email) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + taskId));

        if (!task.getUser().getEmail().equalsIgnoreCase(email)) {
            throw new AccessDeniedException("You do not have permission to modify this task!");
        }

        task.setStatus(status);
        Task updatedTask = taskRepository.save(task);
        return mapToDTO(updatedTask);
    }

    // 5. Update task priority WITH ownership check
    public TaskResponseDTO updateTaskPrioritySecure(Long taskId, Task.Priority priority, String email) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + taskId));

        if (!task.getUser().getEmail().equalsIgnoreCase(email)) {
            throw new AccessDeniedException("You do not have permission to modify this task!");
        }

        task.setPriority(priority);
        Task updatedTask = taskRepository.save(task);
        return mapToDTO(updatedTask);
    }

    // 6. Delete a task WITH ownership check
    public void deleteTaskSecure(Long taskId, String email) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + taskId));

        if (!task.getUser().getEmail().equalsIgnoreCase(email)) {
            throw new AccessDeniedException("You do not have permission to delete this task!");
        }

        taskRepository.delete(task);
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