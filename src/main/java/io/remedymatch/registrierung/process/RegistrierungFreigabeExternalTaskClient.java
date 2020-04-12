package io.remedymatch.registrierung.process;

import javax.annotation.PostConstruct;

import org.camunda.bpm.client.ExternalTaskClient;
import org.camunda.bpm.client.backoff.ExponentialBackoffStrategy;
import org.camunda.bpm.client.task.ExternalTask;
import org.springframework.stereotype.Component;

import io.remedymatch.registrierung.domain.KeycloakUserId;
import io.remedymatch.registrierung.domain.RegistrierungFreigabeService;
import io.remedymatch.registrierung.properties.EngineProperties;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Component
class RegistrierungFreigabeExternalTaskClient {

	private static final String TASK_BENUTZER_FREIGEBEN = "rm_registrierung_freigabe_externalTask_benutzerFreigeben";
	private static final String TASK_BENUTZER_ABLEHNEN = "rm_registrierung_freigabe_externalTask_benutzerAblehnen";

	private final EngineProperties engineProperties;
	private RegistrierungFreigabeService freigabeService;

	@PostConstruct
	public void doSubscribe() {

		ExternalTaskClient client = ExternalTaskClient.create() //
				.baseUrl(engineProperties.getUrl() + "/rest") //
				.backoffStrategy(new ExponentialBackoffStrategy(3000, 2, 3000)) //
				.build();

		client.subscribe(TASK_BENUTZER_FREIGEBEN).lockDuration(2000).handler((externalTask, externalTaskService) -> {
			benutzerFreigeben(externalTask);
			externalTaskService.complete(externalTask);
		}).open();

		client.subscribe(TASK_BENUTZER_ABLEHNEN).lockDuration(2000).handler((externalTask, externalTaskService) -> {
			benutzerAblehnen(externalTask);
			externalTaskService.complete(externalTask);
		}).open();
	}

	private void benutzerFreigeben(final ExternalTask externalTask) {
		freigabeService.userFreigeben(getUserId(externalTask));
	}

	private void benutzerAblehnen(final ExternalTask externalTask) {
		freigabeService.userAblehnen(getUserId(externalTask), externalTask.getVariable("grund").toString());
	}

	private KeycloakUserId getUserId(final ExternalTask externalTask) {
		return new KeycloakUserId(externalTask.getBusinessKey());
	}
}
