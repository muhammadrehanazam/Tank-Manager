package com.taskmanager.dto;

public record JwtResponseDTO(
        String token,
        String type,
        String email
) {
    public JwtResponseDTO(String token, String email) {
        this(token, "Bearer", email);
    }
}