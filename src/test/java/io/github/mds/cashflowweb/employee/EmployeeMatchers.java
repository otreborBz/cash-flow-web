package io.github.mds.cashflowweb.employee;

import org.hamcrest.Matcher;

import static org.hamcrest.Matchers.*;

public final class EmployeeMatchers {

    private EmployeeMatchers() {}

    public static Matcher<Employee> employee(String name, String email, String cpf, String password, String phone, String department) {
        return allOf(
                hasProperty("name", is(name)),
                hasProperty("email", is(email)),
                hasProperty("cpf", is(cpf)),
                hasProperty("password", is(password)),
                hasProperty("phone", is(phone)),
                hasProperty("department", is(department))
        );
    }

    public static Matcher<Employee> employee() {
        return employee(null, null, null, null, null, null);
    }

}
