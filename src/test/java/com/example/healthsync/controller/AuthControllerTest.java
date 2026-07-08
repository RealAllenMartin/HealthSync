package com.example.healthsync.controller;

import com.example.healthsync.dto.AuthResponseDto;
import com.example.healthsync.dto.LoginRequestDto;
import com.example.healthsync.dto.RegisterRequestDto;
import com.example.healthsync.entity.Role;
import com.example.healthsync.repository.UserRepository;
import com.example.healthsync.security.JwtService;
import com.example.healthsync.service.AuthService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(AuthControllerTest.TestConfig.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserRepository userRepository;

    @MockitoBean
    private JwtService jwtService;

    @Test
    void registerReturnsBadRequestWhenRequestBodyIsInvalid() throws Exception {
        String invalidRegisterJson = """
                {
                  "firstName": "",
                  "lastName": "User",
                  "email": "not-an-email",
                  "password": "short"
                }
                """;

        mockMvc.perform(post("/auth/register")
                        .contentType(APPLICATION_JSON)
                        .content(invalidRegisterJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void registerReturnsAuthResponseWhenRequestBodyIsValid() throws Exception {
        String validRegisterJson = """
                {
                  "firstName": "Test",
                  "lastName": "User",
                  "email": "test.user@example.com",
                  "password": "password123"
                }
                """;

        mockMvc.perform(post("/auth/register")
                        .contentType(APPLICATION_JSON)
                        .content(validRegisterJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("User registered successfully"))
                .andExpect(jsonPath("$.email").value("test.user@example.com"))
                .andExpect(jsonPath("$.role").value("USER"))
                .andExpect(jsonPath("$.token").isEmpty());
    }

    @Test
    void loginReturnsBadRequestWhenRequestBodyIsInvalid() throws Exception {
        String invalidLoginJson = """
                {
                  "email": "not-an-email",
                  "password": ""
                }
                """;

        mockMvc.perform(post("/auth/login")
                        .contentType(APPLICATION_JSON)
                        .content(invalidLoginJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void loginReturnsAuthResponseWhenRequestBodyIsValid() throws Exception {
        String validLoginJson = """
                {
                  "email": "test.user@example.com",
                  "password": "password123"
                }
                """;

        mockMvc.perform(post("/auth/login")
                        .contentType(APPLICATION_JSON)
                        .content(validLoginJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Login successful"))
                .andExpect(jsonPath("$.email").value("test.user@example.com"))
                .andExpect(jsonPath("$.role").value("USER"))
                .andExpect(jsonPath("$.token").value("fake-jwt-token"));
    }

    @TestConfiguration
    static class TestConfig {

        @Bean
        AuthService authService() {
            return new AuthService() {
                @Override
                public AuthResponseDto register(RegisterRequestDto request) {
                    return new AuthResponseDto(
                            "User registered successfully",
                            request.getEmail(),
                            Role.USER,
                            null
                    );
                }

                @Override
                public AuthResponseDto login(LoginRequestDto request) {
                    return new AuthResponseDto(
                            "Login successful",
                            request.getEmail(),
                            Role.USER,
                            "fake-jwt-token"
                    );
                }
            };
        }
    }
}
