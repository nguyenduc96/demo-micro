package com.example.authserver.controller;


import com.example.authserver.domain.dto.TokenRefreshRes;
import com.example.authserver.domain.model.RefreshToken;
import com.example.authserver.helper.ResponseAPI;
import com.example.authserver.domain.dto.TokenRefreshReq;
import com.example.authserver.domain.dto.UserDTO;
import com.example.authserver.helper.TokenRefreshException;
import com.example.authserver.service.JwtService;
import com.example.authserver.service.RefreshTokenService;
import com.example.authserver.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/v1")
public class LoginController {
	@Autowired
	private UserService userService;

	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	RefreshTokenService refreshTokenService;

	@Autowired
	JwtService jwtService;

	@Autowired
	PasswordEncoder passwordEncoder;

	@PostMapping("/login")
	public ResponseAPI<?> login(@RequestBody UserDTO userDTO) {
		return this.userService.login(userDTO, authenticationManager);
	}

	@PostMapping("/register")
	public ResponseAPI<?> register(@RequestBody UserDTO userDTO) {
		userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));
		return this.userService.save(userDTO);
	}

	@PostMapping("/logout")
	public ResponseAPI<?> logout(HttpServletRequest request, HttpServletResponse response) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null) {
			new SecurityContextLogoutHandler().logout(request, response, authentication);
		}
		return new ResponseAPI<>(HttpStatus.OK, "Success");
	}

	@PostMapping("/refresh-token")
	public ResponseAPI<?> refreshToken(@RequestBody TokenRefreshReq tokenRefreshReq, Authentication authentication) {
		String tokenRq = tokenRefreshReq.getRefreshToken();

		return refreshTokenService.findByToken(tokenRq)
			.map(refreshTokenService::verifyExpiration)
			.map(RefreshToken::getUser)
			.map(user -> {
				String token = jwtService.generateTokenFromUsername(user.getUsername());
				return new ResponseAPI<>(HttpStatus.OK ,new TokenRefreshRes(token, tokenRq));
			})
			.orElseThrow(() -> new TokenRefreshException(tokenRq,
				"Refresh token is not in database!"));
	}
}
