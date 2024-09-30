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

    // In-memory storage for signaling messages for each peer (thread-safe)
    private final Map<String, BlockingQueue<SignalingDataDTO>> peerQueues = new ConcurrentHashMap<>();

    // Timeout value for long-polling in milliseconds (e.g., 10 seconds)
    private static final long POLLING_TIMEOUT_SEC = 30;

    /**
     * Long-polling GET request to retrieve new messages for a peer. This method
     * blocks until a message is available or a timeout occurs.
     */
    @GetMapping("/poll/{peerId}")
    public ResponseEntity<SignalingDataDTO> pollForMessages(@PathVariable String peerId) throws InterruptedException {
        BlockingQueue<SignalingDataDTO> queue = peerQueues.computeIfAbsent(peerId, id -> new LinkedBlockingQueue<>());
        System.out.println("Peer queues map: " + peerQueues);
        try {
            SignalingDataDTO signalingMessage = queue.poll(POLLING_TIMEOUT_SEC, TimeUnit.SECONDS);

            // If no message is available, return 204 No Content
            if (signalingMessage == null) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            System.out.println("Signaling message from GET method: " + signalingMessage);
            return new ResponseEntity<>(signalingMessage, HttpStatus.OK);
        } catch (InterruptedException e) {
            // Log the exception for debugging and reset the interrupted flag
            Thread.currentThread().interrupt();
            logger.error("Interrupted while waiting for signaling message for peer {}", peerId, e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * POST request to send a signaling message to a peer. The message is queued for
     * the target peer, to be retrieved via the poll endpoint.
     */
    @PostMapping("/message")
    public ResponseEntity<Void> sendMessage(@RequestBody SignalingDataDTO signalingDataDTO) {
    	System.out.println("Post method signaling: " + signalingDataDTO);
        String targetPeerId = null;
        if(signalingDataDTO.getRole().equalsIgnoreCase("interviewer")) {
        	targetPeerId = "interviewee";
        }else {
        	targetPeerId = "interviewer";
        }

        // Add the message to the target peer's queue
        BlockingQueue<SignalingDataDTO> queue = peerQueues.computeIfAbsent(targetPeerId, id -> new LinkedBlockingQueue<>());
        try {
            queue.put(signalingDataDTO); // put() is used to ensure blocking behavior if the queue is full
            logger.info("Message added to peer {}'s queue", targetPeerId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.error("Interrupted while adding message to peer {}'s queue", targetPeerId, e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
