package com.lhcms.dto;

import com.lhcms.model.LabResult;
import com.lhcms.model.MedicalRecord;
import com.lhcms.model.Prescription;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class MedicalRecordResponse {

    private Long id;
    private String type;
    private String title;
    private String description;
    private LocalDateTime uploadedAt;

    private Long patientId;
    private String patientName;

    private Long doctorId;
    private String doctorName;

    // Prescription fields
    private String medication;
    private String dosage;
    private String frequency;
    private String duration;
    private String prescriptionNotes;

    // Lab result fields
    private String testName;
    private String result;
    private String unit;
    private String referenceRange;
    private String labStatus;

    public static MedicalRecordResponse from(MedicalRecord record) {
        MedicalRecordResponse response = new MedicalRecordResponse();
        response.setId(record.getId());
        response.setTitle(record.getTitle());
        response.setDescription(record.getDescription());
        response.setUploadedAt(record.getUploadedAt());
        response.setPatientId(record.getMedicalHistory().getPatient().getId());
        response.setPatientName(
                record.getMedicalHistory().getPatient().getFirstName() + " " +
                record.getMedicalHistory().getPatient().getLastName()
        );
        response.setDoctorId(record.getUploadedBy().getId());
        response.setDoctorName(
                record.getUploadedBy().getFirstName() + " " + record.getUploadedBy().getLastName()
        );

        if (record instanceof Prescription p) {
            response.setType("PRESCRIPTION");
            response.setMedication(p.getMedication());
            response.setDosage(p.getDosage());
            response.setFrequency(p.getFrequency());
            response.setDuration(p.getDuration());
            response.setPrescriptionNotes(p.getPrescriptionNotes());
        } else if (record instanceof LabResult lr) {
            response.setType("LAB_RESULT");
            response.setTestName(lr.getTestName());
            response.setResult(lr.getResult());
            response.setUnit(lr.getUnit());
            response.setReferenceRange(lr.getReferenceRange());
            response.setLabStatus(lr.getLabStatus());
        }

        return response;
    }
}
