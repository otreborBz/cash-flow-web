package io.github.mds.cashflowweb.user;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {

    EMPLOYEE,
    MANAGER;

    @Override
    public String getAuthority() {
        return "ROLE_" + name();
    }

}