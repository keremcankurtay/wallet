package com.kurtay.wallet.service;

import java.util.Optional;

import com.kurtay.wallet.domain.authentication.Authenticatable;

public interface Authenticator {

    Optional<Authenticatable> findByUsername(String username);

}
