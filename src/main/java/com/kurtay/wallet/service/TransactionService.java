package com.kurtay.wallet.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kurtay.wallet.domain.entity.Transaction;
import com.kurtay.wallet.domain.entity.Wallet;
import com.kurtay.wallet.domain.enums.TransactionOppositePartyType;
import com.kurtay.wallet.domain.enums.TransactionStatus;
import com.kurtay.wallet.domain.enums.TransactionType;
import com.kurtay.wallet.domain.request.DepositRequest;
import com.kurtay.wallet.domain.request.Transact;
import com.kurtay.wallet.domain.request.TransactionApproval;
import com.kurtay.wallet.domain.request.WithdrawRequest;
import com.kurtay.wallet.domain.response.TransactionListResponse;
import com.kurtay.wallet.domain.response.TransactionResponse;
import com.kurtay.wallet.mapper.TransactionMapper;
import com.kurtay.wallet.repository.TransactionalRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionalRepository transactionalRepository;
    private final TransactionMapper transactionMapper;
    private final WalletService walletService;
    private final WalletGuard walletGuard;

    @Transactional(readOnly = true)
    public List<TransactionListResponse> getTransactionList(Long walletId) {
        walletGuard.checkUserHasAccessToWallet(walletId);

        List<Transaction> transactions = transactionalRepository.findByWalletIdOrderByCreateDateDesc(walletId);
        return transactionMapper.mapToTransactionListResponse(transactions);
    }

    @Transactional
    public TransactionResponse deposit(DepositRequest depositRequest) {
        walletGuard.checkUserHasAccessToWallet(depositRequest.getWalletId());

        Transaction transaction = saveTransaction(depositRequest, TransactionType.DEPOSIT);

        TransactionStatus transactionStatus = depositRequest.getTransactionStatus();

        if (transactionStatus == TransactionStatus.APPROVED) {
            walletService.depositApprovedAmount(depositRequest);
        } else {
            walletService.depositPendingAmount(depositRequest);
        }

        return TransactionResponse.builder().transactionId(transaction.getId()).status(transactionStatus).build();
    }

    @Transactional
    public TransactionResponse updateDepositStatus(TransactionApproval transactionApproval) {
        return updateTransactionStatus(transactionApproval, TransactionType.DEPOSIT, walletService::approveDeposit,
            walletService::denyDeposit);
    }

    @Transactional
    public TransactionResponse updateWithdrawStatus(TransactionApproval transactionApproval) {
        return updateTransactionStatus(transactionApproval, TransactionType.WITHDRAWAL, walletService::approveWithdraw,
            walletService::denyWithdraw);
    }

    protected TransactionResponse updateTransactionStatus(TransactionApproval transactionApproval,
        TransactionType transactionType, Consumer<Transaction> approveMethod, Consumer<Transaction> denyMethod) {
        Optional<Transaction> transactionOptional =
            transactionalRepository.findByIdAndStatusAndTransactionType(transactionApproval.getTransactionId(),
                TransactionStatus.PENDING, transactionType);

        if (transactionOptional.isEmpty()) {
            throw new IllegalArgumentException("Pending transaction not found");
        }

        Transaction transaction = transactionOptional.get();
        walletGuard.checkUserHasAccessToWallet(transaction.getWallet().getId());

        if (TransactionStatus.APPROVED.equals(transactionApproval.getStatus())) {
            approveMethod.accept(transaction);
            transaction.setStatus(TransactionStatus.APPROVED);
            return TransactionResponse.builder()
                .transactionId(transaction.getId())
                .status(TransactionStatus.APPROVED)
                .build();
        } else if (TransactionStatus.DENIED.equals(transactionApproval.getStatus())) {
            transaction.setStatus(TransactionStatus.DENIED);
            denyMethod.accept(transaction);
            return TransactionResponse.builder()
                .transactionId(transaction.getId())
                .status(TransactionStatus.DENIED)
                .build();
        } else {
            throw new IllegalArgumentException("Invalid transaction status");
        }
    }

    @Transactional
    public TransactionResponse withdraw(WithdrawRequest withdrawRequest) {
        walletGuard.checkUserHasAccessToWallet(withdrawRequest.getWalletId());

        Wallet wallet = walletService.findWallet(withdrawRequest.getWalletId());

        validateWithdraw(withdrawRequest, wallet);

        Transaction transaction = saveTransaction(withdrawRequest, TransactionType.WITHDRAWAL);

        TransactionStatus transactionStatus = withdrawRequest.getTransactionStatus();

        if (transactionStatus == TransactionStatus.APPROVED) {
            walletService.withdrawApprovedAmount(withdrawRequest);
        } else {
            walletService.withdrawPendingAmount(withdrawRequest);
        }

        return TransactionResponse.builder()
            .transactionId(transaction.getId())
            .status(withdrawRequest.getTransactionStatus())
            .build();
    }

    protected void validateWithdraw(WithdrawRequest withdrawRequest, Wallet wallet) {
        validateWithdrawPartyType(withdrawRequest, wallet);

        TransactionStatus transactionStatus = withdrawRequest.getTransactionStatus();
        if (transactionStatus == TransactionStatus.APPROVED) {
            validateApprovedWithdraw(withdrawRequest, wallet);
        } else {
            validatePendingWithdraw(withdrawRequest, wallet);
        }
    }

    protected void validateApprovedWithdraw(WithdrawRequest withdrawRequest, Wallet wallet) {
        if (checkWalletForWithdraw(withdrawRequest, wallet.getBalance())) {
            throw new IllegalArgumentException("Insufficient balance for withdrawal");
        }

        if (checkWalletForWithdraw(withdrawRequest, wallet.getUsableBalance())) {
            throw new IllegalArgumentException("Insufficient usable balance for withdrawal");
        }
    }

    protected void validatePendingWithdraw(WithdrawRequest withdrawRequest, Wallet wallet) {
        if (checkWalletForWithdraw(withdrawRequest, wallet.getBalance())) {
            throw new IllegalArgumentException("Insufficient balance for withdrawal");
        }
    }

    private static boolean checkWalletForWithdraw(WithdrawRequest withdrawRequest, BigDecimal walletBalance) {
        return walletBalance.subtract(withdrawRequest.getAmount()).compareTo(new BigDecimal(0)) < 0;
    }

    private static void validateWithdrawPartyType(WithdrawRequest withdrawRequest, Wallet wallet) {
        if (withdrawRequest.getTransactionOppositePartyType().equals(TransactionOppositePartyType.IBAN)
            && !wallet.isActiveForWithdraw()) {
            throw new IllegalArgumentException("Wallet is not active for withdraw");
        }

        if (withdrawRequest.getTransactionOppositePartyType().equals(TransactionOppositePartyType.PAYMENT)
            && !wallet.isActiveForShopping()) {
            throw new IllegalArgumentException("Wallet is not active for payment");
        }
    }

    protected Transaction saveTransaction(Transact transact, TransactionType transactionType) {
        Wallet wallet = walletService.findWallet(transact.getWalletId());
        Transaction transaction = transactionMapper.mapToTransaction(transact, transactionType, wallet);
        return transactionalRepository.save(transaction);
    }

}
