package com.lhcms.repository;

import com.lhcms.model.MedicalRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MedicalRecordRepository extends JpaRepository<MedicalRecord, Long> {

    List<MedicalRecord> findByMedicalHistoryId(Long medicalHistoryId);

    List<MedicalRecord> findByMedicalHistoryPatientId(Long patientId);

    List<MedicalRecord> findByUploadedById(Long doctorId);
}
