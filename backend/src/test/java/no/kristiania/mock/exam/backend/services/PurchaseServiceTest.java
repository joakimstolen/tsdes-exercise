package no.kristiania.mock.exam.backend.services;

import no.kristiania.mock.exam.backend.TestApplication;
import no.kristiania.mock.exam.backend.entity.Purchase;
import no.kristiania.mock.exam.backend.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = TestApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class PurchaseServiceTest extends ServiceTestBase{
    @Autowired
    private TripService tripService;

    @Autowired
    private PurchaseService purchaseService;

    @Autowired
    private UserService userService;

    @Test
    public void testCreatePurchase() {
        userService.createUser("Armin","Armin", "Guber" ,"123","a@gmail.com", "user");
        Long tripID = tripService.createTrip("Test", "My desc", 200L, "Bosnia", LocalDate.now(), LocalDate.of(2020, 9, 2));
        Long purchaseID = purchaseService.newPurchase(tripID,"Armin");
        assertNotNull(purchaseID);
    }
    @Test
    public void testFilterPurchasesByUser() {
        String userName = "Armin";
        //For simplicity username and name are same
        userService.createUser(userName, userName, "Guber" ,"123","a@gmail.com", "user");
        Long firstTrip = tripService.createTrip("Test", "My desc", 200L, "Bosnia", LocalDate.now(), LocalDate.of(2020, 9, 2));
        Long secondTrip = tripService.createTrip("Test-2", "My desc-2", 300L, "Norway", LocalDate.now(), LocalDate.of(2020, 9, 21));
        Long firstPurchase = purchaseService.newPurchase(firstTrip, userName);
        Long secondPurchase = purchaseService.newPurchase(secondTrip, userName);
        User user = userService.findUserByUserName(userName);
        assertNotNull(firstPurchase);
        assertNotNull(secondPurchase);


        List<Purchase> userPurchases = purchaseService.filterPurchasesByUser(user.getUserID());

        assertEquals(2, userPurchases.size());
    }

    @Test
    public void testFilterPurchasesByTrip() {
        String firstUser = "Mirha";
        String secondUser = "Emil";
        //For simplicity username and name are same
        userService.createUser(firstUser, firstUser, "Guber" ,"123","a@gmail.com", "user");
        userService.createUser(secondUser, secondUser, "Guber" ,"123","at@gmail.com", "user");
        Long firstTrip = tripService.createTrip("Test", "My desc", 200L, "Bosnia", LocalDate.now(), LocalDate.of(2020, 9, 2));
        Long secondTrip = tripService.createTrip("Test-2", "My desc-2", 300L, "Norway", LocalDate.now(), LocalDate.of(2020, 9, 21));
        Long firstPurchase = purchaseService.newPurchase(firstTrip, firstUser);
        Long secondPurchase = purchaseService.newPurchase(secondTrip, firstUser);
        Long thirdPurchase = purchaseService.newPurchase(secondTrip, secondUser);
        assertNotNull(firstPurchase);
        assertNotNull(secondPurchase);
        assertNotNull(thirdPurchase);


        List<Purchase> firstTripFilter = purchaseService.filterPurchasesByTrip(firstTrip);
        List<Purchase> secondTripFilter = purchaseService.filterPurchasesByTrip(secondTrip);

        assertEquals(1, firstTripFilter.size());
        assertEquals(2, secondTripFilter.size());
    }
}
