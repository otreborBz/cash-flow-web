package io.github.mds.cashflowweb.travel;

import io.github.mds.cashflowweb.employee.Employee;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@Controller
@RequestMapping("/api/travels")
public class TravelController {

    private final TravelService travelService;

    public TravelController(TravelService travelService) {
        this.travelService = travelService;
    }

    @PostMapping
    public ResponseEntity<?> createTravel(@Valid @RequestBody TravelRequest travel, @AuthenticationPrincipal Employee employee, UriComponentsBuilder builder) {
        var id = travelService.createTravel(travel.toEntity(), employee);
        var location = builder.path("/api/travels/{id}")
                .buildAndExpand(id)
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @GetMapping
    public ResponseEntity<?> listTravels(@AuthenticationPrincipal Employee employee) {
        var travels = travelService.listTravels(employee).stream()
                .map(Travel::toResponse)
                .toList();
        return ResponseEntity.ok(travels);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateTravel(@PathVariable("id") long id, @Valid @RequestBody TravelRequest updatedTravel, @AuthenticationPrincipal Employee employee) {
        travelService.updateTravel(id, updatedTravel.toEntity(), employee);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTravel(@PathVariable("id") long id, @AuthenticationPrincipal Employee employee) {
        travelService.deleteTravel(id, employee);
        return ResponseEntity.noContent().build();
    }

}
