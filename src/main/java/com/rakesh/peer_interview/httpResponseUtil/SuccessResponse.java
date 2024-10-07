package com.rakesh.peer_interview.httpResponseUtil;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

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
public class SuccessResponse {
	private HttpStatus status;
	private String message;
	private LocalDateTime dateTime;
	private Object data;
	
	public static SuccessResponse getResponse(HttpStatus status, String message, Object data) {
		return SuccessResponse.builder()
				.status(status)
				.message(message)
				.dateTime(LocalDateTime.now())
				.data(data)
				.build();
	}

}
