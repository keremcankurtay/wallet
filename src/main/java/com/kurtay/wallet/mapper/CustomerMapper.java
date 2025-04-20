package com.kurtay.wallet.mapper;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.kurtay.wallet.domain.entity.Customer;
import com.kurtay.wallet.domain.request.CreateCustomerRequest;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CustomerMapper {

    private final PasswordEncoder passwordEncoder;

    public Customer mapToCustomer(CreateCustomerRequest createCustomerRequest) {
        return Customer.builder()
            .name(createCustomerRequest.getUsername())
            .password(passwordEncoder.encode(createCustomerRequest.getPassword()))
            .surname(createCustomerRequest.getSurname())
            .tckn(createCustomerRequest.getTckn())
            .username(createCustomerRequest.getUsername())
            .build();
    }
}
