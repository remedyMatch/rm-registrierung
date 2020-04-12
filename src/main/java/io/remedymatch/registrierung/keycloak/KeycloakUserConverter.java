package io.remedymatch.registrierung.keycloak;

import static io.remedymatch.registrierung.keycloak.KeycloakAttribute.KEYCLOAK_USER_ATTRIBUT_CITY;
import static io.remedymatch.registrierung.keycloak.KeycloakAttribute.KEYCLOAK_USER_ATTRIBUT_COMPANY;
import static io.remedymatch.registrierung.keycloak.KeycloakAttribute.KEYCLOAK_USER_ATTRIBUT_COMPANY_TYPE;
import static io.remedymatch.registrierung.keycloak.KeycloakAttribute.KEYCLOAK_USER_ATTRIBUT_COUNTRY;
import static io.remedymatch.registrierung.keycloak.KeycloakAttribute.KEYCLOAK_USER_ATTRIBUT_HOUSENUMBER;
import static io.remedymatch.registrierung.keycloak.KeycloakAttribute.KEYCLOAK_USER_ATTRIBUT_PHONE;
import static io.remedymatch.registrierung.keycloak.KeycloakAttribute.KEYCLOAK_USER_ATTRIBUT_STATUS;
import static io.remedymatch.registrierung.keycloak.KeycloakAttribute.KEYCLOAK_USER_ATTRIBUT_STREET;
import static io.remedymatch.registrierung.keycloak.KeycloakAttribute.KEYCLOAK_USER_ATTRIBUT_ZIPCODE;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.stream.Collectors;

import org.keycloak.representations.idm.UserRepresentation;

import io.remedymatch.registrierung.domain.KeycloakGruppe;
import io.remedymatch.registrierung.domain.KeycloakStatus;
import io.remedymatch.registrierung.domain.KeycloakUserId;
import io.remedymatch.registrierung.domain.RegistrierterUser;

final class KeycloakUserConverter {
	private KeycloakUserConverter() {

	}

	static RegistrierterUser convert(final UserRepresentation userRepresentation) {
		Map<String, List<String>> attribute = userRepresentation.getAttributes();

		return RegistrierterUser.builder() //
				// Keycloak
				.keycloakUserId(new KeycloakUserId(userRepresentation.getId())) //
				.keycloakGruppe(new KeycloakGruppe(getGruppenWertAlsText(userRepresentation.getGroups()))) //
				.createdAt(convertToLocalDateTime(userRepresentation.getCreatedTimestamp())) //
				.keycloakStatus(new KeycloakStatus(getAttributWertAlsText(attribute, KEYCLOAK_USER_ATTRIBUT_STATUS))) //
				// Institution
				.institutionName(getAttributWertAlsText(attribute, KEYCLOAK_USER_ATTRIBUT_COMPANY)) //
				.institutionTyp(getAttributWertAlsText(attribute, KEYCLOAK_USER_ATTRIBUT_COMPANY_TYPE)) //
				// Person
				.username(userRepresentation.getUsername()) //
				.vorname(userRepresentation.getFirstName()) //
				.nachname(userRepresentation.getLastName()) //
				.email(userRepresentation.getEmail()) //
				.telefon(getAttributWertAlsText(attribute, KEYCLOAK_USER_ATTRIBUT_PHONE)) //

				.strasse(getAttributWertAlsText(attribute, KEYCLOAK_USER_ATTRIBUT_STREET)) //
				.hausnummer(getAttributWertAlsText(attribute, KEYCLOAK_USER_ATTRIBUT_HOUSENUMBER)) //
				.plz(getAttributWertAlsText(attribute, KEYCLOAK_USER_ATTRIBUT_ZIPCODE)) //
				.ort(getAttributWertAlsText(attribute, KEYCLOAK_USER_ATTRIBUT_CITY)) //
				.land(getAttributWertAlsText(attribute, KEYCLOAK_USER_ATTRIBUT_COUNTRY)) //

				.build();
	}

	private static LocalDateTime convertToLocalDateTime(final Long timestamp) {
		return LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), TimeZone.getDefault().toZoneId());
	}

	private static String getGruppenWertAlsText(final List<String> gruppen) {
		return gruppen.stream().collect(Collectors.joining(", "));
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
