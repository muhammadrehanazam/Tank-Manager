package com.taskmanager.controller;

import com.taskmanager.dto.TaskRequestDTO;
import com.taskmanager.dto.TaskResponseDTO;
import com.taskmanager.model.Task;
import com.taskmanager.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;

    // 1. Create a task for a specific user
    @PostMapping("/user/{userId}")
    public ResponseEntity<TaskResponseDTO> createTask(
            @PathVariable Long userId,
            @Valid @RequestBody TaskRequestDTO taskDTO) {
        TaskResponseDTO createdTask = taskService.createTask(userId, taskDTO);
        return new ResponseEntity<>(createdTask, HttpStatus.CREATED); // Returns HTTP 201
    }

    // 2. Get all tasks for a specific user
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<TaskResponseDTO>> getTasksByUserId(@PathVariable Long userId) {
        List<TaskResponseDTO> tasks = taskService.getTasksByUserId(userId);
        return ResponseEntity.ok(tasks); // Returns HTTP 200 with DTO list
    }

    // get task by task id
// Get single task by Task ID (GET /api/tasks/1)
    @GetMapping("/{taskId}")
    public ResponseEntity<TaskResponseDTO> getTaskById(@PathVariable Long taskId) {
        TaskResponseDTO task = taskService.getTaskById(taskId);
        return ResponseEntity.ok(task);
    }

    // 3. Update task status
    @PatchMapping("/{taskId}/status")
    public ResponseEntity<TaskResponseDTO> updateTaskStatus(
            @PathVariable Long taskId,
            @RequestParam Task.Status status) {
        TaskResponseDTO updatedTask = taskService.updateTaskStatus(taskId, status);
        return ResponseEntity.ok(updatedTask); // Returns HTTP 200 with updated DTO
    }

    // 4. Delete a task by ID
    @DeleteMapping("/{taskId}")
    public ResponseEntity<String> deleteTask(@PathVariable Long taskId) {
        taskService.deleteTask(taskId);
        return ResponseEntity.ok("Task deleted successfully with ID: " + taskId);
    }
}