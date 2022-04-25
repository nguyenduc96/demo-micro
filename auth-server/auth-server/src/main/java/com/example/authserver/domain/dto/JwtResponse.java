package com.example.authserver.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class JwtResponse {
	private String refresh_token;
	private Long id;

	private String token;

	private boolean isExpired;

	private String username;

	private String type = "Bearer";

	private Collection<? extends GrantedAuthority> roles;

	public JwtResponse(Long id, String token, String username, String refresh_token, Collection<? extends GrantedAuthority> roles, boolean isExpired) {
		this.id = id;
		this.token = token;
		this.username = username;
		this.refresh_token = refresh_token;
		this.roles = roles;
		this.isExpired = isExpired;
	}
}
