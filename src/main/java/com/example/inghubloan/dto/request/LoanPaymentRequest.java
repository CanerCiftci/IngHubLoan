package com.example.inghubloan.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class LoanPaymentRequest {
    @NotNull
    private Long loanId;
    @NotNull
    private BigDecimal amount;
}