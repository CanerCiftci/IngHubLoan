package com.example.inghubloan.validation;

import com.example.inghubloan.dto.ValidationDTO;

public abstract class ValidationHandler {

    private ValidationHandler nextHandler;

    public void setNextValidation(ValidationHandler nextHandler) {
        this.nextHandler = nextHandler;
    }

    public ValidationDTO validate(ValidationDTO validationDTO) {
        ValidationDTO result = validateRequest(validationDTO);
        if (!result.isValid()) {
            return result;
        }
        if (nextHandler != null) {
            return nextHandler.validate(validationDTO);
        }
        return result;
    }

    protected abstract ValidationDTO validateRequest(ValidationDTO validationDTO);
}