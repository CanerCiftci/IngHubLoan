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
public class CreateLoanResponse {
    private Long loanId;
    private BigDecimal loanAmount;
    private String validationMessage;
    private boolean isValid;
}
