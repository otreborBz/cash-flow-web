package io.github.mds.cashflowweb.employee;

import net.datafaker.Faker;

public final class EmployeeFactory {

    private static final Faker faker = new Faker();
    public static final String CPF = faker.cpf().valid(true);

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

    public static Employee createRandomEmployee() {
        return new Employee(
                faker.name().fullName(),
                faker.internet().emailAddress(),
                faker.cpf().valid(true),
                faker.internet().password(),
                faker.phoneNumber().cellPhone(),
                faker.company().profession()
        );
    }

}
