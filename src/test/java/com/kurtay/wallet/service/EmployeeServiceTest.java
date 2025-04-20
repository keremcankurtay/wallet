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
import com.kurtay.wallet.domain.entity.Employee;
import com.kurtay.wallet.domain.request.CreateEmployeeRequest;
import com.kurtay.wallet.mapper.EmployeeMapper;
import com.kurtay.wallet.repository.EmployeeRepository;

import jakarta.validation.ValidationException;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @InjectMocks
    private EmployeeService employeeService;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private EmployeeMapper employeeMapper;

    @Test
    void createEmployee_ShouldSaveAndReturnEmployee_WhenRequestIsValid() {
        CreateEmployeeRequest request =
            CreateEmployeeRequest.builder().username("username").password("password").build();
        Employee employee = new Employee();
        when(employeeMapper.mapToEmployee(request)).thenReturn(employee);
        when(employeeRepository.save(employee)).thenReturn(employee);

        Employee result = employeeService.createEmployee(request);

        assertEquals(employee, result);
        verify(employeeMapper).mapToEmployee(request);
        verify(employeeRepository).save(employee);
    }

    @Test
    void createEmployee_ShouldThrowException_WhenRequestIsInvalid() {
        CreateEmployeeRequest request = new CreateEmployeeRequest();

        assertThrows(ValidationException.class, () -> employeeService.createEmployee(request));
        verifyNoInteractions(employeeMapper);
        verifyNoInteractions(employeeRepository);
    }

    @Test
    void findByUsername_ShouldReturnAuthenticatable_WhenUsernameExists() {
        String username = "testUser";
        Employee employee = new Employee();
        when(employeeRepository.findByUsername(username)).thenReturn(Optional.of(employee));

        Optional<Authenticatable> result = employeeService.findByUsername(username);

        assertTrue(result.isPresent());
        assertEquals(employee, result.get());
        verify(employeeRepository).findByUsername(username);
    }

    @Test
    void findByUsername_ShouldReturnEmptyOptional_WhenUsernameDoesNotExist() {
        String username = "testUser";
        when(employeeRepository.findByUsername(username)).thenReturn(Optional.empty());

        Optional<Authenticatable> result = employeeService.findByUsername(username);

        assertTrue(result.isEmpty());
        verify(employeeRepository).findByUsername(username);
    }

}