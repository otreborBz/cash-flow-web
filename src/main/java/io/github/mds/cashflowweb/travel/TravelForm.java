package io.github.mds.cashflowweb.travel;

import io.github.mds.cashflowweb.employee.Employee;
import jakarta.validation.constraints.NotBlank;
//import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
//import org.hibernate.validator.constraints.UniqueElements;

//import java.math.BigDecimal;
import java.time.LocalDate;
//import java.util.List;

public class TravelForm {

    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate endDate;

    @NotBlank
    private String origin;

    @NotBlank
    private String destination;

    @NotBlank
    private  String description;
/*
    private BigDecimal budget;

    @UniqueElements @NotEmpty
    private List<String> itinerary;
*/
    @NotNull
    private Long employeeId;

    public TravelForm() {}

    public TravelForm(LocalDate startDate, LocalDate endDate, String origin, String destination, String description, /*BigDecimal budget, List<String> itinerary,*/ Long employeeId) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.origin = origin;
        this.destination = destination;
        this.description = description;
        //this.budget = budget;
        //this.itinerary = itinerary;
        this.employeeId = employeeId;
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
/*
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
*/
    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public Travel toEntity() {
        return new Travel(
                startDate,
                endDate,
                origin,
                destination,
                description,
                //budget,
                //itinerary,
                TravelStatus.SCHEDULED,
                new Employee(employeeId)
        );
    }

}
