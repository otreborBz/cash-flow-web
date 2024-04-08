package io.github.mds.cashflowweb.employee;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

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
            model.addAttribute("duplicatedFields", e.getFields());
            model.addAttribute("mode", "create");
            return "employee/employee-form";
        }
        return "redirect:/employee/list";
    }

    @GetMapping("/list")
    public String listEmployees(Model model) {
        var employees = employeeService.listEmployees();
        model.addAttribute("employees", employees);
        return "employee/employee-table";
    }

    @GetMapping("/find")
    public String findEmployees(@RequestParam("name") String name, Model model) {
        var employees = employeeService.findEmployees(name);
        model.addAttribute("employees", employees);
        return "employee/employee-table";
    }

    @GetMapping("/update/{id}")
    public String retrieveUpdateEmployeePage(@PathVariable("id") long id, Model model) {
        var employee = employeeService.findEmployee(id);
        model.addAttribute("employee", employee.toForm());
        model.addAttribute("mode", "edit");
        return "employee/employee-form";
    }

    @PostMapping("/update/{id}")
    public String updateEmployee(@PathVariable("id") long id, @Valid @ModelAttribute("employee") EmployeeForm updatedEmployee, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("employee", updatedEmployee);
            model.addAttribute("mode", "edit");
            return "employee/employee-form";
        }
        try {
            employeeService.updateEmployee(id, updatedEmployee.toEntity());
        } catch (NonUniqueEmployeeException e) {
            model.addAttribute("employee", updatedEmployee);
            model.addAttribute("duplicatedFields", e.getFields());
            model.addAttribute("mode", "edit");
            return "employee/employee-form";
        }
        return "redirect:/employee/list";
    }

    @PostMapping("/delete/{id}")
    public String deleteEmployee(@PathVariable("id") long id) {
        employeeService.deleteEmployee(id);
        return "redirect:/employee/list";
    }

}
