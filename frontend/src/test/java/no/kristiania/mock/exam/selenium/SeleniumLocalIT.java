package no.kristiania.mock.exam.selenium;

import no.kristiania.mock.exam.Application;
import no.kristiania.mock.exam.backend.entity.Trip;
import no.kristiania.mock.exam.backend.services.TripService;
import no.kristiania.mock.exam.selenium.po.DetailsPO;
import no.kristiania.mock.exam.selenium.po.IndexPO;
import no.kristiania.mock.exam.selenium.po.SignUpPO;
import no.kristiania.mock.exam.selenium.po.UserPO;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class, webEnvironment = RANDOM_PORT)
public class SeleniumLocalIT {

    private static WebDriver driver;

    @LocalServerPort
    private int port;

    @Autowired
    TripService tripService;

    @BeforeAll
    public static void initClass() {

        driver = SeleniumDriverHandler.getChromeDriver();

        assumeTrue(driver != null, "Cannot find/initialize Chrome driver");
    }

    @AfterAll
    public static void tearDown() {
        if (driver != null) {
            driver.close();
        }
    }

    protected WebDriver getDriver() {
        return driver;
    }

    protected String getServerHost() {
        return "localhost";
    }

    protected int getServerPort() {
        return port;
    }

    private static final AtomicInteger counter = new AtomicInteger(0);

    private String getUniqueId() {
        return "foo_SeleniumLocalIT_" + counter.getAndIncrement();
    }


    private IndexPO home;


    private IndexPO createNewUser(String username, String password) {

        home.toStartingPage();

        SignUpPO signUpPO = home.toSignUp();

        IndexPO indexPO = signUpPO.signUP(username, password);
        assertNotNull(indexPO);

        return indexPO;
    }

    @BeforeEach
    public void initTest() {

        /*
            we want to have a new session, otherwise the tests
            will share the same Session beans
         */
        getDriver().manage().deleteAllCookies();

        home = new IndexPO(getDriver(), getServerHost(), getServerPort());

        home.toStartingPage();

        assertTrue(home.isOnPage(), "Failed to start from Home Page");
    }

    @Test
    public void testDefaultTrips() {
        //As we are testing default data we know it has to be 5 trips displayed
        assertEquals(5, home.getNumberOfTripsDisplayed());
    }

    @Test
    public void testCreateAndLogoutUser() {
        assertFalse(home.isLoggedIn());

        String userID = getUniqueId();
        String password = "123456";

        home = createNewUser(userID, password);

        assertTrue(home.isLoggedIn());
        assumeTrue(home.getDriver().getPageSource().contains(userID));

        home.doLogout();

        assertFalse(home.isLoggedIn());
        assertFalse(home.getDriver().getPageSource().contains(userID));
    }

    @Test
    public void testDisplayTripDetails() {
        //Get first row of table
        List<String> rowElements = getDriver().findElements(By.xpath("//*[@id=\"tripTable\"]/tbody/tr[1]/td"))
                .stream().map(t -> t.getText()).collect(Collectors.toList());

        String buttonToClick = getDriver().findElement(By.xpath("/html/body/table/tbody/tr[1]/td[5]/form/input[2]")).getAttribute("id");
        DetailsPO detailsPO = home.getDetails(buttonToClick);
        //Check that details page has location name same as name i 4th column of index page in corresponding row
        assertTrue(detailsPO.getDriver().getPageSource().contains(rowElements.get(3)));
        assertTrue(detailsPO.getDriver().getPageSource().contains("Price: " + rowElements.get(2).toString()));
    }

    @Test
    public void testBookATrip() {
        //Lets first go to description page without logging in
        //We are just choosing any trip at random
        String buttonId = home.getRandomButton();
        DetailsPO detailsPO = home.getDetails(buttonId);
        assertTrue(detailsPO.isOnPage());
        //Button to make purchase should not be displayed if there is no user logged in
        assertNull(detailsPO.makePurchase(""));

        String userID = getUniqueId();
        home = createNewUser(userID, "123123");
        //Now we are at home page and have to go again to details page
        detailsPO = home.getDetails(buttonId);
        detailsPO.makePurchase(userID);
        assertTrue(detailsPO.isOnPage());
        //Our id should be in table of users that purchased trip
        assertTrue(detailsPO.isInFirstColumn(userID));
    }

    @Test
    public void testDisplayUSerInfo() {
        //Try to access user info page
        //As we are not logged in it should fail
        UserPO userPO = home.getUserInfo();
        assertNull(userPO);
        String userID = getUniqueId();
        home = createNewUser(userID, "123123");
        userPO = home.getUserInfo();
        assertNotNull(userPO);
        assertTrue(userPO.getUserName().contains(userID));
        userPO.doLogout();
        userPO = home.getUserInfo();
        assertNull(userPO);
    }

    @Test
    public void testSearch() {
        List<Trip> allTrips = tripService.getAllTrips(false);
        Trip firstTrip = allTrips.get(0);
        home = home.searchOnPage("byCost", firstTrip.getCost().toString());
        assertTrue(home.isInFirstColumn(firstTrip.getId().toString()));
        home.toStartingPage();
        home = home.searchOnPage("byLocation", firstTrip.getLocationName());
        assertTrue(home.isInFirstColumn(firstTrip.getId().toString()));
    }
}
