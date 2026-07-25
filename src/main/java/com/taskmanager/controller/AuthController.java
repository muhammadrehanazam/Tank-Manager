package com.taskmanager.controller;

import com.taskmanager.dto.JwtResponseDTO;
import com.taskmanager.dto.LoginRequestDTO;
import com.taskmanager.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public JwtResponseDTO login(@Valid @RequestBody LoginRequestDTO loginDTO) {
        return authService.login(loginDTO);
    }
}