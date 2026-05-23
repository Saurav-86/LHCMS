package com.lhcms.dto;

import com.lhcms.model.Message;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class MessageResponse {

    private Long id;
    private Long conversationId;
    private Long senderId;
    private String senderName;
    private String senderRole;
    private String content;
    private LocalDateTime sentAt;

    public static MessageResponse from(Message m) {
        return new MessageResponse(
                m.getId(),
                m.getConversation().getId(),
                m.getSenderId(),
                m.getSenderName(),
                m.getSenderRole(),
                m.getContent(),
                m.getSentAt()
        );
    }
}
