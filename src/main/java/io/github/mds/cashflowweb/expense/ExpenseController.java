package io.github.mds.cashflowweb.expense;

import io.github.mds.cashflowweb.employee.Employee;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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

}
