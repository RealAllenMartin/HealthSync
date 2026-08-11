package com.example.healthsync.service;

import com.example.healthsync.dto.AppointmentRequestDto;
import com.example.healthsync.dto.AppointmentResponseDto;
import com.example.healthsync.entity.Appointment;
import com.example.healthsync.entity.Patient;
import com.example.healthsync.exception.ResourceNotFoundException;
import com.example.healthsync.mapper.AppointmentMapper;
import com.example.healthsync.repository.AppointmentRepository;
import com.example.healthsync.repository.PatientRepository;
import org.springframework.stereotype.Service;

@Service
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final AppointmentMapper appointmentMapper;
    private final PatientRepository patientRepository;

    public AppointmentServiceImpl(AppointmentRepository appointmentRepository, AppointmentMapper appointmentMapper, PatientRepository patientRepository) {
        this.appointmentRepository = appointmentRepository;
        this.appointmentMapper = appointmentMapper;
        this.patientRepository = patientRepository;
    }

    @Override
    public AppointmentResponseDto createAppointment(AppointmentRequestDto appointmentRequestDto) {
        Patient patient = patientRepository.findById(appointmentRequestDto.getPatientId())
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + appointmentRequestDto.getPatientId()));

        Appointment appointment = appointmentMapper.toAppointment(appointmentRequestDto, patient);
        Appointment savedAppointment = appointmentRepository.save(appointment);

        return appointmentMapper.toResponseDto(savedAppointment);
    }
}
