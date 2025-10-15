package com.example.expensetracker.service;

import com.example.expensetracker.dao.ExpenseDAO;
import com.example.expensetracker.model.Expense;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExpenseService {

    private final ExpenseDAO expenseDAO;

    public ExpenseService(ExpenseDAO expenseDAO) {
        this.expenseDAO = expenseDAO;
    }

    public void addExpense(Expense e) {
        expenseDAO.addExpense(e);
    }

    public List<Expense> getExpenses(String email) {
        return expenseDAO.getExpensesByEmail(email);
    }

    public void transfer(String email, String fromWallet, String toWallet, double amount, String description, String date) {
        expenseDAO.transfer(email, fromWallet, toWallet, amount, description, date);
    }
}
