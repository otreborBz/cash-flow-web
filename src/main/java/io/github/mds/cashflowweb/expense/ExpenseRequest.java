package io.github.mds.cashflowweb.expense;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record ExpenseRequest(
        @NotBlank String description,
        String category,
        @DecimalMin("0") @NotNull BigDecimal amount,
        @NotBlank String location
) {

    public Expense toEntity() {
        return new Expense(description, category, amount, location, null, null);
    }

}
