package io.github.mds.cashflowweb;

import io.github.mds.cashflowweb.employee.Employee;
import io.github.mds.cashflowweb.employee.EmployeeRepository;
import io.github.mds.cashflowweb.manager.Manager;
import io.github.mds.cashflowweb.manager.ManagerRepository;
import io.github.mds.cashflowweb.travel.Travel;
import io.github.mds.cashflowweb.travel.TravelRepository;
import io.github.mds.cashflowweb.travel.TravelStatus;
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
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        var employee = employeeRepository.save(new Employee(
                "employee",
                "employee@email.com",
                "508.500.588-05",
                passwordEncoder.encode("password"),
                "(xx) xxxxx-xxxx",
                "department"
        ));
        managerRepository.save(new Manager(
                "manager",
                "manager@email.com",
                "360.094.308-10",
                passwordEncoder.encode("password"),
                "(yy) yyyyy-yyyy",
                "department"
        ));
        travelRepository.saveAll(List.of(
                new Travel(LocalDate.now(), LocalDate.now(), "origin", "destination", "description", BigDecimal.ZERO, List.of("city"), TravelStatus.SCHEDULED, employee),
                new Travel(LocalDate.now(), LocalDate.now(), "origin", "destination", "description", BigDecimal.ZERO, List.of("city"), TravelStatus.SCHEDULED, employee),
                new Travel(LocalDate.now(), LocalDate.now(), "origin", "destination", "description", BigDecimal.ZERO, List.of("city"), TravelStatus.SCHEDULED, employee),
                new Travel(LocalDate.now(), LocalDate.now(), "origin", "destination", "description", BigDecimal.ZERO, List.of("city"), TravelStatus.SCHEDULED, employee),
                new Travel(LocalDate.now(), LocalDate.now(), "origin", "destination", "description", BigDecimal.ZERO, List.of("city"), TravelStatus.SCHEDULED, employee)
        ));
    }

}
