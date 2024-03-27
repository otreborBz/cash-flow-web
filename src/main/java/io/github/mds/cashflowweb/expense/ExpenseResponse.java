package io.github.mds.cashflowweb.expense;

import java.math.BigDecimal;

public record ExpenseResponse(
        long id,
        String description,
        BigDecimal amount
) {

}