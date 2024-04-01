package io.github.mds.cashflowweb.travel;

import io.github.mds.cashflowweb.employee.Employee;
import jakarta.validation.Valid;
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

    public TravelController(TravelService travelService) {
        this.travelService = travelService;
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
        model.addAttribute("mode", "create");
        return "travel/travel-form";
    }

    @PostMapping("/travel/create")
    public String createTravel(@Valid @ModelAttribute("travel") TravelRequest travel, Model model, BindingResult result) {
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

    @GetMapping("/travels")
    public ResponseEntity<?> RetrivedListTravelsPage(@AuthenticationPrincipal Employee employee){
        var travels = travelService.listTravels((employee)).stream()
                .map((Travel::toResponse))
                .toList();
        return ResponseEntity.ok(travels);
    }

    @GetMapping("/api/travels/{id}")
    public ResponseEntity<?> findTravel(@PathVariable("id") long id, @AuthenticationPrincipal Employee employee) {
        var travel = travelService.findTravel(id, employee);
        return ResponseEntity.ok(travel.toFullResponse());
    }

    @PutMapping("/api/travels/{id}")
    public ResponseEntity<?> updateTravel(@PathVariable("id") long id, @Valid @RequestBody TravelRequest updatedTravel, @AuthenticationPrincipal Employee employee) {
        travelService.updateTravel(id, updatedTravel.toEntity(), employee);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/api/travels/{id}")
    public ResponseEntity<?> deleteTravel(@PathVariable("id") long id, @AuthenticationPrincipal Employee employee) {
        travelService.deleteTravel(id, employee);
        return ResponseEntity.noContent().build();
    }

}
