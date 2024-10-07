package com.rakesh.peer_interview.signaler;

import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/signal")
public class SignalingController {

    private static final Logger logger = LoggerFactory.getLogger(SignalingController.class);

    private final Map<String, BlockingQueue<SignalingDataDTO>> peerQueues = new ConcurrentHashMap<>();

    private static final long POLLING_TIMEOUT_SEC = 30;

    @GetMapping("/poll/{fromUsername}")
    public ResponseEntity<SignalingDataDTO> pollForMessages(@PathVariable String fromUsername) throws InterruptedException {
        BlockingQueue<SignalingDataDTO> queue = peerQueues.computeIfAbsent(fromUsername, id -> new LinkedBlockingQueue<>());
        try {
            SignalingDataDTO signalingMessage = queue.poll(POLLING_TIMEOUT_SEC, TimeUnit.SECONDS);

            if (signalingMessage == null) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            
            return new ResponseEntity<>(signalingMessage, HttpStatus.OK);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.error("Interrupted while waiting for signaling message for peer {}", fromUsername, e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/message")
    public ResponseEntity<Void> sendMessage(@RequestBody SignalingDataDTO signalingDataDTO) {
        String toUsername = signalingDataDTO.getTo();
        
        BlockingQueue<SignalingDataDTO> queue = peerQueues.computeIfAbsent(toUsername, id -> new LinkedBlockingQueue<>());
        try {
            queue.put(signalingDataDTO);
            logger.info("Message added to peer {}'s queue", toUsername);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.error("Interrupted while adding message to peer {}'s queue", toUsername, e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
