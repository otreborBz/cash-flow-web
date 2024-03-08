package io.github.mds.cashflowweb.employee;

import io.github.mds.cashflowweb.manager.ManagerFactory;
import io.github.mds.cashflowweb.manager.ManagerRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static io.github.mds.cashflowweb.employee.EmployeeMatchers.employee;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@WithUserDetails(value = "manager@email.com", setupBefore = TestExecutionEvent.TEST_EXECUTION)
public class EmployeeEndpointsTests {

    @Autowired
    private MockMvc client;

    @Autowired
    private ManagerRepository managerRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setup() {
        var manager = ManagerFactory.createManager();
        manager.setPassword(passwordEncoder.encode(manager.getPassword()));
        managerRepository.save(manager);
    }

    @AfterEach
    void tearDown() {
        employeeRepository.deleteAll();
        managerRepository.deleteAll();
    }

    @Nested
    class CreateEmployeeTests {

        @Test
        void createEmployee() throws Exception {
            // when
            var result = client.perform(post("/employee/create")
                    .param("name", "employee")
                    .param("email", "employee@email.com")
                    .param("cpf", EmployeeFactory.CPF)
                    .param("password", "password")
                    .param("phone", "(xx) xxxxx-xxxx")
                    .param("department", "department")
                    .with(csrf())
            );
            // then
            result.andExpectAll(
                    status().isFound(),
                    redirectedUrl("/employee/list")
            );
            var optionalEmployee = employeeRepository.findByEmail("employee@email.com");
            assertThat(optionalEmployee).get()
                    .matches(employee -> passwordEncoder.matches("password", employee.getPassword()))
                    .extracting("name", "email", "cpf", "phone", "department")
                    .containsExactly("employee", "employee@email.com", EmployeeFactory.CPF, "(xx) xxxxx-xxxx", "department");
        }

    }

    @Nested
    class ListEmployeeTests {

        @Test
        void listEmployees() throws Exception {
           // given
            var employees = List.of(
                    EmployeeFactory.createRandomEmployee(),
                    EmployeeFactory.createRandomEmployee(),
                    EmployeeFactory.createRandomEmployee()
            );
            employeeRepository.saveAll(employees);
            // when
            var result = client.perform(get("/employee/list"));
            // then
            result.andExpectAll(
                    status().isOk(),
                    model().attribute("employees", contains(
                            employee(employees.get(0)),
                            employee(employees.get(1)),
                            employee(employees.get(2))
                    )),
                    view().name("employee/employee-table")
            );
        }

    }

    @Nested
    class UpdateEmployee {

        @Test
        void updateEmployee() throws Exception {
            // given
            var id = employeeRepository.save(EmployeeFactory.createEmployee()).getId();
            var updatedEmployee = EmployeeFactory.createRandomEmployee();
            // when
            var result = client.perform(post("/employee/update/{id}", id)
                    .param("name", updatedEmployee.getName())
                    .param("email", updatedEmployee.getEmail())
                    .param("cpf", updatedEmployee.getCpf())
                    .param("password", updatedEmployee.getPassword())
                    .param("phone", updatedEmployee.getPhone())
                    .param("department", updatedEmployee.getDepartment())
                    .with(csrf())
            );
            // then
            result.andExpectAll(
                    status().isFound(),
                    redirectedUrl("/employee/list")
            );
            var optionalEmployee = employeeRepository.findByEmail(updatedEmployee.getEmail());
            assertThat(optionalEmployee).get()
                    .matches(employee -> passwordEncoder.matches(updatedEmployee.getPassword(), employee.getPassword()))
                    .usingRecursiveComparison()
                    .ignoringFields("id", "password")
                    .isEqualTo(updatedEmployee);
        }

    }

}
