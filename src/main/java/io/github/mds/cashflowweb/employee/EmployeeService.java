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

    @Transactional(readOnly = true)
    public Employee findEmployee(long id) {
        return employeeRepository.findById(id)
                .orElseThrow(EmployeeNotFoundException::new);
    }

    @Transactional
    public void updateEmployee(long id, Employee updatedEmployee) {
        var employee = employeeRepository.findById(id)
                .orElseThrow(EmployeeNotFoundException::new);
        verifyEmployeeUniqueness(employee, updatedEmployee);
        employee.setName(updatedEmployee.getName());
        employee.setEmail(updatedEmployee.getEmail());
        employee.setCpf(updatedEmployee.getCpf());
        employee.setPassword(passwordEncoder.encode(updatedEmployee.getPassword()));
        employee.setPhone(updatedEmployee.getPhone());
        employee.setDepartment(updatedEmployee.getDepartment());
        employeeRepository.save(employee);
    }

    private void verifyEmployeeUniqueness(Employee employee, Employee updatedEmployee) {
        var exception = new NonUniqueEmployeeException();
        if (!employee.getEmail().equals(updatedEmployee.getEmail())
                && employeeRepository.existsByEmail(updatedEmployee.getEmail())) {
            exception.addField("email");
        }
        if (!employee.getCpf().equals(updatedEmployee.getCpf())
                && employeeRepository.existsByCpf(updatedEmployee.getCpf())) {
            exception.addField("cpf");
        }
        if (!employee.getPhone().equals(updatedEmployee.getPhone())
                && employeeRepository.existsByPhone(updatedEmployee.getPhone())) {
            exception.addField("phone");
        }
        if (exception.hasFields()) {
            throw exception;
        }
    }

}
