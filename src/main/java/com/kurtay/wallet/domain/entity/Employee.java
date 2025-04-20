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
@Table(name = "EMPLOYEE")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Employee implements Authenticatable {

    @Id
    @Column(name = "ID", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "USERNAME", nullable = false, unique = true, length = 50, updatable = false)
    private String username;

    @Column(name = "PASSWORD", nullable = false, length = 60, updatable = false)
    private String password;

    @Override
    public Role getRole() {
        return Role.EMPLOYEE;
    }
}
