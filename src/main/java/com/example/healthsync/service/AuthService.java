package com.example.healthsync.service;

import com.example.healthsync.dto.AuthResponseDto;
import com.example.healthsync.dto.LoginRequestDto;
import com.example.healthsync.dto.RegisterRequestDto;

public interface AuthService {

    AuthResponseDto register(RegisterRequestDto request);

    AuthResponseDto login(LoginRequestDto request);

}
