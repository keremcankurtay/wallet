package com.kurtay.wallet.domain.entity;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import org.hibernate.annotations.CreationTimestamp;

import com.kurtay.wallet.domain.enums.TransactionOppositePartyType;
import com.kurtay.wallet.domain.enums.TransactionStatus;
import com.kurtay.wallet.domain.enums.TransactionType;
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
@Table(name = "TRANSACTION")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {

    @Id
    @Column(name = "ID", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "WALLET_ID", referencedColumnName = "ID", nullable = false)
    private Wallet wallet;

    @Column(name = "AMOUNT", nullable = false)
    private BigDecimal amount;

    @Column(name = "TYPE", nullable = false)
    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

    @Column(name = "OPPOSITE_PARTY_TYPE", nullable = false)
    @Enumerated(EnumType.STRING)
    private TransactionOppositePartyType oppositePartyType;

    @Column(name = "STATUS", nullable = false)
    @Enumerated(EnumType.STRING)
    private TransactionStatus status;

    @Column(name = "CREATE_USER", nullable = false, length = 50)
    private String createUser;

    @CreationTimestamp
    @Column(name = "CREATE_DATE", nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private OffsetDateTime createDate;

    @PrePersist
    public void prePersist() {
        this.createUser = SecurityUtility.getUsername();
    }

}
