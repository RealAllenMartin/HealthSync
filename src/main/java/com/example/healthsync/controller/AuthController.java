package com.example.healthsync.controller;

import com.example.healthsync.dto.AuthResponseDto;
import com.example.healthsync.dto.LoginRequestDto;
import com.example.healthsync.dto.RegisterRequestDto;
import com.example.healthsync.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public AuthResponseDto register(@Valid @RequestBody RegisterRequestDto registerRequestDto) {
        return authService.register(registerRequestDto);
    }

    @PostMapping("/login")
    public AuthResponseDto login(@Valid @RequestBody LoginRequestDto loginRequestDto) {
        return authService.login(loginRequestDto);
    }

}
