package com.kurtay.wallet.mapper;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.kurtay.wallet.domain.entity.Employee;
import com.kurtay.wallet.domain.request.CreateEmployeeRequest;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class EmployeeMapper {

    private final PasswordEncoder passwordEncoder;

    public Employee mapToEmployee(CreateEmployeeRequest createEmployeeRequest) {
        return Employee.builder()
            .username(createEmployeeRequest.getUsername())
            .password(passwordEncoder.encode(createEmployeeRequest.getPassword()))
            .build();
    }
}
