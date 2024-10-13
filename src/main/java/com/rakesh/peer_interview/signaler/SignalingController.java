package com.rakesh.peer_interview.signaler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rakesh.peer_interview.httpResponseUtil.CustomResponse;

@RestController
@RequestMapping("/api/v1/signal")
public class SignalingController {
	
	@Autowired
	SignalingService signalingService;

    @GetMapping("/poll/{fromUsername}")
    public ResponseEntity<CustomResponse> pollForMessages(@PathVariable String fromUsername) throws InterruptedException {
    	CustomResponse customResponse = signalingService.pollForMessages(fromUsername);
        return new ResponseEntity<CustomResponse>(customResponse, HttpStatusCode.valueOf(customResponse.getStatus()));
    }

    @PostMapping("/message")
    public ResponseEntity<CustomResponse> sendMessage(@RequestBody SignalingDataDTO signalingDataDTO) {
    	CustomResponse customResponse = signalingService.sendMessage(signalingDataDTO);
    	return new ResponseEntity<CustomResponse>(customResponse, HttpStatusCode.valueOf(customResponse.getStatus())); 
    }
}
