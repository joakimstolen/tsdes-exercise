package no.kristiania.mock.exam.backend.services;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * This class is copy of:
 * https://github.com/arcuri82/testing_security_development_enterprise_systems/blob/master/intro/exercise-solutions/quiz-game/part-11/backend/src/test/java/org/tsdes/intro/exercises/quizgame/backend/service/ServiceTestBase.java
 */
public class ServiceTestBase {

    @Autowired
    private ResetService resetService;

    @BeforeEach
    public void cleanDatabase() {
        resetService.resetDatabase();
    }
}
