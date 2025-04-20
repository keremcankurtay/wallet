package com.kurtay.wallet.domain.request;

import java.math.BigDecimal;

import com.kurtay.wallet.domain.enums.TransactionOppositePartyType;
import com.kurtay.wallet.domain.enums.TransactionStatus;

public interface Transact {

    Long getWalletId();

    BigDecimal getAmount();

    TransactionOppositePartyType getTransactionOppositePartyType();

    TransactionStatus getTransactionStatus();

}
