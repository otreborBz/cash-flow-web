package io.github.mds.cashflowweb.employee;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTest {

    @InjectMocks
    private EmployeeService employeeService;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Nested
    class CreateEmployeeTests {

        @Test
        void createEmployee() {
            // given
            var employee = EmployeeFactory.createEmployee();
            when(employeeRepository.existsByEmail(employee.getEmail())).thenReturn(false);
            when(employeeRepository.existsByCpf(employee.getCpf())).thenReturn(false);
            when(employeeRepository.existsByPhone(employee.getPhone())).thenReturn(false);
            when(passwordEncoder.encode(employee.getPassword())).thenReturn("encodedPassword");
            // when
            employeeService.createEmployee(employee);
            // then
            assertThat(employee.getPassword()).isEqualTo("encodedPassword");
            verify(employeeRepository, times(1)).save(employee);
        }

        @Test
        void doNotCreateEmployeeWithDuplicatedFields() {
            // given
            var employee = EmployeeFactory.createEmployee();
            when(employeeRepository.existsByEmail(employee.getEmail())).thenReturn(true);
            when(employeeRepository.existsByCpf(employee.getCpf())).thenReturn(true);
            when(employeeRepository.existsByPhone(employee.getPhone())).thenReturn(true);
            // when
            var exception = catchThrowableOfType(() -> employeeService.createEmployee(employee), NonUniqueEmployeeException.class);
            // then
            assertThat(exception.getFields()).contains("email", "cpf", "phone");
            verify(employeeRepository, never()).save(any(Employee.class));
        }

    }

    @Nested
    class UpdateEmployeeTests {

        @Test
        void doNotUpdateNonexistentEmployee() {
            // given
            var updatedEmployee = EmployeeFactory.createRandomEmployee();
            when(employeeRepository.findById(1L)).thenReturn(Optional.empty());
            // when
            catchThrowableOfType(() -> employeeService.updateEmployee(1L, updatedEmployee), EmployeeNotFoundException.class);
            // then
            verify(employeeRepository, never()).save(any(Employee.class));
        }

        @Test
        void doNotUpdateEmployeeWithDuplicatedFields() {
            // given
            var employee = EmployeeFactory.createEmployee();
            var updatedEmployee = EmployeeFactory.createRandomEmployee();
            when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
            when(employeeRepository.existsByEmail(updatedEmployee.getEmail())).thenReturn(true);
            when(employeeRepository.existsByCpf(updatedEmployee.getCpf())).thenReturn(true);
            when(employeeRepository.existsByPhone(updatedEmployee.getPhone())).thenReturn(true);
            // when
            var exception = catchThrowableOfType(() -> employeeService.updateEmployee(1L, updatedEmployee), NonUniqueEmployeeException.class);
            // then
            assertThat(exception.getFields()).contains("email", "cpf", "phone");
            verify(employeeRepository, never()).save(any(Employee.class));
        }

    }

}
