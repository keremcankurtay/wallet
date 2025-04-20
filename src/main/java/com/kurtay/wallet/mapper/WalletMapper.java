package com.kurtay.wallet.mapper;

import java.util.List;

import org.springframework.stereotype.Component;

import com.kurtay.wallet.domain.entity.Customer;
import com.kurtay.wallet.domain.entity.Wallet;
import com.kurtay.wallet.domain.request.CreateWalletRequest;
import com.kurtay.wallet.domain.response.CreateWalletResponse;
import com.kurtay.wallet.domain.response.SearchWalletResponse;
import com.kurtay.wallet.service.CustomerService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class WalletMapper {

    private final CustomerService customerService;

    public Wallet mapToWallet(CreateWalletRequest request) {
        Customer customer = customerService.findCustomer(request.getCustomerId());

        return Wallet.builder()
            .name(request.getWalletName())
            .customer(customer)
            .currency(request.getCurrency())
            .balance(request.getInitialBalance())
            .usableBalance(request.getInitialBalance())
            .activeForShopping(request.isActiveForShopping())
            .activeForWithdraw(request.isActiveForWithdraw())
            .build();
    }

    public CreateWalletResponse mapToCreateResponse(Wallet wallet) {
        return CreateWalletResponse.builder()
            .walletId(wallet.getId())
            .walletName(wallet.getName())
            .currency(wallet.getCurrency())
            .balance(wallet.getBalance())
            .usableBalance(wallet.getUsableBalance())
            .activeForWithdraw(wallet.isActiveForWithdraw())
            .activeForShopping(wallet.isActiveForShopping())
            .build();
    }

    public List<SearchWalletResponse> mapToSearchResponses(List<Wallet> wallets) {
        return wallets.stream().map(this::mapToSearchResponse).toList();
    }

    public SearchWalletResponse mapToSearchResponse(Wallet wallet) {
        return SearchWalletResponse.builder()
            .id(wallet.getId())
            .name(wallet.getName())
            .currency(wallet.getCurrency())
            .balance(wallet.getBalance())
            .usableBalance(wallet.getUsableBalance())
            .activeForWithdraw(wallet.isActiveForWithdraw())
            .activeForShopping(wallet.isActiveForShopping())
            .build();
    }
}
