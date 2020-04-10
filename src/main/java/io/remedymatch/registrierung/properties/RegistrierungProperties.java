package io.remedymatch.registrierung.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * Diese Klasse enthält alle Eigenschaften für das Ausstellen und Lesen von JSON Web Tokens.
 */
@Getter
@Setter
@Validated
@ConfigurationProperties(prefix = "io.remedymatch.registrierung")
public class RegistrierungProperties {
    /**
     * Enthält das Passwort für die Datasource.
     */
    @NotNull
    @NotBlank
    private String engineUrl;
}
