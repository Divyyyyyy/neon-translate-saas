package com.example.smartexpense.controller;

import com.example.smartexpense.dto.AddExpenseRequest;
import com.example.smartexpense.service.DebtService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/expenses")
@CrossOrigin
public class ExpenseController {

    private final DebtService debtService;

    public ExpenseController(DebtService debtService) {
        this.debtService = debtService;
    }

    @PostMapping
    public void addExpense(@RequestBody AddExpenseRequest request) {
        debtService.addExpense(request);
    }
}

