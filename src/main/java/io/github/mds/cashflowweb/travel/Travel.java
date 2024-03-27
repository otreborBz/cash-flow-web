package io.github.mds.cashflowweb.travel;

import io.github.mds.cashflowweb.employee.Employee;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.UniqueElements;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
public class Travel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(nullable = false)
    private LocalDate startDate;

    @NotNull
    @Column(nullable = false)
    private LocalDate endDate;

    @NotBlank
    @Column(nullable = false)
    private String origin;

    @NotBlank
    @Column(nullable = false)
    private String destination;

    @NotBlank
    @Column(nullable = false)
    private String description;

    private BigDecimal budget;

    @UniqueElements
    @NotEmpty
    @ElementCollection(fetch = FetchType.EAGER)
    @Column(nullable = false)
    private List<String> itinerary;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TravelStatus status;

    @NotNull
    @ManyToOne(optional = false)
    private Employee employee;

    public Travel() {}

    public Travel(LocalDate startDate, LocalDate endDate, String origin, String destination, String description, BigDecimal budget, List<String> itinerary, TravelStatus status, Employee employee) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.origin = origin;
        this.destination = destination;
        this.description = description;
        this.budget = budget;
        this.itinerary = itinerary;
        this.status = status;
        this.employee = employee;
    }

    public Long getId() {
        return id;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getBudget() {
        return budget;
    }

    public void setBudget(BigDecimal budget) {
        this.budget = budget;
    }

    public List<String> getItinerary() {
        return itinerary;
    }

    public void setItinerary(List<String> itinerary) {
        this.itinerary = itinerary;
    }

    public TravelStatus getStatus() {
        return status;
    }

    public void setStatus(TravelStatus status) {
        this.status = status;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Employee getEmployee() {
        return employee;
    }

    public TravelResponse toResponse() {
        return new TravelResponse(id, startDate, destination, description);
    }

    public FullTravelResponse toFullResponse() {
        return new FullTravelResponse(
                id,
                startDate,
                endDate,
                origin,
                destination,
                description
        );
    }

}
