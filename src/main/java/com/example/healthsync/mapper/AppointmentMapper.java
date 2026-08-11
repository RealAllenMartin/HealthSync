package com.example.healthsync.mapper;

import com.example.healthsync.dto.AppointmentRequestDto;
import com.example.healthsync.dto.AppointmentResponseDto;
import com.example.healthsync.entity.Appointment;
import com.example.healthsync.entity.Patient;
import org.springframework.stereotype.Component;

@Component
public class AppointmentMapper {

    public Appointment toAppointment(AppointmentRequestDto appointmentRequestDto, Patient patient) {
        Appointment appointment = new Appointment();

        appointment.setPatient(patient);
        appointment.setAppointmentDateTime(appointmentRequestDto.getAppointmentDateTime());
        appointment.setStatus(appointmentRequestDto.getStatus());
        appointment.setDescription(appointmentRequestDto.getDescription());

        return appointment;
    }

    public AppointmentResponseDto toResponseDto(Appointment appointment) {
        AppointmentResponseDto appointmentResponseDto = new AppointmentResponseDto();

        appointmentResponseDto.setAppointmentId(appointment.getId());
        appointmentResponseDto.setPatientId(appointment.getPatient().getId());
        appointmentResponseDto.setAppointmentDateTime(appointment.getAppointmentDateTime());
        appointmentResponseDto.setStatus(appointment.getStatus());
        appointmentResponseDto.setDescription(appointment.getDescription());
        appointmentResponseDto.setCreatedAt(appointment.getCreatedAt());
        appointmentResponseDto.setUpdatedAt(appointment.getUpdatedAt());

        return appointmentResponseDto;
    }
}
