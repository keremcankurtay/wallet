package com.kurtay.wallet.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kurtay.wallet.domain.authentication.Authenticatable;
import com.kurtay.wallet.domain.entity.Customer;
import com.kurtay.wallet.domain.request.CreateCustomerRequest;
import com.kurtay.wallet.mapper.CustomerMapper;
import com.kurtay.wallet.repository.CustomerRepository;
import com.kurtay.wallet.util.ValidationUtility;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomerService implements Authenticator {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    @Override
    public Optional<Authenticatable> findByUsername(String username) {
        Optional<Customer> customer = customerRepository.findByUsername(username);

        return customer.map(Authenticatable.class::cast);
    }

    @Transactional(readOnly = true)
    public Customer findCustomer(Long customerId) {
        return customerRepository.findById(customerId)
            .orElseThrow(() -> new IllegalArgumentException("Customer not found with id: " + customerId));
    }

    @Transactional
    public Customer createCustomer(CreateCustomerRequest createCustomerRequest) {
        ValidationUtility.validate(createCustomerRequest);
        Customer customer = customerMapper.mapToCustomer(createCustomerRequest);
        return customerRepository.save(customer);
    }
}
