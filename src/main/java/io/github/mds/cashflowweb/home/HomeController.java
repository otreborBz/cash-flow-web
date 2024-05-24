package io.github.mds.cashflowweb.home;

import io.github.mds.cashflowweb.employee.EmployeeService;
import io.github.mds.cashflowweb.travel.TravelService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final EmployeeService employeeService;
    private final TravelService travelService;

    public HomeController(EmployeeService employeeService, TravelService travelService) {
        this.employeeService = employeeService;
        this.travelService = travelService;
    }

    @GetMapping("/home")
    public String home(Model model) {
        var employees = employeeService.listEmployees();
        var travels = travelService.listTravels();
        model.addAttribute("employees", employees);
        model.addAttribute("travels", travels);
        return "home";
    }

}
