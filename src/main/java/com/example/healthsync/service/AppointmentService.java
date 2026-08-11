package com.example.healthsync.service;

import com.example.healthsync.dto.AppointmentRequestDto;
import com.example.healthsync.dto.AppointmentResponseDto;

public interface AppointmentService {

    AppointmentResponseDto createAppointment(AppointmentRequestDto appointmentRequestDto);

}
