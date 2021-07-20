package com.fedorova.anna.openspasex;

import com.fedorova.anna.openspasex.model.Launch;
import com.fedorova.anna.openspasex.model.Rocket;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class InsightsControllerTest {
    @LocalServerPort
    private int port;
    TestRestTemplate restTemplate = new TestRestTemplate();

    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }

    @Test
    public void nextLaunchTest() {
        ResponseEntity<Launch> response = restTemplate.getForEntity(createURLWithPort("/next_launch"), Launch.class);
        assertEquals(response.getStatusCode(), HttpStatus.OK);

        Launch launch = response.getBody();
        assertNotNull(launch);
        assertNotNull(launch.getId());
        assertNotNull(launch.getName());
        assertNotNull(launch.getDate());
        assertNotNull(launch.getLogo());
    }

    private <T> T getQuantity(String url, Class<T> responseType, Object... variables) {
        ResponseEntity<T> response = restTemplate.getForEntity(url, responseType, variables);
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        T quantity = response.getBody();
        assertNotNull(quantity);
        return quantity;
    }

    private <T extends Comparable<T>> void quantityTest(String url, boolean withYear, Class<T> responseType, T baseline) {
        T count = getQuantity(url + (withYear ? "?year=" + LocalDate.now().getYear() : ""), responseType);
        assertTrue(count.compareTo(baseline) >= 0);
    }

    private <T extends Comparable<T>> void quantityPastYearTest(String url, Class<T> responseType, T result) {
        T count = getQuantity(url + "?year=2020", responseType);
        assertEquals(count.compareTo(result), 0);
    }

    @Test
    public void launchCountTest() {
        quantityTest(createURLWithPort("/launches_count"), false, Integer.class, 123);
    }

    @Test
    public void launchCountPastYearTest() {
        quantityPastYearTest(createURLWithPort("/launches_count"), Integer.class, 26);
    }

    @Test
    public void launchCountThisYearTest() {
        quantityTest(createURLWithPort("/launches_count"), true, Integer.class, 0);
    }

    @Test
    public void crewCountTest() {
        quantityTest(createURLWithPort("/crew"), false, Integer.class, 10);
    }

    @Test
    public void crewCountPastYearTest() {
        quantityPastYearTest(createURLWithPort("/crew"), Integer.class, 6);
    }

    @Test
    public void crewCountThisYearTest() {
        quantityTest(createURLWithPort("/crew"), true, Integer.class, 0);
    }

    @Test
    public void costTest() {
        quantityTest(createURLWithPort("/cost"), false, Long.class, 6503500000L);
    }

    @Test
    public void costPastYearTest() {
        quantityPastYearTest(createURLWithPort("/cost"), Long.class, 1300000000L);
    }

    @Test
    public void costThisYearTest() {
        quantityTest(createURLWithPort("/cost"), true, Long.class, 0L);
    }

    @Test
    public void massTest() {
        quantityTest(createURLWithPort("/mass"), false, Long.class, 73372611L);
    }

    @Test
    public void massPastYearTest() {
        quantityPastYearTest(createURLWithPort("/mass"), Long.class, 14538341L);
    }

    @Test
    public void massThisYearTest() {
        quantityTest(createURLWithPort("/mass"), true, Long.class, 0L);
    }

    @Test
    public void successRateTest() {
        quantityTest(createURLWithPort("/success_rate"), false, Double.class, 0.9621212121212122);
    }

    @Test
    public void successRatePastYearTest() {
        quantityPastYearTest(createURLWithPort("/success_rate"), Double.class, 1.);
    }

    @Test
    public void successRateThisYearTest() {
        quantityTest(createURLWithPort("/success_rate"), true, Double.class, 0.);
    }

    @Test
    public void rocketsTest() {
        ResponseEntity<Rocket[]> response = restTemplate.getForEntity(
                createURLWithPort("/rockets"),
                Rocket[].class);
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        Rocket[] rockets = response.getBody();
        assertNotNull(rockets);
        assertTrue(rockets.length >= 4);
        for (Rocket rocket : rockets) {
            assertNotNull(rocket.getId());
            assertNotNull(rocket.getName());
            assertNotNull(rocket.getImage());
            assertNotNull(rocket.getDescription());
        }
    }
}