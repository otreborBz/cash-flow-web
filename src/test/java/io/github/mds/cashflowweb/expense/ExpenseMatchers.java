package io.github.mds.cashflowweb.expense;

import java.util.Map;

public final class ExpenseMatchers {

    private ExpenseMatchers() {}

    public static Map<String, String> expense(Expense expense) {
        return Map.of(
                "description", expense.getDescription(),
                "category", expense.getCategory(),
                "amount", expense.getAmount().toString(),
                "location", expense.getLocation()
        );
    }

}
