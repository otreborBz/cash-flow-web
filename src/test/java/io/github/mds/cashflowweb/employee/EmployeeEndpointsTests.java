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

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

}
