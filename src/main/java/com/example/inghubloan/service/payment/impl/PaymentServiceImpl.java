package com.example.inghubloan.service.payment.impl;

import com.example.inghubloan.dto.response.PaymentResponse;
import com.example.inghubloan.entity.Loan;
import com.example.inghubloan.entity.LoanInstallment;

import com.example.inghubloan.repository.LoanRepository;
import com.example.inghubloan.service.payment.PaymentService;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final LoanRepository loanRepository;

    @Override
    @Transactional
    public PaymentResponse payLoan(Long loanId, BigDecimal paymentAmount) {
        PaymentResponse response = new PaymentResponse();
        try {
            Loan loan = loanRepository.findById(loanId).orElse(null);
            validatePaymentRequest(loan, paymentAmount);
            BigDecimal remainingPayment = paymentAmount;
            List<LoanInstallment> sortedInstallmentList = getSortedUnPaidInstallmentList(loan.getInstallments());
            LocalDate maxDueDate = getMaxDueDate();
            LocalDate paymentDate = LocalDate.now();

            if (!CollectionUtils.isEmpty(sortedInstallmentList)){
                int installmentsPaid = 0;
                BigDecimal totalPaid = BigDecimal.ZERO;

                for (LoanInstallment installment : sortedInstallmentList) {
                    if (installment.getDueDate().isAfter(maxDueDate)) {
                        break;
                    }
                    if (remainingPayment.compareTo(installment.getAmount()) >= 0) {
                        remainingPayment = calculateRemainingPayment(remainingPayment, installment.getAmount());
                        installment.setPaidAmount(installment.getAmount());
                        installment.setIsPaid(1);
                        installment.setPaymentDate(paymentDate);
                        totalPaid = totalPaid.add(installment.getAmount());
                        installmentsPaid++;
                    } else {
                        break;
                    }
                }
                boolean isLoanFullyPaid = isLoanFullyPaid(loan);
                loan.setIsPaid(isLoanFullyPaid ? 1 : 0);
                loanRepository.save(loan);
                setResponseFields(response, loan, installmentsPaid, totalPaid);
            }else {
                response.setValidationMessage("Installment could not be found !");
            }
        }catch (Exception e){
            response.setValidationMessage("Payment failed : " + e.getMessage());
        }
        return response;
    }

    private static boolean isLoanFullyPaid(Loan loan) {
        return loan.getInstallments().stream().allMatch(installment -> installment.getIsPaid() == 1);
    }

    private static BigDecimal calculateRemainingPayment(BigDecimal remainingPayment, BigDecimal installmentAmount) {
        return remainingPayment.subtract(installmentAmount);
    }

    private static void setResponseFields(PaymentResponse response, Loan loan, int installmentsPaid, BigDecimal totalPaid) {
        response.setLoanId(loan.getId());
        response.setInstallmentsPaid(installmentsPaid);
        response.setTotalAmountPaid(totalPaid);
        response.setLoanFullyPaid(1 == loan.getIsPaid());
        response.setValidationMessage("Payment processed successfully.");
    }

    private static List<LoanInstallment> getSortedUnPaidInstallmentList(List<LoanInstallment> installments) {
        if(!CollectionUtils.isEmpty(installments)) {
           return installments.stream()
                    .filter(installment ->  0 == installment.getIsPaid())
                    .sorted(Comparator.comparing(LoanInstallment::getDueDate)).toList();
        }
        return new ArrayList<>();
    }

    private void validatePaymentRequest(Loan loan, BigDecimal paymentAmount) {
        if (loan == null) {
            throw new ValidationException("Loan not found.");
        }
        if (loan.getIsPaid() == 1) {
            throw new ValidationException("Loan is already fully paid.");
        }
        if (CollectionUtils.isEmpty(loan.getInstallments())) {
            throw new ValidationException("Installments not found.");
        }
        LoanInstallment unpaidInstallment = loan.getInstallments().stream()
                .filter(installment -> installment.getIsPaid() == 0)
                .findFirst()
                .orElse(null);

        if (unpaidInstallment != null && paymentAmount.compareTo(unpaidInstallment.getAmount()) < 0) {
            throw new ValidationException("Insufficient payment amount. The payment is less than the amount of one installment.");
        }
    }

    private static LocalDate getMaxDueDate(){
        return  LocalDate.now().plusMonths(3).withDayOfMonth(1);
    }

}