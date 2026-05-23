package com.lhcms.repository;

import com.lhcms.model.Doctor;
import com.lhcms.model.Specialization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {

    Optional<Doctor> findByUsername(String username);

    List<Doctor> findBySpecialization(Specialization specialization);

    List<Doctor> findBySpecializationId(Long specializationId);

    List<Doctor> findBySpecializationName(String specializationName);
}
