package com.lhcms.controller;

import com.lhcms.dto.LabResultRequest;
import com.lhcms.dto.MedicalRecordResponse;
import com.lhcms.dto.PrescriptionRequest;
import com.lhcms.security.UserPrincipal;
import com.lhcms.service.MedicalRecordService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class MedicalRecordController {

    private final MedicalRecordService medicalRecordService;

    public MedicalRecordController(MedicalRecordService medicalRecordService) {
        this.medicalRecordService = medicalRecordService;
    }

    @PostMapping("/api/medical-records/prescription")
    public ResponseEntity<MedicalRecordResponse> uploadPrescription(
            @Valid @RequestBody PrescriptionRequest request,
            @AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok(medicalRecordService.uploadPrescription(request, principal.getId()));
    }

    @PostMapping("/api/medical-records/lab-result")
    public ResponseEntity<MedicalRecordResponse> uploadLabResult(
            @Valid @RequestBody LabResultRequest request,
            @AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok(medicalRecordService.uploadLabResult(request, principal.getId()));
    }

    @GetMapping("/api/patients/{patientId}/records")
    public ResponseEntity<List<MedicalRecordResponse>> getPatientRecords(@PathVariable Long patientId) {
        return ResponseEntity.ok(medicalRecordService.getPatientRecords(patientId));
    }

    @GetMapping("/api/medical-records/{id}")
    public ResponseEntity<MedicalRecordResponse> getRecordById(@PathVariable Long id) {
        return ResponseEntity.ok(medicalRecordService.getRecordById(id));
    }

    @GetMapping("/api/medical-records/my-uploads")
    public ResponseEntity<List<MedicalRecordResponse>> myUploads(
            @AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok(medicalRecordService.getDoctorUploads(principal.getId()));
    }
}
