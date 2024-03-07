package io.github.mds.cashflowweb.manager;

import net.datafaker.Faker;

public final class ManagerFactory {

    public static final String CPF = new Faker().cpf().valid(true);

    private ManagerFactory() {}

    public static Manager createManager() {
        return new Manager(
                "manager",
                "manager@email.com",
                CPF,
                "password",
                "(yy) yyyyy-yyyy",
                "department"
        );
    }

}
