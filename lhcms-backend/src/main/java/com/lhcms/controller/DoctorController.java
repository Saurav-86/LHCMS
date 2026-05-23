package com.lhcms.controller;

import com.lhcms.dto.AppointmentResponse;
import com.lhcms.dto.DoctorResponse;
import com.lhcms.service.AppointmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/doctors")
@CrossOrigin(origins = "http://localhost:3000")
public class DoctorController {

    private final AppointmentService appointmentService;

    public DoctorController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @GetMapping
    public ResponseEntity<List<DoctorResponse>> getDoctors(
            @RequestParam(required = false) String specialization) {
        if (specialization != null && !specialization.isBlank()) {
            return ResponseEntity.ok(appointmentService.getDoctorsBySpecialization(specialization));
        }
        return ResponseEntity.ok(appointmentService.getAllDoctors());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DoctorResponse> getDoctorById(@PathVariable Long id) {
        return ResponseEntity.ok(appointmentService.getDoctorById(id));
    }

    @GetMapping("/{id}/appointments")
    public ResponseEntity<List<AppointmentResponse>> getDoctorAppointments(@PathVariable Long id) {
        return ResponseEntity.ok(appointmentService.getDoctorAppointments(id));
    }
}
