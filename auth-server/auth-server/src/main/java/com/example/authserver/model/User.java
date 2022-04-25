package com.example.authserver.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Entity
@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	private Long id;

	@Column
	private String username;

	@Column
	private String password;

	@ManyToMany(fetch = FetchType.EAGER)
	private Set<Role> roles;

	public static User loadFromUserDTO(UserDTO userDTO) {
		User user = new User();
		user.id = userDTO.getId();
		user.username = userDTO.getUsername();
		user.password = userDTO.getPassword();
		return user;
	}
}
