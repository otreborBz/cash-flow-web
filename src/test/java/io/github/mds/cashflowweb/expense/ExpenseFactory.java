package io.github.mds.cashflowweb.expense;

import java.math.BigDecimal;

public final class ExpenseFactory {

    private ExpenseFactory() {}

    public static ExpenseRequest createExpenseRequest() {
        return new ExpenseRequest(
                "description",
                "category",
                new BigDecimal("1.00"),
                "location"
        );
    }

    public static ExpenseRequest createInvalidExpenseRequest() {
        return new ExpenseRequest(null, null, null, null);
    }

}
