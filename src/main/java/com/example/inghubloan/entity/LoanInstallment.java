package com.example.inghubloan.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Getter
@Setter
public class LoanInstallment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "loan_id", nullable = false)
    private Loan loan;

    private BigDecimal amount;
    private BigDecimal paidAmount = BigDecimal.ZERO;
    private LocalDate dueDate;
    private LocalDate paymentDate;
    private Integer isPaid;
}