package com.example.healthsync.security;

import com.example.healthsync.dto.AuthResponseDto;
import com.example.healthsync.dto.PatientResponseDto;
import com.example.healthsync.entity.Role;
import com.example.healthsync.entity.User;
import com.example.healthsync.repository.UserRepository;
import com.example.healthsync.service.AuthService;
import com.example.healthsync.service.PatientService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = {
        "spring.autoconfigure.exclude=org.springframework.boot.jdbc.autoconfigure.DataSourceAutoConfiguration," +
                "org.springframework.boot.hibernate.autoconfigure.HibernateJpaAutoConfiguration," +
                "org.springframework.boot.data.jpa.autoconfigure.DataJpaRepositoriesAutoConfiguration"
})
@AutoConfigureMockMvc
class SecurityServiceRouteTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserRepository userRepository;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private AuthService authService;

    @MockitoBean
    private PatientService patientService;

    @Test
    void registerEndpointIsPublic() throws Exception {
        when(authService.register(any())).thenReturn(new AuthResponseDto(
                "User registered successfully",
                "test.user@example.com",
                Role.USER,
                null
        ));

        String requestJson = """
                {
                  "firstName": "Test",
                  "lastName": "User",
                  "email": "test.user@example.com",
                  "password": "password123"
                }
                """;

        mockMvc.perform(post("/auth/register")
                        .contentType(APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test.user@example.com"));
    }

    @Test
    void loginEndpointIsPublic() throws Exception {
        when(authService.login(any())).thenReturn(new AuthResponseDto(
                "Login successful",
                "test.user@example.com",
                Role.USER,
                "fake-jwt-token"
        ));

        String requestJson = """
                {
                  "email": "test.user@example.com",
                  "password": "password123"
                }
                """;

        mockMvc.perform(post("/auth/login")
                        .contentType(APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("fake-jwt-token"));
    }

    @Test
    void patientsEndpointReturnsForbiddenWithoutAuthentication() throws Exception {
        mockMvc.perform(get("/patients"))
                .andExpect(status().isForbidden());
    }

    @Test
    void patientsEndpointAllowsRequestWithValidJwt() throws Exception {
        User user = new User(
                1L,
                "Test",
                "User",
                "test.user@example.com",
                "hashed-password",
                Role.USER
        );

        PatientResponseDto patient = new PatientResponseDto(
                1L,
                "Jane",
                "Smith",
                "jane.smith@example.com",
                LocalDate.of(1990, 1, 1),
                "5551234567"
        );

        when(jwtService.extractUsername("valid-token")).thenReturn("test.user@example.com");
        when(userRepository.findByEmail("test.user@example.com")).thenReturn(Optional.of(user));
        when(jwtService.isTokenValid("valid-token", user)).thenReturn(true);
        when(patientService.getAllPatients()).thenReturn(List.of(patient));

        mockMvc.perform(get("/patients")
                        .header(AUTHORIZATION, "Bearer valid-token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].email").value("jane.smith@example.com"));
    }
}
