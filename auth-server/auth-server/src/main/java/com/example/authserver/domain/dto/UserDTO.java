package com.example.authserver.domain.dto;

import com.example.authserver.domain.model.User;
import lombok.Data;

@Data
public class UserDTO {
	private Long id;

	private String username;

	private String password;

	public static UserDTO loadFromUser(User user) {
		UserDTO userDTO = new UserDTO();
		userDTO.id = user.getId();
		userDTO.username = user.getUsername();
		userDTO.password = user.getPassword();
		return userDTO;
	}
}
