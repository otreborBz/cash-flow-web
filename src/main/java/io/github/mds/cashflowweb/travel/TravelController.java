package io.github.mds.cashflowweb.travel;

import io.github.mds.cashflowweb.employee.Employee;
import io.github.mds.cashflowweb.employee.EmployeeService;
import io.github.mds.cashflowweb.expense.ExpenseService;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@Controller
public class TravelController {

    private final TravelService travelService;
    private final ExpenseService expenseService;
    private final EmployeeService employeeService;

    public TravelController(TravelService travelService, ExpenseService expenseService, EmployeeService employeeService) {
        this.travelService = travelService;
        this.expenseService = expenseService;
        this.employeeService = employeeService;
    }

    @PostMapping("/api/travels")
    public ResponseEntity<?> createTravel(@Valid @RequestBody TravelRequest travel, @AuthenticationPrincipal Employee employee, UriComponentsBuilder builder) {
        var id = travelService.createTravel(travel.toEntity(), employee);
        var location = builder.path("/api/travels/{id}")
                .buildAndExpand(id)
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @GetMapping("/travel/create")
    public String retrieveCreateTravelPage(Model model) {
        model.addAttribute("travel", new TravelForm());
        model.addAttribute("employees", employeeService.listEmployees());
        model.addAttribute("mode", "create");
        return "travel/travel-form";
    }

    @PostMapping("/travel/create")
    public String createTravel(@Valid @ModelAttribute("travel") TravelForm travel, Model model, BindingResult result) {
        if (result.hasErrors()) {
            model.addAttribute("travel", travel);
            model.addAttribute("mode", "create");
            return "travel/travel-form";
        }
        travelService.createTravel(travel.toEntity());
        return "redirect:/travel/list";
    }

    @GetMapping("/api/travels")
    public ResponseEntity<?> listTravels(@AuthenticationPrincipal Employee employee) {
        var travels = travelService.listTravels(employee).stream()
                .map(Travel::toResponse)
                .toList();
        return ResponseEntity.ok(travels);
    }

    @GetMapping("/travel/list")
    public String listTravels(Model model){
        var travels = travelService.listTravels();
        model.addAttribute("travels", travels);
        return "travel/travel-table";
    }

    @GetMapping("/api/travels/{id}/print")
    public ResponseEntity<?> printTravel(@PathVariable("id") long id) {
        var document = travelService.printTravel(id);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header("Content-Disposition", "attachment; filename=" + document.filename())
                .contentLength(document.size())
                .body(document.content().toByteArray());
    }

    @GetMapping("/travel/find")
    public String findTravels(@RequestParam("description") String description, Model model) {
        var travels = travelService.findTravels(description);
        model.addAttribute("travels", travels);
        return "travel/travel-table";
    }

    @GetMapping("/api/travels/{id}")
    public ResponseEntity<?> findTravel(@PathVariable("id") long id, @AuthenticationPrincipal Employee employee) {
        var travel = travelService.findTravel(id, employee);
        return ResponseEntity.ok(travel.toFullResponse());
    }

    @GetMapping("/travel/update/{id}")
    public String retrieveUpdateTravelPage(@PathVariable("id") long id, Model model) {
        var travel = travelService.findTravel(id);
        var expenses = expenseService.listExpenses(id);
        model.addAttribute("travelId", id);
        model.addAttribute("travel", travel.toForm());
        model.addAttribute("employees", employeeService.listEmployees());
        model.addAttribute("expenses", expenses);
        model.addAttribute("mode", "update");
        return "travel/travel-form";
    }

    @PostMapping("/travel/update/{id}")
    public String updateTravel(@PathVariable("id") long id, @Valid @ModelAttribute TravelForm travel, Model model, BindingResult result) {
        if (result.hasErrors()) {
            model.addAttribute("travel", travel);
            model.addAttribute("mode", "update");
            return "travel/travel-form";
        }
        travelService.updateTravel(id, travel.toEntity());
        return "redirect:/travel/list";
    }

    @PutMapping("/api/travels/{id}")
    public ResponseEntity<?> updateTravel(@PathVariable("id") long id, @Valid @RequestBody TravelRequest updatedTravel, @AuthenticationPrincipal Employee employee) {
        travelService.updateTravel(id, updatedTravel.toEntity(), employee);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/travel/delete/{id}")
    public String deleteTravel(@PathVariable("id") long id) {
        travelService.deleteTravel(id);
        return "redirect:/travel/list";
    }

    @DeleteMapping("/api/travels/{id}")
    public ResponseEntity<?> deleteTravel(@PathVariable("id") long id, @AuthenticationPrincipal Employee employee) {
        travelService.deleteTravel(id, employee);
        return ResponseEntity.noContent().build();
    }

}
