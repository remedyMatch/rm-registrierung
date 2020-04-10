package io.remedymatch.registrierung.infrastructure;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.sun.istack.NotNull;

import io.remedymatch.registrierung.domain.ProzessInstanzId;
import io.remedymatch.registrierung.properties.KeycloakProperties;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Validated
@Service
public class KeycloakClient {

	@Autowired
	private KeycloakProperties properties;

	public List<KeycloakUser> findVerifizierteUsers() {

		return keycloakUsers().list().stream() //
				.filter(user -> user.getAttributes() != null) //
				.filter(user -> user.getAttributes().get("status") != null) //
				.filter(user -> user.getAttributes().get("status").contains("VERIFIZIERT")
						|| user.getAttributes().get("status").contains("verifiziert")) //
				.map(KeycloakUserConverter::convert) //
				.collect(Collectors.toList());
	}
	
	public void setUserAufInFreigabe(//
			final @NotNull @Valid KeycloakUserId userId, //
			final @NotNull @Valid ProzessInstanzId prozessInstanzId) {
		UserResource userResource = keycloakUsers().get(userId.getValue());
		UserRepresentation user = userResource.toRepresentation();
		
		user.getAttributes().put("status", Arrays.asList("IN_FREIGABE"));
		user.getAttributes().put("prozessInstanzId", Arrays.asList(prozessInstanzId.getValue()));
		
		userResource.update(user);
	}
	
	public void userFreigeben(//
			final @NotNull @Valid KeycloakUserId userId) {
		UserResource userResource = keycloakUsers().get(userId.getValue());
		UserRepresentation user = userResource.toRepresentation();
		
		user.setEnabled(true);
		user.getAttributes().put("status", Arrays.asList("FREIGEGEBEN"));
		
		userResource.update(user);
	}
	
	public void userAblehnen(//
			final @NotNull @Valid KeycloakUserId userId, //
			final @NotBlank String grund) {
		UserResource userResource = keycloakUsers().get(userId.getValue());
		UserRepresentation user = userResource.toRepresentation();
		
		user.setEnabled(false);
		user.getAttributes().put("status", Arrays.asList("ABGELEHNT"));
		user.getAttributes().put("ablehnungsGrund", Arrays.asList(grund));
		
		userResource.update(user);
	}
	
	private UsersResource keycloakUsers() {
		return getKeycloak().realm(properties.getRealm()).users();
	}
	
	private Keycloak getKeycloak() {
		return KeycloakBuilder.builder() //
				.serverUrl(properties.getUrl()) //
				.realm(properties.getRealm()) //
				.username(properties.getUsername()) //
				.password(properties.getPassword()) //
				.clientId(properties.getClientId()) //
				.clientSecret(properties.getClientSecret()) //
				.resteasyClient(new ResteasyClientBuilder().connectionPoolSize(10).build()) //
				.build();
	}
}
