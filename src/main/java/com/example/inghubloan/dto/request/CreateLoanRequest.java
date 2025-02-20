package com.example.inghubloan.dto.request;


import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;

@Getter
@Setter
public class CreateLoanRequest {
    @NotNull
    private Long customerId;
    @NotNull
    private BigDecimal amount;
    @NotNull
    private BigDecimal interestRate;
    @NotNull
    private Integer numberOfInstallments;
}
