package no.kristiania.mock.exam.backend.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;

//Trip: having info like title, description, cost, location, time of the year, etc.
@Entity
public class Trip {
    @Id
    @GeneratedValue
    private Long id;

    @NotBlank
    @Size(max = 300)
    private String title;

    @NotBlank
    private String description;

    @NotNull
    private Long cost;

    @NotNull
    @Size(max = 300)
    private String locationName;

    @NotNull
    private LocalDate departureDate;

    @NotNull
    private LocalDate returnDate;

    //Information about all people whom are taking this trip
    //One user can book many trips
    //One trip can be booked by many users
    //That is why we have many to many relationship
    //Default Lazy fetch is fine, as it is not expected that we are going to need all travelers that often
    @ManyToMany(mappedBy = "bookedTrips")
    private List<Users> allTravelers;

    public Trip() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getCost() {
        return cost;
    }

    public void setCost(Long cost) {
        this.cost = cost;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public LocalDate getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(LocalDate departureDate) {
        this.departureDate = departureDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    public List<Users> getAllTravelers() {
        return allTravelers;
    }

    public void setAllTravelers(List<Users> allTravelers) {
        this.allTravelers = allTravelers;
    }
}
