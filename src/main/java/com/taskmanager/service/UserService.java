package com.taskmanager.service;

import com.taskmanager.dto.UserRequestDTO;
import com.taskmanager.dto.UserResponseDTO;
import com.taskmanager.exception.ResourceNotFoundException;
import com.taskmanager.model.User;
import com.taskmanager.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // Public Registration / Create User
    public UserResponseDTO createUser(UserRequestDTO dto) {
        if (userRepository.existsByEmail(dto.email())) {
            throw new ResourceNotFoundException("Email already registered!");
        }
        User user = new User();
        user.setName(dto.name());
        user.setEmail(dto.email());
        user.setPassword(passwordEncoder.encode(dto.password()));

        User savedUser = userRepository.save(user);
        return mapToDTO(savedUser);
    }

    // Get Currently Logged-in User Profile (Secure Self-Profile Fetch)
    public UserResponseDTO getMyProfile(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
        return mapToDTO(user);
    }

    // Secure User Fetch by ID (With Ownership Check)
    public UserResponseDTO getUserByIdSecure(Long id, String loggedInEmail) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        // Ownership Verification
        if (!user.getEmail().equals(loggedInEmail)) {
            throw new AccessDeniedException("You are not authorized to view another user's profile!");
        }

        return mapToDTO(user);
    }

    // Get All Users (Ideally for Admin Roles only)
    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    private UserResponseDTO mapToDTO(User user) {
        return new UserResponseDTO(
                user.getId(),
                user.getName(),
                user.getEmail()
        );
    }
}