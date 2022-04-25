package com.example.authserver.domain.dto;

import lombok.Data;

@Data
public class TokenRefreshReq {
	private String refreshToken;
}
