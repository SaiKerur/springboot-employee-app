package com.example.demo.dto;

import java.util.List;

public record DepartmentSalaryPaymentResponseDTO(
        Integer departmentId,
        String departmentName,
        String paymentProvider,
        String salaryMonth,
        Integer totalEmployees,
        Double totalAmountPaid,
        List<EmployeePaymentResultDTO> payments
) {
}
