package com.example.healthsync.service;

import com.example.healthsync.dto.PatientRequestDto;
import com.example.healthsync.dto.PatientResponseDto;
import com.example.healthsync.entity.Patient;
import com.example.healthsync.exception.ResourceNotFoundException;
import com.example.healthsync.mapper.PatientMapper;
import com.example.healthsync.repository.PatientRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;
    private final PatientMapper patientMapper;

    public PatientServiceImpl(PatientRepository patientRepository, PatientMapper patientMapper) {
        this.patientRepository = patientRepository;
        this.patientMapper = patientMapper;
    }

    public List<PatientResponseDto> getAllPatients() {
        return patientRepository.findAll()
                .stream()
                .map(patientMapper::toResponseDto)
                .toList();
    }

    public PatientResponseDto getPatientById(Long id) {
        return patientRepository.findById(id)
                .map(patientMapper::toResponseDto)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + id));
    }

    public PatientResponseDto createPatient(PatientRequestDto patientRequestDto) {
        Patient patient = patientMapper.toEntity(patientRequestDto);
        Patient savedPatient = patientRepository.save(patient);

        return patientMapper.toResponseDto(savedPatient);
    }
}
