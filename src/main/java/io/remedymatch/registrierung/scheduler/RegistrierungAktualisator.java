package io.remedymatch.registrierung.scheduler;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import io.remedymatch.registrierung.domain.ProzessInstanzId;
import io.remedymatch.registrierung.infrastructure.KeycloakClient;
import io.remedymatch.registrierung.infrastructure.KeycloakUser;
import io.remedymatch.registrierung.process.EngineClient;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Component
class RegistrierungAktualisator {

	@Autowired
	private KeycloakClient keycloakClient;

	@Autowired
	private EngineClient engineClient;

	/* Jede Minute */
	@Scheduled(fixedRate = 10 * 1000)
	void uberpruefeNeueRegistrationen() {

		// 1) Users mit bestätigtem Mail finden
		List<KeycloakUser> verifizierteUsers = keycloakClient.findVerifizierteUsers();

		// 2) Prozess starten
		verifizierteUsers.forEach(this::starteRegistrierungProzess);
	}

	private void starteRegistrierungProzess(final KeycloakUser user) {
		ProzessInstanzId prozessInstanzId = engineClient.prozessStarten(user);
		keycloakClient.setUserAufInFreigabe(user.getId(), prozessInstanzId);
	}
}
