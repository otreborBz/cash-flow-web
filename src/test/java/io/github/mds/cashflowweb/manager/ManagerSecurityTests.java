package io.github.mds.cashflowweb.manager;

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
public class ManagerSecurityTests {

    @Autowired
    private MockMvc client;

    @Autowired
    private ManagerRepository managerRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @BeforeEach
    void setup() {;
        managerRepository.save(new Manager(
                "manager",
                "manager@email.com",
                "xxx.xxx.xxx-xx",
                passwordEncoder.encode("password"),
                "(xx) xxxxx-xxxx",
                "department"
        ));
    }

    @AfterEach
    void tearDown() {
        managerRepository.deleteAll();
    }

    @Test
    void retrieveManagerLoginPage() throws Exception {
        // when
        var result = client.perform(get("/login"));
        // then
        result.andExpectAll(
                status().isOk(),
                view().name("login")
        );
    }

    @Test
    void loginManager() throws Exception {
        // when
        var result = client.perform(formLogin()
                .userParameter("email")
                .user("manager@email.com")
                .password("password")
        );
        // then
        result.andExpectAll(
                status().isFound(),
                redirectedUrl("/home")
        );
    }

    @Test
    void doNotLoginManagerWithInvalidUsernameOrPassword() throws Exception {
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
    @WithUserDetails(value = "manager@email.com", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void logoutManager() throws Exception {
        // when
        var result = client.perform(logout());
        // then
        result.andExpectAll(
                status().isFound(),
                redirectedUrl("/login?logout")
        );
    }

    @Test
    void redirectUnauthenticatedManagerToLoginPage() throws Exception {
        // when
        var result = client.perform(get("/protected-url"));
        // then
        result.andExpectAll(
                status().isFound(),
                redirectedUrl("http://localhost/login")
        );
    }

    @Test
    @WithUserDetails(value = "manager@email.com", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void allowAuthenticatedManagerToAccessProtectedPage() throws Exception {
        // when
        var result = client.perform(get("/protected-url"));
        // then
        result.andExpectAll(status().isNotFound());
    }

    @Test
    @WithUserDetails(value = "manager@email.com", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void preventAuthenticatedManagerToAccessEmployeeResources() throws Exception {
        // when
        var result = client.perform(get("/api/protected-url"));
        // then
        result.andExpectAll(status().isForbidden());
    }


}
