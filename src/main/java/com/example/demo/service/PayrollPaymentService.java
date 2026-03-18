package com.example.demo.service;

import com.example.demo.dto.DepartmentSalaryPaymentRequestDTO;
import com.example.demo.dto.DepartmentSalaryPaymentResponseDTO;

public interface PayrollPaymentService {

    DepartmentSalaryPaymentResponseDTO payDepartmentSalaries(DepartmentSalaryPaymentRequestDTO requestDTO);
}
