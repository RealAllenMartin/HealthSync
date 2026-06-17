package com.example.healthsync.service;

import com.example.healthsync.dto.AuthResponseDto;
import com.example.healthsync.dto.LoginRequestDto;
import com.example.healthsync.dto.RegisterRequestDto;
import com.example.healthsync.entity.Role;
import com.example.healthsync.entity.User;
import com.example.healthsync.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public AuthResponseDto register(RegisterRequestDto request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email is already registered");
        }

        User user = new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.USER);

        User savedUser = userRepository.save(user);

        return new AuthResponseDto(
                "User registered successfully",
                savedUser.getEmail(),
                savedUser.getRole()
        );
    }

    @Override
    public AuthResponseDto login(LoginRequestDto request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }

        return new AuthResponseDto(
                "Login successful",
                user.getEmail(),
                user.getRole()
        );
    }
}
