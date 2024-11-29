package com.example.inghubloan.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
public class Loan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    private BigDecimal loanAmount;
    private Integer numberOfInstallments;
    private LocalDate createDate;
    private Integer isPaid;

    @OneToMany(mappedBy = "loan", cascade = CascadeType.ALL)
    private List<LoanInstallment> installments;
}