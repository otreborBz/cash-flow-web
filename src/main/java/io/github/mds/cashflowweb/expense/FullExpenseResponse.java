package io.github.mds.cashflowweb.expense;

import java.math.BigDecimal;

public record FullExpenseResponse(
        BigDecimal amount,
        String category,
        String location,
        String description
) {

}
