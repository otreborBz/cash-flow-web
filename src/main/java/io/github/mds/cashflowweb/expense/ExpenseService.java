package io.github.mds.cashflowweb.expense;

import io.github.mds.cashflowweb.employee.Employee;
import io.github.mds.cashflowweb.travel.TravelNotFoundException;
import io.github.mds.cashflowweb.travel.TravelRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final TravelRepository travelRepository;

    public ExpenseService(ExpenseRepository expenseRepository, TravelRepository travelRepository) {
        this.expenseRepository = expenseRepository;
        this.travelRepository = travelRepository;
    }

    @Transactional
    public long createExpense(long travelId, Expense expense, Employee employee) {
        var travel = travelRepository.findByIdAndEmployee(travelId, employee)
                .orElseThrow(TravelNotFoundException::new);
        expense.setTravel(travel);
        return expenseRepository.save(expense).getId();
    }

    @Transactional(readOnly = true)
    public List<Expense> listExpenses(long travelId, Employee employee) {
        return expenseRepository.findAllByTravelIdAndTravelEmployee(travelId, employee);
    }

}
