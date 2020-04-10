package io.remedymatch.registrierung.properties;

import javax.validation.constraints.NotBlank;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import lombok.Getter;
import lombok.Setter;

/**
 * Einstellungen zur Keycloak
 */
@Getter
@Setter
@Validated
@ConfigurationProperties(prefix = "io.remedymatch.keycloak")
public class KeycloakProperties {

	@NotBlank
	private String url;

	@NotBlank
	private String realm;

	@NotBlank
	private String uername;

	@NotBlank
	private String password;

	@NotBlank
	private String adminCli;
	
}
