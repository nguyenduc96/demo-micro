package com.example.authserver.controller;

import com.example.authserver.helper.ResponseAPI;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/test")
public class Controller {

	@GetMapping("/admin")
	public ResponseAPI<?> get(Authentication authentication) {
		return new ResponseAPI<>(HttpStatus.OK, authentication.getPrincipal());
	}

	@GetMapping("/user")
	public ResponseAPI<?> get2(Authentication authentication) {
		authentication = SecurityContextHolder.getContext().getAuthentication();
		return new ResponseAPI<>(HttpStatus.OK, authentication.getPrincipal());
	}
}
