package io.remedymatch.registrierung.process;

import java.time.format.DateTimeFormatter;

import org.camunda.bpm.engine.variable.VariableMap;
import org.camunda.bpm.engine.variable.Variables;

import io.remedymatch.registrierung.domain.RegistrierterUser;

final class ProzessVariableConverter {
	private ProzessVariableConverter() {

	}

	static VariableMap convertToVariables(final RegistrierterUser user) {
		return Variables.createVariables() //
				.putValue("keycloakUserId", user.getKeycloakUserId().getValue()) //
				.putValue("keycloakGruppe", user.getKeycloakGruppe().getValue()) //
				.putValue("createdAt", user.getCreatedAt().format(DateTimeFormatter.ISO_DATE_TIME)) //
				.putValue("keycloakStatus", user.getKeycloakStatus()) //

				.putValue("institutionName", user.getInstitutionName()) //
				.putValue("institutionTyp", user.getInstitutionTyp()) //

				.putValue("username", user.getUsername()) //
				.putValue("vorname", user.getVorname()) //
				.putValue("nachname", user.getNachname()) //
				.putValue("email", user.getEmail()) //
				.putValue("telefon", user.getTelefon()) //
				
				.putValue("strasse", user.getStrasse()) //
				.putValue("hausnummer", user.getHausnummer()) //
				.putValue("plz", user.getPlz()) //
				.putValue("ort", user.getOrt()) //
				.putValue("land", user.getLand());
	}
}
