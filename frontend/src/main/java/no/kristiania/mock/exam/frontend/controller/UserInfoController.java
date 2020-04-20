package no.kristiania.mock.exam.frontend.controller;

import no.kristiania.mock.exam.backend.entity.Purchase;
import no.kristiania.mock.exam.backend.services.PurchaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import java.util.List;

/**
 * This class is adaptation of:
 * https://github.com/arcuri82/testing_security_development_enterprise_systems/blob/master/intro/exercise-solutions/quiz-game/part-11/frontend/src/main/java/org/tsdes/intro/exercises/quizgame/frontend/controller/UserInfoController.java
 */
@Named
@RequestScoped
public class UserInfoController {

    @Autowired
    private PurchaseService purchaseService;

    public String getUserName() {
        return ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
    }

    public List<Purchase> listPurchases() {
        return purchaseService.filterPurchasesByUser(getUserName());
    }
}
