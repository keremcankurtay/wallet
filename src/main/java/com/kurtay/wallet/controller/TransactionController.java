package com.kurtay.wallet.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kurtay.wallet.domain.request.DepositRequest;
import com.kurtay.wallet.domain.request.TransactionApproval;
import com.kurtay.wallet.domain.request.WithdrawRequest;
import com.kurtay.wallet.domain.response.TransactionListResponse;
import com.kurtay.wallet.domain.response.TransactionResponse;
import com.kurtay.wallet.service.TransactionService;

import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/v1/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping("/deposits")
    @RolesAllowed({ "CUSTOMER", "EMPLOYEE" })
    public ResponseEntity<TransactionResponse> deposit(@RequestBody @Valid DepositRequest request) {
        log.info("Deposit request: {}", request);
        TransactionResponse transactionResponse = transactionService.deposit(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(transactionResponse);
    }

    @PutMapping("/deposits/status")
    @RolesAllowed({ "EMPLOYEE" })
    public ResponseEntity<TransactionResponse> updateDepositStatus(@RequestBody @Valid TransactionApproval request) {
        log.info("Deposit status update request: {}", request);
        TransactionResponse transactionResponse = transactionService.updateDepositStatus(request);
        return ResponseEntity.status(HttpStatus.OK).body(transactionResponse);
    }

    @PostMapping("/withdraw")
    @RolesAllowed({ "CUSTOMER", "EMPLOYEE" })
    public ResponseEntity<TransactionResponse> withdraw(@RequestBody @Valid WithdrawRequest request) {
        log.info("Withdraw request: {}", request);
        TransactionResponse transactionResponse = transactionService.withdraw(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(transactionResponse);
    }

    @PutMapping("/withdraw/status")
    @RolesAllowed({ "EMPLOYEE" })
    public ResponseEntity<TransactionResponse> updateWithdrawStatus(@RequestBody @Valid TransactionApproval request) {
        log.info("Withdraw status update request: {}", request);
        TransactionResponse transactionResponse = transactionService.updateWithdrawStatus(request);
        return ResponseEntity.status(HttpStatus.OK).body(transactionResponse);
    }

    @GetMapping("/wallets/{id}")
    @RolesAllowed({ "CUSTOMER", "EMPLOYEE" })
    public ResponseEntity<List<TransactionListResponse>> getTransactions(@NotNull @PathVariable("id") Long id) {
        log.info("Get transactions for wallet with id: {}", id);
        List<TransactionListResponse> transactions = transactionService.getTransactionList(id);
        return ResponseEntity.status(HttpStatus.OK).body(transactions);
    }
}
