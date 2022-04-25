package com.example.authserver.domain.dto;

import lombok.Data;

@Data
public class TokenRefreshRes {
	private String accessToken;
	private String refreshToken;

	private String type = "Bearer";

	public TokenRefreshRes(String accessToken, String refreshToken) {
		this.accessToken = accessToken;
		this.refreshToken = refreshToken;
	}
}
