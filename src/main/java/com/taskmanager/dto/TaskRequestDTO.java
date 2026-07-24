package com.taskmanager.dto;

import com.taskmanager.model.Task;
import jakarta.validation.constraints.NotBlank;

public record TaskRequestDTO(
        @NotBlank(message = "Title cant be null")
        String title,

        Task.Status status,
        Task.Priority priority
) {}