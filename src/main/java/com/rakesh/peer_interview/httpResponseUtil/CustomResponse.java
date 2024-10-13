package com.rakesh.peer_interview.httpResponseUtil;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomResponse {
	private int status;
	private String message;
	private LocalDateTime dateTime;
	private Object data;
	
	public static CustomResponse getResponse(int status, String message, Object data) {
		return CustomResponse.builder()
				.status(status)
				.message(message)
				.dateTime(LocalDateTime.now())
				.data(data)
				.build();
	}

}
