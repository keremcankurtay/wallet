package com.kurtay.wallet.util;

import org.springframework.security.core.context.SecurityContextHolder;

import com.kurtay.wallet.domain.authentication.AuthenticatedUser;
import com.kurtay.wallet.domain.enums.Role;

import lombok.experimental.UtilityClass;

@UtilityClass
public class SecurityUtility {

    public static String getUsername() {
        AuthenticatedUser authenticatedUser = getAuthenticatedUser();

        return authenticatedUser.getUsername();
    }

    public static AuthenticatedUser getAuthenticatedUser() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) {
            throw new SecurityException("No authentication found");
        }

        return (AuthenticatedUser) authentication.getPrincipal();
    }

    public static void checkUserHasAccessToWallets(String customerName) {
        AuthenticatedUser authenticatedUser = SecurityUtility.getAuthenticatedUser();

        if (authenticatedUser.hasRole(Role.EMPLOYEE) || authenticatedUser.getUsername().equals(customerName)) {
            return;
        }

        throw new SecurityException("You are not authorized to perform wallets");
    }

}
