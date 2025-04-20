package com.kurtay.wallet.domain.response;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import com.kurtay.wallet.domain.enums.TransactionOppositePartyType;
import com.kurtay.wallet.domain.enums.TransactionStatus;
import com.kurtay.wallet.domain.enums.TransactionType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionListResponse {
    private Long id;
    private Long walletId;
    private BigDecimal amount;
    private TransactionType transactionType;
    private TransactionOppositePartyType oppositePartyType;
    private TransactionStatus status;
    private String createUser;
    private OffsetDateTime createDate;
}
