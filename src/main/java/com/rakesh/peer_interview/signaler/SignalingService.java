package com.rakesh.peer_interview.signaler;

import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.rakesh.peer_interview.httpResponseUtil.CustomResponse;

@Service
public class SignalingService {
    private static final Logger logger = LoggerFactory.getLogger(SignalingService.class);

    private final Map<String, BlockingQueue<SignalingDataDTO>> peerQueues = new ConcurrentHashMap<>();

    private static final long POLLING_TIMEOUT_SEC = 30;
    
    public CustomResponse pollForMessages(String fromUsername) {
    	 BlockingQueue<SignalingDataDTO> queue = peerQueues.computeIfAbsent(fromUsername, id -> new LinkedBlockingQueue<>());
         try {
             SignalingDataDTO signalingMessage = queue.poll(POLLING_TIMEOUT_SEC, TimeUnit.SECONDS);

             if (signalingMessage == null) {
                 return CustomResponse.getResponse(HttpStatus.OK.value(), "signaling data not available", "");
             }
             
             return CustomResponse.getResponse(HttpStatus.OK.value(), "signaling data available", signalingMessage);
         } catch (InterruptedException e) {
             Thread.currentThread().interrupt();
             logger.error("Interrupted while waiting for signaling message for peer {}", fromUsername, e);
             return CustomResponse.getResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "something went wrong on server side", "");
         }
    }
    
    public CustomResponse sendMessage(SignalingDataDTO signalingDataDTO) {
    	String toUsername = signalingDataDTO.getTo();
        
        BlockingQueue<SignalingDataDTO> queue = peerQueues.computeIfAbsent(toUsername, id -> new LinkedBlockingQueue<>());
        try {
            queue.put(signalingDataDTO);
            logger.info("Message added to peer {}'s queue", toUsername);
            return CustomResponse.getResponse(HttpStatus.OK.value(), "signaling message received", "");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.error("Interrupted while adding message to peer {}'s queue", toUsername, e);
            return CustomResponse.getResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "something went wrong on server side", "");
        }
    }
}
