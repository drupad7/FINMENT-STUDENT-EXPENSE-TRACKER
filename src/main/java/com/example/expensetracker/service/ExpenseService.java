package com.example.expensetracker.service;

import com.example.expensetracker.model.Expense;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;

@Service
public class ExpenseService {
    private final List<Expense> expenses = new ArrayList<>();
    private double monthlyBudget = 0;

    public Expense addExpense(Expense e) {
        expenses.add(e);
        return e;
    }

    public List<Expense> getAllExpenses() {
        return expenses;
    }

    public double getDailyTotal(LocalDate date) {
        return expenses.stream()
                .filter(e -> e.getDate().equals(date))
                .mapToDouble(Expense::getAmount)
                .sum();
    }

    public double getMonthlyTotal(int year, int month) {
        return expenses.stream()
                .filter(e -> e.getDate().getYear() == year && e.getDate().getMonthValue() == month)
                .mapToDouble(Expense::getAmount)
                .sum();
    }

    public Map<Integer, Double> getMonthlyCalendar(int year, int month) {
        YearMonth ym = YearMonth.of(year, month);
        Map<Integer, Double> map = new LinkedHashMap<>();
        for (int day = 1; day <= ym.lengthOfMonth(); day++) {
            LocalDate d = LocalDate.of(year, month, day);
            double total = expenses.stream()
                    .filter(e -> e.getDate().equals(d))
                    .mapToDouble(Expense::getAmount)
                    .sum();
            map.put(day, total);
        }
        return map;
    }

    public void setBudget(double budget) {
        this.monthlyBudget = budget;
    }

    public double getBudget() {
        return monthlyBudget;
    }

    public String checkBudget(int year, int month) {
        double total = getMonthlyTotal(year, month);
        if (total > monthlyBudget) {
            return "⚠️ You exceeded your budget!";
        } else {
            return "✅ You are within budget.";
        }
    }
}
