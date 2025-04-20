package com.kurtay.wallet.domain.response;

import java.math.BigDecimal;

import com.kurtay.wallet.domain.enums.Currency;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SearchWalletResponse {

    private Long id;
    private String name;
    private String username;
    private Currency currency;
    private BigDecimal balance;
    private BigDecimal usableBalance;
    private boolean activeForWithdraw;
    private boolean activeForShopping;

}
