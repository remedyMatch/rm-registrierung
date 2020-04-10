package io.remedymatch.registrierung.infrastructure;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.stream.Collectors;

import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.representations.idm.UserRepresentation;
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

	public List<KeycloakUser> findVerifizierteUsers() {

		Keycloak keycloak = KeycloakBuilder.builder().serverUrl(properties.getUrl()).realm(properties.getRealm())
				.username(properties.getUername()).password(properties.getPassword()).clientId(properties.getAdminCli())
				.resteasyClient(new ResteasyClientBuilder().connectionPoolSize(10).build()).build();

		return keycloak.realm(properties.getRealm()).users().list().stream()
	        	.filter(user -> user.getAttributes() != null)
	        	.filter(user -> user.getAttributes().get("status") != null)
	        	.filter(user -> user.getAttributes().get("status").contains("EMAIL_VERIFIKATION"))
	        	.map(this::convert)
	        	.collect(Collectors.toList());
	}
	
	private KeycloakUser convert(final UserRepresentation userRepresentation) {
		Map<String, List<String>> attribute = userRepresentation.getAttributes();
		
		return KeycloakUser.builder() //
				.id(userRepresentation.getId()) //
				.username(userRepresentation.getUsername()) //
				.createdAt(convertToLocalDateTime(userRepresentation.getCreatedTimestamp())) //
				.firstName(userRepresentation.getFirstName()) //
				.lastName(userRepresentation.getLastName()) //
				.email(userRepresentation.getEmail()) //
				.zipcode(getAttributWertAlsText(attribute, "zipcode")) //
				.country(getAttributWertAlsText(attribute, "country")) //
				.city(getAttributWertAlsText(attribute, "city")) //
				.phone(getAttributWertAlsText(attribute, "phone")) //
				.housenumber(getAttributWertAlsText(attribute, "housenumber")) //
				.companyType(getAttributWertAlsText(attribute, "compant-type")) //
				.street(getAttributWertAlsText(attribute, "street")) //
				.status(getAttributWertAlsText(attribute, "status")) //
				.build();
	}
	
	private final LocalDateTime convertToLocalDateTime(final Long timestamp) {
		return LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), TimeZone.getDefault().toZoneId());
	}
	
	private final String getAttributWertAlsText(final Map<String, List<String>> attribute, final String attributKey) {
		if (attribute.containsKey(attributKey)) {
			return attribute.get(attributKey).stream().collect(Collectors.joining(", "));
		}
		return null;
	}
}
