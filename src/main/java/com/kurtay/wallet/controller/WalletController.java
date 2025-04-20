package com.kurtay.wallet.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kurtay.wallet.domain.request.CreateWalletRequest;
import com.kurtay.wallet.domain.request.SearchWalletRequest;
import com.kurtay.wallet.domain.response.CreateWalletResponse;
import com.kurtay.wallet.domain.response.SearchWalletResponse;
import com.kurtay.wallet.service.WalletService;

import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/v1/wallets")
@RequiredArgsConstructor
public class WalletController {

    private final WalletService walletService;

    @PostMapping
    @RolesAllowed({ "CUSTOMER", "EMPLOYEE" })
    public ResponseEntity<CreateWalletResponse> createWallet(@RequestBody @Valid CreateWalletRequest request) {
        log.info("Create wallet request: {}", request);
        CreateWalletResponse wallet = walletService.createWallet(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(wallet);
    }

    @GetMapping
    @RolesAllowed({ "CUSTOMER", "EMPLOYEE" })
    public ResponseEntity<List<SearchWalletResponse>> searchWallets(@Valid SearchWalletRequest searchWalletRequest) {
        log.info("Search wallets request: {}", searchWalletRequest);

        List<SearchWalletResponse> response = walletService.searchWallets(searchWalletRequest);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
