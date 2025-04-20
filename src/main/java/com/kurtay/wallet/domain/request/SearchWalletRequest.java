package com.kurtay.wallet.domain.request;

import java.math.BigDecimal;

import com.kurtay.wallet.domain.enums.Currency;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SearchWalletRequest {

    @NotNull(message = "TCKN cannot be null")
    private String tckn;
    private Currency currency;
    private BigDecimal balance;

}
