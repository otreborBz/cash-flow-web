package io.github.mds.cashflowweb;

import io.github.mds.cashflowweb.employee.Employee;
import io.github.mds.cashflowweb.employee.EmployeeRepository;
import io.github.mds.cashflowweb.expense.Expense;
import io.github.mds.cashflowweb.expense.ExpenseRepository;
import io.github.mds.cashflowweb.manager.Manager;
import io.github.mds.cashflowweb.manager.ManagerRepository;
import io.github.mds.cashflowweb.travel.Travel;
import io.github.mds.cashflowweb.travel.TravelRepository;
import io.github.mds.cashflowweb.travel.TravelStatus;
import org.apache.tomcat.util.buf.HexUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Component
@Profile("local")
public class DatabaseInitializer implements CommandLineRunner {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ManagerRepository managerRepository;

    @Autowired
    private TravelRepository travelRepository;

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        var employee = employeeRepository.save(new Employee(
                "employee",
                "employee@email.com",
                "071.868.130-46", // replace with a valid cpf
                passwordEncoder.encode("password"),
                "(xx) xxxxx-xxxx",
                "department"
        ));
        managerRepository.save(new Manager(
                "manager",
                "manager@email.com",
                "737.346.670-20", // replace with a valid cpf
                passwordEncoder.encode("password"),
                "(yy) yyyyy-yyyy",
                "department"
        ));
        var travels = travelRepository.saveAll(List.of(
                new Travel(LocalDate.now(), LocalDate.now(), "origin", "destination", "description", /*BigDecimal.ZERO, List.of("city"),*/ TravelStatus.SCHEDULED, employee),
                new Travel(LocalDate.now(), LocalDate.now(), "origin", "destination", "description", /*BigDecimal.ZERO, List.of("city"),*/ TravelStatus.SCHEDULED, employee),
                new Travel(LocalDate.now(), LocalDate.now(), "origin", "destination", "description", /*BigDecimal.ZERO, List.of("city"),*/ TravelStatus.SCHEDULED, employee),
                new Travel(LocalDate.now(), LocalDate.now(), "origin", "destination", "description", /*BigDecimal.ZERO, List.of("city"),*/ TravelStatus.SCHEDULED, employee),
                new Travel(LocalDate.now(), LocalDate.now(), "origin", "destination", "description", /*BigDecimal.ZERO, List.of("city"),*/ TravelStatus.SCHEDULED, employee)
        ));
        expenseRepository.saveAll(List.of(
                new Expense("description", "category", BigDecimal.ONE, "location", getImage(), travels.get(0)),
                new Expense("description", "category", BigDecimal.ONE, "location", getImage(), travels.get(0)),
                new Expense("description", "category", BigDecimal.ONE, "location", getImage(), travels.get(0)),
                new Expense("description", "category", BigDecimal.ONE, "location", getImage(), travels.get(0)),
                new Expense("description", "category", BigDecimal.ONE, "location", getImage(), travels.get(0))
        ));
    }

    private static byte[] getImage() {
        return new byte[] { 0x01, 0x02, 0x03 };
    }

}
