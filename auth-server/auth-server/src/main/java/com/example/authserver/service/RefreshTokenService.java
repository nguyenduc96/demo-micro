package com.example.authserver.service;

import com.example.authserver.domain.model.RefreshToken;
import com.example.authserver.domain.model.User;
import com.example.authserver.domain.model.UserPrinciple;
import com.example.authserver.helper.ConfigProperties;
import com.example.authserver.helper.TokenRefreshException;
import com.example.authserver.repository.IRefreshTokenRepository;
import com.example.authserver.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {
	@Autowired
	private IRefreshTokenRepository refreshTokenRepository;

	@Autowired
	private ConfigProperties configProperties;

	@Autowired
	private IUserRepository userRepository;

	public Optional<RefreshToken> findByToken(String token) {
		return refreshTokenRepository.findByToken(token);
	}

	public RefreshToken createRefreshToken(Long userId) {
		RefreshToken refreshToken = new RefreshToken();
		refreshToken.setUser(userRepository.findById(userId).get());
		refreshToken.setExpiryDate(Instant.now().plusMillis(configProperties.getRefreshTime() * 1000));
		refreshToken.setToken(UUID.randomUUID().toString());
		refreshToken = refreshTokenRepository.save(refreshToken);
		return refreshToken;
	}

	public RefreshToken verifyExpiration(RefreshToken token) {
		if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
			refreshTokenRepository.delete(token);
			throw new TokenRefreshException(token.getToken(), "Refresh token was expired. Please make a new signin request");
		}

			return token;
	}

	@Transactional
	public Long deleteByUserId(Long userId) {
		return refreshTokenRepository.deleteByUser(userRepository.findById(userId).get());
	}
}
