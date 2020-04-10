package io.remedymatch.registrierung.process;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.client.RestTemplate;

import io.remedymatch.registrierung.domain.ProzessInstanzId;
import io.remedymatch.registrierung.infrastructure.KeycloakUser;
import io.remedymatch.registrierung.properties.EngineProperties;
import lombok.AllArgsConstructor;
import lombok.val;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@Validated
@Service
@Slf4j
public class EngineClient {

	private static final String REGISTRIERUNG_FREIGABE_PROZESS_KEY = "rm_registrierung_freigabe";

	@Autowired
	private EngineProperties engineProperties;

	public ProzessInstanzId prozessStarten(final @NotNull KeycloakUser user) {
		log.info("Starte Prozess fuer User: " + user.getUsername());

		val request = ProzessStartRequest.builder() //
				.prozessKey(REGISTRIERUNG_FREIGABE_PROZESS_KEY) //
				.businessKey(user.getId().getValue()) //
				.variables(ProzessVariableConverter.convertToVariables(user)) //
				.build();

		val restTemplate = new RestTemplate();
		ResponseEntity<String> response = restTemplate
				.postForEntity(engineProperties.getUrl() + "/restapi/prozess/start/", request, String.class);

		if (response.getStatusCode().isError()) {
			throw new RuntimeException("Beim Starten des Prozesses ist etwas fehlgeschlagen");
		}

		return new ProzessInstanzId(response.getBody());
	}
}
