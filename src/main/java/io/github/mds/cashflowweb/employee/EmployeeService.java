package io.github.mds.cashflowweb.employee;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;

    public EmployeeService(EmployeeRepository employeeRepository, PasswordEncoder passwordEncoder) {
        this.employeeRepository = employeeRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void createEmployee(Employee employee) {
        verifyEmployeeUniqueness(employee);
        employee.setPassword(passwordEncoder.encode(employee.getPassword()));
        employeeRepository.save(employee);
    }
    
    private void verifyEmployeeUniqueness(Employee employee) {
        var exception = new NonUniqueEmployeeException();
        if (employeeRepository.existsByEmail(employee.getEmail())) {
            exception.addField("email");
        }
        if (employeeRepository.existsByCpf(employee.getCpf())) {
            exception.addField("cpf");
        }
        if (employeeRepository.existsByPhone(employee.getPhone())) {
            exception.addField("phone");
        }
        if (exception.hasFields()) {
            throw exception;
        }
    }

    @Transactional(readOnly = true)
    public List<Employee> listEmployees() {
        return employeeRepository.findAll();
    }

}
