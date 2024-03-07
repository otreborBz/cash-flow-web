package io.github.mds.cashflowweb.employee;

import net.datafaker.Faker;

public final class EmployeeFactory {

    public static final String CPF = new Faker().cpf().valid(true);

    private EmployeeFactory() {}

    public static Employee createEmployee() {
        return new Employee(
                "employee",
                "employee@email.com",
                CPF,
                "password",
                "(xx) xxxxx-xxxx",
                "department"
        );
    }

}
