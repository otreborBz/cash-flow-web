package io.github.mds.cashflowweb.employee;

import io.github.mds.cashflowweb.user.Role;
import io.github.mds.cashflowweb.user.User;
import jakarta.persistence.Entity;

@Entity
public class Employee extends User {

    public Employee() {}

    public Employee(String name, String email, String cpf, String password, String phone, String department) {
        super(null, name, email, cpf, password, phone, Role.EMPLOYEE, department);
    }

    public Employee(Long id) {
        this.id = id;
    }

    public EmployeeForm toForm() {
        return new EmployeeForm(name, email, cpf, null, phone, department);
    }

}

