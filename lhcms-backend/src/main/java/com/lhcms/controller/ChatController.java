package com.lhcms.controller;

import com.lhcms.dto.ConversationResponse;
import com.lhcms.dto.MessageRequest;
import com.lhcms.dto.MessageResponse;
import com.lhcms.security.UserPrincipal;
import com.lhcms.service.ChatService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class ChatController {

    private final ChatService chatService;
    private final SimpMessagingTemplate messagingTemplate;

    public ChatController(ChatService chatService, SimpMessagingTemplate messagingTemplate) {
        this.chatService = chatService;
        this.messagingTemplate = messagingTemplate;
    }

    // REST — start or fetch an existing conversation
    @PostMapping("/api/conversations")
    public ResponseEntity<ConversationResponse> startConversation(
            @RequestParam Long patientId,
            @RequestParam Long doctorId) {
        return ResponseEntity.ok(chatService.startConversation(patientId, doctorId));
    }

    // REST — list conversations for the logged-in user
    @GetMapping("/api/conversations")
    public ResponseEntity<List<ConversationResponse>> myConversations(
            @AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok(chatService.getMyConversations(principal.getId()));
    }

    // REST — load message history
    @GetMapping("/api/conversations/{id}/messages")
    public ResponseEntity<List<MessageResponse>> getMessages(@PathVariable Long id) {
        return ResponseEntity.ok(chatService.getMessages(id));
    }

    // REST — send a message (HTTP fallback)
    @PostMapping("/api/conversations/{id}/messages")
    public ResponseEntity<MessageResponse> sendMessageHttp(
            @PathVariable Long id,
            @Valid @RequestBody MessageRequest request,
            @AuthenticationPrincipal UserPrincipal principal) {
        MessageResponse response = chatService.sendMessage(id, request, principal);
        messagingTemplate.convertAndSend("/topic/conversation/" + id, response);
        return ResponseEntity.ok(response);
    }

    // WebSocket — real-time message via STOMP
    // Client publishes to: /app/chat/{conversationId}
    // Server broadcasts to: /topic/conversation/{conversationId}
    @MessageMapping("/chat/{conversationId}")
    public void sendMessageWs(
            @DestinationVariable Long conversationId,
            @Payload MessageRequest request,
            @AuthenticationPrincipal UserPrincipal principal) {
        MessageResponse response = chatService.sendMessage(conversationId, request, principal);
        messagingTemplate.convertAndSend("/topic/conversation/" + conversationId, response);
    }
}
