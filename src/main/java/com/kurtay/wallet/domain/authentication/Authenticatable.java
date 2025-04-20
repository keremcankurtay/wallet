package com.kurtay.wallet.domain.authentication;

import com.kurtay.wallet.domain.enums.Role;

public interface Authenticatable {

    String getUsername();

    String getPassword();

    Role getRole();
}
