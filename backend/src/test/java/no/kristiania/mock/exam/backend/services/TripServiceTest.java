package no.kristiania.mock.exam.backend.services;

import no.kristiania.mock.exam.backend.TestApplication;
import no.kristiania.mock.exam.backend.entity.Trip;
import no.kristiania.mock.exam.backend.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = TestApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class TripServiceTest extends ServiceTestBase {
    @Autowired
    private TripService tripService;

    @Test
    public void testCreateTrip() {
        Long id = tripService.createTrip(
                "Title",
                "My description",
                100L,
                "Norway",
                LocalDate.now(),
                LocalDate.of(2020, 8, 3)
        );
        assertNotNull(id);

        Trip myTrip = tripService.getTrip(id, true);
        List<User> users = myTrip.getAllTravelers();
        assertNotNull(myTrip);
    }

    @Test
    public void testGetAllTrips() {
        Long firstTrip = tripService.createTrip(
                "Title",
                "My description",
                100L,
                "Norway",
                LocalDate.now(),
                LocalDate.of(2020, 8, 3)
        );
        Long secondTrip = tripService.createTrip(
                "Title-2",
                "My description-2",
                100L,
                "Norway",
                LocalDate.now(),
                LocalDate.of(2020, 8, 3)
        );
        assertNotNull(firstTrip);
        assertNotNull(secondTrip);

        List<Trip> allTrips = tripService.getAllTrips(false);
        assertEquals(2, allTrips.size());
    }

    @Test
    public void testDeleteTrip() {
        Long id = tripService.createTrip(
                "Title",
                "My description",
                100L,
                "Norway",
                LocalDate.now(),
                LocalDate.of(2020, 8, 3)
        );
        //Check that it is in database
        assertNotNull(id);

        tripService.deleteTrip(id);

        //Should not find trip with this id in database since we just deleted it
        assertThrows(IllegalStateException.class, () -> tripService.getTrip(id, false));
    }

    @Test
    public void testFilterTripsByLocation() {
        Long firstTrip = tripService.createTrip(
                "Title",
                "My description",
                100L,
                "Bosnia",
                LocalDate.now(),
                LocalDate.of(2020, 8, 3)
        );
        Long secondTrip = tripService.createTrip(
                "Title-2",
                "My description-2",
                100L,
                "Norway",
                LocalDate.now(),
                LocalDate.of(2020, 8, 3)
        );
        Long thirdTrip = tripService.createTrip(
                "Title-2",
                "My description-2",
                100L,
                "Norway",
                LocalDate.now(),
                LocalDate.of(2020, 8, 3)
        );
        assertNotNull(firstTrip);
        assertNotNull(secondTrip);
        assertNotNull(thirdTrip);

        List<Trip> bosnianTrips = tripService.filterTripsByLocation("Bosnia");
        List<Trip> norwegianTrips = tripService.filterTripsByLocation("Norway");

        assertEquals(1,bosnianTrips.size());
        assertEquals(2,norwegianTrips.size());
    }
    @Test
    public void filterByCost() {
        Long firstTrip = tripService.createTrip(
                "Title",
                "My description",
                200L,
                "Bosnia",
                LocalDate.now(),
                LocalDate.of(2020, 8, 3)
        );
        Long secondTrip = tripService.createTrip(
                "Title-2",
                "My description-2",
                100L,
                "Norway",
                LocalDate.now(),
                LocalDate.of(2020, 8, 3)
        );
        Long thirdTrip = tripService.createTrip(
                "Title-2",
                "My description-2",
                200L,
                "Norway",
                LocalDate.now(),
                LocalDate.of(2020, 8, 3)
        );
        assertNotNull(firstTrip);
        assertNotNull(secondTrip);
        assertNotNull(thirdTrip);

        List<Trip> cheapTrips = tripService.filterByCost(100L);
        List<Trip> expensiveTrips = tripService.filterByCost(200L );

        assertEquals(1,cheapTrips.size());
        assertEquals(2,expensiveTrips.size());
    }


}
