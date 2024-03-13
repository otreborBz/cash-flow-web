package io.github.mds.cashflowweb.travel;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.mds.cashflowweb.employee.Employee;
import io.github.mds.cashflowweb.employee.EmployeeFactory;
import io.github.mds.cashflowweb.employee.EmployeeRepository;

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

import static io.github.mds.cashflowweb.travel.TravelMatchers.travel;
import static org.assertj.core.api.Assertions.*;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@WithUserDetails(value = "employee@email.com", setupBefore = TestExecutionEvent.TEST_EXECUTION)
public class TravelEndpointsTests {

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

    private Employee employee;

    @BeforeEach
    void setup() {
        employee = EmployeeFactory.createEmployee();
        employee.setPassword(passwordEncoder.encode(employee.getPassword()));
        employeeRepository.save(employee);
    }

    @AfterEach
    void tearDown() {
        travelRepository.deleteAll();
        employeeRepository.deleteAll();
    }

    @Nested
    class CreateTravelTests {

        @Test
        void createTravel() throws Exception {
            // given
            var travel = TravelFactory.createTravelRequest();
            // when
            var result = client.perform(post("/api/travels")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(travel))
            );
            // then
            result.andExpectAll(
                    status().isCreated(),
                    header().string("Location", containsString("/api/travels"))
            );
            var possibleTravel = travelRepository.findAll().getFirst();
            assertThat(possibleTravel).isNotNull()
                    .extracting(
                            "startDate",
                            "endDate",
                            "origin",
                            "destination",
                            "description",
                            "budget",
                            "itinerary",
                            "status",
                            "employee.email"
                    )
                    .usingRecursiveFieldByFieldElementComparator()
                    .containsExactlyInAnyOrder(
                            travel.startDate(),
                            travel.endDate(),
                            travel.origin(),
                            travel.destination(),
                            travel.description(),
                            travel.budget(),
                            travel.itinerary(),
                            TravelStatus.SCHEDULED,
                            employee.getEmail()
                    );
        }

    }

    @Nested
    class ListTravelsTests {

        @Test
        void listTravels() throws Exception {
            // given
            var travels = List.of(
                    TravelFactory.createRandomTravel(employee),
                    TravelFactory.createRandomTravel(employee),
                    TravelFactory.createRandomTravel(employee)
            );
            travelRepository.saveAll(travels);
            // when
            var result = client.perform(get("/api/travels"));
            // then
            result.andExpectAll(
                    status().isOk(),
                    content().contentType(MediaType.APPLICATION_JSON),
                    jsonPath("$", contains(
                            travel(travels.get(0)),
                            travel(travels.get(1)),
                            travel(travels.get(2))
                    ))
            );
        }

    }

    @Nested
    class UpdateTravelTests {

        @Test
        void updateTravel() throws Exception {
            // given
            var id = travelRepository.save(TravelFactory.createTravel(employee)).getId();
            var updatedTravel = TravelFactory.createRandomTravelRequest();
            // when
            var result = client.perform(put("/api/travels/{id}", id)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(updatedTravel))
            );
            // then
            result.andExpect(status().isNoContent());
            var possibleTravel = travelRepository.findAll().getFirst();
            assertThat(possibleTravel).isNotNull()
                    .extracting(
                            "startDate",
                            "endDate",
                            "origin",
                            "destination",
                            "description",
                            "budget",
                            "itinerary",
                            "status",
                            "employee.email"
                    )
                    .usingRecursiveFieldByFieldElementComparator()
                    .containsExactlyInAnyOrder(
                            updatedTravel.startDate(),
                            updatedTravel.endDate(),
                            updatedTravel.origin(),
                            updatedTravel.destination(),
                            updatedTravel.description(),
                            updatedTravel.budget(),
                            updatedTravel.itinerary(),
                            TravelStatus.SCHEDULED,
                            employee.getEmail()
                    );
        }

    }

}
