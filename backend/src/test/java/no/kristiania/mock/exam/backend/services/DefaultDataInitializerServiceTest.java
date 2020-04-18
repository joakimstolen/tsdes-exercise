package no.kristiania.mock.exam.backend.services;

import no.kristiania.mock.exam.backend.TestApplication;
import no.kristiania.mock.exam.backend.entity.Purchase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_CLASS;

/**
 * This class is adaptation of:
 * https://github.com/arcuri82/testing_security_development_enterprise_systems/blob/master/intro/exercise-solutions/quiz-game/part-11/backend/src/main/java/org/tsdes/intro/exercises/quizgame/backend/service/DefaultDataInitializerService.java
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = TestApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@DirtiesContext(classMode = BEFORE_CLASS)
public class DefaultDataInitializerServiceTest {

    @Autowired
    private PurchaseService purchaseService;

    @Autowired
    private TripService tripService;

    @Test
    public void testInit() {

        assertTrue(purchaseService.getAllPurchases().size() > 0);

        assertTrue(tripService.getAllTrips(true).stream()
                .mapToLong(t -> t.getAllTravelers().size())
                .sum() > 0);

        assertTrue(tripService.getAllTrips(false).size() > 0);

    }
}