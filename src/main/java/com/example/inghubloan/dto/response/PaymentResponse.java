package com.example.inghubloan.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponse {
    private Long loanId;
    private int installmentsPaid;
    private BigDecimal totalAmountPaid;
    private boolean isLoanFullyPaid;
    private String validationMessage;
}
