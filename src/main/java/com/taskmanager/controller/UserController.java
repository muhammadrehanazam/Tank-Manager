package com.taskmanager.controller;

import com.taskmanager.dto.UserRequestDTO;
import com.taskmanager.dto.UserResponseDTO;
import com.taskmanager.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // 1. User Registration (Public Endpoint - No Token Needed)
    @PostMapping
    public ResponseEntity<UserResponseDTO> createUser(@Valid @RequestBody UserRequestDTO userDTO) {
        UserResponseDTO savedUser = userService.createUser(userDTO);
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }

    // 2. Get Logged-In User Profile (GET /api/users/me)
    @GetMapping("/me")
    public ResponseEntity<UserResponseDTO> getMyProfile(Authentication authentication) {
        String email = authentication.getName();
        UserResponseDTO profile = userService.getMyProfile(email);
        return ResponseEntity.ok(profile);
    }

    // 3. Get User By ID (With Ownership Security)
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(
            @PathVariable Long id,
            Authentication authentication) {

        String loggedInEmail = authentication.getName();
        UserResponseDTO user = userService.getUserByIdSecure(id, loggedInEmail);
        return ResponseEntity.ok(user);
    }

    // 4. Get All Users (Admin Feature / Restricted)
    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        List<UserResponseDTO> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }
}