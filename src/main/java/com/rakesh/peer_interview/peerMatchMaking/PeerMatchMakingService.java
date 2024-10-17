package com.rakesh.peer_interview.peerMatchMaking;

import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.rakesh.peer_interview.httpResponseUtil.CustomResponse;

@Service
public class PeerMatchMakingService {
	
	private final Map<String, Map<String, BlockingQueue<String>>> topicRoom = new ConcurrentHashMap<>();
	
	private static final long POLLING_TIMEOUT_SEC = 10;
	
	
	public CustomResponse findPeer(PeerFormDataDTO peerFormDataDTO) throws InterruptedException {
		String topic = peerFormDataDTO.getTopic();
		String role = peerFormDataDTO.getRole();
		
		System.out.println("Peer form data: " + peerFormDataDTO);
		
		try {
			addPeerToRoom(peerFormDataDTO);
			
			String targetPeerUsername = getTargetPeerUsername(peerFormDataDTO);
			if(targetPeerUsername == null) {
				topicRoom.get(topic).get(role).poll();
				return CustomResponse.getResponse(HttpStatus.OK.value(),"cannot find peer" , "");
			}
			return CustomResponse.getResponse(HttpStatus.OK.value(),"found peer" , targetPeerUsername);
		}catch(NullPointerException n) {
			return CustomResponse.getResponse(HttpStatus.UNPROCESSABLE_ENTITY.value(),"cannot process the request with provided data" , "");
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
	
	private void addPeerToRoom(PeerFormDataDTO peerFormDataDTO) throws InterruptedException {
		String topic = peerFormDataDTO.getTopic();
		String fromUsername = peerFormDataDTO.getFromUsername();
		String role = peerFormDataDTO.getRole();
		
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
	}
	
}
