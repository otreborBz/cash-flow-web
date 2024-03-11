package io.github.mds.cashflowweb.travel;

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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TravelController.class)
@Import(NoSecurityConfiguration.class)
public class TravelControllerTests {

    @Autowired
    private MockMvc client;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private TravelService travelService;

    @Nested
    class CreateEmployeeTests {

        @Test
        void doNotCreateTravelWithInvalidFields() throws Exception {
            // given
            var travel = TravelFactory.createInvalidTravelRequest();
            // when
            var result = client.perform(post("/api/travels")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(travel))
            );
            // then
            result.andExpect(status().isBadRequest());
        }

    }

}
