package io.github.mds.cashflowweb.expense;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.mds.cashflowweb.employee.Employee;
import io.github.mds.cashflowweb.employee.EmployeeFactory;
import io.github.mds.cashflowweb.employee.EmployeeRepository;
import io.github.mds.cashflowweb.travel.Travel;
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

import java.util.List;

import static io.github.mds.cashflowweb.expense.ExpenseMatchers.expense;
import static io.github.mds.cashflowweb.travel.TravelMatchers.travel;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

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

    private Travel travel;

    @BeforeEach
    void setup() {
        var employee = EmployeeFactory.createEmployee();
        employee.setPassword(passwordEncoder.encode(employee.getPassword()));
        employeeRepository.save(employee);
        travel = travelRepository.save(TravelFactory.createTravel(employee));
    }

    @AfterEach
    void tearDown() {
        expenseRepository.deleteAll();
        travelRepository.deleteAll();
        employeeRepository.deleteAll();
    }

    @Nested
    class CreateExpenseTests {

        // TODO: fix this test
        @Test
        void createExpense() throws Exception {
            // given
            var expense = ExpenseFactory.createExpenseRequest();
            // when
            var result = client.perform(post("/api/travels/{travelId}/expenses", travel.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(expense))
            );
            // then
            result.andExpectAll(
                    status().isCreated(),
                    header().string("Location", containsString("/api/travels/" + travel.getId() + "/expenses"))
            );
            var possibleExpense = expenseRepository.findAll().getFirst();
            assertThat(possibleExpense).isNotNull()
                    .extracting("description", "category", "amount", "location", "travel.id")
                    .contains(expense.description(), expense.category(), expense.amount(), expense.location(), travel.getId());
        }

    }

    // TODO: fix this test
    @Nested
    class ListExpensesTests {

        @Test
        void listExpenses() throws Exception {
            // given
            var expenses = List.of(
                    ExpenseFactory.createRandomExpense(travel),
                    ExpenseFactory.createRandomExpense(travel),
                    ExpenseFactory.createRandomExpense(travel)
            );
            expenseRepository.saveAll(expenses);
            // when
            var result = client.perform(get("/api/travels/{travelId}/expenses", travel.getId()));
            // then
            result.andExpectAll(
                    status().isOk(),
                    content().contentType(MediaType.APPLICATION_JSON),
                    jsonPath("$", contains(
                            expense(expenses.get(0)),
                            expense(expenses.get(1)),
                            expense(expenses.get(2))
                    ))
            );
        }

    }

    // TODO: implement tests for find expense

    // TODO: implement tests for delete expense

}
