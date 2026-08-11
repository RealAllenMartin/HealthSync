package com.example.healthsync.controller;

import com.example.healthsync.dto.AppointmentRequestDto;
import com.example.healthsync.dto.AppointmentResponseDto;
import com.example.healthsync.repository.UserRepository;
import com.example.healthsync.security.JwtService;
import com.example.healthsync.service.AppointmentService;
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

@WebMvcTest(AppointmentController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(AppointmentControllerTest.TestConfig.class)
public class AppointmentControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private UserRepository userRepository;
    @MockitoBean
    private JwtService jwtService;

    @Test
    void createAppointmentReturnsBadRequestWhenRequestBodyIsInvalid() throws Exception {
        String invalidAppointmentJson = """
                {
                  "patientId": null,
                  "appointmentDateTime": "1999-01-01T00:00:00.891Z",
                  "description": "",
                  "status": null
                }
                """;

        mockMvc.perform(post("/appointments")
                    .contentType(APPLICATION_JSON)
                    .content(invalidAppointmentJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createAppointmentReturnsResponseWhenRequestBodyIsValid() throws Exception {
        String validAppointmentJson = """
                {
                  "patientId": 1,
                  "appointmentDateTime": "2100-01-01T00:00:00",
                  "description": "Annual checkup",
                  "status": "SCHEDULED"
                }
                """;

        mockMvc.perform(post("/appointments")
                    .contentType(APPLICATION_JSON)
                    .content(validAppointmentJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.appointmentId").value(1))
                .andExpect(jsonPath("$.patientId").value(1))
                .andExpect(jsonPath("$.description").value("Annual checkup"))
                .andExpect(jsonPath("$.status").value("SCHEDULED"));
    }

    @TestConfiguration
    static class TestConfig {

        @Bean
        AppointmentService appointmentService() {
            return new AppointmentService() {
                @Override
                public AppointmentResponseDto createAppointment(AppointmentRequestDto appointmentRequestDto) {
                    return new AppointmentResponseDto(
                            1L,
                            appointmentRequestDto.getPatientId(),
                            appointmentRequestDto.getAppointmentDateTime(),
                            appointmentRequestDto.getStatus(),
                            appointmentRequestDto.getDescription(),
                            null,
                            null
                    );
                }
            };
        }
    }

}
