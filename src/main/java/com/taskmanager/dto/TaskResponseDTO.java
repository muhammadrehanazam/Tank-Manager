package com.taskmanager.dto;

import com.taskmanager.model.Task;

public record TaskResponseDTO(
        Long id,
        String title,
        Task.Status status,
        Task.Priority priority,
        Long userId
) {}