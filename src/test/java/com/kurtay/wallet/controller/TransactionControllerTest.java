package com.kurtay.wallet.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.kurtay.wallet.controller.utility.UnitTestUtil;
import com.kurtay.wallet.domain.enums.TransactionOppositePartyType;
import com.kurtay.wallet.domain.enums.TransactionStatus;
import com.kurtay.wallet.domain.request.DepositRequest;
import com.kurtay.wallet.domain.request.TransactionApproval;
import com.kurtay.wallet.domain.request.WithdrawRequest;
import com.kurtay.wallet.domain.response.TransactionResponse;
import com.kurtay.wallet.service.TransactionService;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;

@ExtendWith(MockitoExtension.class)
class TransactionControllerTest {

    @InjectMocks
    private TransactionController transactionController;

    @Mock
    private TransactionService transactionService;

    @Test
    void deposit_ShouldSaveDepositToTransactions() {
        ListAppender<ILoggingEvent> eventListAppender = UnitTestUtil.startLoggerFor(TransactionController.class);

        DepositRequest depositRequest = DepositRequest.builder()
            .walletId(1L)
            .amount(new BigDecimal(10))
            .source(TransactionOppositePartyType.IBAN)
            .build();

        when(transactionService.deposit(depositRequest)).thenReturn(
            TransactionResponse.builder().transactionId(1L).status(TransactionStatus.APPROVED).build());

        ResponseEntity<TransactionResponse> deposit = transactionController.deposit(depositRequest);

        assertNotNull(deposit);
        assertEquals(HttpStatus.CREATED, deposit.getStatusCode());

        TransactionResponse transactionResponse = deposit.getBody();

        assertNotNull(transactionResponse);
        assertEquals(1L, transactionResponse.getTransactionId());
        assertEquals(TransactionStatus.APPROVED, transactionResponse.getStatus());

        List<ILoggingEvent> loggingEvents = eventListAppender.list;
        assertNotNull(loggingEvents);
        assertEquals(1, loggingEvents.size());
        assertEquals("Deposit request: DepositRequest(walletId=1, amount=10, source=IBAN)",
            loggingEvents.get(0).getFormattedMessage());

    }

    @Test
    void withdraw_ShouldSaveWithdrawToTransactions() {
        ListAppender<ILoggingEvent> eventListAppender = UnitTestUtil.startLoggerFor(TransactionController.class);

        WithdrawRequest withdrawRequest = WithdrawRequest.builder()
            .walletId(1L)
            .amount(new BigDecimal(10))
            .destination(TransactionOppositePartyType.IBAN)
            .build();

        when(transactionService.withdraw(withdrawRequest)).thenReturn(
            TransactionResponse.builder().transactionId(1L).status(TransactionStatus.APPROVED).build());

        ResponseEntity<TransactionResponse> deposit = transactionController.withdraw(withdrawRequest);

        assertNotNull(deposit);
        assertEquals(HttpStatus.CREATED, deposit.getStatusCode());

        TransactionResponse transactionResponse = deposit.getBody();

        assertNotNull(transactionResponse);
        assertEquals(1L, transactionResponse.getTransactionId());
        assertEquals(TransactionStatus.APPROVED, transactionResponse.getStatus());

        List<ILoggingEvent> loggingEvents = eventListAppender.list;
        assertNotNull(loggingEvents);
        assertEquals(1, loggingEvents.size());
        assertEquals("Withdraw request: WithdrawRequest(walletId=1, amount=10, destination=IBAN)",
            loggingEvents.get(0).getFormattedMessage());
    }

    @Test
    void updateDepositStatus_ShouldUpdateDepositStatus() {
        ListAppender<ILoggingEvent> eventListAppender = UnitTestUtil.startLoggerFor(TransactionController.class);

        TransactionApproval transactionApproval =
            TransactionApproval.builder().transactionId(1L).status(TransactionStatus.APPROVED).build();

        when(transactionService.updateDepositStatus(transactionApproval)).thenReturn(
            TransactionResponse.builder().transactionId(1L).status(TransactionStatus.APPROVED).build());

        ResponseEntity<TransactionResponse> response = transactionController.updateDepositStatus(transactionApproval);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        TransactionResponse transactionResponse = response.getBody();

        assertNotNull(transactionResponse);
        assertEquals(1L, transactionResponse.getTransactionId());
        assertEquals(TransactionStatus.APPROVED, transactionResponse.getStatus());

        List<ILoggingEvent> loggingEvents = eventListAppender.list;
        assertNotNull(loggingEvents);
        assertEquals(1, loggingEvents.size());
        assertEquals("Deposit status update request: TransactionApproval(transactionId=1, status=APPROVED)",
            loggingEvents.get(0).getFormattedMessage());
    }

    @Test
    void updateWithdrawStatus_ShouldUpdateWithdrawStatus() {
        ListAppender<ILoggingEvent> eventListAppender = UnitTestUtil.startLoggerFor(TransactionController.class);

        TransactionApproval transactionApproval =
            TransactionApproval.builder().transactionId(1L).status(TransactionStatus.APPROVED).build();

        when(transactionService.updateWithdrawStatus(transactionApproval)).thenReturn(
            TransactionResponse.builder().transactionId(1L).status(TransactionStatus.APPROVED).build());

        ResponseEntity<TransactionResponse> response = transactionController.updateWithdrawStatus(transactionApproval);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        TransactionResponse transactionResponse = response.getBody();

        assertNotNull(transactionResponse);
        assertEquals(1L, transactionResponse.getTransactionId());
        assertEquals(TransactionStatus.APPROVED, transactionResponse.getStatus());

        List<ILoggingEvent> loggingEvents = eventListAppender.list;
        assertNotNull(loggingEvents);
        assertEquals(1, loggingEvents.size());
        assertEquals("Withdraw status update request: TransactionApproval(transactionId=1, status=APPROVED)",
            loggingEvents.get(0).getFormattedMessage());
    }

}