package com.example.demo.service;

import com.example.demo.dto.DepartmentSalaryPaymentRequestDTO;
import com.example.demo.dto.DepartmentSalaryPaymentResponseDTO;
import com.example.demo.dto.EmployeePaymentResultDTO;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Department;
import com.example.demo.model.Employee;
import com.example.demo.payment.PaymentProviderType;
import com.example.demo.payment.PaymentStrategyFactory;
import com.example.demo.payment.SalaryPaymentCommand;
import com.example.demo.payment.SalaryPaymentResult;
import com.example.demo.payment.SalaryPaymentStrategy;
import com.example.demo.repository.DepartmentRepository;
import com.example.demo.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.YearMonth;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PayrollPaymentServiceImpl implements PayrollPaymentService {

    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository;
    private final PaymentStrategyFactory paymentStrategyFactory;

    @Override
    @Transactional(readOnly = true)
    public DepartmentSalaryPaymentResponseDTO payDepartmentSalaries(DepartmentSalaryPaymentRequestDTO requestDTO) {
        PaymentProviderType providerType = PaymentProviderType.from(requestDTO.paymentProvider());
        SalaryPaymentStrategy strategy = paymentStrategyFactory.getStrategy(providerType);
        YearMonth salaryMonth = YearMonth.parse(requestDTO.salaryMonth());

        Department department = departmentRepository.findById(requestDTO.departmentId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Department not found with id: " + requestDTO.departmentId()
                ));

        List<Employee> employees = employeeRepository.findAllByDepartmentId(department.getId());
        if (employees.isEmpty()) {
            throw new IllegalArgumentException(
                    "No employees found for department id: " + requestDTO.departmentId()
            );
        }

        List<EmployeePaymentResultDTO> paymentResults = employees.stream()
                .map(employee -> mapEmployeePaymentResult(employee, salaryMonth, strategy))
                .toList();

        double totalAmount = paymentResults.stream()
                .mapToDouble(EmployeePaymentResultDTO::amount)
                .sum();

        log.info(
                "Processed salary payment for {} employees in department {} using {}",
                paymentResults.size(),
                department.getId(),
                providerType
        );

        return new DepartmentSalaryPaymentResponseDTO(
                department.getId(),
                department.getName(),
                providerType.name(),
                salaryMonth.toString(),
                paymentResults.size(),
                totalAmount,
                paymentResults
        );
    }

    private EmployeePaymentResultDTO mapEmployeePaymentResult(
            Employee employee,
            YearMonth salaryMonth,
            SalaryPaymentStrategy strategy
    ) {
        SalaryPaymentCommand command = new SalaryPaymentCommand(
                employee.getId(),
                employee.getName(),
                employee.getEmail(),
                employee.getSalary(),
                salaryMonth
        );
        SalaryPaymentResult paymentResult = strategy.paySalary(command);
        return new EmployeePaymentResultDTO(
                employee.getId(),
                employee.getName(),
                employee.getEmail(),
                employee.getSalary(),
                paymentResult.success() ? "SUCCESS" : "FAILED",
                paymentResult.transactionId(),
                paymentResult.message()
        );
    }
}
