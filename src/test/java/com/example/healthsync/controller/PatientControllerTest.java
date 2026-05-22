package com.example.healthsync.controller;

import com.example.healthsync.dto.PatientRequestDto;
import com.example.healthsync.dto.PatientResponseDto;
import com.example.healthsync.service.PatientService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PatientController.class)
@Import(PatientControllerTest.TestConfig.class)
class PatientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void createPatientReturnsBadRequestWhenRequestBodyIsInvalid() throws Exception {
        String invalidPatientJson = """
                {
                  "firstName": "",
                  "lastName": "Smith",
                  "email": "not-an-email",
                  "dateOfBirth": "2999-01-01",
                  "phoneNumber": "123456789012345678901"
                }
                """;

        mockMvc.perform(post("/patients")
                        .contentType(APPLICATION_JSON)
                        .content(invalidPatientJson))
                .andExpect(status().isBadRequest());
    }

    @TestConfiguration
    static class TestConfig {

        @Bean
        PatientService patientService() {
            return new PatientService() {
                @Override
                public PatientResponseDto createPatient(PatientRequestDto patientRequestDto) {
                    throw new AssertionError("Invalid requests should not reach the service layer");
                }

                @Override
                public List<PatientResponseDto> getAllPatients() {
                    throw new AssertionError("This test should not call getAllPatients");
                }

                @Override
                public PatientResponseDto getPatientById(Long id) {
                    throw new AssertionError("This test should not call getPatientById");
                }
            };
        }
    }
}
