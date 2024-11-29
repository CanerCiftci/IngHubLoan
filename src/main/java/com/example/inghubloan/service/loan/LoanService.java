package com.example.inghubloan.service.loan;

import com.example.inghubloan.dto.Installment;
import com.example.inghubloan.dto.request.CreateLoanRequest;
import com.example.inghubloan.dto.response.CreateLoanResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface LoanService {
    CreateLoanResponse createLoan(CreateLoanRequest request);

    List<Installment> listInstallments(Long customerId);

}