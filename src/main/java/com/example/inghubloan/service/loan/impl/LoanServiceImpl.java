package com.example.inghubloan.service.loan.impl;

import com.example.inghubloan.dto.Installment;
import com.example.inghubloan.dto.ValidationDTO;
import com.example.inghubloan.dto.request.CreateLoanRequest;
import com.example.inghubloan.dto.response.CreateLoanResponse;
import com.example.inghubloan.entity.Customer;
import com.example.inghubloan.entity.Loan;
import com.example.inghubloan.entity.LoanInstallment;
import com.example.inghubloan.repository.CustomerRepository;
import com.example.inghubloan.repository.LoanRepository;
import com.example.inghubloan.service.loan.LoanService;
import com.example.inghubloan.validation.CustomerValidationHandler;
import com.example.inghubloan.validation.RateAndInstallmentNumberValidationHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LoanServiceImpl implements LoanService {

    private final LoanRepository loanRepository;
    private final CustomerValidationHandler customerValidationHandler;
    private final RateAndInstallmentNumberValidationHandler rateAndInstallmentValidationHandler;
    private final CustomerRepository customerRepository;

    public CreateLoanResponse createLoan(CreateLoanRequest request) {
        CreateLoanResponse response = new CreateLoanResponse();
        try {
            Customer customer = getCustomerByCustomerId(request.getCustomerId());
            BigDecimal totalLoanAmount = calculateTotalAmountWithInterestRate(request.getAmount(), request.getInterestRate());
            ValidationDTO validationResult = doChainValidations(request, totalLoanAmount, customer);
            if (!validationResult.isValid()) {
                response.setValidationMessage(validationResult.getValidationMessage());
                response.setValid(false);
                return response;
            }

            customer.setUsedCreditLimit(customer.getUsedCreditLimit().add(totalLoanAmount));
            Loan loan = initializeLoan(request.getNumberOfInstallments(), customer, totalLoanAmount);
            loanRepository.save(loan);
            setResponseFields(response, loan, totalLoanAmount);
        } catch (Exception e) {
            response.setValidationMessage("Loan creation failed: " + e.getMessage());
            response.setValid(false);
        }
        return response;
    }

    private static void setResponseFields(CreateLoanResponse response, Loan loan, BigDecimal totalLoanAmount) {
        response.setLoanId(loan.getId());
        response.setValidationMessage("Loan created successfully.");
        response.setValid(true);
        response.setLoanAmount(totalLoanAmount);
    }

    private ValidationDTO doChainValidations(CreateLoanRequest request, BigDecimal totalLoanAmount, Customer customer) {
        ValidationDTO validationDTO = mapValidationDTO(request, customer, totalLoanAmount);
        rateAndInstallmentValidationHandler.setNextValidation(customerValidationHandler);
        return rateAndInstallmentValidationHandler.validate(validationDTO);
    }

    private ValidationDTO mapValidationDTO(CreateLoanRequest request, Customer customer, BigDecimal totalLoanAmount) {
        ValidationDTO validationDTO = new ValidationDTO();
        validationDTO.setAmount(request.getAmount());
        validationDTO.setInterestRate(request.getInterestRate());
        validationDTO.setNumberOfInstallments(request.getNumberOfInstallments());
        validationDTO.setTotalAmount(totalLoanAmount);
        validationDTO.setCustomer(customer);
        return validationDTO;
    }

    private Loan initializeLoan(int numberOfInstallments, Customer customer, BigDecimal totalLoanAmount) {
        Loan loan = new Loan();
        loan.setCustomer(customer);
        loan.setLoanAmount(totalLoanAmount);
        loan.setNumberOfInstallments(numberOfInstallments);
        loan.setCreateDate(LocalDate.now());
        loan.setIsPaid(0);
        List<LoanInstallment> installments = calculateInstallments(loan, numberOfInstallments, totalLoanAmount);
        loan.setInstallments(installments);
        return loan;
    }

    private static BigDecimal calculateTotalAmountWithInterestRate(BigDecimal amount, BigDecimal interestRate) {
        return amount.multiply(BigDecimal.ONE.add(interestRate)).setScale(2, RoundingMode.HALF_UP);
    }

    private Customer getCustomerByCustomerId(Long customerId) {
        return customerRepository.findById(customerId).orElse(null);
    }

    private List<LoanInstallment> calculateInstallments(Loan loan, int numberOfInstallments, BigDecimal totalLoanAmount) {
        List<LoanInstallment> installmentList = new ArrayList<>();

        BigDecimal installmentAmount = calculateInstallmentAmount(numberOfInstallments, totalLoanAmount);
        LocalDate currentDueDate = getCurrentDueDate();

        for (int installmentCounter = 1; installmentCounter <= numberOfInstallments; installmentCounter++) {
            LoanInstallment installment = new LoanInstallment();
            installment.setLoan(loan);

            if (installmentCounter == numberOfInstallments) {
                installment.setAmount(getLastInstallmentAmount(numberOfInstallments, totalLoanAmount, installmentAmount));
            } else {
                installment.setAmount(installmentAmount);
            }

            installment.setPaidAmount(BigDecimal.ZERO);
            installment.setDueDate(currentDueDate);
            installment.setIsPaid(0);
            installmentList.add(installment);

            currentDueDate = getNextDueDate(currentDueDate);
        }

        return installmentList;
    }

    private static BigDecimal getLastInstallmentAmount(int numberOfInstallments, BigDecimal totalLoanAmount, BigDecimal installmentAmount) {
        BigDecimal lastInstallmentAmount = totalLoanAmount.subtract(installmentAmount.multiply(BigDecimal.valueOf(numberOfInstallments - 1)));
        lastInstallmentAmount = lastInstallmentAmount.setScale(2, RoundingMode.HALF_UP);
        return lastInstallmentAmount;
    }

    private static BigDecimal calculateInstallmentAmount(int numberOfInstallments, BigDecimal totalLoanAmount) {
        return totalLoanAmount.divide(BigDecimal.valueOf(numberOfInstallments), 2, RoundingMode.HALF_UP);
    }

    private static LocalDate getNextDueDate(LocalDate currentDueDate) {
        return currentDueDate.plusMonths(1);
    }

    private static LocalDate getCurrentDueDate() {
        return LocalDate.now().plusMonths(1).withDayOfMonth(1);
    }

    public List<Installment> listInstallments(Long customerId) {
        try {
            List<Installment> installmentList = new ArrayList<>();
            List<Loan> loanList = loanRepository.findByCustomerId(customerId);
            if (!CollectionUtils.isEmpty(loanList)){
                for (Loan loan : loanList) {
                    installmentList.addAll(
                            loan.getInstallments().stream().map(loanInstallment -> {
                                Installment installment = new Installment();
                                installment.setInstallmentId(loanInstallment.getId());
                                installment.setLoanId(loan.getId());
                                installment.setAmount(loanInstallment.getAmount());
                                installment.setPaidAmount(loanInstallment.getPaidAmount());
                                installment.setDueDate(loanInstallment.getDueDate());
                                installment.setPaymentDate(loanInstallment.getPaymentDate());
                                installment.setIsPaid(loanInstallment.getIsPaid());
                                return installment;
                            }).toList()
                    );
                }
                return installmentList;
            }
            return installmentList;
        } catch (Exception e) {
            throw new RuntimeException("Error listing installments: " + e.getMessage());
        }
    }
}