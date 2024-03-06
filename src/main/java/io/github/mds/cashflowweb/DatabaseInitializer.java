package io.github.mds.cashflowweb;

import io.github.mds.cashflowweb.employee.Employee;
import io.github.mds.cashflowweb.employee.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@Profile("local")
public class DatabaseInitializer implements CommandLineRunner {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        employeeRepository.save(new Employee(
                "employee",
                "employee@email.com",
                "xxx.xxx.xxx-xx",
                passwordEncoder.encode("password"),
                "(xx) xxxxx-xxxx",
                "department"
        ));
    }

}
