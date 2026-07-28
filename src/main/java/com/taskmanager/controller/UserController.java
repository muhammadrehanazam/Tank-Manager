package com.taskmanager.controller;

import com.taskmanager.model.User;
import com.taskmanager.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder; // 👈 Make sure PasswordEncoder is injected

    // 1. PUBLIC REGISTRATION ENDPOINT
    @PostMapping
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        // Check if email already exists
        if (userRepository.existsByEmail(user.getEmail())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Email is already registered!");
        }

        // Encode password before saving
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Assign default role if missing
        if (user.getRole() == null) {
            user.setRole(User.Role.ROLE_USER);
        }

        User savedUser = userRepository.save(user);
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }

    // 2. ADMIN ONLY: Get all registered users
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userRepository.findAll());
    }

    // 3. ADMIN ONLY: Update User Role
    @PutMapping("/{id}/role")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateUserRole(@PathVariable Long id, @RequestBody Map<String, String> request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        String newRole = request.get("role");
        if (newRole != null && (newRole.equalsIgnoreCase("ROLE_ADMIN") || newRole.equalsIgnoreCase("ROLE_USER"))) {
            user.setRole(User.Role.valueOf(newRole.toUpperCase()));
            userRepository.save(user);
            return ResponseEntity.ok(user);
        }
        return ResponseEntity.badRequest().body("Invalid Role specified.");
    }

    // 4. ADMIN ONLY: Delete user (Prevent Self-Deletion)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteUser(@PathVariable Long id, Authentication authentication) {
        String loggedInEmail = authentication.getName();
        User userToDelete = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        if (userToDelete.getEmail().equalsIgnoreCase(loggedInEmail)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("You cannot delete your own active Admin account!");
        }

        userRepository.delete(userToDelete);
        return ResponseEntity.ok("User deleted successfully.");
    }
}