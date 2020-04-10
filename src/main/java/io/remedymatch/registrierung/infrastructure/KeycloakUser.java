package io.remedymatch.registrierung.infrastructure;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Builder
public class KeycloakUser {
	private KeycloakUserId id;
	private String username;
	private LocalDateTime createdAt;
	private String firstName;
	private String lastName;
	private String email;
	private String phone;
	private String street;
	private String housenumber;
	private String zipcode;
	private String city;
	private String country;
	private String companyType;
	private String status;
}
