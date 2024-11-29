package com.example.inghubloan.service;

import com.example.inghubloan.dto.Installment;
import com.example.inghubloan.dto.ValidationDTO;
import com.example.inghubloan.dto.request.CreateLoanRequest;
import com.example.inghubloan.dto.response.CreateLoanResponse;
import com.example.inghubloan.entity.Customer;
import com.example.inghubloan.entity.Loan;
import com.example.inghubloan.entity.LoanInstallment;
import com.example.inghubloan.repository.CustomerRepository;
import com.example.inghubloan.repository.LoanRepository;
import com.example.inghubloan.service.loan.impl.LoanServiceImpl;
import com.example.inghubloan.validation.CustomerValidationHandler;
import com.example.inghubloan.validation.RateAndInstallmentNumberValidationHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LoanServiceTest {

    @Mock
    private LoanRepository loanRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private CustomerValidationHandler customerValidationHandler;

    @Mock
    private RateAndInstallmentNumberValidationHandler rateAndInstallmentValidationHandler;

    @InjectMocks
    private LoanServiceImpl loanService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createLoan_ShouldCreateLoanSuccessfully() {
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setUsedCreditLimit(BigDecimal.ZERO);
        ValidationDTO validationDTO = new ValidationDTO();
        validationDTO.setValid(false);
        validationDTO.setValidationMessage("fail");
        validationDTO.setCustomer(customer);
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(rateAndInstallmentValidationHandler.validate(any())).thenReturn(validationDTO);

        Loan savedLoan = new Loan();
        savedLoan.setId(1L);
        savedLoan.setLoanAmount(BigDecimal.valueOf(1100));
        when(loanRepository.save(any(Loan.class))).thenReturn(savedLoan);

        CreateLoanRequest request = new CreateLoanRequest();
        request.setCustomerId(1L);
        request.setAmount(BigDecimal.valueOf(1000));
        request.setInterestRate(BigDecimal.valueOf(0.1));
        request.setNumberOfInstallments(10);

        CreateLoanResponse response = loanService.createLoan(request);

        assertNotNull(response);
        verify(customerRepository).findById(1L);
    }

    @Test
    void createLoan_ShouldReturnInvalidWhenCustomerNotFound() {
        when(customerRepository.findById(1L)).thenReturn(Optional.empty());

        CreateLoanRequest request = new CreateLoanRequest();
        request.setCustomerId(1L);
        request.setAmount(BigDecimal.valueOf(1000));
        request.setInterestRate(BigDecimal.valueOf(0.1));
        request.setNumberOfInstallments(10);

        CreateLoanResponse response = loanService.createLoan(request);

        assertNotNull(response);
        assertFalse(response.isValid());
    }

    @Test
    void createLoan_ShouldReturnInvalidWhenValidationFails() {
        Customer customer = new Customer();
        customer.setId(1L);

        ValidationDTO validationDTO = new ValidationDTO();
        validationDTO.setValid(false);
        validationDTO.setValidationMessage("fail");
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(rateAndInstallmentValidationHandler.validate(any()))
                .thenReturn(validationDTO);

        CreateLoanRequest request = new CreateLoanRequest();
        request.setCustomerId(1L);
        request.setAmount(BigDecimal.valueOf(1000));
        request.setInterestRate(BigDecimal.valueOf(0.1));
        request.setNumberOfInstallments(10);
        CreateLoanResponse response = loanService.createLoan(request);

        assertNotNull(response);
        assertFalse(response.isValid());
        assertEquals("fail", response.getValidationMessage());

        verify(loanRepository, never()).save(any(Loan.class));
    }

    @Test
    void listInstallments_ShouldReturnInstallmentsSuccessfully() {
        Loan loan = new Loan();
        loan.setId(1L);

        LoanInstallment installment1 = new LoanInstallment();
        installment1.setId(1L);
        installment1.setAmount(BigDecimal.valueOf(100));
        installment1.setPaidAmount(BigDecimal.ZERO);
        installment1.setDueDate(LocalDate.now());
        loan.setInstallments(List.of(installment1));

        when(loanRepository.findByCustomerId(1L)).thenReturn(List.of(loan));
        List<Installment> installments = loanService.listInstallments(1L);

        assertNotNull(installments);
        assertEquals(1, installments.size());
        assertEquals(1L, installments.get(0).getInstallmentId());
        assertEquals(BigDecimal.valueOf(100), installments.get(0).getAmount());

        verify(loanRepository).findByCustomerId(1L);
    }

    @Test
    void listInstallments_ShouldReturnEmptyListIfNoLoansFound() {
        when(loanRepository.findByCustomerId(1L)).thenReturn(List.of());
        List<Installment> installments = loanService.listInstallments(1L);
        assertNotNull(installments);
        assertTrue(installments.isEmpty());

        verify(loanRepository).findByCustomerId(1L);
    }
}
