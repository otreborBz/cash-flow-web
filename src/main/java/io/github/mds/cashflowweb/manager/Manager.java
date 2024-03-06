package io.github.mds.cashflowweb.manager;

import io.github.mds.cashflowweb.user.Role;
import io.github.mds.cashflowweb.user.User;
import jakarta.persistence.Entity;

@Entity
public class Manager extends User {

    public Manager() {}

    public Manager(String name, String email, String cpf, String password, String phone, String department) {
        super(null, name, email, cpf, password, phone, Role.MANAGER, department);
    }

}
