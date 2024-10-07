package com.rakesh.peer_interview.peerMatchMaking;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PeerFormDataDTO {
	private String topic;
	private String fromUsername;
	private String role;
}
