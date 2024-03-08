package io.github.mds.cashflowweb.employee;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static io.github.mds.cashflowweb.employee.EmployeeMatchers.employee;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EmployeeController.class)
@WithMockUser
public class EmployeeControllerTest {

    @Autowired
    private MockMvc client;

    @MockBean
    private EmployeeService employeeService;

    @Nested
    class CreateEmployeeTests {

        @Test
        void retrieveCreateEmployeePage() throws Exception {
            // when
            var result = client.perform(get("/employee/create"));
            // then
            result.andExpectAll(
                    status().isOk(),
                    model().attribute("employee", is(employee())),
                    model().attribute("mode", "create"),
                    view().name("employee/employee-form")
            );
        }

        @Test
        void doNotCreateDuplicatedEmployee() throws Exception {
            // given
            doThrow(NonUniqueEmployeeException.class).when(employeeService).createEmployee(any(Employee.class));
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
                    status().isOk(),
                    model().attribute("employee", is(employee(
                            "employee",
                            "employee@email.com",
                            EmployeeFactory.CPF,
                            "password",
                            "(xx) xxxxx-xxxx",
                            "department"
                    ))),
                    view().name("employee/employee-form")
            );
            verify(employeeService, times(1)).createEmployee(any(Employee.class));
        }

        @Test
        void doNotCreateEmployeeWithInvalidFields() throws Exception {
            // when
            var result = client.perform(post("/employee/create")
                    .param("name", "")
                    .param("email", "")
                    .param("cpf", "")
                    .param("password", "")
                    .param("phone", "")
                    .param("department", "")
                    .with(csrf())
            );
            // then
            result.andExpectAll(
                    status().isOk(),
                    model().attribute("employee", is(employee("", "", "", "", "", ""))),
                    model().attribute("mode", "create"),
                    view().name("employee/employee-form")
            );
        }

    }

    @Nested
    class UpdateEmployeeTests {

        @Test
        void retrieveUpdateEmployeePage() throws Exception {
            // given
            var employee = EmployeeFactory.createEmployee();
            when(employeeService.findEmployee(anyLong())).thenReturn(employee);
            // when
            var result = client.perform(get("/employee/update/{id}", 1L));
            // then
            result.andExpectAll(
                    status().isOk(),
                    model().attribute("employee", is(employee(employee))),
                    model().attribute("mode", "edit"),
                    view().name("employee/employee-form")
            );
        }

        @Test
        void show404ErrorPageIfEmployeeDoesNotExists() throws Exception {
            // given
            doThrow(EmployeeNotFoundException.class).when(employeeService).findEmployee(anyLong());
            // when
            var result = client.perform(get("/employee/update/{id}", anyLong()));
            // then
            result.andExpect(status().isNotFound());
        }

        @Test
        void show404ErrorPageIfEmployeeDoesNotExistsWhenUpdating() throws Exception {
            // given
            var updatedEmployee = EmployeeFactory.createRandomEmployee();
            doThrow(EmployeeNotFoundException.class).when(employeeService).updateEmployee(anyLong(), any(Employee.class));
            // when
            var result = client.perform(post("/employee/update/{id}", 1L)
                    .param("name", updatedEmployee.getName())
                    .param("email", updatedEmployee.getEmail())
                    .param("cpf", updatedEmployee.getCpf())
                    .param("password", updatedEmployee.getPassword())
                    .param("phone", updatedEmployee.getPhone())
                    .param("department", updatedEmployee.getDepartment())
                    .with(csrf())
            );
            // then
            result.andExpect(status().isNotFound());
        }

        @Test
        void retrieveFormWithEmployeeFieldsIfEmployeeHasDuplicatedFields() throws Exception {
            // given
            var updatedEmployee = EmployeeFactory.createRandomEmployee();
            doThrow(NonUniqueEmployeeException.class).when(employeeService).updateEmployee(anyLong(), any(Employee.class));
            // when
            var result = client.perform(post("/employee/update/{id}", 1L)
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
                    status().isOk(),
                    model().attribute("employee", is(employee(updatedEmployee))),
                    model().attribute("mode", "edit"),
                    view().name("employee/employee-form")
            );
        }

        @Test
        void doNotUpdateEmployeeWithInvalidFields() throws Exception {
            // when
            var result = client.perform(post("/employee/update/{id}", 1L)
                    .param("name", "")
                    .param("email", "")
                    .param("cpf", "")
                    .param("password", "")
                    .param("phone", "")
                    .param("department", "")
                    .with(csrf())
            );
            // then
            result.andExpectAll(
                    status().isOk(),
                    model().attribute("employee", is(employee("", "", "", "", "", ""))),
                    model().attribute("mode", "edit"),
                    view().name("employee/employee-form")
            );
        }

    }

    @Nested
    class DeleteEmployeeTests {

        @Test
        void show404ErrorPageIfEmployeeDoesNotExists() throws Exception {
            // given
            doThrow(EmployeeNotFoundException.class).when(employeeService).deleteEmployee(anyLong());
            // when
            var result = client.perform(post("/employee/delete/{id}", 1L)
                    .with(csrf())
            );
            // then
            result.andExpect(status().isNotFound());
        }

    }

}
