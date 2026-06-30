package com.example.healthsync.service;

import com.example.healthsync.dto.AuthResponseDto;
import com.example.healthsync.dto.LoginRequestDto;
import com.example.healthsync.dto.RegisterRequestDto;
import com.example.healthsync.entity.Role;
import com.example.healthsync.entity.User;
import com.example.healthsync.exception.InvalidCredentialsException;
import com.example.healthsync.exception.ResourceConflictException;
import com.example.healthsync.repository.UserRepository;
import com.example.healthsync.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Override
    public AuthResponseDto register(RegisterRequestDto request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ResourceConflictException("Email is already registered");
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
                savedUser.getRole(),
                null
        );
    }

    @Override
    public AuthResponseDto login(LoginRequestDto request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid email or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Invalid email or password");
        }

        String token = jwtService.generateToken(user);

        return new AuthResponseDto(
                "Login successful",
                user.getEmail(),
                user.getRole(),
                token
        );
    }
}
