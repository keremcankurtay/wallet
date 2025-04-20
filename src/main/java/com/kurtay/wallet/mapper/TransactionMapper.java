package com.kurtay.wallet.mapper;

import java.util.List;

import org.springframework.stereotype.Component;

import com.kurtay.wallet.domain.entity.Transaction;
import com.kurtay.wallet.domain.entity.Wallet;
import com.kurtay.wallet.domain.enums.TransactionType;
import com.kurtay.wallet.domain.request.Transact;
import com.kurtay.wallet.domain.response.TransactionListResponse;

@Component
public class TransactionMapper {

    public Transaction mapToTransaction(Transact transact, TransactionType transactionType, Wallet wallet) {
        return Transaction.builder()
            .transactionType(transactionType)
            .amount(transact.getAmount())
            .oppositePartyType(transact.getTransactionOppositePartyType())
            .status(transact.getTransactionStatus())
            .wallet(wallet)
            .build();
    }

    public List<TransactionListResponse> mapToTransactionListResponse(List<Transaction> transactions) {
        return transactions.stream()
            .map(transaction -> TransactionListResponse.builder()
                .id(transaction.getId())
                .walletId(transaction.getWallet().getId())
                .transactionType(transaction.getTransactionType())
                .amount(transaction.getAmount())
                .oppositePartyType(transaction.getOppositePartyType())
                .status(transaction.getStatus())
                .createDate(transaction.getCreateDate())
                .createUser(transaction.getCreateUser())
                .build())
            .toList();
    }
}
