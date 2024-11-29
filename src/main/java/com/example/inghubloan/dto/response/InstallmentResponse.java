package com.example.inghubloan.dto.response;

import com.example.inghubloan.dto.Installment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InstallmentResponse {
    private List<Installment> installmentList;
}