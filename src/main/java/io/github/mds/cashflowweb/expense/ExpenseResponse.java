package io.github.mds.cashflowweb.expense;

import java.math.BigDecimal;

public record ExpenseResponse(
        String description,
        BigDecimal amount
) {

}