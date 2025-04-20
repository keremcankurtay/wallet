package com.kurtay.wallet.service;

import java.util.List;
import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.kurtay.wallet.domain.authentication.Authenticatable;
import com.kurtay.wallet.domain.authentication.AuthenticatedUser;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final List<Authenticator> authenticators;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Authenticatable authenticatable = findUser(username);

        return new AuthenticatedUser(authenticatable);
    }

    protected Authenticatable findUser(String username) {
        for (Authenticator authenticator : authenticators) {
            Optional<Authenticatable> authenticatorByUsername = authenticator.findByUsername(username);
            if (authenticatorByUsername.isPresent()) {
                return authenticatorByUsername.get();
            }
        }

        throw new UsernameNotFoundException("User not found with username: " + username);
    }

}
