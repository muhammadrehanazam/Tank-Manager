package com.taskmanager.controller;

import com.taskmanager.dto.TaskRequestDTO;
import com.taskmanager.dto.TaskResponseDTO;
import com.taskmanager.model.Task;
import com.taskmanager.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;

    // 1. Create task for logged-in user
    @PostMapping
    public ResponseEntity<TaskResponseDTO> createTask(
            Authentication authentication,
            @Valid @RequestBody TaskRequestDTO dto) {

        String userEmail = authentication.getName();
        TaskResponseDTO task = taskService.createTaskByEmail(userEmail, dto);
        return new ResponseEntity<>(task, HttpStatus.CREATED);
    }

    // 2. Get all tasks for LOGGED-IN user (URL change: GET /api/tasks)
    @GetMapping
    public ResponseEntity<List<TaskResponseDTO>> getMyTasks(Authentication authentication) {
        String userEmail = authentication.getName();
        List<TaskResponseDTO> tasks = taskService.getMyTasks(userEmail);
        return ResponseEntity.ok(tasks);
    }

    // 3. Update task status (With ownership check)
    @PatchMapping("/{taskId}/status")
    public ResponseEntity<TaskResponseDTO> updateTaskStatus(
            Authentication authentication,
            @PathVariable Long taskId,
            @RequestParam Task.Status status) {

        String userEmail = authentication.getName();
        TaskResponseDTO updatedTask = taskService.updateTaskStatusSecure(taskId, status, userEmail);
        return ResponseEntity.ok(updatedTask);
    }

    // 4. Delete a task (With ownership check)
    @DeleteMapping("/{taskId}")
    public ResponseEntity<String> deleteTask(
            Authentication authentication,
            @PathVariable Long taskId) {

        String userEmail = authentication.getName();
        taskService.deleteTaskSecure(taskId, userEmail);
        return ResponseEntity.ok("Task deleted successfully with ID: " + taskId);
    }
}