package io.github.mds.cashflowweb.user;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

import static java.util.Collections.singleton;

@Entity
@Table(name = "\"user\"")
@Inheritance
public abstract class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @NotBlank
    @Column(nullable = false)
    protected String name;

    @NotBlank
    @Column(nullable = false, unique = true)
    protected String email;

    @NotBlank
    @Column(nullable = false, unique = true)
    protected String cpf;

    @NotBlank
    @Column(nullable = false)
    protected String password;

    @NotBlank
    @Column(nullable = false, unique = true)
    protected String phone;

    @NotNull
    @Column(nullable = false)
    protected Role role;

    protected String department;

    protected User() {}

    protected User(Long id, String name, String email, String cpf, String password, String phone, Role role, String department) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.cpf = cpf;
        this.password = password;
        this.phone = phone;
        this.role = role;
        this.department = department;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getUsername() {
        return email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return singleton(role);
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}