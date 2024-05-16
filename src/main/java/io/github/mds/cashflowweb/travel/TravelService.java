package io.github.mds.cashflowweb.travel;

import io.github.mds.cashflowweb.employee.Employee;
import io.github.mds.cashflowweb.employee.EmployeeNotFoundException;
import io.github.mds.cashflowweb.employee.EmployeeRepository;
import io.github.mds.cashflowweb.expense.ExpenseRepository;
import io.github.mds.cashflowweb.util.Document;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TravelService {

    private final TravelRepository travelRepository;
    private final EmployeeRepository employeeRepository;
    private final ExpenseRepository expenseRepository;
    private final TravelDocumentGenerator travelDocumentGenerator;

    public TravelService(TravelRepository travelRepository, EmployeeRepository employeeRepository, ExpenseRepository expenseRepository, TravelDocumentGenerator travelDocumentGenerator) {
        this.travelRepository = travelRepository;
        this.employeeRepository = employeeRepository;
        this.expenseRepository = expenseRepository;
        this.travelDocumentGenerator = travelDocumentGenerator;
    }

    @Transactional
    public long createTravel(Travel travel, Employee employee) {
        travel.setEmployee(employee);
        return travelRepository.save(travel).getId();
    }

    @Transactional
    public void createTravel(Travel travel) {
        travelRepository.save(travel);
    }

    @Transactional(readOnly = true)
    public List<Travel> listTravels(Employee employee) {
        return travelRepository.findAllByEmployee(employee);
    }

    @Transactional(readOnly = true)
    public List<Travel> listTravels() {
        return travelRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Travel> findTravels(String description) {
        return travelRepository.findAllByDescriptionContainingIgnoreCase(description);
    }

    @Transactional(readOnly = true)
    public Travel findTravel(long id, Employee employee) {
        return travelRepository.findByIdAndEmployee(id, employee)
                .orElseThrow(TravelNotFoundException::new);
    }

    @Transactional(readOnly = true)
    public Travel findTravel(long id) {
        return travelRepository.findById(id)
                .orElseThrow(TravelNotFoundException::new);
    }

    @Transactional(readOnly = true)
    public Document printTravel(long id) {
        var travel = travelRepository.findById(id).orElseThrow(TravelNotFoundException::new);
        var expenses = expenseRepository.findAllByTravelId(id);
        return travelDocumentGenerator.generateOrderDocument(travel, expenses);
    }

    @Transactional
    public void updateTravel(long id, Travel updatedTravel, Employee employee) {
        var travel = travelRepository.findByIdAndEmployee(id, employee)
                .orElseThrow(TravelNotFoundException::new);
        updateTravelDetails(travel, updatedTravel);
        travelRepository.save(travel);
    }

    @Transactional
    public void updateTravel(long id, Travel updatedTravel) {
        var travel = travelRepository.findById(id)
                .orElseThrow(TravelNotFoundException::new);
        var employee = employeeRepository.findById(updatedTravel.getEmployee().getId())
                        .orElseThrow(EmployeeNotFoundException::new);
        travel.setEmployee(employee);
        updateTravelDetails(travel, updatedTravel);
        travelRepository.save(travel);
    }

    private void updateTravelDetails(Travel travel, Travel updatedTravel) {
        travel.setStartDate(updatedTravel.getStartDate());
        travel.setEndDate(updatedTravel.getEndDate());
        travel.setOrigin(updatedTravel.getOrigin());
        travel.setDestination(updatedTravel.getDestination());
        travel.setDescription(updatedTravel.getDescription());
        //travel.setBudget(updatedTravel.getBudget());
        //travel.setItinerary(updatedTravel.getItinerary());
    }

    @Transactional
    public void deleteTravel(long id) {
        if (!travelRepository.existsById(id)) {
            throw new TravelNotFoundException();
        }
        travelRepository.deleteById(id);
    }

    @Transactional
    public void deleteTravel(long id, Employee employee) {
        if (!travelRepository.existsByIdAndEmployee(id, employee)) {
            throw new TravelNotFoundException();
        }
        travelRepository.deleteByIdAndEmployee(id, employee);
    }

}
