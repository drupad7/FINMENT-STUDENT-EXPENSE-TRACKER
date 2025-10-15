package com.example.expensetracker.service;

import com.example.expensetracker.dao.BudgetDAO;
import com.example.expensetracker.model.Budget;
import org.springframework.stereotype.Service;

import java.time.YearMonth;

@Service
public class BudgetService {

    private final BudgetDAO budgetDAO;

    public BudgetService(BudgetDAO budgetDAO) {
        this.budgetDAO = budgetDAO;
    }

    public Budget getMonthlyBudget(String email, int month, int year) {
        return budgetDAO.getBudget(email, month, year);
    }

    public void setMonthlyBudget(String email, double amount, int month, int year) {
        Budget budget = new Budget(email, month, year, amount);
        budgetDAO.setBudget(budget);
    }

    public Budget getCurrentMonthBudget(String email) {
        YearMonth ym = YearMonth.now();
        return getMonthlyBudget(email, ym.getMonthValue(), ym.getYear());
    }
}
