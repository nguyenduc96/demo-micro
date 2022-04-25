package com.example.authserver.repository;

import com.example.authserver.domain.model.RefreshToken;
import com.example.authserver.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IRefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
	Optional<RefreshToken> findByToken(String token);

	Long deleteByUser(User user);
}
