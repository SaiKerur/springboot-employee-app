package com.example.demo.controller;

import com.example.demo.dto.ApiResponse;
import com.example.demo.dto.DepartmentSalaryPaymentRequestDTO;
import com.example.demo.dto.DepartmentSalaryPaymentResponseDTO;
import com.example.demo.service.PayrollPaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("/payments")
public class PayrollPaymentController {

    private final PayrollPaymentService payrollPaymentService;

    @PostMapping("/salary/department")
    public ResponseEntity<ApiResponse<DepartmentSalaryPaymentResponseDTO>> payDepartmentSalary(
            @Valid @RequestBody DepartmentSalaryPaymentRequestDTO requestDTO
    ) {
        DepartmentSalaryPaymentResponseDTO responseData = payrollPaymentService.payDepartmentSalaries(requestDTO);
        ApiResponse<DepartmentSalaryPaymentResponseDTO> response = new ApiResponse<>(
                LocalDateTime.now(),
                HttpStatus.OK.value(),
                "Department salary payment completed",
                responseData
        );
        return ResponseEntity.ok(response);
    }
}
