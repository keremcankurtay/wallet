package com.kurtay.wallet.domain.response;

import com.kurtay.wallet.domain.enums.TransactionStatus;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TransactionResponse {
    private Long transactionId;
    private TransactionStatus status;
}
