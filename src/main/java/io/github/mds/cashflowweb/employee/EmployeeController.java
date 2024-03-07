package io.github.mds.cashflowweb.employee;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/employee")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping("/create")
    public String retrieveCreateEmployeePage(Model model) {
        model.addAttribute("employee", new EmployeeForm());
        model.addAttribute("mode", "create");
        return "employee/employee-form";
    }

    @PostMapping("/create")
    public String createEmployee(@Valid @ModelAttribute("employee") EmployeeForm employee, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("employee", employee);
            model.addAttribute("mode", "create");
            return "employee/employee-form";
        }
        try {
            employeeService.createEmployee(employee.toEntity());
        } catch (NonUniqueEmployeeException e) {
            model.addAttribute("employee", employee);
            model.addAttribute("mode", "create");
            return "employee/employee-form";
        }
        return "redirect:/employee/list";
    }

}
