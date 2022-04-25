package com.example.authserver.helper;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ResponseAPI<T extends Object> {
	@JsonProperty("response_code")
	private Long rpCode;

	@JsonProperty("response_message")
	private String errMsg;

	@JsonProperty("data")
	private T data;

	public ResponseAPI(HttpStatus httpStatus, T data) {
		this.rpCode = (long) httpStatus.value();
		this.errMsg = httpStatus.getReasonPhrase();
		this.data = data;
	}

	public  ResponseAPI(HttpStatus httpStatus) {
		this.rpCode = (long) httpStatus.value();
		this.errMsg = httpStatus.getReasonPhrase();
		this.data = null;
	}

}
