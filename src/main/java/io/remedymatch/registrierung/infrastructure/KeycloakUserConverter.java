package io.remedymatch.registrierung.infrastructure;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.stream.Collectors;

import org.keycloak.representations.idm.UserRepresentation;

final class KeycloakUserConverter {
	private KeycloakUserConverter() {
		
	}
	
	static KeycloakUser convert(final UserRepresentation userRepresentation) {
		Map<String, List<String>> attribute = userRepresentation.getAttributes();
		
		return KeycloakUser.builder() //
				.id(new KeycloakUserId(userRepresentation.getId())) //
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
	
	private static LocalDateTime convertToLocalDateTime(final Long timestamp) {
		return LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), TimeZone.getDefault().toZoneId());
	}
	
	private static String getAttributWertAlsText(final Map<String, List<String>> attribute, final String attributKey) {
		if (attribute == null) {
			return null;
		}
		
		if (attribute.containsKey(attributKey)) {
			return attribute.get(attributKey).stream().collect(Collectors.joining(", "));
		}
		return null;
	}
}
