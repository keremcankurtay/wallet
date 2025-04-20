package com.kurtay.wallet.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kurtay.wallet.domain.entity.Transaction;
import com.kurtay.wallet.domain.enums.TransactionStatus;
import com.kurtay.wallet.domain.enums.TransactionType;

public interface TransactionalRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByWalletIdOrderByCreateDateDesc(Long walletId);

    Optional<Transaction> findByIdAndStatusAndTransactionType(Long id, TransactionStatus status, TransactionType transactionType);
}
