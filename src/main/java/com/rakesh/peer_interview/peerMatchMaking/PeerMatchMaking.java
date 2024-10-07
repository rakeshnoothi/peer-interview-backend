package com.rakesh.peer_interview.peerMatchMaking;

import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/peerMatch")
public class PeerMatchMaking {
	private final Map<String, Map<String, BlockingQueue<String>>> topicRoom = new ConcurrentHashMap<>();
	
	private static final long POLLING_TIMEOUT_SEC = 10;
	
	@PostMapping("/findPeer")
	public ResponseEntity<String> findPeer(@RequestBody PeerFormDataDTO peerFormDataDTO){
		String topic = peerFormDataDTO.getTopic();
		String fromUsername = peerFormDataDTO.getFromUsername();
		String role = peerFormDataDTO.getRole();
		
		
		
		System.out.println("Peer form data: " + peerFormDataDTO);
		
		try {
			if(!topicRoom.containsKey(topic)) {
				Map<String, BlockingQueue<String>> map = new ConcurrentHashMap<>();
				
				map.put("interviewer", new LinkedBlockingQueue<>());
				map.put("interviewee", new LinkedBlockingQueue<>());
				
				if(role.equalsIgnoreCase("interviewer")) {
					map.get("interviewer").put(fromUsername);
				}else {
					map.get("interviewee").put(fromUsername);
				}
				
				topicRoom.put(topic, map);
			}else {
				topicRoom.get(topic).get(role).put(fromUsername);
			}
			
			String targetPeerUsername = getTargetPeerUsername(peerFormDataDTO);
			if(targetPeerUsername == null) {
				topicRoom.get(topic).get(role).poll();
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}
			return new ResponseEntity<>(targetPeerUsername, HttpStatus.OK);
		}catch(InterruptedException e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}catch(NullPointerException n) {
			return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
		}
	}
	
	private String getTargetPeerUsername(PeerFormDataDTO peerFormDataDTO) {
		String topic = peerFormDataDTO.getTopic();
		String role = null;
		
		if(peerFormDataDTO.getRole().equalsIgnoreCase("interviewer")) {
			role = "interviewee";
		}else {
			role = "interviewer";
		}
		
		try {
			String targetUsername = topicRoom.get(topic).get(role).poll(POLLING_TIMEOUT_SEC, TimeUnit.SECONDS);
			return targetUsername;
		} catch (InterruptedException e) {
			e.printStackTrace();
			return null;
		}
	}
}
