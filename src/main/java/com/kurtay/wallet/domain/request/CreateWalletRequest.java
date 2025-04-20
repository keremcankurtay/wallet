package com.kurtay.wallet.domain.request;

import java.math.BigDecimal;

import com.kurtay.wallet.domain.enums.Currency;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateWalletRequest {

    @NotEmpty(message = "Wallet name cannot be empty")
    @Size(min = 5, max = 50, message = "Wallet name must be between 5 and 50 characters")
    private String walletName;

    @NotNull(message = "Wallet currency cannot be null")
    private Currency currency;

    @NotNull(message = "Wallet initial balance cannot be null")
    private BigDecimal initialBalance;

    @NotNull(message = "Customer id cannot be null")
    private Long customerId;

    private boolean activeForWithdraw;

    private boolean activeForShopping;

}
