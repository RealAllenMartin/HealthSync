package com.example.healthsync.service;

import com.example.healthsync.dto.PatientRequestDto;
import com.example.healthsync.dto.PatientResponseDto;
import com.example.healthsync.entity.Patient;
import com.example.healthsync.exception.ResourceNotFoundException;
import com.example.healthsync.mapper.PatientMapper;
import com.example.healthsync.repository.PatientRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PatientServiceImplTest {

    @Mock
    private PatientRepository patientRepository;
    @Mock
    private PatientMapper patientMapper;
    @InjectMocks
    private PatientServiceImpl patientServiceImpl;

    @Test
    void getAllPatientsReturnsMappedResponseDtos() throws Exception {

        Patient patientOne = new Patient (
                1L,
                "firstNameOne",
                "lastNameOne",
                "emailOne@email.com",
                LocalDate.of(1990, 1, 1),
                "555-123-4567"
        );

        Patient patientTwo = new Patient (
                2L,
                "firstNameTwo",
                "lastNameTwo",
                "emailTwo@email.com",
                LocalDate.of(1991, 2, 2),
                "555-234-5678"
        );

        PatientResponseDto patientResponseDtoOne = new PatientResponseDto ();
        PatientResponseDto patientResponseDtoTwo = new PatientResponseDto ();

        when(patientRepository.findAll()).thenReturn(List.of(patientOne, patientTwo));
        when(patientMapper.toResponseDto(patientOne)).thenReturn(patientResponseDtoOne);
        when(patientMapper.toResponseDto(patientTwo)).thenReturn(patientResponseDtoTwo);

        List<PatientResponseDto> result = patientServiceImpl.getAllPatients();

        assertThat(result.size()).isEqualTo(2);
        assertThat(result.getFirst()).isEqualTo(patientResponseDtoOne);
        assertThat(result.getLast()).isEqualTo(patientResponseDtoTwo);

        verify(patientRepository).findAll();
        verify(patientMapper).toResponseDto(patientOne);
        verify(patientMapper).toResponseDto(patientTwo);
    }

    @Test
    void getPatientByIdReturnsMappedResponseDtoWhenPatientExists() throws Exception {
        Patient patient = new Patient (
                1L,
                "firstName",
                "lastName",
                "email@email.com",
                LocalDate.of(1990, 1, 1),
                "555-123-4567"
        );

        PatientResponseDto patientResponseDto = new PatientResponseDto ();

        when(patientRepository.findById(patient.getId())).thenReturn(Optional.of(patient));
        when(patientMapper.toResponseDto(patient)).thenReturn(patientResponseDto);

        assertThat(patientServiceImpl.getPatientById(patient.getId())).isEqualTo(patientResponseDto);

        verify(patientRepository).findById(patient.getId());
        verify(patientMapper).toResponseDto(patient);
    }

    @Test
    void getPatientByIdThrowsResourceNotFoundWhenPatientDoesNotExist() throws Exception {
        when(patientRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> patientServiceImpl.getPatientById(999L));

        verify(patientRepository).findById(999L);
        verify(patientMapper, never()).toResponseDto(any());
    }

    @Test
    void createPatientSavesMappedEntityAndReturnsResponseDto() throws Exception {
        PatientRequestDto patientRequestDto = new PatientRequestDto();

        Patient mappedRequest = new Patient();

        Patient patient = new Patient (
                1L,
                "firstName",
                "lastName",
                "email@email.com",
                LocalDate.of(1990, 1, 1),
                "555-123-4567"
        );

        PatientResponseDto patientResponseDto = new PatientResponseDto ();

        when(patientMapper.toEntity(patientRequestDto)).thenReturn(mappedRequest);
        when(patientRepository.save(mappedRequest)).thenReturn(patient);
        when(patientMapper.toResponseDto(patient)).thenReturn(patientResponseDto);

        assertThat(patientServiceImpl.createPatient(patientRequestDto)).isEqualTo(patientResponseDto);

        verify(patientMapper).toEntity(patientRequestDto);
        verify(patientRepository).save(mappedRequest);
        verify(patientMapper).toResponseDto(patient);
    }

}
