package com.example.inghubloan.mapper;

import com.example.inghubloan.dto.ValidationDTO;
import com.example.inghubloan.dto.request.CreateLoanRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CoreMapper {

    ValidationDTO toValidationDTO(CreateLoanRequest request);
}