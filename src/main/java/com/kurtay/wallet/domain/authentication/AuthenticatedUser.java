package com.kurtay.wallet.domain.authentication;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.kurtay.wallet.domain.enums.Role;

public class AuthenticatedUser implements UserDetails {

    private final String username;
    private final String password;
    private final GrantedAuthority authority;

    public AuthenticatedUser(Authenticatable authenticatable) {
        this.username = authenticatable.getUsername();
        this.password = authenticatable.getPassword();
        this.authority = new SimpleGrantedAuthority(authenticatable.getRole().name());
    }

    public boolean hasRole(Role role) {
        return authority.getAuthority().equals(role.name());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(authority);
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }
}
