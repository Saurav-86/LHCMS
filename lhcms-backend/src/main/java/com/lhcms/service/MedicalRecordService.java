package com.lhcms.service;

import com.lhcms.dto.LabResultRequest;
import com.lhcms.dto.MedicalRecordResponse;
import com.lhcms.dto.PrescriptionRequest;
import com.lhcms.model.*;
import com.lhcms.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MedicalRecordService {

    private final MedicalRecordRepository medicalRecordRepository;
    private final MedicalHistoryRepository medicalHistoryRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;

    public MedicalRecordService(MedicalRecordRepository medicalRecordRepository,
                                MedicalHistoryRepository medicalHistoryRepository,
                                PatientRepository patientRepository,
                                DoctorRepository doctorRepository) {
        this.medicalRecordRepository = medicalRecordRepository;
        this.medicalHistoryRepository = medicalHistoryRepository;
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
    }

    @Transactional
    public MedicalRecordResponse uploadPrescription(PrescriptionRequest request, Long doctorId) {
        MedicalHistory history = getOrCreateHistory(request.getPatientId());
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new IllegalArgumentException("Doctor not found: " + doctorId));

        Prescription prescription = new Prescription();
        prescription.setMedicalHistory(history);
        prescription.setUploadedBy(doctor);
        prescription.setTitle(request.getTitle());
        prescription.setDescription(request.getDescription());
        prescription.setMedication(request.getMedication());
        prescription.setDosage(request.getDosage());
        prescription.setFrequency(request.getFrequency());
        prescription.setDuration(request.getDuration());
        prescription.setPrescriptionNotes(request.getPrescriptionNotes());

        return MedicalRecordResponse.from(medicalRecordRepository.save(prescription));
    }

    @Transactional
    public MedicalRecordResponse uploadLabResult(LabResultRequest request, Long doctorId) {
        MedicalHistory history = getOrCreateHistory(request.getPatientId());
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new IllegalArgumentException("Doctor not found: " + doctorId));

        LabResult labResult = new LabResult();
        labResult.setMedicalHistory(history);
        labResult.setUploadedBy(doctor);
        labResult.setTitle(request.getTitle());
        labResult.setDescription(request.getDescription());
        labResult.setTestName(request.getTestName());
        labResult.setResult(request.getResult());
        labResult.setUnit(request.getUnit());
        labResult.setReferenceRange(request.getReferenceRange());
        labResult.setLabStatus(request.getLabStatus());

        return MedicalRecordResponse.from(medicalRecordRepository.save(labResult));
    }

    @Transactional(readOnly = true)
    public List<MedicalRecordResponse> getPatientRecords(Long patientId) {
        return medicalRecordRepository.findByMedicalHistoryPatientId(patientId).stream()
                .map(MedicalRecordResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public MedicalRecordResponse getRecordById(Long id) {
        return medicalRecordRepository.findById(id)
                .map(MedicalRecordResponse::from)
                .orElseThrow(() -> new IllegalArgumentException("Record not found: " + id));
    }

    @Transactional(readOnly = true)
    public List<MedicalRecordResponse> getDoctorUploads(Long doctorId) {
        return medicalRecordRepository.findByUploadedById(doctorId).stream()
                .map(MedicalRecordResponse::from)
                .toList();
    }

    private MedicalHistory getOrCreateHistory(Long patientId) {
        return medicalHistoryRepository.findByPatientId(patientId).orElseGet(() -> {
            Patient patient = patientRepository.findById(patientId)
                    .orElseThrow(() -> new IllegalArgumentException("Patient not found: " + patientId));
            MedicalHistory history = new MedicalHistory();
            history.setPatient(patient);
            return medicalHistoryRepository.save(history);
        });
    }
}
