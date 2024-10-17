package com.rakesh.peer_interview.signaler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class SignalingController {

    @Autowired
    private SignalingService signalingService;
    
    @MessageMapping("/signaling/send") // Send message from client to this destination
    @SendTo("/topic/signaling/message") // Subscribe to this destination for messages.
    public void handleSignaling(SignalingDataDTO signalingData) {
    	signalingService.sendSignalingMessage(signalingData);
    }
}
