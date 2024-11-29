package com.example.inghubloan.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String surname;

    private BigDecimal creditLimit;
    private BigDecimal usedCreditLimit = BigDecimal.ZERO;
}