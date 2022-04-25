package com.example.authserver.helper;

import java.util.Date;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class ExceptionHelper {

	@ExceptionHandler(value = TokenRefreshException.class)
	@ResponseStatus(HttpStatus.FORBIDDEN)
	public ResponseAPI<?> handleTokenRefreshException(TokenRefreshException ex, WebRequest request) {
		return new ResponseAPI<>(HttpStatus.FORBIDDEN, ex.getMessage() + request.getDescription(false));
	}
}
