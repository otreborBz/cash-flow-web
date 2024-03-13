package io.github.mds.cashflowweb.travel;

import io.github.mds.cashflowweb.employee.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TravelRepository extends JpaRepository<Travel, Long> {

    boolean existsByIdAndEmployee(long id, Employee employee);

    List<Travel> findAllByEmployee(Employee employee);

    Optional<Travel> findByIdAndEmployee(long id, Employee employee);

    void deleteByIdAndEmployee(long id, Employee employee);

}
