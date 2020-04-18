package no.kristiania.mock.exam.backend.entity;

//Purchase: having info like which user booked which trip, and when.

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
public class Purchase {
    @Id
    @GeneratedValue
    private Long id;

    @OneToOne//One purchase is connected to one user
    @NotNull
    private User bookedBy;

    @NotNull
    private LocalDate dateOfBooking;

    //To know which trips this Purchase is referring to we have to have information about it
    //Although user has information about is own trips it could be that user booked multiple flights on same day
    //As for that we have to distinguish between those having special field trip
    //One trip can have many purchases/bookings and one booking is related to only one trip
    @ManyToOne
    @NotNull
    private Trip tripInformation;

    public Purchase() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getBookedBy() {
        return bookedBy;
    }

    public void setBookedBy(User bookedBy) {
        this.bookedBy = bookedBy;
    }

    public LocalDate getDateOfBooking() {
        return dateOfBooking;
    }

    public void setDateOfBooking(LocalDate dateOfBooking) {
        this.dateOfBooking = dateOfBooking;
    }

    public Trip getTripInformation() {
        return tripInformation;
    }

    public void setTripInformation(Trip tripInformation) {
        this.tripInformation = tripInformation;
    }
}
