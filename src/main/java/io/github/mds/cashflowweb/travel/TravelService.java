package io.github.mds.cashflowweb.travel;

import io.github.mds.cashflowweb.employee.Employee;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TravelService {

    private final TravelRepository travelRepository;

    public TravelService(TravelRepository travelRepository) {
        this.travelRepository = travelRepository;
    }

    @Transactional
    public long createTravel(Travel travel, Employee employee) {
        travel.setEmployee(employee);
        return travelRepository.save(travel).getId();
    }

    @Transactional(readOnly = true)
    public List<Travel> listTravels(Employee employee) {
        return travelRepository.findAllByEmployee(employee);
    }

    @Transactional
    public void updateTravel(long id, Travel updatedTravel, Employee employee) {
        var travel = travelRepository.findByIdAndEmployee(id, employee)
                .orElseThrow(TravelNotFoundException::new);
        travel.setStartDate(updatedTravel.getStartDate());
        travel.setEndDate(updatedTravel.getEndDate());
        travel.setOrigin(updatedTravel.getOrigin());
        travel.setDestination(updatedTravel.getDestination());
        travel.setDescription(updatedTravel.getDescription());
        travel.setBudget(updatedTravel.getBudget());
        travel.setItinerary(updatedTravel.getItinerary());
        travelRepository.save(travel);
    }

    @Transactional
    public void deleteTravel(long id, Employee employee) {
        if (!travelRepository.existsByIdAndEmployee(id, employee)) {
            throw new TravelNotFoundException();
        }
        travelRepository.deleteByIdAndEmployee(id, employee);
    }

}
