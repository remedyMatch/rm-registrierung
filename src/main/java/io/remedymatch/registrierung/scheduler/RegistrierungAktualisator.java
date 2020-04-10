package io.remedymatch.registrierung.scheduler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import io.remedymatch.registrierung.infrastructure.KeycloakClient;
import lombok.AllArgsConstructor;
import lombok.val;

@AllArgsConstructor
@Component
class RegistrierungAktualisator {

	@Autowired
	private KeycloakClient keycloakClient;

	/* Jede Minute */
	@Scheduled(fixedRate = 60 * 1000)
	void uberpruefeNeueRegistrationen() {

		// 1) Users mit bestätigtem Mail finden
		val verifizierteUsers = keycloakClient.findVerifizierteUsers();

		// 2) Prozess starten

		// 3) Status auf inFreigabe aendern
	}
}
