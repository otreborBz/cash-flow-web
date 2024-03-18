package io.github.mds.cashflowweb.expense;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.mds.cashflowweb.employee.Employee;
import io.github.mds.cashflowweb.employee.EmployeeFactory;
import io.github.mds.cashflowweb.employee.EmployeeRepository;
import io.github.mds.cashflowweb.travel.TravelFactory;
import io.github.mds.cashflowweb.travel.TravelRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@WithUserDetails(value = "employee@email.com", setupBefore = TestExecutionEvent.TEST_EXECUTION)
public class ExpenseEndpointsTests {

    @Autowired
    private MockMvc client;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TravelRepository travelRepository;

    @Autowired
    private ExpenseRepository expenseRepository;

    private Employee employee;

    @BeforeEach
    void setup() {
        employee = EmployeeFactory.createEmployee();
        employee.setPassword(passwordEncoder.encode(employee.getPassword()));
        employeeRepository.save(employee);
    }

    @AfterEach
    void tearDown() {
        expenseRepository.deleteAll();
        travelRepository.deleteAll();
        employeeRepository.deleteAll();
    }

    @Nested
    class CreateExpenseTests {

        @Test
        void createExpense() throws Exception {
            // given
            var travelId = travelRepository.save(TravelFactory.createTravel(employee)).getId();
            var expense = ExpenseFactory.createExpenseRequest();
            // when
            var result = client.perform(post("/api/travels/{travelId}/expenses", travelId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(expense))
            );
            // then
            result.andExpectAll(
                    status().isCreated(),
                    header().string("Location", containsString("/api/travels/" + travelId + "/expenses"))
            );
            var possibleExpense = expenseRepository.findAll().getFirst();
            assertThat(possibleExpense).isNotNull()
                    .extracting("description", "category", "amount", "location", "travel.id")
                    .contains(expense.description(), expense.category(), expense.amount(), expense.location(), travelId);
        }

    }

}
