package com.kurtay.wallet.listener;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.kurtay.wallet.domain.entity.Customer;
import com.kurtay.wallet.domain.entity.Employee;
import com.kurtay.wallet.domain.request.CreateCustomerRequest;
import com.kurtay.wallet.domain.request.CreateEmployeeRequest;
import com.kurtay.wallet.service.CustomerService;
import com.kurtay.wallet.service.EmployeeService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserCreator {
    private final CustomerService customerService;
    private final EmployeeService employeeService;

    @EventListener(ApplicationReadyEvent.class)
    public void run() {
        CreateEmployeeRequest employee = createEmployee();
        Employee createdEmployee = employeeService.createEmployee(employee);
        log.info("Employee created: {}", createdEmployee);

        CreateCustomerRequest customer = createCustomer("customer1", "12345678901");
        Customer createdCustomer = customerService.createCustomer(customer);
        log.info("Customer created: {}", createdCustomer);

        CreateCustomerRequest customer2 = createCustomer("customer2", "12345678902");
        Customer createdCustomer2 = customerService.createCustomer(customer2);
        log.info("Customer created: {}", createdCustomer2);
    }

    protected CreateEmployeeRequest createEmployee() {
        return CreateEmployeeRequest.builder().username("employee1").password("password1").build();
    }

    protected CreateCustomerRequest createCustomer(String username, String tckn) {
        return CreateCustomerRequest.builder()
            .username(username)
            .password("password1")
            .name("Ali")
            .surname("Veli")
            .tckn(tckn)
            .build();
    }
}
