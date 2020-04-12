package io.remedymatch.registrierung.domain;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.sun.istack.NotNull;

import io.remedymatch.registrierung.keycloak.KeycloakService;
import io.remedymatch.registrierung.process.RegistrierungFreigabeProzessStarterService;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Validated
@Service
public class RegistrierungFreigabeService {

	private final KeycloakService keycloakService;
	private final RegistrierungFreigabeProzessStarterService prozessStarterService;

	public void starteFreigabeprozess(final @NotNull @Valid RegistrierterUser user) {
		ProzessInstanzId prozessInstanzId = prozessStarterService.prozessStarten(user);
		keycloakService.userAufInFreigabeSetzen(user.getKeycloakUserId());
	}

	public void userFreigeben(final @NotNull @Valid KeycloakUserId userId) {
		keycloakService.userAufFreigebenSetzen(userId);
		// FIXME noch ins backend uebernehmen ...
	}

	public void userAblehnen(final @NotNull @Valid KeycloakUserId userId, final @NotBlank String grund) {
		keycloakService.userAufAbgelehntSetzen(userId, grund);
	}
}
