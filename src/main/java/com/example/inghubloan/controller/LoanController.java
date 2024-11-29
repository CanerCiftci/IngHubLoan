package com.example.inghubloan.controller;

import com.example.inghubloan.dto.request.CreateLoanRequest;
import com.example.inghubloan.dto.response.CreateLoanResponse;
import com.example.inghubloan.dto.request.LoanPaymentRequest;
import com.example.inghubloan.dto.response.InstallmentResponse;
import com.example.inghubloan.dto.response.PaymentResponse;
import com.example.inghubloan.service.loan.LoanService;
import com.example.inghubloan.service.payment.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/loans")
@RequiredArgsConstructor
public class LoanController {

    private final LoanService loanService;
    private final PaymentService paymentService;

    @PostMapping("/createLoan")
    public ResponseEntity<CreateLoanResponse> createLoan(@Valid @RequestBody CreateLoanRequest request) {
        CreateLoanResponse response = loanService.createLoan(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/payLoan")
    public ResponseEntity<PaymentResponse> payLoan(@Valid @RequestBody LoanPaymentRequest request) {
        PaymentResponse response = paymentService.payLoan(request.getLoanId(), request.getAmount());
        return ResponseEntity.ok(response);
    }


    @GetMapping("/listInstallments/{customerId}")
    public ResponseEntity<InstallmentResponse> listInstallments(@PathVariable Long customerId) {
        InstallmentResponse response = new InstallmentResponse();
        response.setInstallmentList(loanService.listInstallments(customerId));
        return ResponseEntity.ok(response);
    }
}

