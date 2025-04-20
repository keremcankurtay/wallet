package com.kurtay.wallet.domain.response;

import java.math.BigDecimal;

import com.kurtay.wallet.domain.enums.Currency;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateWalletResponse {
    private Long walletId;
    private String walletName;
    private Currency currency;
    private boolean activeForWithdraw;
    private boolean activeForShopping;
    private BigDecimal balance;
    private BigDecimal usableBalance;
}
