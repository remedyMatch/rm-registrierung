package io.remedymatch.registrierung.infrastructure;

import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.UsersResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import io.remedymatch.registrierung.properties.KeycloakProperties;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Validated
@Service
public class KeycloakClient {

	@Autowired
	private KeycloakProperties properties;

	void findNeueUsers() {

		Keycloak keycloak = KeycloakBuilder.builder().serverUrl(properties.getUrl()).realm(properties.getRealm())
				.username(properties.getUername()).password(properties.getPassword()).clientId(properties.getAdminCli())
				.resteasyClient(new ResteasyClientBuilder().connectionPoolSize(10).build()).build();

//        CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
//        credentialRepresentation.setType(CredentialRepresentation.PASSWORD);
//        credentialRepresentation.setValue("12345678");

		UsersResource usersResource = keycloak.realm(properties.getRealm()).users();

		// FIXME:

//        hier kommt Filter 
	}
}
