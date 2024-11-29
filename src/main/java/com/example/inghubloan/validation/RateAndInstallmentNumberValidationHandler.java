package com.example.inghubloan.validation;

import com.example.inghubloan.dto.ValidationDTO;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class RateAndInstallmentNumberValidationHandler extends ValidationHandler {

    private final BigDecimal minInterestRate = new BigDecimal("0.1");
    private final BigDecimal maxInterestRate = new BigDecimal("0.5");
    private final List<Integer> validInstallments = List.of(6, 9, 12, 24);

    @Override
    protected ValidationDTO validateRequest(ValidationDTO validationDTO) {
        if (validationDTO.getInterestRate().compareTo(minInterestRate) < 0 || validationDTO.getInterestRate().compareTo(maxInterestRate) > 0) {
            validationDTO.setValid(false);
            validationDTO.setValidationMessage("Invalid interest rate. Allowed range: 0.1 - 0.5.");
            return validationDTO;
        }

        if (!validInstallments.contains(validationDTO.getNumberOfInstallments())) {
            validationDTO.setValidationMessage("Invalid number of installments. Allowed values: 6, 9, 12, 24.");
            validationDTO.setValid(false);
            return validationDTO;
        }
        return validationDTO;
    }
}