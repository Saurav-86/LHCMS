package com.lhcms.service;

import com.lhcms.dto.ConversationResponse;
import com.lhcms.dto.MessageRequest;
import com.lhcms.dto.MessageResponse;
import com.lhcms.model.Conversation;
import com.lhcms.model.Doctor;
import com.lhcms.model.Message;
import com.lhcms.model.Patient;
import com.lhcms.repository.ConversationRepository;
import com.lhcms.repository.DoctorRepository;
import com.lhcms.repository.MessageRepository;
import com.lhcms.repository.PatientRepository;
import com.lhcms.security.UserPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ChatService {

    private final ConversationRepository conversationRepository;
    private final MessageRepository messageRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;

    public ChatService(ConversationRepository conversationRepository,
                       MessageRepository messageRepository,
                       PatientRepository patientRepository,
                       DoctorRepository doctorRepository) {
        this.conversationRepository = conversationRepository;
        this.messageRepository = messageRepository;
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
    }

    @Transactional
    public ConversationResponse startConversation(Long patientId, Long doctorId) {
        return conversationRepository.findByPatientIdAndDoctorId(patientId, doctorId)
                .map(ConversationResponse::from)
                .orElseGet(() -> {
                    Patient patient = patientRepository.findById(patientId)
                            .orElseThrow(() -> new IllegalArgumentException("Patient not found: " + patientId));
                    Doctor doctor = doctorRepository.findById(doctorId)
                            .orElseThrow(() -> new IllegalArgumentException("Doctor not found: " + doctorId));
                    Conversation conversation = new Conversation();
                    conversation.setPatient(patient);
                    conversation.setDoctor(doctor);
                    return ConversationResponse.from(conversationRepository.save(conversation));
                });
    }

    @Transactional(readOnly = true)
    public List<ConversationResponse> getMyConversations(Long userId) {
        List<Conversation> asPatient = conversationRepository.findByPatientId(userId);
        List<Conversation> asDoctor = conversationRepository.findByDoctorId(userId);
        return java.util.stream.Stream.concat(asPatient.stream(), asDoctor.stream())
                .map(ConversationResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<MessageResponse> getMessages(Long conversationId) {
        return messageRepository.findByConversationIdOrderBySentAtAsc(conversationId).stream()
                .map(MessageResponse::from)
                .toList();
    }

    @Transactional
    public MessageResponse sendMessage(Long conversationId, MessageRequest request, UserPrincipal principal) {
        Conversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new IllegalArgumentException("Conversation not found: " + conversationId));

        Message message = new Message();
        message.setConversation(conversation);
        message.setSenderId(principal.getId());
        message.setSenderName(principal.getFirstName() + " " + principal.getLastName());
        message.setSenderRole(principal.getRole());
        message.setContent(request.getContent());

        return MessageResponse.from(messageRepository.save(message));
    }
}
