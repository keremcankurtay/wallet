package com.kurtay.wallet.service;

import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.stereotype.Service;

import com.kurtay.wallet.domain.authentication.AuthenticatedUser;
import com.kurtay.wallet.domain.entity.Wallet;
import com.kurtay.wallet.domain.enums.Role;
import com.kurtay.wallet.util.SecurityUtility;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WalletGuard {

    private final WalletService walletService;

    public void checkUserHasAccessToWallet(Long walletId) {
        AuthenticatedUser authenticatedUser = SecurityUtility.getAuthenticatedUser();

        if (authenticatedUser.hasRole(Role.EMPLOYEE)) {
            return;
        }

        Wallet wallet = walletService.findWallet(walletId);

        String username = authenticatedUser.getUsername();
        if (!username.equals(wallet.getCustomer().getUsername())) {
            throw new AuthorizationDeniedException("You are not authorized to perform this transaction");
        }

    }

}
