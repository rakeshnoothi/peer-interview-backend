package com.rakesh.peer_interview.signaler;

import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class SignalingService {

    private final Map<String, BlockingQueue<SignalingDataDTO>> peerQueues = new ConcurrentHashMap<>();

    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    
    public void sendSignalingMessage(SignalingDataDTO signalingDataDTO) {
    	String toUsername = signalingDataDTO.getTo();
    	String fromUsername = signalingDataDTO.getFrom();
        
        BlockingQueue<SignalingDataDTO> queue = peerQueues.computeIfAbsent(toUsername, id -> new LinkedBlockingQueue<>());
        
        try {
			queue.put(signalingDataDTO);
			
			SignalingDataDTO signalingMessage = null;
	        if(peerQueues.containsKey(fromUsername)) {
	        	signalingMessage = peerQueues.get(fromUsername).poll();
	        	messagingTemplate.convertAndSend("/topic/signaling/message", signalingMessage);
	        }
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
        
    }
}
