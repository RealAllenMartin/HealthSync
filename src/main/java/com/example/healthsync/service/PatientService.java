package com.example.healthsync.service;

import com.example.healthsync.dto.PatientRequestDto;
import com.example.healthsync.dto.PatientResponseDto;
import java.util.List;

public interface PatientService {

    List<PatientResponseDto> getAllPatients();

    PatientResponseDto getPatientById(Long id);

    PatientResponseDto createPatient(PatientRequestDto patientRequestDto);

}
