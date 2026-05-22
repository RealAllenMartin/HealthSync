package com.example.healthsync.mapper;

import com.example.healthsync.dto.PatientRequestDto;
import com.example.healthsync.dto.PatientResponseDto;
import com.example.healthsync.entity.Patient;
import org.springframework.stereotype.Component;

@Component
public class PatientMapper {

    public Patient toEntity(PatientRequestDto patientRequestDto) {
        Patient patient = new Patient();

        patient.setFirstName(patientRequestDto.getFirstName());
        patient.setLastName(patientRequestDto.getLastName());
        patient.setEmail(patientRequestDto.getEmail());
        patient.setDateOfBirth(patientRequestDto.getDateOfBirth());
        patient.setPhoneNumber(patientRequestDto.getPhoneNumber());

        return patient;
    }

    public PatientResponseDto toResponseDto(Patient patient) {
        PatientResponseDto patientResponseDto = new PatientResponseDto();

        patientResponseDto.setId(patient.getId());
        patientResponseDto.setFirstName(patient.getFirstName());
        patientResponseDto.setLastName(patient.getLastName());
        patientResponseDto.setEmail(patient.getEmail());
        patientResponseDto.setDateOfBirth(patient.getDateOfBirth());
        patientResponseDto.setPhoneNumber(patient.getPhoneNumber());

        return patientResponseDto;
    }

}
