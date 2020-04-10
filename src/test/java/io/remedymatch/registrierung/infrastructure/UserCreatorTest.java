package io.remedymatch.registrierung.infrastructure;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.test.context.ActiveProfiles;

import lombok.val;
import lombok.extern.slf4j.Slf4j;

// Nur fuer lokale Tests

@ActiveProfiles(profiles = "test")
@Slf4j
public class UserCreatorTest {
	private static final String SERVER_URL = "http://localhost:8090/auth";
	private static final String REALM = "master";
	private static final String USERNAME = "admin";
	private static final String PASSWORD = "Pa55w0rd";
	private static final String CLIENT_ID = "admin-cli";
	private static final String CLIENT_SECRET = "admin";

	private static Keycloak keycloak;

	@BeforeAll
	public static void prepare() {
		keycloak = KeycloakBuilder.builder() //
				.serverUrl(SERVER_URL) //
				.realm(REALM) //
				.username(USERNAME) //
				.password(PASSWORD) //
				.clientId(CLIENT_ID) //
				.clientSecret(CLIENT_SECRET) //
				.resteasyClient(new ResteasyClientBuilder().connectionPoolSize(10).build()) //
				.build();
	}

	@Test
	void readAllUsers() {
		log.info("Alle Users: ");

		keycloak.realm(REALM).users().list().stream()//
				.forEach(user -> log.info("Alle User: " + KeycloakUserConverter.convert(user)));

		readAlleVerifiziertePersonen();
		readAllePersonenInFreigabe();
		readAlleFreigegebenePersonen();
	}

	@Test
	@Disabled
	void readAlleVerifiziertePersonen() {
		log.info("Verifizierte Users: ");

		keycloak.realm(REALM).users().list().stream()//
				.filter(user -> hasStatus(user, "VERIFIZIERT"))//
				.forEach(user -> log.info("Verifizierte User: " + KeycloakUserConverter.convert(user)));
	}

	@Test
	@Disabled
	void readAllePersonenInFreigabe() {
		log.info("Users in Freigabe: ");

		keycloak.realm(REALM).users().list().stream()//
				.filter(user -> hasStatus(user, "IN_FREIGABE"))//
				.forEach(user -> log.info("InFreigabe User: " + KeycloakUserConverter.convert(user)));
	}

	@Test
	@Disabled
	void readAlleFreigegebenePersonen() {
		log.info("Freigegeben Users: ");

		keycloak.realm(REALM).users().list().stream()//
				.filter(user -> hasStatus(user, "FREIGEGEBEN"))//
				.forEach(user -> log.info("Freigegebene User: " + KeycloakUserConverter.convert(user)));
	}

	@Test
	void testUserAnlegen() {
		
		val randomInt = random(10000, 99999);
		
		UserRepresentation user = new UserRepresentation();
		user.setUsername("testUser_" + randomInt);
		user.setFirstName("Thorsten");
		user.setLastName("Testeer");
		user.setEmail( "testUser_" + randomInt + "@testerhost.local");

		Map<String, List<String>> attribute = new HashMap<>();
		attribute.put("zipcode", Arrays.asList("Test ZipCode"));
		attribute.put("country", Arrays.asList("Test Country"));
		attribute.put("city", Arrays.asList("Test City"));
		attribute.put("phone", Arrays.asList("Test Phone"));
		attribute.put("housenumber", Arrays.asList("Test Hausnummer"));
		attribute.put("compant-type", Arrays.asList("Test Hausnummer"));
		attribute.put("street", Arrays.asList("Test Hausnummer"));
		attribute.put("status", Arrays.asList("VERIFIZIERT"));

		user.setAttributes(attribute);

		keycloak.realm(REALM).users().create(user);
		
		readAlleVerifiziertePersonen();
		
		readAllePersonenInFreigabe();
		
		readAlleFreigegebenePersonen();
	}

	private int random(final int min, final int max) {
		return new Random().nextInt((max - min) + 1) + min;
	}
	
	/*
	 * .zipcode(getAttributWertAlsText(attribute, "zipcode")) //
	 * .country(getAttributWertAlsText(attribute, "country")) //
	 * .city(getAttributWertAlsText(attribute, "city")) //
	 * .phone(getAttributWertAlsText(attribute, "phone")) //
	 * .housenumber(getAttributWertAlsText(attribute, "housenumber")) //
	 * .companyType(getAttributWertAlsText(attribute, "compant-type")) //
	 * .street(getAttributWertAlsText(attribute, "street")) //
	 * .status(getAttributWertAlsText(attribute, "status")) //
	 */

	private boolean hasStatus(//
			final UserRepresentation user, //
			final String status) {
		val attribute = user.getAttributes();
		if (attribute == null) {
			return false;
		}

		val statusWerte = attribute.get("status");
		if (statusWerte == null) {
			return false;
		}

		return statusWerte.contains(status);
	}
}
