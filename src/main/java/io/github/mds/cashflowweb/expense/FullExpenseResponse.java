package io.github.mds.cashflowweb.expense;

import java.math.BigDecimal;

public record FullExpenseResponse(
        long id,
        BigDecimal amount,
        String category,
        String location,
        String description
) {

}
