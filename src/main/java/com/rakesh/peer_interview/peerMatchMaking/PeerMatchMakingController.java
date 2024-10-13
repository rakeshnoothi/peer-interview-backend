package com.rakesh.peer_interview.peerMatchMaking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rakesh.peer_interview.httpResponseUtil.CustomResponse;

@RestController
@RequestMapping("/api/v1/peerMatch")
public class PeerMatchMakingController {
	
	@Autowired
	private PeerMatchMakingService peerMatchMakingService;
	
	@PostMapping("/findPeer")
	public ResponseEntity<CustomResponse> findPeer(@RequestBody PeerFormDataDTO peerFormDataDTO) throws InterruptedException{
		
		CustomResponse customResponse = peerMatchMakingService.findPeer(peerFormDataDTO);
		int status = customResponse.getStatus();
		
		return new ResponseEntity<CustomResponse>(customResponse, HttpStatusCode.valueOf(status));
	}
}
