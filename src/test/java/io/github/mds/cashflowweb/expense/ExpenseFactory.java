package io.github.mds.cashflowweb.expense;

import io.github.mds.cashflowweb.travel.Travel;
import net.datafaker.Faker;

import java.math.BigDecimal;

public final class ExpenseFactory {

    private static final Faker faker = new Faker();

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

    public static Expense createRandomExpense(Travel travel) {
        return new Expense(
                faker.text().text(),
                faker.text().text(3, 14),
                new BigDecimal("1.00"),
                faker.country().capital(),
                null,
                travel
        );
    }

}
