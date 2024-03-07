package io.github.mds.cashflowweb.employee;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.logout;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class EmployeeSecurityTests {

    @Autowired
    private MockMvc client;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setup() {
        var employee = EmployeeFactory.createEmployee();
        employee.setPassword(passwordEncoder.encode(employee.getPassword()));
        employeeRepository.save(employee);
    }

    @AfterEach
    void tearDown() {
        employeeRepository.deleteAll();
    }

    @Test
    void loginEmployee() throws Exception {
        // when
        var result = client.perform(formLogin()
                .loginProcessingUrl("/api/login")
                .userParameter("email")
                .user("employee@email.com")
                .password("password")
        );
        // then
        result.andExpectAll(
                status().isOk()
        );
    }

    @Test
    void doNotLoginEmployeeWithInvalidUsernameOrPassword() throws Exception {
        // when
        var result = client.perform(formLogin()
                .loginProcessingUrl("/api/login")
                .userParameter("email")
                .user("invalid")
                .password("invalid")
        );
        // then
        result.andExpectAll(
                status().isBadRequest()
        );
    }

    @Test
    @WithUserDetails(value = "employee@email.com", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void logoutEmployee() throws Exception {
        // when
        var result = client.perform(logout().logoutUrl("/api/logout"));
        // then
        result.andExpect(status().isNoContent());
    }

    @Test
    void showErrorToUnauthenticatedEmployee() throws Exception {
        // when
        var result = client.perform(get("/api/protected-url"));
        // then
        result.andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails(value = "employee@email.com", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void allowAuthenticatedEmployeeToAccessProtectedResource() throws Exception {
        // when
        var result = client.perform(get("/api/protected-url"));
        // then
        result.andExpectAll(status().isNotFound());
    }

    @Test
    @WithUserDetails(value = "employee@email.com", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void preventAuthenticatedEmployeeToAccessManagerPages() throws Exception {
        // when
        var result = client.perform(get("/protected-url"));
        // then
        result.andExpectAll(status().isForbidden());
    }

}