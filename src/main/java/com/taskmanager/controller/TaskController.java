package com.taskmanager.controller;

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

    // 1. Create a task for a specific user (POST /api/tasks/user/{userId})
    @PostMapping("/user/{userId}") // userid ko task assign kr0
    public ResponseEntity<?> createTask(@PathVariable Long userId, @Valid @RequestBody Task task) {
        try {
            Task createdTask = taskService.createTask(userId, task);
            return new ResponseEntity<>(createdTask, HttpStatus.CREATED); // Returns HTTP 201
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage()); // Returns HTTP 404 if user doesn't exist
        }
    }

    // 2. Get all tasks for a specific user (GET /api/tasks/user/{userId})
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getTasksByUserId(@PathVariable Long userId) {
        try {
            List<Task> tasks = taskService.getTasksByUserId(userId);
            return ResponseEntity.ok(tasks); // Returns HTTP 200 with JSON list
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage()); // Returns HTTP 404 with error string
        }
    }

    // 3. Update task status (PATCH /api/tasks/{taskId}/status?status=COMPLETED)
    @PatchMapping("/{taskId}/status")
    public ResponseEntity<?> updateTaskStatus(
            @PathVariable Long taskId,
            @RequestParam Task.Status status) {
        try {
            Task updatedTask = taskService.updateTaskStatus(taskId, status);
            return ResponseEntity.ok(updatedTask);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // 4. Delete a task by ID (DELETE /api/tasks/{taskId})
    @DeleteMapping("/{taskId}")
    public ResponseEntity<?> deleteTask(@PathVariable Long taskId) {
        try {
            taskService.deleteTask(taskId);
            return ResponseEntity.ok("Task deleted successfully with ID: " + taskId);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}