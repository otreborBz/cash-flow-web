package io.github.mds.cashflowweb.user;

import io.github.mds.cashflowweb.employee.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;
import java.util.Optional;

@NoRepositoryBean
public interface UserRepository<T extends User> extends JpaRepository<T, Long> {

    boolean existsByEmail(String email);

    boolean existsByCpf(String cpf);

    boolean existsByPhone(String phone);

    Optional<T> findByEmail(String email);

    List<Employee> findAllByNameContainingIgnoreCase(String name);

}
