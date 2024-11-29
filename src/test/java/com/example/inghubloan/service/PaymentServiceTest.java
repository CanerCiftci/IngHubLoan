package com.example.inghubloan.service;

import com.example.inghubloan.dto.response.PaymentResponse;
import com.example.inghubloan.entity.Loan;
import com.example.inghubloan.entity.LoanInstallment;
import com.example.inghubloan.repository.LoanRepository;
import com.example.inghubloan.service.payment.PaymentService;
import com.example.inghubloan.service.payment.impl.PaymentServiceImpl;
import jakarta.validation.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PaymentServiceTest {

    @Mock
    private LoanRepository loanRepository;

    @InjectMocks
    private PaymentServiceImpl paymentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void payLoan_ShouldProcessPaymentSuccessfully() {
        Loan loan = new Loan();
        loan.setId(1L);
        loan.setIsPaid(0);

        LoanInstallment installment1 = new LoanInstallment();
        installment1.setId(1L);
        installment1.setAmount(BigDecimal.valueOf(100));
        installment1.setIsPaid(0);
        installment1.setDueDate(LocalDate.now());

        LoanInstallment installment2 = new LoanInstallment();
        installment2.setId(2L);
        installment2.setAmount(BigDecimal.valueOf(150));
        installment2.setIsPaid(0);
        installment2.setDueDate(LocalDate.now().plusMonths(1));
        loan.setInstallments(List.of(installment1, installment2));

        when(loanRepository.findById(1L)).thenReturn(Optional.of(loan));

        PaymentResponse response = paymentService.payLoan(1L, BigDecimal.valueOf(250));
        assertNotNull(response);
        assertEquals(1L, response.getLoanId());
        assertEquals(2, response.getInstallmentsPaid());
        assertEquals(BigDecimal.valueOf(250), response.getTotalAmountPaid());
        assertTrue(response.isLoanFullyPaid());
        verify(loanRepository).save(any(Loan.class));
    }

    @Test
    void payLoan_ShouldThrowExceptionForInsufficientPaymentAmount() {
        Loan loan = new Loan();
        loan.setId(1L);
        loan.setIsPaid(0);
        LoanInstallment installment1 = new LoanInstallment();
        installment1.setId(1L);
        installment1.setAmount(BigDecimal.valueOf(100));
        installment1.setIsPaid(0);
        installment1.setDueDate(LocalDate.now());
        loan.setInstallments(List.of(installment1));
        when(loanRepository.findById(1L)).thenReturn(Optional.of(loan));
        PaymentResponse paymentResponse = paymentService.payLoan(1L, BigDecimal.valueOf(10));

        assertNotNull(paymentResponse.getValidationMessage());
        verify(loanRepository, never()).save(any(Loan.class));
    }

    @Test
    void payLoan_ShouldThrowExceptionIfLoanNotFound() {
        when(loanRepository.findById(1L)).thenReturn(Optional.empty());
        PaymentResponse paymentResponse = paymentService.payLoan(1L, BigDecimal.valueOf(100));

        assertNotNull(paymentResponse.getValidationMessage());
    }

    @Test
    void payLoan_ShouldThrowExceptionIfLoanAlreadyPaid() {
        Loan loan = new Loan();
        loan.setId(1L);
        loan.setIsPaid(1);

        when(loanRepository.findById(1L)).thenReturn(Optional.of(loan));
        PaymentResponse paymentResponse = paymentService.payLoan(1L, BigDecimal.valueOf(100));
        assertNotNull(paymentResponse.getValidationMessage());
    }

    @Test
    void payLoan_ShouldThrowExceptionIfNoInstallments() {
        Loan loan = new Loan();
        loan.setId(1L);
        loan.setIsPaid(0);
        loan.setInstallments(new ArrayList<>());
        when(loanRepository.findById(1L)).thenReturn(Optional.of(loan));
        PaymentResponse paymentResponse = paymentService.payLoan(1L, BigDecimal.valueOf(100));
        assertNotNull(paymentResponse.getValidationMessage());
    }
}
