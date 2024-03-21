package io.github.mds.cashflowweb.expense;

import io.github.mds.cashflowweb.employee.Employee;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

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
    public ResponseEntity<?> findExpense(@PathVariable("travelId") long travelId, @PathVariable("expenseId") long expenseId, @AuthenticationPrincipal Employee employee) {
        var expense = expenseService.findExpense(travelId, expenseId, employee);
        return ResponseEntity.ok(expense.toFullResponse());
    }

    @PutMapping("/{expenseId}/fiscalNote")
    public ResponseEntity<?> updateExpenseFiscalNote(@PathVariable("travelId") long travelId, @PathVariable("expenseId") long expenseId, @RequestParam("fiscalNote") MultipartFile fiscalNote, @AuthenticationPrincipal Employee employee) throws IOException {
        expenseService.updateExpenseFiscalNote(travelId, expenseId, fiscalNote, employee);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{expenseId}")
    public ResponseEntity<?> deleteExpense(@PathVariable("travelId") long travelId, @PathVariable("expenseId") long expenseId, @AuthenticationPrincipal Employee employee) {
        expenseService.deleteExpense(travelId, expenseId, employee);
        return ResponseEntity.noContent().build();
    }

}
