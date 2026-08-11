package com.example.healthsync.service;

import com.example.healthsync.dto.AppointmentRequestDto;
import com.example.healthsync.dto.AppointmentResponseDto;
import com.example.healthsync.entity.Appointment;
import com.example.healthsync.entity.AppointmentStatus;
import com.example.healthsync.entity.Patient;
import com.example.healthsync.exception.ResourceNotFoundException;
import com.example.healthsync.mapper.AppointmentMapper;
import com.example.healthsync.repository.AppointmentRepository;
import com.example.healthsync.repository.PatientRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AppointmentServiceImplTest {

    @Mock
    private AppointmentRepository appointmentRepository;
    @Mock
    private AppointmentMapper appointmentMapper;
    @Mock
    private PatientRepository patientRepository;
    @InjectMocks
    private AppointmentServiceImpl appointmentService;

    @Test
    void createAppointmentReturnsResponseWhenPatientExists() throws Exception {
        Long patientId = 1L;
        LocalDateTime appointmentDateTime = LocalDateTime.now().plusDays(1);

         AppointmentRequestDto request = new AppointmentRequestDto(
                 patientId,
                 appointmentDateTime,
                 "Annual checkup",
                 AppointmentStatus.SCHEDULED
         );

         Patient patient = new Patient(
                 patientId,
                 "FirstName",
                 "LastName",
                 "email@email.com",
                 LocalDate.of(1990, 1, 1),
                 "555-123-4567"
         );

         Appointment mappedAppointment = new Appointment();
         Appointment savedAppointment = new Appointment(
                 1L,
                 patient,
                 appointmentDateTime,
                 AppointmentStatus.SCHEDULED,
                 "Annual checkup",
                 LocalDateTime.now(),
                 LocalDateTime.now()
         );

        AppointmentResponseDto responseDto = new AppointmentResponseDto(
                1L,
                patientId,
                appointmentDateTime,
                AppointmentStatus.SCHEDULED,
                "Annual checkup",
                savedAppointment.getCreatedAt(),
                savedAppointment.getUpdatedAt()
        );

        when(patientRepository.findById(patientId)).thenReturn(Optional.of(patient));
        when(appointmentMapper.toAppointment(request, patient)).thenReturn(mappedAppointment);
        when(appointmentRepository.save(mappedAppointment)).thenReturn(savedAppointment);
        when(appointmentMapper.toResponseDto(savedAppointment)).thenReturn(responseDto);

        AppointmentResponseDto result = appointmentService.createAppointment(request);

        assertThat(result).isEqualTo(responseDto);
        assertThat(result.getAppointmentId()).isEqualTo(1L);
        assertThat(result.getPatientId()).isEqualTo(patientId);

        verify(patientRepository).findById(patientId);
        verify(appointmentMapper).toAppointment(request, patient);
        verify(appointmentRepository).save(mappedAppointment);
        verify(appointmentMapper).toResponseDto(savedAppointment);
    }

    @Test
    void createAppointmentThrowsResourceNotFoundWhenPatientDoesNotExist() {
        Long patientId = 999L;

        AppointmentRequestDto request = new AppointmentRequestDto(
                patientId,
                LocalDateTime.now().plusDays(1),
                "Annual checkup",
                AppointmentStatus.SCHEDULED
        );

        when(patientRepository.findById(patientId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> appointmentService.createAppointment(request));

        verify(patientRepository).findById(patientId);
        verify(appointmentMapper, never()).toAppointment(any(), any());
        verify(appointmentRepository, never()).save(any());
        verify(appointmentMapper, never()).toResponseDto(any());
    }
}
