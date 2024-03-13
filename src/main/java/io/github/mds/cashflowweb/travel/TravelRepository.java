package io.github.mds.cashflowweb.travel;

import io.github.mds.cashflowweb.employee.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TravelRepository extends JpaRepository<Travel, Long> {

    List<Travel> findAllByEmployee(Employee employee);

}
