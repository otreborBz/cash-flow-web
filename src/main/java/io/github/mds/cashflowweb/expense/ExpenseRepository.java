package io.github.mds.cashflowweb.expense;

import io.github.mds.cashflowweb.employee.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    List<Expense> findAllByTravelId(long id);

    List<Expense> findAllByTravelIdAndTravelEmployee(long travelId, Employee employee);

}
