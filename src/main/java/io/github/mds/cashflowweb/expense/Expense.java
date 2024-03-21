package io.github.mds.cashflowweb.expense;

import io.github.mds.cashflowweb.travel.Travel;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String description;

    private String category;

    @DecimalMin("0")
    @NotNull
    @Column(nullable = false)
    private BigDecimal amount;

    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime dateTime;

    @NotNull
    @Column(nullable = false)
    private String location;

    private byte[] fiscalNote;

    @ManyToOne(optional = false)
    private Travel travel;

    public Expense() {}

    public Expense(String description, String category, BigDecimal amount, String location, byte[] fiscalNote, Travel travel) {
        this.description = description;
        this.category = category;
        this.amount = amount;
        this.location = location;
        this.fiscalNote = fiscalNote;
        this.travel = travel;
    }

    public Long getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public byte[] getFiscalNote() {
        return fiscalNote;
    }

    public void setFiscalNote(byte[] fiscalNote) {
        this.fiscalNote = fiscalNote;
    }

    public Travel getTravel() {
        return travel;
    }

    public void setTravel(Travel travel) {
        this.travel = travel;
    }

    public ExpenseResponse toResponse() {
        return new ExpenseResponse(description, amount);
    }

}
