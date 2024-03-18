package io.github.mds.cashflowweb.expense;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.mds.cashflowweb.NoSecurityConfiguration;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ExpenseController.class)
@Import(NoSecurityConfiguration.class)
public class ExpenseControllerTests {

    @Autowired
    private MockMvc client;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private ExpenseService expenseService;

    @Nested
    class CreateExpenseTests {

        @Test
        void doNotCreateExpenseWithInvalidFields() throws Exception {
            // given
            var expense = ExpenseFactory.createInvalidExpenseRequest();
            // when
            var result = client.perform(post("/api/travels/{travelId}/expenses", 1)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(expense))
            );
            // then
            result.andExpect(status().isBadRequest());
        }

        // TODO: implement test case when travel doesn't exists

    }

}
