package io.github.mds.cashflowweb.employee;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.br.CPF;

public class EmployeeForm {

    @NotBlank
    private String name;

    @Email
    @NotBlank
    private String email;

    @CPF
    @NotBlank
    private String cpf;

    @NotBlank
    private String password;

    @NotBlank
    private String phone;

    private String department;

    public EmployeeForm() {}

    public EmployeeForm(String name, String email, String cpf, String password, String phone, String department) {
        this.name = name;
        this.email = email;
        this.cpf = cpf;
        this.password = password;
        this.phone = phone;
        this.department = department;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public Employee toEntity() {
        return new Employee(name, email, cpf, password, phone, department);
    }

}
