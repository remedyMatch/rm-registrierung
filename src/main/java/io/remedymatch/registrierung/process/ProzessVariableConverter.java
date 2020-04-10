package io.remedymatch.registrierung.process;

import java.time.format.DateTimeFormatter;

import org.camunda.bpm.engine.variable.VariableMap;
import org.camunda.bpm.engine.variable.Variables;

import io.remedymatch.registrierung.infrastructure.KeycloakUser;

final class ProzessVariableConverter {
	private ProzessVariableConverter() {

	}

	static VariableMap convertToVariables(final KeycloakUser user) {
		return Variables.createVariables() //
				.putValue("keycloakBenutzerId", user.getId().getValue()) //
				.putValue("username", user.getUsername()) //
				.putValue("createdAt", user.getCreatedAt().format(DateTimeFormatter.ISO_DATE_TIME)) //
				.putValue("vorname", user.getFirstName()) //
				.putValue("nachname", user.getLastName()) //
				.putValue("email", user.getEmail()) //
				.putValue("telefonnummer", user.getPhone()) //
				.putValue("strasse", user.getStreet()) //
				.putValue("hausnummer", user.getHousenumber()) //
				.putValue("plz", user.getZipcode()) //
				.putValue("ort", user.getCity()) //
				.putValue("land", user.getCountry()) //
				.putValue("institutionTyp", user.getCompanyType()) //
				.putValue("status", user.getStatus());
	}
}
