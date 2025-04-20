package com.kurtay.wallet.domain.request;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kurtay.wallet.domain.enums.TransactionOppositePartyType;
import com.kurtay.wallet.domain.enums.TransactionStatus;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class WithdrawRequest implements Transact {

    @NotNull(message = "Wallet ID cannot be null")
    private Long walletId;

    @NotNull(message = "Wallet amount cannot be null")
    private BigDecimal amount;

    @NotNull(message = "Destination cannot be null")
    private TransactionOppositePartyType destination;

    @JsonIgnore
    public TransactionStatus getTransactionStatus() {
        return amount.compareTo(new BigDecimal(1000)) >= 0 ? TransactionStatus.PENDING : TransactionStatus.APPROVED;
    }

    @Override
    public TransactionOppositePartyType getTransactionOppositePartyType() {
        return destination;
    }
}
