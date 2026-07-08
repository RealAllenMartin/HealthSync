package com.example.healthsync.mapper;

import com.example.healthsync.dto.PatientRequestDto;
import com.example.healthsync.dto.PatientResponseDto;
import com.example.healthsync.entity.Patient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestTemplate;

import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class PatientMapperTest {
    private final PatientMapper patientMapper = new PatientMapper();

    @Test
    void toEntityMapsRequestDtoToPatient() throws Exception {
        PatientRequestDto requestDto = new PatientRequestDto(
                "firstName",
                "lastName",
                "email@example.com",
                LocalDate.of(1990, 1, 1),
                "555-123-4567"
        );

        Patient patient = patientMapper.toEntity(requestDto);

        assertThat(patient.getId()).isNull();
        assertThat(patient.getFirstName()).isEqualTo(requestDto.getFirstName());
        assertThat(patient.getLastName()).isEqualTo(requestDto.getLastName());
        assertThat(patient.getEmail()).isEqualTo(requestDto.getEmail());
        assertThat(patient.getDateOfBirth()).isEqualTo(requestDto.getDateOfBirth());
        assertThat(patient.getPhoneNumber()).isEqualTo(requestDto.getPhoneNumber());
    }

    @Test
    void toResponseDtoMapsPatientToResponseDto() throws Exception {
        Patient patient = new Patient (
                1L,
                "firstNameOne",
                "lastNameOne",
                "emailOne@email.com",
                LocalDate.of(1990, 1, 1),
                "555-123-4567"
        );

        PatientResponseDto responseDto = patientMapper.toResponseDto(patient);

        assertThat(responseDto.getId()).isEqualTo(patient.getId());
        assertThat(responseDto.getFirstName()).isEqualTo(patient.getFirstName());
        assertThat(responseDto.getLastName()).isEqualTo(patient.getLastName());
        assertThat(responseDto.getEmail()).isEqualTo(patient.getEmail());
        assertThat(responseDto.getDateOfBirth()).isEqualTo(patient.getDateOfBirth());
        assertThat(responseDto.getPhoneNumber()).isEqualTo(patient.getPhoneNumber());
    }
}
