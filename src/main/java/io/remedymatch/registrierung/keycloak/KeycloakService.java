package io.remedymatch.registrierung.keycloak;

import static io.remedymatch.registrierung.keycloak.KeycloakAttribute.KEYCLOAK_USER_ATTRIBUT_ABLEHNUNGSGRUND;
import static io.remedymatch.registrierung.keycloak.KeycloakAttribute.KEYCLOAK_USER_ATTRIBUT_STATUS;
import static io.remedymatch.registrierung.keycloak.KeycloakAttribute.KEYCLOAK_USER_STATUS_ABGELEHNT;
import static io.remedymatch.registrierung.keycloak.KeycloakAttribute.KEYCLOAK_USER_STATUS_FREIGEGEBEN;
import static io.remedymatch.registrierung.keycloak.KeycloakAttribute.KEYCLOAK_USER_STATUS_IN_FREIGABE;
import static io.remedymatch.registrierung.keycloak.KeycloakAttribute.KEYCLOAK_USER_STATUS_VERIFIZIERT;

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
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.sun.istack.NotNull;

import io.remedymatch.registrierung.domain.KeycloakUserId;
import io.remedymatch.registrierung.domain.RegistrierterUser;
import io.remedymatch.registrierung.properties.KeycloakProperties;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Validated
@Service
public class KeycloakService {

	private final KeycloakProperties properties;

	public List<RegistrierterUser> findVerifizierteUsers() {

		return keycloakUsers().list().stream() //
				.filter(user -> isInStatus(user, KEYCLOAK_USER_STATUS_VERIFIZIERT)) //
				.map(KeycloakUserConverter::convert) //
				.collect(Collectors.toList());
	}

	public void userAufInFreigabeSetzen(//
			final @NotNull @Valid KeycloakUserId userId) {
		UserResource userResource = keycloakUsers().get(userId.getValue());
		UserRepresentation user = userResource.toRepresentation();

		updateStatus(user, KEYCLOAK_USER_STATUS_IN_FREIGABE);

		userResource.update(user);
	}

	public void userAufFreigebenSetzen(//
			final @NotNull @Valid KeycloakUserId userId) {
		UserResource userResource = keycloakUsers().get(userId.getValue());
		UserRepresentation user = userResource.toRepresentation();

		user.setEnabled(true);
		updateStatus(user, KEYCLOAK_USER_STATUS_FREIGEGEBEN);

		userResource.update(user);
	}

	public void userAufAbgelehntSetzen(//
			final @NotNull @Valid KeycloakUserId userId, //
			final @NotBlank String grund) {
		UserResource userResource = keycloakUsers().get(userId.getValue());
		UserRepresentation user = userResource.toRepresentation();

		user.setEnabled(false);
		updateStatus(user, KEYCLOAK_USER_STATUS_ABGELEHNT);
		updateAttribut(user, KEYCLOAK_USER_ATTRIBUT_ABLEHNUNGSGRUND, grund);

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

	private boolean isInStatus(final UserRepresentation user, final String status) {
		return user.getAttributes() != null && user.getAttributes().containsKey(KEYCLOAK_USER_ATTRIBUT_STATUS)
				&& user.getAttributes().get(KEYCLOAK_USER_ATTRIBUT_STATUS).contains(status);
	}

	private void updateStatus(final UserRepresentation user, final String status) {
		updateAttribut(user, KEYCLOAK_USER_ATTRIBUT_STATUS, status);
	}

	private void updateAttribut(final UserRepresentation user, final String attributKey, final String attributWert) {
		user.getAttributes().put(attributKey, Arrays.asList(attributWert));
	}
}
