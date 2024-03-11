package io.github.mds.cashflowweb.travel;

import io.github.mds.cashflowweb.employee.Employee;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

}
