package com.taskmanager.service;

import com.taskmanager.dto.JwtResponseDTO;
import com.taskmanager.dto.LoginRequestDTO;
import com.taskmanager.exception.ResourceNotFoundException;
import com.taskmanager.model.User;
import com.taskmanager.repository.UserRepository;
import com.taskmanager.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    public JwtResponseDTO login(LoginRequestDTO dto) {
        // 1. Check user exists
        User user = userRepository.findByEmail(dto.email())
                .orElseThrow(() -> new ResourceNotFoundException("Invalid email or password"));

        // 2. Check password match
        if (!passwordEncoder.matches(dto.password(), user.getPassword())) {
            throw new BadCredentialsException("Invalid email or password");
        }

        // 3. Generate Token
        String token = jwtUtils.generateToken(user.getEmail());

        return new JwtResponseDTO(token, user.getEmail());
    }
}