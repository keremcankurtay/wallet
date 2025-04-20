package com.kurtay.wallet.domain.entity;

import com.kurtay.wallet.domain.authentication.Authenticatable;
import com.kurtay.wallet.domain.enums.Role;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "CUSTOMER")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Customer implements Authenticatable {

    @Id
    @Column(name = "ID", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "surname", nullable = false, length = 50)
    private String surname;

    @Column(name = "TCKN", nullable = false, unique = true, length = 11, updatable = false)
    private String tckn;

    @Column(name = "USERNAME", nullable = false, unique = true, length = 50)
    private String username;

    @Column(name = "PASSWORD", nullable = false, length = 60)
    private String password;

    @Override
    public Role getRole() {
        return Role.CUSTOMER;
    }
}
