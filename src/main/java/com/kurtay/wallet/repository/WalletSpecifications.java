package com.kurtay.wallet.repository;

import java.math.BigDecimal;

import org.springframework.data.jpa.domain.Specification;

import com.kurtay.wallet.domain.entity.Wallet;
import com.kurtay.wallet.domain.enums.Currency;

import jakarta.persistence.criteria.Join;
import lombok.experimental.UtilityClass;

@UtilityClass
public class WalletSpecifications {

    public static Specification<Wallet> tcknEquals(String tckn) {
        return (root, query, criteriaBuilder) -> {
            Join<Wallet, CustomerRepository> customer = root.join("customer");
            return criteriaBuilder.equal(customer.get("tckn"), tckn);
        };
    }

    public static Specification<Wallet> currencyEquals(Currency currency) {
        return (root, query, criteriaBuilder) -> currency != null ?
            criteriaBuilder.equal(root.get("currency"), currency) :
            criteriaBuilder.conjunction();
    }

    public static Specification<Wallet> balanceEquals(BigDecimal balance) {
        return (root, query, criteriaBuilder) -> balance != null ?
            criteriaBuilder.equal(root.get("balance"), balance) :
            criteriaBuilder.conjunction();
    }
}
