package com.lhcms.repository;

import com.lhcms.model.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation, Long> {

    List<Conversation> findByPatientId(Long patientId);

    List<Conversation> findByDoctorId(Long doctorId);

    Optional<Conversation> findByPatientIdAndDoctorId(Long patientId, Long doctorId);
}
