package com.kurtay.wallet.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @InjectMocks
    private TransactionService transactionService;

    @Mock
    private TransactionalRepository transactionalRepository;

    @Mock
    private TransactionMapper transactionMapper;

    @Mock
    private WalletService walletService;

    @Mock
    private WalletGuard walletGuard;

    @Test
    void getTransactionList_ShouldReturnTransactionList_WhenWalletIdIsValid() {
        Long walletId = 1L;
        List<Transaction> transactions = List.of(new Transaction());
        List<TransactionListResponse> response = List.of(new TransactionListResponse());

        when(transactionalRepository.findByWalletIdOrderByCreateDateDesc(walletId)).thenReturn(transactions);
        when(transactionMapper.mapToTransactionListResponse(transactions)).thenReturn(response);

        List<TransactionListResponse> result = transactionService.getTransactionList(walletId);

        assertEquals(response, result);
        verify(walletGuard).checkUserHasAccessToWallet(walletId);
        verify(transactionalRepository).findByWalletIdOrderByCreateDateDesc(walletId);
        verify(transactionMapper).mapToTransactionListResponse(transactions);
    }

    @Test
    void deposit_ShouldReturnTransactionResponse_WhenDepositRequestIsValid() {
        DepositRequest depositRequest = DepositRequest.builder().walletId(1L).amount(new BigDecimal(5)).build();
        Transaction transaction = Transaction.builder().id(1L).build();

        Wallet wallet = mock(Wallet.class);
        when(walletService.findWallet(1L)).thenReturn(wallet);

        when(
            transactionMapper.mapToTransaction(any(Transact.class), any(TransactionType.class), eq(wallet))).thenReturn(
            transaction);
        when(transactionalRepository.save(transaction)).thenReturn(transaction);

        TransactionResponse result = transactionService.deposit(depositRequest);

        assertEquals(transaction.getId(), result.getTransactionId());
        assertEquals(TransactionStatus.APPROVED, result.getStatus());
        verify(walletGuard).checkUserHasAccessToWallet(depositRequest.getWalletId());
        verify(walletService).depositApprovedAmount(depositRequest);
    }

    @Test
    void updateDepositStatus_ShouldThrowException_WhenTransactionNotFound() {
        TransactionApproval transactionApproval =
            TransactionApproval.builder().transactionId(1L).status(TransactionStatus.APPROVED).build();

        when(transactionalRepository.findByIdAndStatusAndTransactionType(transactionApproval.getTransactionId(),
            TransactionStatus.PENDING, TransactionType.DEPOSIT)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
            () -> transactionService.updateDepositStatus(transactionApproval));

        assertEquals("Pending transaction not found", exception.getMessage());
        verify(transactionalRepository).findByIdAndStatusAndTransactionType(transactionApproval.getTransactionId(),
            TransactionStatus.PENDING, TransactionType.DEPOSIT);
    }

    @Test
    void withdraw_ShouldThrowException_WhenInsufficientBalance() {
        WithdrawRequest withdrawRequest = WithdrawRequest.builder()
            .walletId(1L)
            .amount(new BigDecimal("1000"))
            .destination(TransactionOppositePartyType.IBAN)
            .build();

        Wallet wallet = Wallet.builder().balance(new BigDecimal(500)).activeForWithdraw(true).build();

        when(walletService.findWallet(withdrawRequest.getWalletId())).thenReturn(wallet);

        IllegalArgumentException exception =
            assertThrows(IllegalArgumentException.class, () -> transactionService.withdraw(withdrawRequest));

        assertEquals("Insufficient balance for withdrawal", exception.getMessage());
        verify(walletGuard).checkUserHasAccessToWallet(withdrawRequest.getWalletId());
        verify(walletService).findWallet(withdrawRequest.getWalletId());
    }

}