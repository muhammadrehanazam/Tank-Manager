package com.taskmanager.dto;

public record UserResponseDTO(
        Long id,
        String name,
        String email
) {}