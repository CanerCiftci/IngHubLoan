package com.example.inghubloan.dto;


import com.example.inghubloan.entity.Customer;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;

@Getter
@Setter
public class ValidationDTO {
    private Long customerId;
    private BigDecimal amount;
    private BigDecimal interestRate;
    private Integer numberOfInstallments;
    private BigDecimal totalAmount;
    private boolean isValid;
    private String validationMessage;
    private Customer customer;
}
