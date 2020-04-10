package io.remedymatch.registrierung.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Component
class RegistrierungAktualisator {

	/* Jede Minute */
	@Scheduled(fixedRate = 60 * 1000)
	void uberpruefeNeueRegistrationen() {

		// 1) Users mit bestätigtem Mail finden
		
		// 2) Prozess starten - für die, wo noch kein ist...
	}
}
