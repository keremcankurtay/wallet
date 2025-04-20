package com.kurtay.wallet.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.kurtay.wallet.domain.authentication.Authenticatable;
import com.kurtay.wallet.domain.entity.Customer;
import com.kurtay.wallet.domain.request.CreateCustomerRequest;
import com.kurtay.wallet.mapper.CustomerMapper;
import com.kurtay.wallet.repository.CustomerRepository;

import jakarta.validation.ValidationException;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @InjectMocks
    private CustomerService customerService;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private CustomerMapper customerMapper;

    @Test
    void findCustomer_ShouldReturnCustomer_WhenCustomerExists() {
        Long customerId = 1L;
        Customer customer = new Customer();
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));

        Customer result = customerService.findCustomer(customerId);

        assertEquals(customer, result);
        verify(customerRepository).findById(customerId);
    }

    @Test
    void findCustomer_ShouldThrowException_WhenCustomerDoesNotExist() {
        Long customerId = 1L;
        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());

        IllegalArgumentException exception =
            assertThrows(IllegalArgumentException.class, () -> customerService.findCustomer(customerId));
        assertEquals("Customer not found with id: " + customerId, exception.getMessage());
        verify(customerRepository).findById(customerId);
    }

    @Test
    void createCustomer_ShouldSaveAndReturnCustomer_WhenRequestIsValid() {
        CreateCustomerRequest request = CreateCustomerRequest.builder()
            .tckn("12345678901")
            .name("ali")
            .surname("veli")
            .username("username")
            .password("pass")
            .build();
        Customer customer = new Customer();
        when(customerMapper.mapToCustomer(request)).thenReturn(customer);
        when(customerRepository.save(customer)).thenReturn(customer);

        Customer result = customerService.createCustomer(request);

        assertEquals(customer, result);
        verify(customerMapper).mapToCustomer(request);
        verify(customerRepository).save(customer);
    }

    @Test
    void createCustomer_ShouldThrowException_WhenRequestIsInvalid() {
        CreateCustomerRequest request = new CreateCustomerRequest();

        assertThrows(ValidationException.class, () -> customerService.createCustomer(request));
        verifyNoInteractions(customerMapper);
        verifyNoInteractions(customerRepository);
    }

    @Test
    void findByUsername_ShouldReturnAuthenticatable_WhenUsernameExists() {
        String username = "testUser";
        Customer customer = new Customer();
        when(customerRepository.findByUsername(username)).thenReturn(Optional.of(customer));

        Optional<Authenticatable> result = customerService.findByUsername(username);

        assertTrue(result.isPresent());
        assertEquals(customer, result.get());
        verify(customerRepository).findByUsername(username);
    }

    @Test
    void findByUsername_ShouldReturnEmptyOptional_WhenUsernameDoesNotExist() {
        String username = "testUser";
        when(customerRepository.findByUsername(username)).thenReturn(Optional.empty());

        Optional<Authenticatable> result = customerService.findByUsername(username);

        assertTrue(result.isEmpty());
        verify(customerRepository).findByUsername(username);
    }
}