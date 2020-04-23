package no.kristiania.mock.exam.selenium.po;

import no.kristiania.mock.exam.selenium.PageObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class DetailsPO extends LayoutPO {
    public DetailsPO(WebDriver driver, String host, int port) {
        super(driver, host, port);
    }

    public DetailsPO(PageObject other) {
        super(other);
    }

    @Override
    public boolean isOnPage() {
        return getDriver().getTitle().contains("Trip details");
    }

    public DetailsPO makePurchase(String userID){
        //If we are not logged in we can't book a trip
        if(getDriver().findElements(By.id("bookingBtn")).size() == 0)
            return null;
        clickAndWait("bookingBtn");
        DetailsPO detailsPO = new DetailsPO(this);
        //After clicking booking button our table should have id of user in it
        //User id is found first column

        assertTrue(isInFirstColumn(userID));
        return detailsPO;
    }
}
