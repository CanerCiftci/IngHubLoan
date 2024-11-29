package com.example.inghubloan.controller;

import com.example.inghubloan.dto.Installment;
import com.example.inghubloan.dto.request.CreateLoanRequest;
import com.example.inghubloan.dto.request.LoanPaymentRequest;
import com.example.inghubloan.dto.response.CreateLoanResponse;
import com.example.inghubloan.dto.response.InstallmentResponse;
import com.example.inghubloan.dto.response.PaymentResponse;
import com.example.inghubloan.entity.LoanInstallment;
import com.example.inghubloan.service.loan.LoanService;
import com.example.inghubloan.service.payment.PaymentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class LoanControllerTest {

    private MockMvc mockMvc;

    @Mock
    private LoanService loanService;

    @Mock
    private PaymentService paymentService;

    @InjectMocks
    private LoanController loanController;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(loanController).build();
    }

    @Test
    public void testCreateLoan_ReturnsHttpStatusOk() throws Exception {
        CreateLoanRequest request = new CreateLoanRequest();
        request.setAmount(BigDecimal.ONE);
        request.setCustomerId(1L);
        request.setInterestRate(BigDecimal.valueOf(0.2));
        request.setNumberOfInstallments(12);

        CreateLoanResponse mockResponse = new CreateLoanResponse();
        mockResponse.setLoanId(1L);
        mockResponse.setValidationMessage("Loan created successfully");

        when(loanService.createLoan(request)).thenReturn(mockResponse);

        mockMvc.perform(post("/api/loans/createLoan")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"amount\":1000,\"customerId\":1,\"interestRate\":0.2,\"numberOfInstallments\":12}"))
                .andExpect(status().isOk());
    }




    @Test
    public void testPayLoan_ReturnsHttpStatusOk() throws Exception {
        LoanPaymentRequest request = new LoanPaymentRequest();
        request.setLoanId(1L);
        request.setAmount(BigDecimal.valueOf(500));

        PaymentResponse mockResponse = new PaymentResponse();
        mockResponse.setValidationMessage("Payment successful");

        when(paymentService.payLoan(1L, BigDecimal.valueOf(500))).thenReturn(mockResponse);

        mockMvc.perform(post("/api/loans/payLoan").contentType(MediaType.APPLICATION_JSON).content("{\"loanId\":1,\"amount\":500}")).andExpect(status().isOk()).andExpect(MockMvcResultMatchers.jsonPath("$.validationMessage").value("Payment successful"));
    }

    @Test
    public void testListInstallments_ReturnsHttpStatusOk() throws Exception {

        InstallmentResponse mockResponse = new InstallmentResponse();
        List<Installment> loanInstallments = new ArrayList<>();

        Installment installment1 = new Installment();
        installment1.setAmount(BigDecimal.valueOf(100));
        installment1.setDueDate(LocalDate.now());
        installment1.setIsPaid(0);

        Installment installment2 = new Installment();
        installment2.setAmount(BigDecimal.valueOf(150));
        installment2.setDueDate(LocalDate.now().plusMonths(1));
        installment2.setIsPaid(0);
        loanInstallments.add(installment2);
        loanInstallments.add(installment1);
        mockResponse.setInstallmentList(loanInstallments);

        when(loanService.listInstallments(1L)).thenReturn(mockResponse.getInstallmentList());

        mockMvc.perform(get("/api/loans/listInstallments/1"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.installmentList").isNotEmpty());
    }

}
