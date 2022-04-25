package com.example.authserver.service;

import com.example.authserver.helper.ConfigProperties;
import com.example.authserver.domain.model.UserPrinciple;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JwtService {

	@Autowired
	private ConfigProperties configProperties;

	private static final Logger logger = LoggerFactory.getLogger(JwtService.class);


	public String generateTokenLogin(Authentication authentication) {
		UserPrinciple userPrincipal = (UserPrinciple) authentication.getPrincipal();

		return Jwts.builder()
			.setSubject((userPrincipal.getUsername()))
			.setIssuedAt(new Date())
			.setExpiration(new Date((new Date()).getTime() + configProperties.getExpiredTime() * 1000))
			.signWith(SignatureAlgorithm.HS512, configProperties.getSecretKey())
			.compact();
	}

	@SuppressWarnings("all")
	public boolean validateJwtToken(String authToken) {
		try {
			Jwts.parser().setSigningKey(configProperties.getSecretKey()).parseClaimsJws(authToken);
			return true;
		} catch (SignatureException e) {
			logger.error("Invalid JWT signature -> Message: {} ", e);
		} catch (MalformedJwtException e) {
			logger.error("Invalid JWT token -> Message: {}", e);
		} catch (ExpiredJwtException e) {
			logger.error("Expired JWT token -> Message: {}", e);
		} catch (UnsupportedJwtException e) {
			logger.error("Unsupported JWT token -> Message: {}", e);
		} catch (IllegalArgumentException e) {
			logger.error("JWT claims string is empty -> Message: {}", e);
		}

		return false;
	}

	public String getUserNameFromJwtToken(String token) {
		return Jwts.parser()
			.setSigningKey(configProperties.getSecretKey())
			.parseClaimsJws(token)
			.getBody().getSubject();
	}

	public String generateTokenFromUsername(String username) {
		return Jwts.builder().setSubject(username).setIssuedAt(new Date())
			.setExpiration(new Date((new Date()).getTime() + configProperties.getExpiredTime()))
			.signWith(SignatureAlgorithm.HS512, configProperties.getSecretKey())
			.compact();
	}
}
