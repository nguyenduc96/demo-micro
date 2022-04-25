package com.example.authserver.service;

import com.example.authserver.domain.dto.JwtResponse;
import com.example.authserver.domain.dto.UserDTO;
import com.example.authserver.domain.model.*;
import com.example.authserver.helper.ResponseAPI;
import com.example.authserver.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService implements UserDetailsService {

	@Autowired
	JwtService jwtService;

	@Autowired
	RefreshTokenService refreshTokenService;

	@Autowired
	private IUserRepository userRepository;

	public Optional<User> findByUsername(String username) {
		return this.userRepository.findByUsername(username);
	}

	public Iterable<User> findAll() {
		return this.userRepository.findAll();
	}

	public Optional<User> findById(Long id) {
		return this.findById(id);
	}

	public ResponseAPI<?> save(UserDTO userDTO) {
		if (userDTO != null) {
			List<User> users = this.userRepository.findAll();
			if (users.size() > 0)
				for (User u: users) {
					if (u.getUsername().equals(userDTO.getUsername())) {
						return new ResponseAPI<>(HttpStatus.CONFLICT);
				}
			}
			User user = User.loadFromUserDTO(userDTO);
			Set<Role> roles = new HashSet<>();
			roles.add(new Role(2L, null));
			user.setRoles(roles);
			user = userRepository.save(user);
			return new ResponseAPI<>(HttpStatus.OK, UserDTO.loadFromUser(user));
		}
		return new ResponseAPI<>(HttpStatus.BAD_REQUEST);
	}

	public void remove(Long id) {
		this.userRepository.deleteById(id);
	}

	public ResponseAPI<?> login(UserDTO userDTO, AuthenticationManager authenticationManager) {
		Authentication authentication = authenticationManager.authenticate(
			new UsernamePasswordAuthenticationToken(userDTO.getUsername(), userDTO.getPassword())
		);

		SecurityContextHolder.getContext().setAuthentication(authentication);
		String token = jwtService.generateTokenLogin(authentication);
		UserPrinciple userPrinciple = (UserPrinciple) authentication.getPrincipal();

		@SuppressWarnings("all")
		User currentUser = this.findByUsername(userPrinciple.getUsername()).get();

		RefreshToken refresh_token = refreshTokenService.createRefreshToken(currentUser.getId());

		JwtResponse jwtResponse = new JwtResponse(
			currentUser.getId(),
			token,
			currentUser.getUsername(),
			refresh_token.getToken(),
			userPrinciple.getAuthorities(),
			userPrinciple.isAccountNonExpired()
			);
		return new ResponseAPI<>(HttpStatus.OK, jwtResponse);
	}



	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = this.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Username not found"));
		return UserPrinciple.build(user);
	}
}
