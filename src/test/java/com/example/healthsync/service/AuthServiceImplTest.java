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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtService jwtService;
    @InjectMocks
    private AuthServiceImpl authService;

    @Test
    void registerCreatesUserWhenEmailIsAvailable() throws Exception {
        RegisterRequestDto request = new RegisterRequestDto(
                "Email",
                "Available",
                "email@available.com",
                "emailAvailable"
        );

        when(userRepository.existsByEmail(request.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(request.getPassword())).thenReturn("hashed-password");

        User savedUser = new User(
                1L,
                request.getFirstName(),
                request.getLastName(),
                request.getEmail(),
                "hashed-password",
                Role.USER
        );

        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        AuthResponseDto response = authService.register(request);

        assertThat(response.getMessage()).isEqualTo("User registered successfully");
        assertThat(response.getEmail()).isEqualTo(request.getEmail());
        assertThat(response.getRole()).isEqualTo(Role.USER);
        assertThat(response.getToken()).isNull();

        verify(passwordEncoder).encode(request.getPassword());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void registerThrowsConflictWhenEmailAlreadyExists() throws Exception {
        RegisterRequestDto request = new RegisterRequestDto(
                "Email",
                "Unavailable",
                "email@unavailable.com",
                "emailUnavailable"
        );

        when(userRepository.existsByEmail(request.getEmail())).thenReturn(true);

        assertThrows(ResourceConflictException.class, () -> authService.register(request));

        verify(passwordEncoder, never()).encode(any());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void loginReturnsTokenWhenCredentialsAreValid() throws Exception {
        LoginRequestDto request = new LoginRequestDto(
                "returns@token.com",
                "returnsToken"
        );

        User user = new User(
                1L,
                "Returns",
                "Token",
                "returns@token.com",
                "hashed-password",
                Role.USER
        );

        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(request.getPassword(), user.getPassword())).thenReturn(true);
        when(jwtService.generateToken(user)).thenReturn("fake-jwt-token");

        AuthResponseDto response = authService.login(request);

        assertThat(response.getMessage()).isEqualTo("Login successful");
        assertThat(response.getEmail()).isEqualTo(user.getEmail());
        assertThat(response.getRole()).isEqualTo(user.getRole());
        assertThat(response.getToken()).isEqualTo("fake-jwt-token");

        verify(passwordEncoder).matches(request.getPassword(), user.getPassword());
        verify(jwtService).generateToken(user);
    }

    @Test
    void loginThrowsInvalidCredentialsWhenEmailIsMissing() throws Exception {
        LoginRequestDto request = new LoginRequestDto(
                "nonExistant@email.com",
                "nonExistantEmail"
        );

        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());

        assertThrows(InvalidCredentialsException.class, () -> authService.login(request));

        verify(passwordEncoder, never()).matches(any(), any());
        verify(jwtService, never()).generateToken(any());
    }

    @Test
    void loginThrowsInvalidCredentialsWhenPasswordDoesNotMatch() throws Exception {
        LoginRequestDto request = new LoginRequestDto(
                "nonMatching@password.com",
                "nonMatchingPassword"
        );

        User user = new User(
                1L,
                "NonMatching",
                "Password",
                "nonMatching@password.com",
                "hashed-password",
                Role.USER
        );

        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(request.getPassword(), user.getPassword())).thenReturn(false);

        assertThrows(InvalidCredentialsException.class, () -> authService.login(request));

        verify(jwtService, never()).generateToken(any());
    }

}
