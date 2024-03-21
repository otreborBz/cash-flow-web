package io.github.mds.cashflowweb.expense;

import io.github.mds.cashflowweb.employee.Employee;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@Controller
@RequestMapping("/api/travels/{travelId}/expenses")
public class ExpenseController {

    private final ExpenseService expenseService;

    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @PostMapping
    public ResponseEntity<?> createExpense(@PathVariable("travelId") long travelId, @Valid @RequestBody ExpenseRequest expense, @AuthenticationPrincipal Employee employee, UriComponentsBuilder builder) {
        var expenseId = expenseService.createExpense(travelId, expense.toEntity(), employee);
        var location = builder.path("/api/travels/{travelId}/expenses")
                .buildAndExpand(expenseId)
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @GetMapping
    public ResponseEntity<?> listExpenses(@PathVariable("travelId") long travelId, @AuthenticationPrincipal Employee employee) {
        var expenses = expenseService.listExpenses(travelId, employee).stream()
                .map(Expense::toResponse)
                .toList();
        return ResponseEntity.ok(expenses);
    }

    @GetMapping("/{expenseId}")
    public ResponseEntity<?> findTravel(@PathVariable("travelId") long travelId, @PathVariable("expenseId") long expenseId, @AuthenticationPrincipal Employee employee) {
        var expense = expenseService.findExpense(travelId, expenseId, employee);
        return ResponseEntity.ok(expense.toFullResponse());
    }

    @DeleteMapping("/{expenseId}")
    public ResponseEntity<?> deleteExpense(@PathVariable("travelId") long travelId, @PathVariable("expenseId") long expenseId, @AuthenticationPrincipal Employee employee) {
        expenseService.deleteExpense(travelId, expenseId, employee);
        return ResponseEntity.noContent().build();
    }

}
