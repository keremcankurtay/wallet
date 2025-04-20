package com.kurtay.wallet.domain.request;

import com.kurtay.wallet.domain.enums.TransactionStatus;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TransactionApproval {

    @NotNull(message = "Transaction ID cannot be null")
    private Long transactionId;

    @NotNull(message = "Transaction status cannot be null")
    private TransactionStatus status;
}
