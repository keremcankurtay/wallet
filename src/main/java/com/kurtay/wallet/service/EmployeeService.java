package com.kurtay.wallet.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kurtay.wallet.domain.authentication.Authenticatable;
import com.kurtay.wallet.domain.entity.Employee;
import com.kurtay.wallet.domain.request.CreateEmployeeRequest;
import com.kurtay.wallet.mapper.EmployeeMapper;
import com.kurtay.wallet.repository.EmployeeRepository;
import com.kurtay.wallet.util.ValidationUtility;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmployeeService implements Authenticator {

    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;

    @Override
    public Optional<Authenticatable> findByUsername(String username) {
        Optional<Employee> employee = employeeRepository.findByUsername(username);

        return employee.map(Authenticatable.class::cast);
    }

    @Transactional
    public Employee createEmployee(CreateEmployeeRequest createEmployeeRequest) {
        ValidationUtility.validate(createEmployeeRequest);
        Employee employee = employeeMapper.mapToEmployee(createEmployeeRequest);
        return employeeRepository.save(employee);
    }
}
