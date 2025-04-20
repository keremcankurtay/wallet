package com.kurtay.wallet.domain.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WalletDto {
    private Long id;
    private BigDecimal balance;
    private BigDecimal usableBalance;
    private boolean activeForWithdraw;
    private boolean activeForShopping;
}
