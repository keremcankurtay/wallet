package com.kurtay.wallet.domain.entity;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.kurtay.wallet.domain.enums.Currency;
import com.kurtay.wallet.util.SecurityUtility;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "WALLET")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Wallet {

    @Id
    @Column(name = "ID", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "NAME", nullable = false, unique = true, length = 50, updatable = false)
    private String name;

    @Column(name = "CURRENCY", nullable = false, length = 3, updatable = false)
    @Enumerated(EnumType.STRING)
    private Currency currency;

    @Column(name = "BALANCE", nullable = false)
    private BigDecimal balance;

    @Column(name = "USABLE_BALANCE", nullable = false)
    private BigDecimal usableBalance;

    @Column(name = "ACTIVE_FOR_WITHDRAW", nullable = false)
    private boolean activeForWithdraw;

    @Column(name = "ACTIVE_FOR_SHOPPING", nullable = false)
    private boolean activeForShopping;

    @ManyToOne
    @JoinColumn(name = "CUSTOMER_ID", referencedColumnName = "ID", nullable = false)
    private Customer customer;

    @Column(name = "CREATE_USER", nullable = false, length = 50)
    private String createUser;

    @CreationTimestamp
    @Column(name = "CREATE_DATE", nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private OffsetDateTime createDate;

    @Column(name = "UPDATE_USER", nullable = false, length = 50)
    private String updateUser;

    @UpdateTimestamp
    @Column(name = "UPDATE_DATE", nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private OffsetDateTime updateDate;

    @PrePersist
    public void prePersist() {
        this.createUser = SecurityUtility.getUsername();
        this.updateUser = SecurityUtility.getUsername();
    }

}
