package com.example.inghubloan.repository;

import com.example.inghubloan.entity.LoanInstallment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoanInstallmentRepository extends JpaRepository<LoanInstallment, Long> {
}