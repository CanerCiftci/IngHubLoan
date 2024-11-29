package com.example.inghubloan.service.payment;


import com.example.inghubloan.dto.response.PaymentResponse;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public interface PaymentService {
    PaymentResponse payLoan(Long loanId, BigDecimal paymentAmount);

}