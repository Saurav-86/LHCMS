package com.lhcms.dto;

import com.lhcms.model.Conversation;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ConversationResponse {

    private Long id;
    private Long patientId;
    private String patientName;
    private Long doctorId;
    private String doctorName;
    private LocalDateTime createdAt;

    public static ConversationResponse from(Conversation c) {
        return new ConversationResponse(
                c.getId(),
                c.getPatient().getId(),
                c.getPatient().getFirstName() + " " + c.getPatient().getLastName(),
                c.getDoctor().getId(),
                c.getDoctor().getFirstName() + " " + c.getDoctor().getLastName(),
                c.getCreatedAt()
        );
    }
}
