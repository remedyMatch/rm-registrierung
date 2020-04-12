package io.remedymatch.registrierung.scheduler;

import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import io.remedymatch.registrierung.domain.RegistrierterUser;
import io.remedymatch.registrierung.domain.RegistrierungFreigabeService;
import io.remedymatch.registrierung.keycloak.KeycloakService;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Component
class RegistrierungAktualisator {

	private final KeycloakService keycloakClient;
	private final RegistrierungFreigabeService registrierungFreigabeService;

	/* Jede Minute */
	@Scheduled(fixedRate = 10 * 1000)
	void uberpruefeNeueRegistrationen() {

		// 1) Users mit bestätigtem Mail finden
		List<RegistrierterUser> verifizierteUsers = keycloakClient.findVerifizierteUsers();

		// 2) Prozess starten
		verifizierteUsers.forEach(user -> registrierungFreigabeService.starteFreigabeprozess(user));
	}
}
