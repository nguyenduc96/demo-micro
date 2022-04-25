package com.example.authserver.model;

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
