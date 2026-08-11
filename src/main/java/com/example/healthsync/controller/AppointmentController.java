package com.example.healthsync.controller;

import com.example.healthsync.dto.AppointmentRequestDto;
import com.example.healthsync.dto.AppointmentResponseDto;
import com.example.healthsync.service.AppointmentService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {

        this.appointmentService = appointmentService;
    }

    @PostMapping
    public AppointmentResponseDto createAppointment(@Valid @RequestBody AppointmentRequestDto appointmentRequestDto) {
        return appointmentService.createAppointment(appointmentRequestDto);
    }

}
