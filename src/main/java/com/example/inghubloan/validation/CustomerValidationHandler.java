package com.example.inghubloan.validation;
import com.example.inghubloan.dto.ValidationDTO;


import org.springframework.stereotype.Component;

@Component
public class CustomerValidationHandler extends ValidationHandler {

    @Override
    protected ValidationDTO validateRequest(ValidationDTO validationDTO) {
        if (validationDTO.getCustomer() == null) {
            validationDTO.setValid(false);
            validationDTO.setValidationMessage("Customer not found.");
            return validationDTO;
        }

        if (validationDTO.getTotalAmount().compareTo(validationDTO.getCustomer().getCreditLimit()) > 0) {
            validationDTO.setValid(false);
            validationDTO.setValidationMessage("Customer has insufficient credit limit.");
            return validationDTO;
        }
        validationDTO.setValid(true);
        return validationDTO;
    }

}