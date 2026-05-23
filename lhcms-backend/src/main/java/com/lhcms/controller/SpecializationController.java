package com.lhcms.controller;

import com.lhcms.model.Specialization;
import com.lhcms.service.AppointmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/specializations")
@CrossOrigin(origins = "http://localhost:3000")
public class SpecializationController {

    private final AppointmentService appointmentService;

    public SpecializationController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @GetMapping
    public ResponseEntity<List<Specialization>> getAllSpecializations() {
        return ResponseEntity.ok(appointmentService.getAllSpecializations());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Specialization> getSpecializationById(@PathVariable Long id) {
        return ResponseEntity.ok(appointmentService.getSpecializationById(id));
    }
}
